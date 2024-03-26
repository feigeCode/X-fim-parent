package com.feige.fim.ack;

public interface AckManager {

    AbsTask<?> getAndRemove(int id);

    void addTask(AbsTask<?> task);
}
