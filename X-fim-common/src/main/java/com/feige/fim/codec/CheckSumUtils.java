package com.feige.fim.codec;

import com.feige.api.codec.ICheckSum;
import com.feige.fim.spi.SpiLoader;


public class CheckSumUtils {

    /**
     *
     * @param body body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     */
    public static void check(byte[] body, short expectedCheckSum) {
        ICheckSum iCheckSum = SpiLoader.getInstance().getSpiByConfigOrPrimary(ICheckSum.class);
        iCheckSum.check(body, expectedCheckSum);
    }


    /**
     *calculate check sum
     * @param body body
     * @return check sum
     */
    public static short calculate(byte[] body) {
        ICheckSum iCheckSum = SpiLoader.getInstance().getSpiByConfigOrPrimary(ICheckSum.class);
        return iCheckSum.getCheckSum(body);
    }
}
