package com.feige.fim.codec;

import com.feige.api.codec.CheckSumException;
import com.feige.api.codec.ICheckSum;
import com.feige.fim.spi.SpiLoaderUtils;


public class CheckSumUtils {

    /**
     *
     * @param body body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     * @throws CheckSumException
     */
    public static void check(byte[] data, short expectedCheckSum)  throws CheckSumException {
        ICheckSum iCheckSum = SpiLoaderUtils.getByConfig(ICheckSum.class);
        iCheckSum.check(data, expectedCheckSum);
    }


    /**
     *calculate check sum
     * @param body body
     * @return check sum
     */
    public static short calculate(byte[] body) {
        ICheckSum iCheckSum = SpiLoaderUtils.getByConfig(ICheckSum.class);
        return iCheckSum.getCheckSum(body);
    }
}
