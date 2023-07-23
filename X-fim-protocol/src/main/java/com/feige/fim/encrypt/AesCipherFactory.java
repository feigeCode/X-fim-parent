package com.feige.fim.encrypt;

import com.feige.api.annotation.SpiComp;
import com.feige.api.cipher.Cipher;
import com.feige.api.cipher.CipherFactory;


@SpiComp("aes")
public class AesCipherFactory  implements CipherFactory {
    @Override
    public Cipher create(String key, String... args) throws Exception {
        AesCipher aesCipher = new AesCipher(key, args[0]);
        aesCipher.initialize();
        return aesCipher;
    }
}
