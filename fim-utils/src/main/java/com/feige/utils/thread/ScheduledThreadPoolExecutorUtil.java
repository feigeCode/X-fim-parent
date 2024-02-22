package com.feige.utils.thread;


import com.feige.utils.logger.Loggers;
import org.slf4j.Logger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feige<br />
 * @ClassName: ScheduledThreadPoolExecutorUtil <br/>
 * @Description: <br/>
 * @date: 2022/1/14 13:48<br/>
 */
public class ScheduledThreadPoolExecutorUtil {

    private static final Logger LOG = Loggers.TASK;
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 2,new NameThreadFactory("im-business-task-"));
    private final AtomicInteger executeCount = new AtomicInteger();
    private final AtomicInteger scheduleCount = new AtomicInteger();

    public static ScheduledThreadPoolExecutorUtil getInstance(){
        return ThreadPoolExecutorInner.EXECUTOR;
    }

    private ScheduledThreadPoolExecutorUtil(){

    }

    public void execute(Runnable task) {
        int startCount = executeCount.incrementAndGet();
        LOG.debug("当前任务数为：{}", startCount);
        final long startTime = System.currentTimeMillis();
        executor.execute(() -> {
            try {
                task.run();
            }catch (Throwable throwable){
                LOG.error("execute im business task error:",throwable);
            }finally {
                int endCount = executeCount.decrementAndGet();
                LOG.debug("当前任务已完成，用时：{}ms， 剩余任务数：{}",System.currentTimeMillis() - startTime, endCount );
            }
        });
    }

    public void schedule(Runnable task, long delay, TimeUnit timeUnit) {
        int startCount = scheduleCount.incrementAndGet();
        LOG.debug("当前定时任务数为：{}", startCount);
        final long startTime = System.currentTimeMillis();
        executor.schedule(() -> {
            try {
                task.run();
            }catch (Throwable throwable){
                LOG.error("schedule im business task error:",throwable);
            } finally {
                int endCount = scheduleCount.decrementAndGet();
                LOG.debug("当前定时任务已完成，用时：{}ms， 剩余定时任务数：{}",System.currentTimeMillis() - startTime, endCount );
            }
        },delay,timeUnit);
    }

    public void shutdown() {
        executor.shutdown();
    }

    private static class ThreadPoolExecutorInner {
        private static final ScheduledThreadPoolExecutorUtil EXECUTOR = new ScheduledThreadPoolExecutorUtil();
    }
}
