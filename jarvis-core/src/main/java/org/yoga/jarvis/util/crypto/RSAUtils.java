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
import org.yoga.jarvis.bean.Pair;
import org.yoga.jarvis.exception.JarvisException;
import org.yoga.jarvis.util.Assert;
import org.yoga.jarvis.util.BinaryUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Description: RSA加密工具类
 * @Author yoga
 * @Date 2022-05-10 17:40
 */
public class RSAUtils {

    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * key algorithm
     */
    private static final String ALGORITHM = "RSA";

    /**
     * key signature algorithm
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * The default key length is 1024
     */
    private static final int KEY_SIZE_DEFAULT = 1024;


    /**
     * generate key pair
     *
     * @param keySize the size of key,
     *                the key length must be a multiple of 64, between 512 and 65536 bits.
     * @return key pair
     * @throws JarvisException jarvis exception
     */
    public static KeyPair generateKeysPair(int keySize) throws JarvisException {
        Assert.isTrue(512 <= keySize && keySize <= 65536, "keySize must between 512 and 65536 bits!");
        Assert.isTrue(BinaryUtils.isMultiple(keySize, 64), "keySize must be a multiple of 64!");

        // KeyPairGenerator is used to generate public and private key pairs.
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * generate key pair with default keySize
     *
     * @return key pair
     * @throws JarvisException jarvis exception
     */
    public static KeyPair generateKeysPair() throws JarvisException {
        return generateKeysPair(KEY_SIZE_DEFAULT);
    }

    /**
     * generate key string pair
     *
     * @param keySize the size of key,
     *                the key length must be a multiple of 64, between 512 and 65536 bits.
     * @return {@link org.yoga.jarvis.bean.Pair} key string pair,
     * left of pair is privateKey, right of pair is publicKey(maybe left and right all null{@link Pair#nullPair()})
     * @throws JarvisException jarvis exception
     */
    public static Pair<String, String> generateKeysPairStr(int keySize) throws JarvisException {
        KeyPair keyPair = generateKeysPair(keySize);
        if (null == keyPair) {
            return Pair.nullPair();
        }
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return Pair.of(Base64.getEncoder().encodeToString(privateKey.getEncoded()),
                Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }

    /**
     * generate key string pair with default keySize
     *
     * @return {@link org.yoga.jarvis.bean.Pair} key string pair,
     * left of pair is privateKey, right of pair is publicKey(maybe left and right all null{@link Pair#nullPair()})
     * @throws JarvisException jarvis exception
     */
    public static Pair<String, String> generateKeysPairStr() throws JarvisException {
        return generateKeysPairStr(KEY_SIZE_DEFAULT);
    }

    /**
     * encrypt by private key
     *
     * @param data       data to be encrypted
     * @param privateKey private key
     * @return string encrypted data
     * @throws JarvisException jarvis exception
     */
    public static String encryptByPrivateKey(final String data, final String privateKey) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(privateKey, "privateKey must not be blank!");

        // get private key
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // generate private key
            PrivateKey aPrivateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, aPrivateKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * decrypt by public key
     *
     * @param data      data to be decrypted
     * @param publicKey public key
     * @return string decrypted data
     * @throws JarvisException jarvis exception
     */
    public static String decryptByPublicKey(final String data, final String publicKey) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(publicKey, "publicKey must not be blank!");

        // get public key
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // initializes the public key, creating a new X509EncodedKeySpec based on the given encoded key.
            PublicKey aPublicKey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, aPublicKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * encrypt by public key
     *
     * @param data      data to be encrypted
     * @param publicKey public key
     * @return string encrypted data
     * @throws JarvisException jarvis exception
     */
    public static String encryptByPublicKey(final String data, final String publicKey) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(publicKey, "publicKey must not be blank!");

        // get public key
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // initializes the public key, creating a new X509EncodedKeySpec based on the given encoded key.
            PublicKey aPublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, aPublicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * decrypt by private key
     *
     * @param data       data to be decrypted
     * @param privateKey private key
     * @return string decrypted data
     * @throws JarvisException jarvis exception
     */
    public static String decryptByPrivateKey(final String data, final String privateKey) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(privateKey, "privateKey must not be blank!");

        // get private key
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // generate private key
            PrivateKey aPrivateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, aPrivateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.getMessage(), e);
            throw new JarvisException(e);
        }
    }

    /**
     * sign
     *
     * @param data       original str
     * @param privateKey private key
     * @return sign
     * @throws JarvisException jarvis exception
     */
    public static String sign(final String data, final String privateKey) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(privateKey, "privateKey must not be blank!");

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // generate private key
            PrivateKey aPrivateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(aPrivateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new JarvisException(e.getMessage());
        }
    }

    /**
     * verify
     *
     * @param data      original str
     * @param publicKey public key
     * @param sign      sign
     * @return Whether the signature is passed
     * @throws JarvisException jarvis exception
     */
    public static boolean verify(final String data, final String publicKey, final String sign) throws JarvisException {
        Assert.notBlank(data, "data must not be blank!");
        Assert.notBlank(publicKey, "publicKey must not be blank!");
        Assert.notBlank(sign, "sign must not be blank!");

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            // generate public key
            PublicKey aPublicKey = keyFactory.generatePublic(x509KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(aPublicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new JarvisException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Pair<String, String> pair = generateKeysPairStr();
        System.out.println(pair.getLeft());
        System.out.println(pair.getRight());
        System.out.println(encryptByPrivateKey("qwer1234", pair.getLeft()));
        System.out.println(decryptByPublicKey(encryptByPrivateKey("qwer1234", pair.getLeft()), pair.getRight()));
    }
}
