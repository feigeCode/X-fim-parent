package com.feige.fim.encrypt;

public interface CipherFactory {
    
    
    Cipher create(String key, String... args);
}
