

package com.feige.fim.utils.crypto;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * @author feige
 */
public class Sm3Utils {

    public static String encrypt(String data) {
        return encrypt(data.getBytes());
    }

    public static String encrypt(byte[] data) {
        return ByteUtils.toHexString(encrypt2Byte(data));
    }

    public static byte[] encrypt2Byte(byte[] data) {
        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);
        byte[] encryptByte = new byte[sm3.getDigestSize()];
        sm3.doFinal(encryptByte, 0);
        return encryptByte;
    }

    public static String encrypt(String data, String key) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), key);
    }

    public static String encrypt(byte[] data, String key) {
        return ByteUtils.toHexString(encrypt2Byte(data, key));
    }

    public static byte[] encrypt2Byte(byte[] data, String key) {
        try {
            byte[] keyByte = key.getBytes(StandardCharsets.UTF_8);
            KeyParameter keyParameter = new KeyParameter(keyByte);
            SM3Digest sm3 = new SM3Digest();
            HMac hMac = new HMac(sm3);
            hMac.init(keyParameter);
            hMac.update(data, 0, data.length);
            byte[] result = new byte[hMac.getMacSize()];
            hMac.doFinal(result, 0);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
