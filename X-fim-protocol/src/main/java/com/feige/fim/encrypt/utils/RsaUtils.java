package com.feige.fim.encrypt.utils;


import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * @author feige
 */
public class RsaUtils {

    private static final String RSA = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int DEFAULT_KEY_SIZE = 1024;


    public static KeyPair generateKey() {
        try {
            KeyPairGenerator pairGen = KeyPairGenerator.getInstance(RSA);
            SecureRandom random = new SecureRandom();
            pairGen.initialize(DEFAULT_KEY_SIZE, random);
            return pairGen.generateKeyPair();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String keyEncrypt(Key key) {
        return Base64.toBase64String(key.getEncoded());
    }

    public static String encrypt(byte[] data, PublicKey publicKey) {
        return encrypt(data, keyEncrypt(publicKey));
    }

    public static String encrypt(String data, PublicKey publicKey) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), keyEncrypt(publicKey));
    }

    public static String encrypt(String data, String publicKey) throws Exception {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), publicKey);
    }

    public static String encrypt(byte[] data, String publicKey) {
        try {
            byte[] decoded = Base64.decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(1, pubKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * MAX_ENCRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            return Base64.toBase64String(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt2Byte(String encryptData, String privateKey) {
        try {
            byte[] inputByte = Base64.decode(encryptData.getBytes(StandardCharsets.UTF_8));
            byte[] decoded = Base64.decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(2, priKey);
            int inputLen = inputByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * MAX_DECRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(inputByte, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(inputByte, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptData, String privateKey) {
        return new String(decrypt2Byte(encryptData, privateKey));
    }

    public static String decrypt(String encryptData, PrivateKey privateKey) {
        return decrypt(encryptData, keyEncrypt(privateKey));
    }

    public static byte[] decrypt2Byte(String encryptData, PrivateKey privateKey) {
        return decrypt2Byte(encryptData, keyEncrypt(privateKey));
    }
}
