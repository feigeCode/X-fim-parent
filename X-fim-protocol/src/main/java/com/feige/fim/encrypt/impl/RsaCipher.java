package com.feige.fim.encrypt.impl;

import com.feige.api.cipher.Cipher;


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
        
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return new byte[0];
    }
}
