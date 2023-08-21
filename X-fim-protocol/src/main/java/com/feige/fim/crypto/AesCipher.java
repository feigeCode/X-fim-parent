package com.feige.fim.crypto;

import com.feige.api.crypto.Cipher;
import com.feige.utils.crypto.AesUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class AesCipher implements Cipher {
    private final byte[] key;
    
    private final byte[] iv;
    
    private SecretKeySpec secretKeySpec;
    
    private IvParameterSpec ivParameterSpec;
    
    public AesCipher(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
        this.secretKeySpec = AesUtils.getKeySpec(this.key);
        this.ivParameterSpec = AesUtils.getIv(this.iv);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return AesUtils.decrypt(data, secretKeySpec, ivParameterSpec);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return AesUtils.encrypt(data, secretKeySpec, ivParameterSpec);
    }

    @Override
    public String[] getArgs() {
        String[] args = new String[2];
        args[0] = Base64.toBase64String(key);
        args[1] = Base64.toBase64String(iv);
        return args;
    }

    @Override
    public String toString() {
        return Arrays.toString(getArgs());
    }
}
