package com.feige.api.cipher;

public interface CipherFactory {
    
    
    Cipher create(String key, String... args) throws Exception;
}
