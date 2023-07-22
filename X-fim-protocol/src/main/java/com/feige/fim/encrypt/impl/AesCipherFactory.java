package com.feige.fim.encrypt.impl;

import com.feige.api.cipher.Cipher;
import com.feige.api.cipher.CipherFactory;


public class AesCipherFactory  implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) throws Exception {
        AesCipher aesCipher = new AesCipher(key, args[0]);
        aesCipher.initialize();
        return aesCipher;
    }
}
