package com.feige.fim.encrypt;

import com.feige.annotation.SpiComp;
import com.feige.api.cipher.Cipher;
import com.feige.api.cipher.CipherFactory;
import com.google.auto.service.AutoService;

@SpiComp("rsa")
@AutoService(CipherFactory.class)
public class RsaCipherFactory implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) {
        RsaCipher rsaCipher = new RsaCipher(key, args[0]);
        rsaCipher.initialize();
        return rsaCipher;
    }
}
