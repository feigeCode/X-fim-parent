package com.feige.fim.api;


import com.feige.fim.codec.Codec;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feige<br />
 * @ClassName: AbstractClient <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public abstract class AbstractClient implements Client {

    protected InetSocketAddress remoteAddress;
    protected final AtomicInteger reconnectCnt = new AtomicInteger(0);
    public static final int MAX_RECONNECT_CNT = 3;
    
    protected final AtomicBoolean connected = new AtomicBoolean();
    
    protected Codec codec;

    public AbstractClient(Codec codec) {
        this.codec = codec;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void connect(InetSocketAddress remoteAddress, ServerStatusListener listener) {
        assert remoteAddress != null;
        this.remoteAddress = remoteAddress;
        this.tryConnect(listener, this::doConnect);
    }

    @Override
    public void stop(ServerStatusListener listener) {
        this.tryStop(listener, this::doStop);
    }
    
    protected void tryConnect(ServerStatusListener listener,  Fun<ServerStatusListener> fun){
        if (connected.compareAndSet(false, true)){
            try {
                this.initialize();
                fun.apply(listener);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }else {
            if (throwIfConnected()){
                throw new IllegalStateException("service already connected.");
            }
        }
    }
    
    protected void tryStop(ServerStatusListener listener, Fun<ServerStatusListener> fun){
        if (connected.compareAndSet(true, false)){
            try {
                fun.apply(listener);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }else {
            if (throwIfStopped()){
                throw new IllegalStateException("service already stopped.");
            }
        }
    }

  
    @Override
    public void reconnect(ServerStatusListener listener) {
        if (reconnectCnt.incrementAndGet() <= MAX_RECONNECT_CNT) {
            this.doReconnect(listener);
        }
    }
    
    
    protected void doReconnect(ServerStatusListener listener){
        this.stop(listener);
        this.connect(remoteAddress, listener);
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }

    protected abstract void doConnect(ServerStatusListener listener);
    
    
    protected abstract void doStop(ServerStatusListener listener);


    /**
     * 控制当服务已经启动后，重复调用connect方法，是否抛出服务已经连接异常
     * 默认是true
     *
     * @return true:抛出异常
     */
    protected boolean throwIfConnected() {
        return true;
    }

    /**
     * 控制当服务已经停止后，重复调用stop方法，是否抛出服务已经停止异常
     * 默认是true
     *
     * @return true:抛出异常
     */
    protected boolean throwIfStopped() {
        return true;
    }

    /**
     * 服务启动停止，超时时间, 默认是10s
     *
     * @return 超时时间
     */
    protected int timeoutMillis() {
        return 1000 * 10;
    }
    protected interface Fun<T> {
        void apply(T handler) throws Throwable;
    }
}
