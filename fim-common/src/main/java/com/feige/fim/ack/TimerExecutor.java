package com.feige.fim.ack;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface TimerExecutor {
    Future<?> schedule(Runnable runnable, long delay, TimeUnit unit);
}
