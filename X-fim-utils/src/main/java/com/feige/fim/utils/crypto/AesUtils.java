package com.feige.fim.utils.crypto;


import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author feige
 */

public class AesUtils {

    static final String AES = "AES";
    static final String SHA1_PRNG = "SHA1PRNG";
    public static final String KEY_ALGORITHM_PADDING = "AES/CBC/PKCS5Padding";
    

    public static SecretKeySpec getKeySpec(byte[] key)  {
        if (key.length == 0){
            return null;
        }
        try {
//            KeyGenerator keygen = KeyGenerator.getInstance(AES);
//            SecureRandom random = SecureRandom.getInstance(SHA1_PRNG);
//            random.setSeed(key);
//            keygen.init(random);
//            SecretKey secretKey = keygen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
            return new SecretKeySpec(key, AES);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
       
    }
    
    public static IvParameterSpec getIv(byte[] iv){
        if (iv.length == 0){
            return null;
        }
        return new IvParameterSpec(iv);
    }

    public static byte[] encrypt(byte[] data, SecretKeySpec secretKeySpec, IvParameterSpec zeroIv) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
            return Base64.encode(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] data, SecretKeySpec secretKeySpec, IvParameterSpec zeroIv) {
        try {
            data = Base64.decode(data);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
