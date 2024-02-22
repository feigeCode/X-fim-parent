package com.feige.api.sc;

import java.util.concurrent.CompletableFuture;

/**
 * @author feige<br />
 * @ClassName: Service <br/>
 * @Description: from mpush <br/>
 * @date: 2023/5/13 14:19<br/>
 */
public interface Service {
    
    void initialize();
    
    void start(Listener listener);

    void stop(Listener listener);

    CompletableFuture<Boolean> start();

    CompletableFuture<Boolean> stop();

    boolean syncStart();

    boolean syncStop();
    
    boolean isRunning();
}
