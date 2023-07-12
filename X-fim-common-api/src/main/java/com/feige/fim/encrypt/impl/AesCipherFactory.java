package com.feige.fim.encrypt.impl;

import com.feige.fim.encrypt.Cipher;
import com.feige.fim.encrypt.CipherFactory;


public class AesCipherFactory  implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) {
        return new AesCipher(key, args[0]);
    }
}
