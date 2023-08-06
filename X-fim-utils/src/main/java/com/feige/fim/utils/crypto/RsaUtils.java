package com.feige.fim.utils.crypto;


import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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
    
    public static RSAPublicKey getPublicKey(byte[] publicKey) {
        if (publicKey.length == 0){
            return null;
        }
        try {
            return  (RSAPublicKey)KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPrivateKey getPrivateKey(byte[] privateKey) {
        if (privateKey.length == 0){
            return null;
        }
        try {
            return  (RSAPrivateKey)KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] data, RSAPublicKey pubKey) {
        try {
           
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
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

            return Base64.encode(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] encryptData, RSAPrivateKey priKey) {
        try {
            byte[] inputByte = Base64.decode(encryptData);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
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
}
