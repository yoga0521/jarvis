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

package org.yoga.jarvis.util.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoga.jarvis.constant.DelimiterType;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Description: AES
 * @Author: yoga
 * @Date: 2022/5/16 13:18
 */
public class AESUtils {

    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    /**
     * key algorithm
     */
    private static final String ALGORITHM = "AES";

    /**
     * default key
     */
    private static final String KEY_DEFAULT = "7r5W2gdIxom8UasI";

    /**
     * default initialization vector value
     */
    private static final String IV_DEFAULT = "mcEiFegEPaOPLKr4";

    /**
     * random Number Implementation Algorithm
     */
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * AES encrypt working mode
     */
    enum AesEncryptMode {
        /**
         * CFB mode(Cipher feedback)
         * <p>
         * Unlike ECB and CBC modes that can only encrypt block data,
         * CFB can convert block ciphertext (Block Cipher) to stream ciphertext (Stream Cipher)
         * <p>
         * Advantage:
         * The same plaintext is different from the ciphertext, and the block key is converted into a stream cipher.
         * Disadvantage:
         * Serial operations are disadvantageous to parallelism, and transmission errors may cause subsequent transmission block errors.
         */
        cfb("CFB"),

        /**
         * OFB mode(Output feedback)
         * <p>
         * OFB is to first generate a key stream (keyStream) with a block cipher,
         * and then XOR the key stream with the plaintext stream to obtain a ciphertext stream.
         * Decryption is to first generate a keyStream with a block cipher, and then combine the key stream with the encrypted stream.
         * The text stream is XORed to get the plaintext.
         * Due to the symmetry of the XOR operation, the encryption and decryption processes are exactly the same.
         * <p>
         * Advantage:
         * The same plaintext is different from the ciphertext, and the block key is converted into a stream cipher.
         * Disadvantage:
         * Serial operations are disadvantageous to parallelism, and transmission errors may cause subsequent transmission block errors.
         */
        ofb("OFB"),

        /**
         * ECB mode(Electronic Codebook Book)
         * <p>
         * This is the simplest block cipher encryption mode.
         * Before encryption, it is divided into several blocks according to the encrypted block size (for example, AES is 128 bits),
         * and then each block is encrypted separately with the same key, and the same is true for decryption.
         * <p>
         * Advantage:
         * 1. Simple, isolated, each block operates independently.
         * 2. Suitable for parallel operation.
         * 3. Transmission errors generally only affect the current block.
         * Disadvantage:
         * 1. Outputting the same ciphertext with the same plaintext may lead to a plaintext attack.
         * 2. Many of the AES encrypt we usually use are in ECB mode, which does not require vector IV for encryption.
         */
        ecb("ECB"),

        /**
         * CBC mode(Cipher-block chaining)
         * <p>
         * In this mode of encryption, each block of ciphertext depends on all blocks of plaintext that precede it.
         * At the same time, in order to ensure the uniqueness of each message, the initialization vector IV needs to be used in the first block.
         * <p>
         * Advantage:
         * Serialization operation, same plaintext but different ciphertext.
         * Disadvantage:
         * 1. An initial vector is required, but this is not a disadvantage(The CTR below also requires random numbers).
         * 2. If there is a transmission error, subsequent results may be all wrong after decryption.
         */
        cbc("CBC"),

        /**
         * CTR mode(Counter mode)
         * <p>
         * The calculator mode is not common.
         * In the CTR mode, there is a self-incrementing operator.
         * This operator uses the key encrypted output and the result of the plaintext XOR to obtain the ciphertext,
         * which is equivalent to one-time pad. This encryption method is simple, fast, safe and reliable, and can be encrypted in parallel,
         * but the key can only be used once if the calculator cannot maintain it for a long time.
         * <p>
         * Advantage:
         * No padding, the same plaintext and different ciphertext, each block is operated independently,
         * suitable for parallel operation.
         * Disadvantage:
         * May lead to plaintext attacks.
         */
        ctr("CTR");

        /**
         * AesEncryptMode code
         */
        private final String code;

        AesEncryptMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * Regarding the padding problem, in the above encryption mode, such as CBC, etc.,
     * have requirements for the input block, which must be an integer multiple of the block.
     * For data that is not a whole block, padding is required. There are many methods for padding.
     * Common There are PKCS 5 and PKCS 7, ISO 10126, etc.
     * <p>
     * For example, if grouped by 16 bytes: For the part of less than 16 bytes (assuming that the difference is n full 16 bytes),
     * fill n bytes (n range (1,15)), and the value of each byte is n.
     * If it is exactly 16 bytes, then fill a block, that is, add 16 bytes, and the value of each byte is 16.
     * <p>
     * CBC、ECB need padding
     */
    enum AesPaddingMode {

