/**
 * 
 */
package com.feige.utils.thread;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.micrometer.core.lang.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liqiang
 *
 */
public class ThreadPoolExecutorMetrics extends ExecutorServiceMetrics {
	private final ThreadPoolExecutor threadPool;
    private final Iterable<Tag> tags;
    private static final String metricPrefix = "thread.pool.";

	public ThreadPoolExecutorMetrics(ThreadPoolExecutor threadPool, String poolName, Iterable<Tag> tags) {
		super(threadPool, poolName, tags);
		this.threadPool = threadPool;
		this.tags = Tags.concat(tags, "name", poolName);
	}
	
    @Override
    public void bindTo(MeterRegistry registry) {
    	monitor(registry,threadPool);
    }
    
    protected void monitor(MeterRegistry registry, @Nullable ThreadPoolExecutor tp) {
        if (tp == null) {
            return;
        }

        // 线程池
		Gauge.builder(metricPrefix + "active.count", tp, ThreadPoolExecutor::getActiveCount).tags(tags).description("线程池活跃程数量").baseUnit(BaseUnits.THREADS).register(registry);
		Gauge.builder(metricPrefix + "core.pool.size", tp, ThreadPoolExecutor::getCorePoolSize).tags(tags).description("线程池核心线程数量").baseUnit(BaseUnits.THREADS).register(registry);
		Gauge.builder(metricPrefix + "maximum.pool.size", tp, ThreadPoolExecutor::getMaximumPoolSize).tags(tags).description("线程池最大线程数").baseUnit(BaseUnits.THREADS).register(registry);
		Gauge.builder(metricPrefix + "largest.pool.size", tp, ThreadPoolExecutor::getLargestPoolSize).tags(tags).description("线程池历史峰值线程数量").baseUnit(BaseUnits.THREADS).register(registry);
		Gauge.builder(metricPrefix + "thread.count", tp, ThreadPoolExecutor::getLargestPoolSize).tags(tags).description("线程池当前线程数量").baseUnit(BaseUnits.THREADS).register(registry);
		FunctionCounter.builder(metricPrefix + "task.count", tp, ThreadPoolExecutor::getTaskCount).tags(tags).description("线程池已提交任务总数").baseUnit(BaseUnits.TASKS).register(registry);
		FunctionCounter.builder(metricPrefix + "completed.task.count", tp, ThreadPoolExecutor::getCompletedTaskCount).tags(tags).description("线程池已执行完任务数").baseUnit(BaseUnits.TASKS).register(registry);
		FunctionCounter.builder(metricPrefix + "task.overstocked.count", tp, tpr -> tpr.getQueue().size()).tags(tags).description("线程池积压任务数").baseUnit(BaseUnits.TASKS).register(registry);
		Gauge.builder(metricPrefix + "active.ratio", tp, this::getActiveRatio).tags(tags).description("线程池活跃度").baseUnit(BaseUnits.THREADS).register(registry);

		// 队列
		BlockingQueue<Runnable> blockingQueue = threadPool.getQueue();
		Gauge.builder(metricPrefix + "queue.size", blockingQueue, BlockingQueue::size).tags(tags).description("当前任务队列大小").baseUnit(BaseUnits.TASKS).register(registry);
		Gauge.builder(metricPrefix + "queue.remained.size", blockingQueue, BlockingQueue::remainingCapacity).tags(tags).description("任务队列剩余量大小").baseUnit(BaseUnits.TASKS).register(registry);
		Gauge.builder(metricPrefix + "queue.ratio", blockingQueue, this::getQueueRatio).tags(tags).description("队列剩余容量占比").baseUnit(BaseUnits.TASKS).register(registry);
    }

	/**
	 * 获取线程池活跃度
	 *
	 * @param poolExecutor
	 * @return
	 */
	private double getActiveRatio(ThreadPoolExecutor poolExecutor) {
		return 1.0 * poolExecutor.getActiveCount() / poolExecutor.getMaximumPoolSize();
	}

	/**
	 * 剩余容量占比
	 *
	 * @param blockingQueue
	 * @return
	 */
	private double getQueueRatio(BlockingQueue<Runnable> blockingQueue) {
		int remainingCapacity = blockingQueue.remainingCapacity();
		int size = blockingQueue.size();
		int capacity = remainingCapacity + size;
		if (capacity == 0) {
			return 1.0;
		}
		return 1.0 * remainingCapacity / capacity;
	}

}
