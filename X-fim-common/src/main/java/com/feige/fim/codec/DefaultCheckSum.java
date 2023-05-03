package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;
import com.feige.api.codec.CheckSumException;

public class DefaultCheckSum implements ICheckSum {

    @Override
    public void check( byte[] body, byte expectedCheckSum) {
        byte checksumResult = getCheckSum(body);
        if ( checksumResult != expectedCheckSum) {
            throw new CheckSumException(String.format(
                    "stream corrupted: mismatching checksum: %d (expected: %d)",
                    checksumResult, expectedCheckSum));
        }
    }

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
