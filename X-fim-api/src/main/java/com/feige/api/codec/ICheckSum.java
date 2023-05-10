package com.feige.api.codec;

import com.feige.api.annotation.LoadOnlyTheFirstOne;
import com.feige.api.spi.Spi;

@LoadOnlyTheFirstOne
public interface ICheckSum extends Spi {
    /**
     *
     * @param body body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     * @throws CheckSumException
     */
    default void check(byte[] body, byte expectedCheckSum) throws CheckSumException {
        byte checksumResult = getCheckSum(body);
        if (checksumResult != expectedCheckSum) {
            throw new CheckSumException(String.format(
                    "stream corrupted: mismatching checksum: %d (expected: %d)",
                    checksumResult, expectedCheckSum));
        }
    }

    /**
     * calculate check sum
     * @param body body
     * @return check sum
     */
    byte getCheckSum(byte[] body);
}
