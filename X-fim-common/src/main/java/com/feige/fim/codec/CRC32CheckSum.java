package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;

import java.util.zip.CRC32;

public class CRC32CheckSum implements ICheckSum {

    @Override
    public byte getCheckSum(byte[] body){
        CRC32 crc32 = new CRC32();
        crc32.update(body, 0, body.length);
        return (byte) crc32.getValue();
    }

    @Override
    public String getKey() {
        return "2";
    }

    @Override
    public int order() {
        return 2;
    }
}
