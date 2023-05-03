package com.feige.fim.codec;


/**
 * 1、type 1个字节   报文类型
 * 2、bl 4个字节 body长度
 * 3、srcId 16个字节 源ID
 * 4、destId 16个字节 目标ID
 * 5、cs 8个字节   校验和
 * 6、body 数据
 */
public class Transport {
    private byte version;
    private byte type;
    private int bodyLength;
    private byte[] srcId;
    private byte[] destId;
    private long cs;
    private byte[] body;
    
    
    
    
    
    
    

    
}
