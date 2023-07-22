package com.feige.api.sc;


import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.Listener;
import com.feige.api.sc.Service;
import com.feige.api.session.Session;

import java.net.InetSocketAddress;


/**
 * @author feige<br />
 * @ClassName: Client <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public interface Client extends Service {
    
    void reconnect(Listener listener);
    
    boolean isConnected();
    /**
     * get codec
     * @return codec
     */
    Codec getCodec();
    
    SessionHandler getSessionHandler();
    
    
    Session getSession();
    
    InetSocketAddress getAddress();
}
