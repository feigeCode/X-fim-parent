package com.feige.api.sc;

import com.feige.api.handler.RemotingException;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: AbstractClient <br/>
 * @Description: <br/>
 * @date: 2023/5/13 14:33<br/>
 */
public abstract class AbstractClient extends ServiceAdapter implements Client {
    protected InetSocketAddress remoteAddress;


    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void reconnect() throws RemotingException {
        tryReconnect();
    }
    
    
    protected abstract void tryReconnect();
}
