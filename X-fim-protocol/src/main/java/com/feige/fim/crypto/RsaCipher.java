package com.feige.fim.crypto;

import com.feige.api.crypto.Cipher;
import com.feige.utils.crypto.RsaUtils;
import org.bouncycastle.util.encoders.Base64;


import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

public class RsaCipher implements Cipher {

    private final RSAPrivateKey rsaPrivateKey;
    private final RSAPublicKey rsaPublicKey;
    
    private final byte[] privateKey;
    private final byte[] publicKey;

    public RsaCipher(byte[] privateKey, byte[] publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
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
    public String[] getArgs() {
        String[] args = new String[2];
        args[0] = Base64.toBase64String(privateKey);
        args[1] = Base64.toBase64String(publicKey);
        return args;
    }

    @Override
    public String toString() {
        return Arrays.toString(getArgs());
    }
}
