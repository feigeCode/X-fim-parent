package com.feige.fim.crypto;

import com.feige.framework.annotation.SpiComp;
import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;
import com.google.auto.service.AutoService;


@SpiComp("aes")
@AutoService(CipherFactory.class)
public class AesCipherFactory  implements CipherFactory {
    @Override
    public Cipher create(byte[] key1, byte[] key2, Object... args) {
        return new AesCipher(key1, key2);
    }
}
