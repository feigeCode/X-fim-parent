package com.feige.fim.ack;

import com.feige.utils.order.Order;
import com.feige.utils.spi.annotation.SPI;
import com.feige.utils.thread.ExecutorFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



@SPI(interfaces = TimerExecutor.class)
@Order(1)
public class DefaultTimerExecutor implements TimerExecutor {
    private final ScheduledExecutorService executorService = ExecutorFactory.createScheduleThreadPool("ack-timer", 4);
    @Override
    public Future<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return executorService.schedule(runnable, delay, unit);
    }
}
