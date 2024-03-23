package com.feige.utils.thread;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


public class ExecutorServiceWrapper implements ExecutorService {

    private final String key;
    private final ExecutorService executorService;

    private final ExecutorFactory.ThreadPoolType type;

    private MeterRegistry registry;
    private Timer executionTimer;
    private Timer idleTimer;

    public ExecutorServiceWrapper(String key, ExecutorFactory.ThreadPoolType type, ExecutorService executorService, MeterRegistry registry) {
        this.key = key;
        this.type = type;
        this.executorService = executorService;
        if (registry != null){
            this.registry = registry;
            Tags tags = Tags.of("poolName", key + "-" + type.name().toLowerCase());
            this.executionTimer = registry.timer("execution", tags);
            this.idleTimer = registry.timer("idle", tags);
        }
    }

    @Override
    public void shutdown() {
        ExecutorFactory.clearExecutorService(key, type);
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        ExecutorFactory.clearExecutorService(key, type);
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(this.wrap(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(this.wrap(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(this.wrap(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(this.wrapAll(tasks));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(this.wrapAll(tasks), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(this.wrapAll(tasks));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(this.wrapAll(tasks), timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(this.wrap(command));
    }

    protected Runnable wrap(Runnable task) {
        return new RunnableWrapper(task, registry, executionTimer, idleTimer);
    }

    protected <T> Callable<T> wrap(Callable<T> task) {
        return new CallableWrapper<>(task, registry, executionTimer, idleTimer);
    }

    private <T> Collection<? extends Callable<T>> wrapAll(Collection<? extends Callable<T>> tasks) {
        return tasks.stream()
                .map(this::wrap)
                .collect(Collectors.toSet());
    }
}
