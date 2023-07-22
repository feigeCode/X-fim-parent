package com.feige.fim.encrypt.impl;

import com.feige.api.cipher.Cipher;
import com.feige.api.cipher.CipherFactory;

public class RsaCipherFactory implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) {
        return new RsaCipher(key, args[0]);
    }
}
