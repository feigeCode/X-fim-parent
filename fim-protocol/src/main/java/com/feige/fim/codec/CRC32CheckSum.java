package com.feige.fim.codec;


import com.feige.api.codec.ICheckSum;
import com.feige.utils.spi.annotation.SPI;

import java.util.zip.CRC32;

@SPI(value = "crc32", interfaces = ICheckSum.class)
public class CRC32CheckSum implements ICheckSum {

    @Override
    public short getCheckSum(byte[] data){
        CRC32 crc32 = new CRC32();
        crc32.update(data, 0, data.length);
        return (short) crc32.getValue();
    }

    @Override
    public String getKey() {
        return "crc32";
    }

}
