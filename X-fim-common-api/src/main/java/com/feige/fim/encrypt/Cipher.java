package com.feige.fim.encrypt;

public interface Cipher {

    void initialize() throws Exception;
    
    byte[] decrypt(byte[] data);

    byte[] encrypt(byte[] data);
    
}
