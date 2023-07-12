package com.feige.fim.msg;

import java.io.Serializable;

public interface Msg extends Serializable {
    
    
    void encryptData();
    
    
    void decryptData();
    
}