        /**
         * No padding, the original data must be an integer multiple of the packet size under this padding,
         * this mode cannot be used when it is not an integer multiple.
         */
        none("NoPadding"),

        /**
         * Fill to an integer multiple of the block size, and the fill value is the number of fills.
         * PKCS5PADDING is a subset of PKCS7PADDING, the block size can only be a fixed value of 8.
         * In the standard AES algorithm, the block size itself is fixed at 8, so PKCS7PADDING and PKCS5PADDING are the same.
         */
        pkcs5("PKCS5Padding"),

        /**
         * Fill to an integer multiple of the block size, and the fill value is the number of fills.
         * The block size of PKCS7PADDING can be any value from 1 to 255.
         */
        pkcs7("PKCS7Padding"),

        /**
         * Pad to an integer multiple of the block size, the last byte of the padding value is the number of padding,
         * and other bytes are processed randomly.
         */
        iso10126("ISO10126Padding");

        /**
         * AesPaddingMode code
         */
        private final String code;

        AesPaddingMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * generate AES working mode
     *
     * @param encryptMode the mode of encrypt
     * @param paddingMode the mode of padding
     * @return AES working mode
     */
    public static String generateAesWorkingMode(AesEncryptMode encryptMode, AesPaddingMode paddingMode) {
        Assert.notNull(encryptMode, "encryptMode must not be null!");
        Assert.notNull(paddingMode, "paddingMode must not be null!");
        return ALGORITHM + DelimiterType.slash.getValue() + encryptMode.getCode() + DelimiterType.slash.getValue() + paddingMode.getCode();
    }

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
     * @throws JarvisException jarvis exception
     */
    public static SecretKey generateAesKey(String key, int aesKeyLength) throws JarvisException {
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
     * encrypt str
     *
     * @param str         str to be encrypted
     * @param key         encryption key
     * @param iv          offset
     * @param encryptMode encrypt mode {@link AESUtils.AesEncryptMode}
     * @param paddingMode padding mode {@link AESUtils.AesPaddingMode}
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encrypt(final String str, String key, String iv,
                                 AesEncryptMode encryptMode, AesPaddingMode paddingMode) throws JarvisException {
        Assert.notBlank(str, "str must not be blank!");
        // secret key
        SecretKey secretKey = generateAesKey(key);
        if (iv == null || iv.length() == 0) {
            iv = IV_DEFAULT;
        }
        // working mode
        String workingMode = generateAesWorkingMode(encryptMode, paddingMode);
        try {
            Cipher cipher = Cipher.getInstance(workingMode);
            // initialize the Cipher object and set it to encryption mode
            if (AesEncryptMode.ecb.equals(encryptMode)) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                // initialization vector
                final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            }
            // AES encrypt
            byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            // convert to BASE64 and return
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * decrypt encryptedStr
     *
     * @param encryptedStr encrypted str
     * @param key          encryption key
     * @param iv           offset
     * @param encryptMode  encrypt mode {@link AESUtils.AesEncryptMode}
     * @param paddingMode  padding mode {@link AESUtils.AesPaddingMode}
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decrypt(final String encryptedStr, String key, String iv,
                                 AesEncryptMode encryptMode, AesPaddingMode paddingMode) throws JarvisException {
        Assert.notBlank(encryptedStr, "encryptedStr must not be blank!");
        // secret key
        SecretKey secretKey = generateAesKey(key);
        if (iv == null || iv.length() == 0) {
            iv = IV_DEFAULT;
        }
        // working mode
        String workingMode = generateAesWorkingMode(encryptMode, paddingMode);
        try {
            Cipher encipher = Cipher.getInstance(workingMode);
            // initialize the Cipher object and set it to decryption mode
            if (AesEncryptMode.ecb.equals(encryptMode)) {
                encipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                // initialization vector
                final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
                encipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            }
            // BASE64 encrypt
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedStr);
            // AES encrypt
            return new String(encipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * encrypt string based on CFB working mode, with default key and iv
     *
     * @param str str to be encrypted
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByCfb(final String str) throws JarvisException {
        return encryptByCfb(str, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * encrypt string based on CFB working mode
     *
     * @param str str to be encrypted
     * @param key encryption key
     * @param iv  offset
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByCfb(final String str, String key, String iv) throws JarvisException {
        return encrypt(str, key, iv, AesEncryptMode.cfb, AesPaddingMode.none);
    }

    /**
     * decrypt strings based on CFB working mode, with default key and iv
     *
     * @param encryptedStr encrypted str
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByCfb(final String encryptedStr) throws JarvisException {
        return decryptByCfb(encryptedStr, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * decrypt strings based on CFB working mode
     *
     * @param encryptedStr encrypted str
     * @param key          encryption key
     * @param iv           offset
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByCfb(final String encryptedStr, String key, String iv) throws JarvisException {
        return decrypt(encryptedStr, key, iv, AesEncryptMode.cfb, AesPaddingMode.none);
    }

    /**
     * encrypt string based on OFB working mode, with default key and iv
     *
     * @param str str to be encrypted
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByOfb(final String str) throws JarvisException {
        return encryptByOfb(str, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * encrypt string based on OFB working mode
     *
     * @param str str to be encrypted
     * @param key encryption key
     * @param iv  offset
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByOfb(final String str, String key, String iv) throws JarvisException {
        return encrypt(str, key, iv, AesEncryptMode.ofb, AesPaddingMode.none);
    }

    /**
     * decrypt strings based on OFB working mode, with default key and iv
     *
     * @param encryptedStr encrypted str
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByOfb(final String encryptedStr) throws JarvisException {
        return decryptByOfb(encryptedStr, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * decrypt strings based on OFB working mode
     *
     * @param encryptedStr encrypted str
     * @param key          encryption key
     * @param iv           offset
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByOfb(final String encryptedStr, String key, String iv) throws JarvisException {
        return decrypt(encryptedStr, key, iv, AesEncryptMode.ofb, AesPaddingMode.none);
    }

    /**
     * encrypt str based on ECB working mode, with default key
     *
     * @param str str to be encrypted
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByEcb(final String str) throws JarvisException {
        return encryptByEcb(str, AESUtils.KEY_DEFAULT);
    }

    /**
     * encrypt str based on ECB working mode
     *
     * @param str str to be encrypted
     * @param key encryption key
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByEcb(final String str, String key) throws JarvisException {
        return encrypt(str, key, null, AesEncryptMode.ecb, AesPaddingMode.pkcs5);
    }

    /**
     * decrypt strings based on ECB working mode, with default key
     *
     * @param encryptedStr encrypted str
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByEcb(final String encryptedStr) throws JarvisException {
        return decryptByEcb(encryptedStr, AESUtils.KEY_DEFAULT);
    }

    /**
     * decrypt strings based on ECB working mode
     *
     * @param encryptedStr encrypted str
     * @param key          encryption key
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByEcb(final String encryptedStr, String key) throws JarvisException {
        return decrypt(encryptedStr, key, null, AesEncryptMode.ecb, AesPaddingMode.pkcs5);
    }

    /**
     * encrypt string based on CBC working mode, with default key and iv
     *
     * @param str str to be encrypted
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByCbc(final String str) throws JarvisException {
        return encryptByCbc(str, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * encrypt string based on CBC working mode
     *
     * @param str str to be encrypted
     * @param key encryption key
     * @param iv  offset
     * @return encrypted str
     * @throws JarvisException jarvis exception
     */
    public static String encryptByCbc(final String str, String key, String iv) throws JarvisException {
        return encrypt(str, key, iv, AesEncryptMode.cbc, AesPaddingMode.pkcs5);
    }

    /**
     * decrypt strings based on CBC working mode, with default key and iv
     *
     * @param encryptedStr encrypted str
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByCbc(final String encryptedStr) throws JarvisException {
        return decryptByCbc(encryptedStr, AESUtils.KEY_DEFAULT, AESUtils.IV_DEFAULT);
    }

    /**
     * decrypt strings based on CBC working mode
     *
     * @param encryptedStr encrypted str
     * @param key          encryption key
     * @param iv           offset
     * @return decrypted str
     * @throws JarvisException jarvis exception
     */
    public static String decryptByCbc(final String encryptedStr, String key, String iv) throws JarvisException {
        return decrypt(encryptedStr, key, iv, AesEncryptMode.cbc, AesPaddingMode.pkcs5);
    }

    public static void main(String[] args) {
        System.out.println(AESUtils.encryptByCfb("qwer123"));
        System.out.println(AESUtils.encryptByOfb("qwer123"));
        System.out.println(AESUtils.encryptByCbc("qwer123"));
        System.out.println(AESUtils.encryptByEcb("qwer123"));
        System.out.println(AESUtils.decryptByCfb("FIK9ksmGcA=="));
        System.out.println(AESUtils.decryptByOfb("FIK9ksmGcA=="));
        System.out.println(AESUtils.decryptByCbc("hUIoxC5eFpGzf/DIKFDP/A=="));
        System.out.println(AESUtils.decryptByEcb("jcsYoWGMRenesYmhyhWiWQ=="));
    }
}
