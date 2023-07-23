package com.feige.fim.utils.encrypt;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author feige
 */

public class AesUtils {

    static final String AES = "AES";
    static final String SHA1_PRNG = "SHA1PRNG";
    public static final String KEY_ALGORITHM_PADDING = "AES/CBC/PKCS5Padding";
    public static final int DEFAULT_KEY_SIZE = 128;
    

    public static SecretKeySpec getKeySpec(byte[] key, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        SecureRandom random = SecureRandom.getInstance(SHA1_PRNG);
        random.setSeed(key);
        keygen.init(keySize, random);
        SecretKey secretKey = keygen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return new SecretKeySpec(enCodeFormat, AES);
    }
    
    public static IvParameterSpec getIv(byte[] iv){
        return new IvParameterSpec(iv);
    }
    
    public static byte[] encrypt(byte[] data, String key, String iv, int keySize) {
        try {
            SecretKeySpec secretKeySpec = getKeySpec(key.getBytes(StandardCharsets.UTF_8), keySize);
            final IvParameterSpec ivParameterSpec = getIv(iv.getBytes(StandardCharsets.UTF_8));
            return encrypt(data, secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] data, SecretKeySpec secretKeySpec, IvParameterSpec zeroIv) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static byte[] decrypt(String encryptData, String key, String iv, int keySize) {
        try {
            SecretKeySpec secretKeySpec = getKeySpec(key.getBytes(StandardCharsets.UTF_8), keySize);
            final IvParameterSpec ivParameterSpec = getIv(iv.getBytes(StandardCharsets.UTF_8));
            return decrypt(encryptData.getBytes(StandardCharsets.UTF_8), secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] data, SecretKeySpec secretKeySpec, IvParameterSpec zeroIv) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
