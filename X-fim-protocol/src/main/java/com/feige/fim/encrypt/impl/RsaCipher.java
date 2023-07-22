package com.feige.fim.encrypt.impl;

import com.feige.api.cipher.Cipher;
import com.feige.fim.encrypt.utils.RsaUtils;


import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaCipher implements Cipher {

    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;
    
    private final String privateKey;
    private final String publicKey;

    public RsaCipher(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    
    @Override
    public void initialize() {
        this.rsaPrivateKey = RsaUtils.getPrivateKey(this.privateKey);
        this.rsaPublicKey = RsaUtils.getPublicKey(this.publicKey);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return RsaUtils.decrypt(data, this.rsaPrivateKey);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return RsaUtils.encrypt(data, this.rsaPublicKey);
    }

    @Override
    public String toString() {
        return "RsaCipher{" +
                "privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
