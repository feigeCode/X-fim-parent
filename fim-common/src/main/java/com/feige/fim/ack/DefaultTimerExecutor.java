package com.feige.fim.ack;

import com.feige.utils.spi.annotation.SPI;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



@SPI(interfaces = TimerExecutor.class)
public class DefaultTimerExecutor implements TimerExecutor {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    @Override
    public Future<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return executorService.schedule(runnable, delay, unit);
    }
}
