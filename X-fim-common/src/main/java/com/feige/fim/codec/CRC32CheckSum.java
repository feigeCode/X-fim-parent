package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;
import com.feige.api.codec.CheckSumException;

import java.util.zip.CRC32;

public class CRC32CheckSum implements ICheckSum {
    @Override
    public void check(byte[] body, byte expectedCheckSum) throws CheckSumException {
        long checksumResult = getCheckSum(body);
        if (checksumResult != expectedCheckSum) {
            throw new CheckSumException(String.format(
                    "stream corrupted: mismatching checksum: %d (expected: %d)",
                    checksumResult, expectedCheckSum));
        }
    }

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
