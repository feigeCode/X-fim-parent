package com.feige.fim.encrypt.impl;

import com.feige.fim.encrypt.Cipher;
import com.feige.fim.encrypt.utils.AesUtils;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher implements Cipher {
    private final String key;
    
    private final String iv;
    
    private SecretKeySpec secretKeySpec;
    
    private IvParameterSpec ivParameterSpec;

    public final int keySize;
    
    public AesCipher(String key, String iv) {
        this(key, iv, AesUtils.DEFAULT_KEY_SIZE);
    }

    public AesCipher(String key, String iv, int keySize) {
        this.key = key;
        this.iv = iv;
        this.keySize = keySize;
    }

    @Override
    public void initialize() throws Exception {
        this.secretKeySpec = AesUtils.getKeySpec(this.key.getBytes(), this.keySize);
        this.ivParameterSpec = AesUtils.getIv(iv.getBytes());
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return AesUtils.decrypt(data, secretKeySpec, ivParameterSpec);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return AesUtils.encrypt(data, secretKeySpec, ivParameterSpec);
    }

    @Override
    public String toString() {
        return "AesCipher{" +
                "key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                ", keySize=" + keySize +
                '}';
    }
}
