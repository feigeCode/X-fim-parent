package com.feige.fim.api;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: Client <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public interface Client {

    void initialize();

    void connect(InetSocketAddress remoteAddress);

    void stop();
    
    void reconnect();
    
    boolean isConnected();
}
