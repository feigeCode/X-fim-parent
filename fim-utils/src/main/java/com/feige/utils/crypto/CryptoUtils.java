package com.feige.utils.crypto;


import java.security.SecureRandom;

public class CryptoUtils {

    public static byte[] randomAesKey(int keySize) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[keySize];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] randomAesIv(int size) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] mixKey(byte[] clientKey, byte[] serverKey, int keySize) {
        byte[] sessionKey = new byte[keySize];
        for (int i = 0; i < keySize; i++) {
            byte a = clientKey[i];
            byte b = serverKey[i];
            int sum = Math.abs(a + b);
            int c = (sum % 2 == 0) ? a ^ b : b ^ a;
            sessionKey[i] = (byte) c;
        }
        return sessionKey;
    }
}
