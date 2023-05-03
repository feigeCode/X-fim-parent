package com.feige.api.codec;

import com.feige.api.spi.Spi;

public interface ICheckSum extends Spi {
    /**
     *
     * @param body body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     * @throws CheckSumException
     */
    void check(byte[] body, byte expectedCheckSum) throws CheckSumException;

    /**
     * calculate check sum
     * @param body body
     * @return check sum
     */
    byte getCheckSum(byte[] body);
}
