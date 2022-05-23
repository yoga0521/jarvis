/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis.listener;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoga.jarvis.constant.OssOperateType;

/**
 * @Description: oss process listener
 * Try not to do time-consuming operations, except in async methods
 * @Author: yoga
 * @Date: 2022/5/13 15:34
 */
public class OssProgressListener extends AbstractListener implements ProgressListener {

    protected final static Logger logger = LoggerFactory.getLogger(OssProgressListener.class);

    private final OssOperateType ossOperateType;

    private long totalBytes = -1;

    private long handledBytes = 0;

    public OssProgressListener(OssOperateType ossOperateType) {
        this.ossOperateType = ossOperateType;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                logger.info(String.format("Start to %s......", ossOperateType.toString()));
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                logger.info(String.format("%d bytes in total will be %s to OSS",this.totalBytes , ossOperateType.getSimplePast()));
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.handledBytes += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.handledBytes * 100.0 / this.totalBytes);
                    logger.info(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.handledBytes + "/" + this.totalBytes + ")");
                } else {
                    logger.info(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.handledBytes + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                logger.info("Succeed to upload, " + this.handledBytes + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                logger.info("Failed to upload, " + this.handledBytes + " bytes have been transferred");
                break;
            default:
                break;
        }
    }
}
