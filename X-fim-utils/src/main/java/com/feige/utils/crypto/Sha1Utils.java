package com.feige.utils.crypto;


import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author feige
 */

public class Sha1Utils {
    private static MessageDigest digest = null;

    static {
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String encrypt(String data) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(byte[] bytes) {
        return ByteUtils.toHexString(encrypt2Byte(bytes));
    }

    public static byte[] encrypt2Byte(byte[] bytes) {
        try {
            digest.update(bytes);
            return digest.digest();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }


}
