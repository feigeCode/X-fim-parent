package com.feige.utils.crypto;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author feige
 */
public class DesUtils {

    private static final String DES = "DES";

    public static String encrypt(String data, String key) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), key);
    }

    public static String encrypt(byte[] data, String key) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptData, String key) {
        return new String(decrypt2Byte(encryptData, key));
    }

    public static byte[] decrypt2Byte(String encryptData, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return cipher.doFinal(Base64.getDecoder().decode(encryptData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
