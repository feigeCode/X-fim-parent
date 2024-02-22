package com.feige.api.codec;


public interface ICheckSum {
    /**
     *
     * @param data body
     * @param expectedCheckSum expected check sum
     * @return Equal or not
     * @throws CheckSumException
     */
    default void check(byte[] data, short expectedCheckSum) throws CheckSumException {
        short checksumResult = getCheckSum(data);
        if (checksumResult != expectedCheckSum) {
            throw new CheckSumException(String.format(
                    "stream corrupted: mismatching checksum: %d (expected: %d)",
                    checksumResult, expectedCheckSum));
        }
    }

    /**
     * calculate check sum
     * @param data body
     * @return check sum
     */
    short getCheckSum(byte[] data);

    /**
     * key
     * @return
     */
    String getKey();
}
