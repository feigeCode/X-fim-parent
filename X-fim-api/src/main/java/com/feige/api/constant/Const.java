package com.feige.api.constant;

public interface Const {

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    byte VERSION = 1;

    /**
     * 心跳
     */
    byte HEARTBEAT = -13;

    /**
     * 头长度
     */
    int HEADER_LEN = 26;

    /**
     * 加密
     */
    byte CRYPTO_FEATURE = 1;
    /**
     * 压缩
     */
    byte COMPRESS_FEATURE = 2;

}
