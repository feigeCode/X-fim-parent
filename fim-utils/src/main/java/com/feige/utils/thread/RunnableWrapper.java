package com.feige.utils.thread;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

public class RunnableWrapper implements Runnable {

    private MeterRegistry registry;
    private Timer executionTimer;
    private Timer idleTimer;
    private Timer.Sample idleSample;

    private final Runnable task;

    private final ClassLoader classLoader;

    public RunnableWrapper(Runnable task, MeterRegistry registry, Timer executionTimer, Timer idleTimer) {
        this(task, task.getClass().getClassLoader(), registry, executionTimer, idleTimer);
    }

    public RunnableWrapper(Runnable task, ClassLoader classLoader, MeterRegistry registry, Timer executionTimer, Timer idleTimer) {
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
    public void run() {
        Timer.Sample executionSample = null;
        if (this.registry != null) {
            this.idleSample.stop(this.idleTimer);
            executionSample = Timer.start(registry);
        }
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (classLoader != null) Thread.currentThread().setContextClassLoader(classLoader);
            if (task != null) task.run();
        }finally {
            if (classLoader != null) Thread.currentThread().setContextClassLoader(originClassLoader);
            if (executionSample != null) executionSample.stop(this.executionTimer);
        }
    }
}
