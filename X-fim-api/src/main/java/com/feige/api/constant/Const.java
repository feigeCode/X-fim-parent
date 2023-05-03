package com.feige.api.constant;

public interface Const {

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    /**
     * 心跳
     */
    byte HEARTBEAT = (byte) 1;

}