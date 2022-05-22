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

package org.yoga.jarvis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoga.jarvis.exception.JarvisException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @Description: AES
 * @Author: yoga
 * @Date: 2022/5/16 13:18
 */
public class AESUtils {

    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 默认的初始化向量值
     */
    private static final String IV_DEFAULT = "mcEiFegEPaOPLKr4";

    /**
     * 随机数实现算法
     */
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * CBC模式：又称密码分组链接（CBC，Cipher-block chaining）模式。在这种加密模式中，每个密文块都依赖于它前面的所有明文块。
     * 同时，为了保证每条消息的唯一性，在第一个块中需要使用初始化向量IV。
     */
    private static final String TRANSFORM_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    /**
     * ECB模式：又称电码本（ECB，Electronic Codebook Book）模式。这是最简单的块密码加密模式，
     * 加密前根据加密块大小（如AES为128位）分成若干块，之后将每块使用相同的密钥单独加密，解密同理。
     */
    private static final String TRANSFORM_ECB_PKCS5 = "AES/ECB/PKCS5Padding";

    /**
     * 生成AES密钥,长度为128
     *
     * @param key key
     * @return secretKey
     */
    public static SecretKey generateAesKey(String key) {
        return AESUtils.generateAesKey(key, 128);
    }

    /**
     * 生成AES密钥,可选长度为128,192,256位
     *
     * @param key          key
     * @param aesKeyLength aseKey长度
     * @return secretKey
     */
    public static SecretKey generateAesKey(String key, int aesKeyLength) {
        Assert.notBlank(key, "key must not be blank!");
        try {
            // 实例化密钥生成器
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            secureRandom.setSeed(key.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(aesKeyLength, secureRandom);
            return keyGenerator.generateKey();
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于ECB工作模式加密字符串
     *
     * @param str 输入字符串
     * @param key 加密key
     * @return 加密后的字符串
     */
    public static String encryptEcbMode(final String str, String key) {
        Assert.notBlank(str, "str must not be blank!");
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            // 初始化Cipher对象，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            // 使用AES加密
            byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            // 转成BASE64返回
            return Base64.getUrlEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于ECB工作模式解密字符串
     *
     * @param encryptedStr 加密字符串
     * @param key          加密key
     * @return 解密后的字符串
     */
    public static String decryptEcbMode(final String encryptedStr, String key) {
        Assert.notBlank(encryptedStr, "encryptedStr must not be blank!");
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            Cipher encipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            // 初始化Cipher对象，设置为解密模式
            encipher.init(Cipher.DECRYPT_MODE, secretKey);
            // BASE64解密
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedStr);
            // AES解密
            return new String(encipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于ECB工作模式加密流
     *
     * @param inputStream 流
     * @param key         加密key
     * @return 加密后的流
     */
    public static InputStream encryptStreamEcbMode(InputStream inputStream, String key) {
        Assert.notNull(inputStream, "inputStream must not be null!");
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            // 实例化Cipher对象，它用于完成实际的加密操作
            Cipher cipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            // 初始化Cipher对象，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new CipherInputStream(inputStream, cipher);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于ECB工作模式解密流
     *
     * @param inputStream 流
     * @param key         加密key
     * @return 解密后的流
     */
    public static InputStream decryptStreamEcbMode(InputStream inputStream, String key) {
        Assert.notNull(inputStream, "inputStream must not be null!");
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            // 实例化Cipher对象，它用于完成实际的加密操作
            Cipher cipher = Cipher.getInstance(TRANSFORM_ECB_PKCS5);
            // 初始化Cipher对象，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new CipherInputStream(inputStream, cipher);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于CBC工作模式加密字符串
     *
     * @param str 输入字符串
     * @param key 加密key
     * @param iv  偏移量
     * @return 加密后的字符串
     */
    public static String encryptCbcMode(final String str, String key, String iv) {
        Assert.notBlank(str, "str must not be blank!");
        if (iv == null || iv.length() == 0) {
            iv = IV_DEFAULT;
        }
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            // 初始化向量器
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORM_CBC_PKCS5);
            // 初始化Cipher对象，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            // 使用AES加密
            byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            // 转成BASE64返回
            return Base64.getUrlEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * 基于CBC工作模式解密字符串
     *
     * @param encryptedStr 加密字符串
     * @param key          加密key
     * @param iv           偏移量
     * @return 解密后的字符串
     */
    public static String decryptCbcMode(final String encryptedStr, String key, String iv) {
        Assert.notBlank(encryptedStr, "encryptedStr must not be blank!");
        if (iv == null || iv.length() == 0) {
            iv = IV_DEFAULT;
        }
        // 秘密密钥
        SecretKey secretKey = generateAesKey(key);
        try {
            // 初始化向量器
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORM_CBC_PKCS5);
            // 初始化Cipher对象，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            // BASE64解密
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedStr);
            // AES解密
            return new String(cipher.doFinal(encryptedBytes));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }
}
