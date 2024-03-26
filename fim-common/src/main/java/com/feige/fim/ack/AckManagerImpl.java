package com.feige.fim.ack;

import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SPI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SPI(interfaces = AckManager.class)
public class AckManagerImpl implements AckManager {
    private final Map<Integer, AbsTask<?>> queue = new ConcurrentHashMap<>();
    @Inject
    private TimerExecutor timer;

    @Override
    public AbsTask<?> getAndRemove(int id){
        return queue.remove(id);
    }

    @Override
    public void addTask(AbsTask<?> task){
        int id = task.getId();
        task.setAckManager(this);
        queue.put(id, task);
        Future<?> future = timer.schedule(task, task.getDelay(), TimeUnit.MILLISECONDS);
        task.setFuture(future);
    }

}
