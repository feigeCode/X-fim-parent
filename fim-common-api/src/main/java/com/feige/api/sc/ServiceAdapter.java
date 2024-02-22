package com.feige.api.sc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author mpush<br />
 * @ClassName: ServiceAdapter <br/>
 * @Description: from mpush<br/>
 * @date: 2023/5/13 14:29<br/>
 */
public abstract class ServiceAdapter implements Service {

    protected final AtomicBoolean started = new AtomicBoolean();
    @Override
    public void initialize() {
        
    }

    @Override
    public void start(Listener listener) {
        this.tryStart(listener, this::doStart);
    }
    
    @Override
    public void stop(Listener listener) {
        this.tryStop(listener, this::doStop);
    }

    @Override
    public CompletableFuture<Boolean> start() {
        FutureListener futureListener = new FutureListener(started);
        start(futureListener);
        return futureListener;
    }
    
    
    

    @Override
    public CompletableFuture<Boolean> stop() {
        FutureListener futureListener = new FutureListener(started);
        stop(futureListener);
        return futureListener;
    }



    @Override
    public boolean syncStart() {
        return start().join();
    }

    @Override
    public boolean syncStop() {
        return stop().join();
    }

    @Override
    public boolean isRunning() {
        return started.get();
    }


    protected void doStart(Listener listener) {
        listener.onSuccess();
    }


    protected void doStop(Listener listener){
        listener.onSuccess();
    }
    
    protected void tryStart(Listener listener, FunctionEx functionEx){
        FutureListener futureListener = wrap(listener);
        if (started.compareAndSet(false, true)){
            try {
                initialize();
                functionEx.apply(futureListener);
                // 主要用于异步，否则应该放置在function.apply(listener)之前
                futureListener.monitor(this);
            }catch (Throwable throwable){
                listener.onFailure(throwable);
                throw new ServiceException(throwable);
            }
        }else {
            if (throwIfStarted()){
                futureListener.onFailure(new ServiceException("service already started."));
            }else {
                listener.onSuccess();
            }
        }
    }

    protected void tryStop(Listener listener, FunctionEx functionEx){
        FutureListener futureListener = wrap(listener);
        if (started.compareAndSet(true, false)) {

            try {
                functionEx.apply(futureListener);
                futureListener.monitor(this);
            }catch (Throwable throwable){
                futureListener.onFailure(throwable);
                throw new ServiceException(throwable);
            }
        }else {
            if (throwIfStopped()){
                listener.onFailure(new ServiceException("service already stopped."));
            }else {
                listener.onSuccess();
            }
        }
    }
    /**
     * 控制当服务已经启动后，重复调用start方法，是否抛出服务已经启动异常
     * 默认是true
     *
     * @return true:抛出异常
     */
    protected boolean throwIfStarted() {
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
    
    
    protected interface FunctionEx {
        void apply(Listener l) throws Throwable;
    }

    /**
     * 服务启动停止，超时时间, 默认是10s
     *
     * @return 超时时间
     */
    protected int timeoutMillis() {
        return 1000 * 10;
    }


    /**
     * 防止Listener被重复执行
     *
     * @param listener listener
     * @return FutureListener
     */
    public FutureListener wrap(Listener listener) {
        if (listener instanceof FutureListener){
            return (FutureListener) listener;
        }
        return new FutureListener(listener, started);
    }
}
