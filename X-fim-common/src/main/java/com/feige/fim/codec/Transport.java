package com.feige.fim.codec;


import java.util.zip.CRC32;

/**
 * 1、type 1个字节   报文类型
 * 2、bl 4个字节 body长度
 * 3、srcId 16个字节 源ID
 * 4、destId 16个字节 目标ID
 * 5、cs 8个字节   校验和
 * 6、body 数据
 */
public class Transport {
    private byte type;
    private int bodyLength;
    private byte[] srcId;
    private byte[] destId;
    private long cs;
    private byte[] body;
    
    
    
    
    
    
    
    
    public boolean check(){
        return getChecksum(body) == cs;
    }
    
    
    public static long getChecksum(byte[] body){
        CRC32 crc32 = new CRC32();
        crc32.update(body, 0, body.length);
        return crc32.getValue();
    }
    
    
    
}
