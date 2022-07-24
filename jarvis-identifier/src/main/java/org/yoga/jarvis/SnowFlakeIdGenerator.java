/*
 *  Copyright 2022 yoga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.yoga.jarvis;

import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.NetUtils;

import java.lang.management.ManagementFactory;

/**
 * @Description: SnowFlakeId Generator
 * when using, you need to ensure a singleton
 * @Author: yoga
 * @Date: 2022/7/12 19:01
 */
public class SnowFlakeIdGenerator {

    /**
     * start timestamp (ms)
     * 20212-07-01 00:00:00
     */
    private static final long TW_EPOCH = 1656604800000L;

    /**
     * the number of bits used by the work machine ID
     */
    private static final int WORKER_ID_BITS = 5;

    /**
     * the number of bits occupied by the data identification ID
     */
    private static final int DATA_CENTER_ID_BITS = 5;

    /**
     * maximum supported work machine ID, the maximum is 31
     */
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * the maximum supported data identification ID, the maximum is 31
     */
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    /**
     * the number of digits the sequence occupies in the ID
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * work machine ID is shifted 12 bits to the left
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * the data identification ID is shifted 17 digits to the left
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * timestamp is shifted 22 bits to the left
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * the mask maximum value of the generated sequence, the maximum is 4095
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * work machine ID (0~31)
     */
    private final long workerId;

    /**
     * data identification ID (0~31)
     */
    private final long dataCenterId;

    /**
     * sequence in milliseconds (0~4095)
     */
    private long sequence = 0L;

    /**
     * timestamp of last ID generation
     */
    private long lastTimestamp = -1L;

    /**
     * create an ID generator
     * use the serial number of the work machine (remove the machine room for the machine ID) [0, 1023]
     * the advantage is that it is convenient to number the machine
     *
     * @param workerId work machine ID (0~31)
     */
    public SnowFlakeIdGenerator(long workerId) {
        // calculate the maximum value
        long maxMachineId = (MAX_DATA_CENTER_ID + 1) * (MAX_WORKER_ID + 1) - 1;
        Assert.isTrue(0 <= workerId && workerId <= maxMachineId,
                String.format("Worker ID can't be greater than %d or less than 0", maxMachineId));

        // take the high part as the ID part of the computer room
        this.dataCenterId = (workerId >> WORKER_ID_BITS) & MAX_DATA_CENTER_ID;

        // take the lower part as the machine ID part
        this.workerId = workerId & MAX_WORKER_ID;
    }

    /**
     * create an ID generator
     * using work machine ID and data identification ID
     * the advantage is that it is convenient for branch data identification management
     *
     * @param workerId     work machine ID (0~31)
     * @param dataCenterId data identification ID (0~31)
     */
    public SnowFlakeIdGenerator(long workerId, long dataCenterId) {
        Assert.isTrue(0 <= workerId && workerId <= MAX_WORKER_ID,
                String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));

        Assert.isTrue(0 <= dataCenterId && dataCenterId <= MAX_DATA_CENTER_ID,
                String.format("DataCenter ID can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * get next ID (thread safe)
     *
     * @return a number of type long with a length of 15 bits
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        // If the current time is less than the timestamp of the last ID generation,
        // it means that a clock callback occurs,
        // and an exception is thrown to ensure that the ID is not repeated.
        Assert.isTrue(timestamp >= lastTimestamp,
                String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        if (lastTimestamp == timestamp) {
            // generated at the same time, the serial number +1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // sequence overflow in milliseconds: max value exceeded
            if (sequence == 0) {
                // block until the next millisecond, get a new timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // timestamp changed, sequence reset in milliseconds
            sequence = 0L;
        }
        // timestamp of last ID generation
        lastTimestamp = timestamp;
        // shift du OR together
        return ((timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * calculate remainder based on NIC MAC address as data center
     */
    protected long getDatacenterId() {
        byte[] mac = NetUtils.getMacAddress(NetUtils.getLocalIpAddress0());
        if (null == mac) {
            return 1L;
        }
        long id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
        return id % (MAX_DATA_CENTER_ID + 1);
    }

    /**
     * get 16 low bits of hashcode based on MAC + PID
     *
     * @param dataCenterId data identification ID (0~31)
     */
    protected long getWorkerId(long dataCenterId) {
        StringBuilder mpId = new StringBuilder();
        mpId.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name != null && name.length() > 0) {
            // get jvmPid
            mpId.append(name.split("@")[0]);
        }

        // the hashcode of MAC + PID gets 16 low bits
        return (mpId.toString().hashCode() & 0xffff) % (MAX_WORKER_ID + 1);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


}