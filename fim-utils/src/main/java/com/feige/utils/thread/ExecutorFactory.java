package com.feige.utils.thread;

import com.feige.utils.micrometer.MeterRegistryHelper;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorFactory {

    private final static String THREAD_PREFIX = "XF-Executors";
    private final static int DEFAULT_CORE_POOL_SIZE = 5;
    private final static int DEFAULT_MAX_POOL_SIZE = 20;
    private final static int PERMIT_MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private final static int MAX_QUEUE_SIZE = 50000;
    private static final  Map<String, ExecutorService> FIXED_EXECUTOR_SERVICE_MAP = new ConcurrentHashMap<>();
    private static final  Map<String, ExecutorService> CACHED_EXECUTOR_SERVICE_MAP = new ConcurrentHashMap<>();
    private static final  Map<String, ScheduledExecutorService> SCHEDULED_EXECUTOR_SERVICE_MAP = new ConcurrentHashMap<>();

    public static ExecutorService createFixedThreadPool(String key,
                                                        int corePoolSize,
                                                        int queueSize){
        return createFixedThreadPool(key, corePoolSize, queueSize, createThreadFactory(key, ThreadPoolType.FIXED), new ThreadPoolExecutor.AbortPolicy());
    }

    public static ExecutorService createFixedThreadPool(String key,
                                                        int corePoolSize,
                                                        int queueSize,
                                                        ThreadFactory threadFactory,
                                                        RejectedExecutionHandler handler){
        ExecutorService fixedThreadPoolExecutor = FIXED_EXECUTOR_SERVICE_MAP.get(key);
        if (fixedThreadPoolExecutor == null){
            synchronized (FIXED_EXECUTOR_SERVICE_MAP){
                fixedThreadPoolExecutor = FIXED_EXECUTOR_SERVICE_MAP.get(key);
                if (fixedThreadPoolExecutor == null) {
                    ThreadPoolExecutor tempExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            corePoolSize,
                            0L,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(queueSize),
                            threadFactory,
                            handler
                    );
                    fixedThreadPoolExecutor = wrap(tempExecutor, key, ThreadPoolType.FIXED);
                    FIXED_EXECUTOR_SERVICE_MAP.put(key, fixedThreadPoolExecutor);
                }
            }
        }
        return fixedThreadPoolExecutor;

    }
    public static ExecutorService createCachedThreadPool(String key,
                                                          int corePoolSize,
                                                          int maximumPoolSize,
                                                          long keepAliveTime,
                                                          int queueSize){
        return createCachedThreadPool(key,corePoolSize, maximumPoolSize, keepAliveTime, queueSize, createThreadFactory(key, ThreadPoolType.CACHED), new ThreadPoolExecutor.AbortPolicy());
    }
    public static ExecutorService createCachedThreadPool(String key,
                                                          int corePoolSize,
                                                          int maximumPoolSize,
                                                          long keepAliveTime,
                                                          int queueSize,
                                                          ThreadFactory threadFactory,
                                                          RejectedExecutionHandler handler){
        ExecutorService cachedThreadPoolExecutor = CACHED_EXECUTOR_SERVICE_MAP.get(key);
        if (cachedThreadPoolExecutor == null){
            synchronized (CACHED_EXECUTOR_SERVICE_MAP){
                cachedThreadPoolExecutor = CACHED_EXECUTOR_SERVICE_MAP.get(key);
                if (cachedThreadPoolExecutor == null){
                    ThreadPoolExecutor tempExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            TimeUnit.SECONDS,
                            queueSize <= 0 ? new SynchronousQueue<>() : new LinkedBlockingQueue<>(queueSize),
                            threadFactory,
                            handler
                    );

                    cachedThreadPoolExecutor = wrap(tempExecutor, key, ThreadPoolType.CACHED);
                    CACHED_EXECUTOR_SERVICE_MAP.put(key, cachedThreadPoolExecutor);
                }
            }
        }
        return cachedThreadPoolExecutor;

    }

    private static ExecutorService wrap(ThreadPoolExecutor executor, String key, ThreadPoolType type){
        String keyAndType = key + "-" + type.name().toLowerCase();
        MeterRegistry meterRegistry = MeterRegistryHelper.getMeterRegistry();
        ThreadPoolExecutorMetrics executorMetrics = new ThreadPoolExecutorMetrics(executor, keyAndType, null);
        executorMetrics.bindTo(meterRegistry);
        return new ExecutorServiceWrapper(key, type, executor, meterRegistry);
    }

    public static ScheduledExecutorService wrap(ScheduledThreadPoolExecutor executor, String key, ThreadPoolType type){
        String keyAndType = key + "-" + type.name().toLowerCase();
        MeterRegistry meterRegistry = MeterRegistryHelper.getMeterRegistry();
        ThreadPoolExecutorMetrics executorMetrics = new ThreadPoolExecutorMetrics(executor, keyAndType, null);
        executorMetrics.bindTo(meterRegistry);
        return new ScheduledExecutorServiceWrapper(key, type, executor, meterRegistry);
    }

    public static ScheduledExecutorService createScheduleThreadPool(String key, int poolSize, ThreadFactory factory){
        ScheduledExecutorService scheduledExecutorService = SCHEDULED_EXECUTOR_SERVICE_MAP.get(key);
        if (scheduledExecutorService == null) {
            synchronized (SCHEDULED_EXECUTOR_SERVICE_MAP) {
                scheduledExecutorService = SCHEDULED_EXECUTOR_SERVICE_MAP.get(key);
                if (scheduledExecutorService == null) {
                    ScheduledThreadPoolExecutor tempExecutor = new ScheduledThreadPoolExecutor(
                            poolSize,
                            factory
                    );
                    scheduledExecutorService = wrap(tempExecutor, key, ThreadPoolType.SCHEDULED);
                    SCHEDULED_EXECUTOR_SERVICE_MAP.put(key, scheduledExecutorService);
                }
            }
        }
        return scheduledExecutorService;

    }
    public static ScheduledExecutorService createScheduleThreadPool(String key, int poolSize){
        return createScheduleThreadPool(key, poolSize, createThreadFactory(key, ThreadPoolType.SCHEDULED));

    }

    public static ThreadFactory createThreadFactory(String key, ThreadPoolType poolType) {
        key = key + "-" + poolType.name();
        return new NameThreadFactory(THREAD_PREFIX + "-" + key);
    }

    public static ExecutorService getDefaultThreadPool(){
        ExecutorService defaultExecutor = CACHED_EXECUTOR_SERVICE_MAP.get("default");
        if (defaultExecutor == null){
            defaultExecutor = createCachedThreadPool(
                    "default",
                    DEFAULT_CORE_POOL_SIZE,
                    DEFAULT_MAX_POOL_SIZE,
                    60L,
                    MAX_QUEUE_SIZE,
                    createThreadFactory("default", ThreadPoolType.CACHED),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }
        return  defaultExecutor;
    }

    public static ExecutorService getFixedThreadPool(String key){
        ExecutorService fixedExecutor = FIXED_EXECUTOR_SERVICE_MAP.get(key);
        if (fixedExecutor == null){
            throw new NullPointerException("The fixed thread pool with [" + key + "] is not created");
        }
        return fixedExecutor;
    }

    public static ExecutorService getCachedThreadPool(String key){
        ExecutorService cachedExecutor = CACHED_EXECUTOR_SERVICE_MAP.get(key);
        if (cachedExecutor == null){
            throw new NullPointerException("The cache thread pool with [" + key + "] is not created");
        }
        return cachedExecutor;
    }

    public static ExecutorService getScheduledThreadPool(String key){
        ScheduledExecutorService scheduledExecutor = SCHEDULED_EXECUTOR_SERVICE_MAP.get(key);
        if (scheduledExecutor == null){
            throw new NullPointerException("The scheduled thread pool with [" + key + "] is not created");
        }
        return scheduledExecutor;
    }

    public static void clearExecutorService(String key, ThreadPoolType type) {
        switch (type){
            case FIXED:
                FIXED_EXECUTOR_SERVICE_MAP.remove(key);
                break;
            case CACHED:
                CACHED_EXECUTOR_SERVICE_MAP.remove(key);
                break;
            case SCHEDULED:
                SCHEDULED_EXECUTOR_SERVICE_MAP.remove(key);
                break;
        }
    }

    public enum ThreadPoolType {
        FIXED,
        CACHED,
        SCHEDULED;
    }


}
