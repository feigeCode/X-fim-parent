package com.feige.fim.adapter;

import com.feige.fim.ack.TimerExecutor;
import com.feige.utils.spi.annotation.SPI;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SPI(interfaces = TimerExecutor.class)
public class WheelTimerExecutor implements TimerExecutor {
    private final HashedWheelTimer wheelTimer = new HashedWheelTimer();
    @Override
    public Future<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        Timeout timeout = wheelTimer.newTimeout(t -> runnable.run(), delay, unit);
        return new TimeoutFuture<>(timeout);
    }

    public static class TimeoutFuture<T> implements Future<T> {

        private final Timeout timeout;

        public TimeoutFuture(Timeout timeout) {
            this.timeout = timeout;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return timeout.cancel();
        }

        @Override
        public boolean isCancelled() {
            return timeout.isCancelled();
        }

        @Override
        public boolean isDone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new UnsupportedOperationException();
        }
    }
}
