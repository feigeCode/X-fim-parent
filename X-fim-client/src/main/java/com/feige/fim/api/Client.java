package com.feige.fim.api;

import com.feige.fim.codec.Codec;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: Client <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public interface Client {

    void initialize();

    void connect(InetSocketAddress remoteAddress, ServerStatusListener listener);

    void stop(ServerStatusListener listener);
    
    void reconnect(ServerStatusListener listener);
    
    boolean isConnected();
    /**
     * get codec
     * @return codec
     */
    Codec getCodec();
    
}
