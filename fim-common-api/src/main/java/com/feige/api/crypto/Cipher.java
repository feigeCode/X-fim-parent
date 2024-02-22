package com.feige.api.crypto;


public interface Cipher {
    
    byte[] decrypt(byte[] data);

    byte[] encrypt(byte[] data);
    
    String[] getArgs();
    
}
