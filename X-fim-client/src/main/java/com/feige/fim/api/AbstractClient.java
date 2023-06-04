package com.feige.fim.api;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feige<br />
 * @ClassName: AbstractClient <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public abstract class AbstractClient implements Client {

    protected final AtomicBoolean connected = new AtomicBoolean();
    @Override
    public void initialize() {

    }

    @Override
    public void connect(InetSocketAddress remoteAddress) {
        this.tryConnect(remoteAddress, this::doConnect);
    }

    @Override
    public void stop() {
        this.tryStop(null, this::doStop);
    }
    
    protected void tryConnect(InetSocketAddress remoteAddress,  Fun<InetSocketAddress> fun){
        if (connected.compareAndSet(false, true)){
            try {
                this.initialize();
                fun.apply(remoteAddress);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }else {
            if (throwIfConnected()){
                throw new IllegalStateException("service already connected.");
            }
        }
    }
    
    protected void tryStop(Object obj, Fun<Object> fun){
        if (connected.compareAndSet(true, false)){
            try {
                fun.apply(obj);
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
    public void reconnect() {
        
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }

    protected abstract void doConnect(InetSocketAddress remoteAddress);
    
    
    protected abstract void doStop(Object obj);


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
