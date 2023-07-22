package com.feige.fim.encrypt.impl;

import com.feige.api.annotation.SpiComp;
import com.feige.api.cipher.Cipher;
import com.feige.api.cipher.CipherFactory;

@SpiComp("rsa")
public class RsaCipherFactory implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) {
        RsaCipher rsaCipher = new RsaCipher(key, args[0]);
        rsaCipher.initialize();
        return rsaCipher;
    }
}
