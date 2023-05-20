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
     * version          byte类型      协议版本   1
     * bodyLength       int类型       消息体长度 4
     * srcId            long类型      源ID      8
     * destId           long类型      目标ID    8
     * cmd              byte类型      操作命令   1
     * serializeType    byte类型      序列化类型  1
     * features         byte类型      启用特性   1
     * cs               short类型      校验和    2
     * body             byte[]类型    数据
     * 
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



    int PC = 1;
    int MOBILE = 2;
    int WEB = 3;
    
    
    String COMMA = ",";
}
