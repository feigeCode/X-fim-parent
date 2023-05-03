package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;
import com.feige.fim.spi.SpiLoader;
import com.feige.fim.spi.SpiNotFoundException;

public class CheckCheckSumUtils {

    /**
     *
     * @param checkSumType check sum type
     * @param body body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     * @throws SpiNotFoundException
     */
    public static void check(byte checkSumType, byte[] body, byte expectedCheckSum) throws SpiNotFoundException {
        ICheckSum iCheckSum = SpiLoader.getInstance().get(String.valueOf(checkSumType), ICheckSum.class);
        iCheckSum.check(body, expectedCheckSum);
    }


    /**
     *calculate check sum
     * @param checkSumType check sum type
     * @param body body
     * @return check sum
     * @throws SpiNotFoundException
     */
    public static byte calculate(byte checkSumType, byte[] body) throws SpiNotFoundException {
        ICheckSum iCheckSum = SpiLoader.getInstance().get(String.valueOf(checkSumType), ICheckSum.class);
        return iCheckSum.getCheckSum(body);
    }
}
