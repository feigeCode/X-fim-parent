package com.feige.api.crypto;

public interface CipherFactory {
    
    
    Cipher create(byte[] key1, byte[] key2, Object... args);
}
