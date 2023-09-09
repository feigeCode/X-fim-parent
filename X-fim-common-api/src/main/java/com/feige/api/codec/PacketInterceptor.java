package com.feige.api.codec;

import com.feige.api.session.Session;

public interface PacketInterceptor {
    
    void writePacket(Session session, Object packet);
    
    
    void readPacket(Session session, Object packet);
    
    
    int order();
    
}
