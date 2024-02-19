package com.feige.fim.crypto;

import com.feige.utils.spi.annotation.SPI;
import com.feige.api.crypto.Cipher;
import com.feige.api.crypto.CipherFactory;


@SPI(value="asymmetricEncryption", interfaces = CipherFactory.class)
public class RsaCipherFactory implements CipherFactory {
    @Override
    public Cipher create(byte[] key1, byte[] key2, Object... args) {
        return new RsaCipher(key1, key2);
    }
}
