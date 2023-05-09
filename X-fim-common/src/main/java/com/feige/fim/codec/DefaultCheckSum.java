package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;

public class DefaultCheckSum implements ICheckSum {

    @Override
    public byte getCheckSum(byte[] data) {
        int sum = 0;
        for (byte b : data) {
            sum += b & 0xff;
        }
        return (byte) (~sum & 0xff);
    }

    @Override
    public String getKey() {
        return "1";
    }

    @Override
    public boolean isPrimary() {
        return true;
    }

    @Override
    public int order() {
        return 1;
    }
}
