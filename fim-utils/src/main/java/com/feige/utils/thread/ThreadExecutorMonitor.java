package com.feige.utils.thread;

import com.feige.utils.logger.Loggers;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExecutorMonitor {

    private static final Logger logger = Loggers.TASK;

    private static final Map<Long, Long> taskExecuteTimeMap = new ConcurrentHashMap<>();
    private static final Map<Long, String> taskClassNames = new ConcurrentHashMap<>();
    private static final long MAX_EXECUTE_TIME = 30*60*1000L; // 最大执行时间30分钟

    private static final ThreadLocal<Map<String, AtomicInteger>> cacheInvokes = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Exception>> cacheInvokeStacktraces = new ThreadLocal<>();
    private static final ThreadLocal<String> cacheInvokerName = new ThreadLocal<>();
    private static final int MAX_INVOKE_NUM = 20;

    private static final boolean monitor = false;



    public static void beginExecuteTask(String taskClassName){
        if(!monitor){
            return;
        }
        taskExecuteTimeMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
        taskClassNames.put(Thread.currentThread().getId(), taskClassName);
        beginCacheInvoke(taskClassName);
    }

    public static void endExecuteTask(){
        if(!monitor){
            return;
        }
        taskExecuteTimeMap.remove(Thread.currentThread().getId());
        taskClassNames.remove(Thread.currentThread().getId());
        endCacheInvoke();
    }

    public static Map<String, Long> getTimeOutTaskName(){
        if(!monitor){
            return Collections.emptyMap();
        }
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Long> taskExecuteTimeInfo = new HashMap<>();
        for(Long threadId : taskExecuteTimeMap.keySet()){
            long timeMillis = currentTimeMillis - taskExecuteTimeMap.get(threadId);
            if(timeMillis > MAX_EXECUTE_TIME){
                taskExecuteTimeInfo.put(taskClassNames.get(threadId), timeMillis);
            }
        }
        return taskExecuteTimeInfo;
    }


    public static void beginCacheInvoke(String invokerName){
        if(!monitor){
            return;
        }
        cacheInvokerName.set(invokerName);
        Map<String, AtomicInteger> cacheInvokeInfo = new ConcurrentHashMap<>();
        cacheInvokes.set(cacheInvokeInfo);
        Map<String, Exception> stacktraces = new ConcurrentHashMap<>();
        cacheInvokeStacktraces.set(stacktraces);
    }

    public static void cacheMethodInvoke(String methodName){
        if(!monitor){
            return;
        }
        Map<String, AtomicInteger> cacheInvokeInfo = cacheInvokes.get();
        if(cacheInvokeInfo != null){
            AtomicInteger invokeNum = cacheInvokeInfo.get(methodName);
            if(invokeNum == null){
                invokeNum = new AtomicInteger(0);
                AtomicInteger tmp = cacheInvokeInfo.putIfAbsent(methodName, invokeNum);
                if(tmp != null){
                    invokeNum = tmp;
                }
                cacheInvokeInfo.put(methodName, invokeNum);
            }
            invokeNum.incrementAndGet();
            cacheInvokeStacktraces.get().put(methodName, new Exception());
        }
    }

    public static void endCacheInvoke(){
        if(!monitor){
            return;
        }
        try {
            Map<String, AtomicInteger> cacheInvokeInfo = cacheInvokes.get();
            if(cacheInvokeInfo == null){
                return;
            }
            int totalNum = 0;
            boolean printHeader = true;
            for (String key : cacheInvokeInfo.keySet()) {
                AtomicInteger invokeNum = cacheInvokeInfo.get(key);
                if (invokeNum != null) {
                    if(invokeNum.get() > MAX_INVOKE_NUM) {
                        if (printHeader) {
                            logger.warn("------------------" + cacheInvokerName.get());
                            printHeader = false;
                        }
                        logger.warn(key + " invoke num : " + invokeNum.get());
                        logger.warn("stacktrace" , cacheInvokeStacktraces.get().get(key));
                    }
                    totalNum += invokeNum.get();
                }
            }
            if(totalNum > MAX_INVOKE_NUM) {
                logger.warn("------------------" + cacheInvokerName.get() + " invoke totalNum : " + totalNum);
                logger.warn("");
            }
        }finally {
            cacheInvokes.remove();
            cacheInvokerName.remove();
            cacheInvokeStacktraces.remove();
        }

    }
}
