package com.feige.api.sc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author mpush <br />
 * @ClassName: FutureListener <br/>
 * @Description: from mpush<br/>
 * @date: 2023/5/18 22:48<br/>
 */
public class FutureListener extends CompletableFuture<Boolean> implements Listener {
    private final Listener listener;
    private final AtomicBoolean started;

    public FutureListener(AtomicBoolean started) {
        this.listener = null;
        this.started = started;
    }
    public FutureListener(Listener listener, AtomicBoolean started) {
        this.listener = listener;
        this.started = started;
    }

    @Override
    public void onSuccess(Object... args) {
        if (isDone()){
            return;
        }
        complete(this.started.get());
        if (this.listener != null){
            this.listener.onSuccess(args);
        }
    }

    /**
     * 防止服务长时间卡在某个地方，增加超时监控
     *
     * @param service 服务
     */
    public void monitor(ServiceAdapter service) {
        if (isDone()) {
            return;// 防止Listener被重复执行
        }
        runAsync(() -> {
            try {
                this.get(service.timeoutMillis(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                this.onFailure(new ServiceException(String.format("service %s monitor timeout", service.getClass().getSimpleName())));
            }
        });
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void onFailure(Throwable cause) {
        if (isDone()){
            completeExceptionally(cause);
        }
        if (this.listener != null){
            this.listener.onFailure(cause);
        }
        throw cause instanceof ServiceException ? (ServiceException) cause : new ServiceException(cause);
    }
}
