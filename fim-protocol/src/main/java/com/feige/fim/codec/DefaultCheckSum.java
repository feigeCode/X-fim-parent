package com.feige.fim.codec;


import com.feige.api.codec.ICheckSum;
import com.feige.utils.spi.annotation.SPI;

@SPI(value = "default", interfaces = ICheckSum.class)
public class DefaultCheckSum implements ICheckSum {

    @Override
    public short getCheckSum(byte[] data) {
        int sum = 0;
        for (byte b : data) {
            sum += b & 0xff;
        }
        return (short) (~sum & 0xff);
    }

    @Override
    public String getKey() {
        return "default";
    }
    

}
