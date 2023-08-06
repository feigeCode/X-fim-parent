package com.feige.api.crypto;

import java.util.Map;

public interface Cipher {
    
    byte[] decrypt(byte[] data);

    byte[] encrypt(byte[] data);
    
    String[] getArgs();
    
}
