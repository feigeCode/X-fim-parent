package com.feige.utils.thread;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.Callable;

public class CallableWrapper<T> implements Callable<T> {

    private MeterRegistry registry;
    private Timer executionTimer;
    private Timer idleTimer;
    private Timer.Sample idleSample;
    private final Callable<T> task;

    private final ClassLoader classLoader;

    public CallableWrapper(Callable<T> task, MeterRegistry registry, Timer executionTimer, Timer idleTimer) {
        this(task, task.getClass().getClassLoader(), registry, executionTimer, idleTimer);
    }

    public CallableWrapper(Callable<T> task, ClassLoader classLoader, MeterRegistry registry, Timer executionTimer, Timer idleTimer) {
        this.task = task;
        this.classLoader = classLoader;
        if (registry != null){
            this.registry = registry;
            this.executionTimer = executionTimer;
            this.idleTimer = idleTimer;
            this.idleSample = Timer.start(registry);
        }
    }

    @Override
    public T call() throws Exception {
        Timer.Sample executionSample = null;
        if (this.registry != null) {
            this.idleSample.stop(this.idleTimer);
            executionSample = Timer.start(registry);
        }
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (classLoader != null) Thread.currentThread().setContextClassLoader(classLoader);
            if (task != null) return task.call();
        }finally {
            if (classLoader != null) Thread.currentThread().setContextClassLoader(originClassLoader);
            if (executionSample != null) executionSample.stop(this.executionTimer);
        }
        return null;
    }
}
