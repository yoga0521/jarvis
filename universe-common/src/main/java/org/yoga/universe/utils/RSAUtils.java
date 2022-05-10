package org.yoga.universe.utils;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.security.*;
import java.util.Base64;

/**
 * @Description: RSA加密工具类
 * @Author yoga
 * @Date 2022-05-10 17:40
 */
public class RSAUtils {

    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = generator.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        logger.debug(String.format("test info %s : %s", Base64.getEncoder().encodeToString(publicKey.getEncoded()),
                Base64.getEncoder().encodeToString(privateKey.getEncoded())));
    }
}
