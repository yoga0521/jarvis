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

package org.yoga.jarvis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @Description: Net Utils
 * @Author: yoga
 * @Date: 2022/7/19 16:01
 */
public class NetUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    private static final Pattern IP_PATTERN = Pattern.compile("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$");

    private static final Pattern MAC_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}$");


    /**
     * get ip address
     *
     * @return ip address, may be null
     */
    public static InetAddress getLocalIpAddress0() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            if (isValidIpAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            logger.warn("Failed to retrieving ip address, {}", e.getMessage(), e);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                //check if there are more than one network interface
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            try {
                                InetAddress ipAddress = addresses.nextElement();
                                if (isValidIpAddress(ipAddress)) {
                                    return ipAddress;
                                }
                            } catch (Throwable e) {
                                logger.warn("Failed to retrieving ip address, {}", e.getMessage(), e);
                            }
                        }
                    } catch (Throwable e) {
                        logger.warn("Failed to retrieving ip address, {}", e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to retrieving ip address, {}", e.getMessage(), e);
        }

        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return null;
    }

    /**
     * get ip address by network Interface Name
     *
     * @param networkInterfaceName network Interface Name
     * @return ip address, may be null
     */
    public static InetAddress getIpAddress0ByName(String networkInterfaceName) {
        try {
            //get network interface by name
            NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
            if (networkInterface == null) {
                return null;
            }
            //get all ip addresses band to this interface
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress ipAddress = addresses.nextElement();
                if (isValidIpAddress(ipAddress)) {
                    return ipAddress;
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to retrieving ip address, {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * check the ip address is valid
     *
     * @param ipAddress ip address
     * @return is valid
     */
    public static boolean isValidIpAddress(InetAddress ipAddress) {
        if (ipAddress == null || ipAddress.isLoopbackAddress()) {
            return false;
        }

        String name = ipAddress.getHostAddress();
        return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name).matches();
    }

    /**
     * get mac address by ip address
     *
     * @param ipAddress ip address
     * @return mac address array
     */
    public static byte[] getMacAddress(InetAddress ipAddress) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(ipAddress);
            if (network != null) {
                return network.getHardwareAddress();
            }
        } catch (Exception e) {
            logger.warn("getMacAddress: {}", e.getMessage());
        }
        return null;
    }

}
