package com.feige.fim.utils.crypto;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Sm4Utils {
   

    public static String encrypt(byte[] in, byte[] keyBytes) {
        byte[] encrypted = encrypt2Byte(in, keyBytes);
        return Base64.toBase64String(encrypted);
    }

    public static byte[] encrypt2Byte(byte[] in, byte[] keyBytes) {
        return encrypt2Byte(in, keyBytes, true);
    }

    private static byte[] encrypt2Byte(byte[] in, byte[] keyBytes, boolean isPadding) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = isPadding;
            ctx.mode = 1;
            Sm4 sm4 = new Sm4();
            sm4.setkeyEnc(ctx, keyBytes);
            return sm4.cryptEcb(ctx, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt2ByteNoPadding(byte[] in, byte[] keyBytes) {
        return encrypt2Byte(in, keyBytes, false);
    }

    public static String encrypt(String content, String key) {
        try {
            byte[] in = content.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = Hex.decode(key);
            return encrypt(in, keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] in, byte[] keyBytes) {
        try {
            byte[] decrypted = decrypt2Byte(in, keyBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt2Byte(byte[] in, byte[] keyBytes) {
        return decrypt2Byte(in, keyBytes, true);
    }

    public static byte[] decrypt2ByteNoPadding(byte[] in, byte[] keyBytes) {
        return decrypt2Byte(in, keyBytes, false);
    }

    private static byte[] decrypt2Byte(byte[] in, byte[] keyBytes, boolean isPadding) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = isPadding;
            ctx.mode = 0;
            Sm4 sm4 = new Sm4();
            sm4.setkeyDec(ctx, keyBytes);
            return sm4.cryptEcb(ctx, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipher, String key) {
        try {
            byte[] in = Base64.decode(cipher);
            byte[] keyBytes = Hex.decode(key);
            return decrypt(in, keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Sm4 {
        private static final byte[] S_BOX_TABLE = new byte[]{-42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5, 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98, -22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111, -43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72};
        public static final int[] FK = new int[]{-1548633402, 1453994832, 1736282519, -1301273892};
        public static final int[] CK = new int[]{462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257};

      
        private long GET_U_LONG_BE(byte[] b, int i) {
            return (long)(b[i] & 255) << 24 | (long)((b[i + 1] & 255) << 16) | (long)((b[i + 2] & 255) << 8) | (long)(b[i + 3] & 255) & 4294967295L;
        }

        private void PUT_U_LONG_BE(long n, byte[] b, int i) {
            b[i] = (byte)((int)(255L & n >> 24));
            b[i + 1] = (byte)((int)(255L & n >> 16));
            b[i + 2] = (byte)((int)(255L & n >> 8));
            b[i + 3] = (byte)((int)(255L & n));
        }

        private long SHL(long x, int n) {
            return (x) << n;
        }

        private long ROT_L(long x, int n) {
            return this.SHL(x, n) | x >> 32 - n;
        }

        private void SWAP(long[] sk, int i) {
            long t = sk[i];
            sk[i] = sk[31 - i];
            sk[31 - i] = t;
        }

        private byte sm4_S_box(byte inch) {
            int i = inch & 255;
            return S_BOX_TABLE[i];
        }

        private long sm4Lt(long ka) {
            long bb = 0L;
            long c = 0L;
            byte[] a = new byte[4];
            byte[] b = new byte[4];
            this.PUT_U_LONG_BE(ka, a, 0);
            b[0] = this.sm4_S_box(a[0]);
            b[1] = this.sm4_S_box(a[1]);
            b[2] = this.sm4_S_box(a[2]);
            b[3] = this.sm4_S_box(a[3]);
            bb = this.GET_U_LONG_BE(b, 0);
            c = bb ^ this.ROT_L(bb, 2) ^ this.ROT_L(bb, 10) ^ this.ROT_L(bb, 18) ^ this.ROT_L(bb, 24);
            return c;
        }

        private long sm4F(long x0, long x1, long x2, long x3, long rk) {
            return x0 ^ this.sm4Lt(x1 ^ x2 ^ x3 ^ rk);
        }

        private long sm4CalciRK(long ka) {
            long bb = 0L;
            long rk = 0L;
            byte[] a = new byte[4];
            byte[] b = new byte[4];
            this.PUT_U_LONG_BE(ka, a, 0);
            b[0] = this.sm4_S_box(a[0]);
            b[1] = this.sm4_S_box(a[1]);
            b[2] = this.sm4_S_box(a[2]);
            b[3] = this.sm4_S_box(a[3]);
            bb = this.GET_U_LONG_BE(b, 0);
            rk = bb ^ this.ROT_L(bb, 13) ^ this.ROT_L(bb, 23);
            return rk;
        }

        private void setkey(long[] SK, byte[] key) {
            long[] MK = new long[4];
            long[] k = new long[36];
            int i = 0;
            MK[0] = this.GET_U_LONG_BE(key, 0);
            MK[1] = this.GET_U_LONG_BE(key, 4);
            MK[2] = this.GET_U_LONG_BE(key, 8);
            MK[3] = this.GET_U_LONG_BE(key, 12);
            k[0] = MK[0] ^ (long)FK[0];
            k[1] = MK[1] ^ (long)FK[1];
            k[2] = MK[2] ^ (long)FK[2];

            for(k[3] = MK[3] ^ (long)FK[3]; i < 32; ++i) {
                k[i + 4] = k[i] ^ this.sm4CalciRK(k[i + 1] ^ k[i + 2] ^ k[i + 3] ^ (long)CK[i]);
                SK[i] = k[i + 4];
            }

        }

        private void oneRound(long[] sk, byte[] input, byte[] output) {
            int i = 0;
            long[] ulbuf = new long[36];
            ulbuf[0] = this.GET_U_LONG_BE(input, 0);
            ulbuf[1] = this.GET_U_LONG_BE(input, 4);
            ulbuf[2] = this.GET_U_LONG_BE(input, 8);

            for(ulbuf[3] = this.GET_U_LONG_BE(input, 12); i < 32; ++i) {
                ulbuf[i + 4] = this.sm4F(ulbuf[i], ulbuf[i + 1], ulbuf[i + 2], ulbuf[i + 3], sk[i]);
            }

            this.PUT_U_LONG_BE(ulbuf[35], output, 0);
            this.PUT_U_LONG_BE(ulbuf[34], output, 4);
            this.PUT_U_LONG_BE(ulbuf[33], output, 8);
            this.PUT_U_LONG_BE(ulbuf[32], output, 12);
        }

        private byte[] padding(byte[] input, int mode) {
            if (input == null) {
                return null;
            } else {
                byte[] ret = null;
                if (mode == 1) {
                    int p = 16 - input.length % 16;
                    ret = new byte[input.length + p];
                    System.arraycopy(input, 0, ret, 0, input.length);

                    for(int i = 0; i < p; ++i) {
                        ret[input.length + i] = (byte)p;
                    }
                } else {
                    int p = input[input.length - 1];
                    ret = new byte[input.length - p];
                    System.arraycopy(input, 0, ret, 0, input.length - p);
                }

                return ret;
            }
        }

        public void setkeyEnc(Sm4Context ctx, byte[] key) throws Exception {
            if (ctx == null) {
                throw new Exception("ctx is null!");
            } else if (key != null && key.length == 16) {
                ctx.mode = 1;
                this.setkey(ctx.sk, key);
            } else {
                throw new Exception("key error!");
            }
        }

        public void setkeyDec(Sm4Context ctx, byte[] key) throws Exception {
            if (ctx == null) {
                throw new Exception("ctx is null!");
            } else if (key != null && key.length == 16) {
                int i;
                ctx.mode = 0;
                this.setkey(ctx.sk, key);

                for(i = 0; i < 16; ++i) {
                    this.SWAP(ctx.sk, i);
                }

            } else {
                throw new Exception("key error!");
            }
        }

        public byte[] cryptEcb(Sm4Context ctx, byte[] input) throws Exception {
            if (input == null) {
                throw new Exception("input is null!");
            } else {
                if (ctx.isPadding && ctx.mode == 1) {
                    input = this.padding(input, 1);
                }

                int length = input.length;
                ByteArrayInputStream bins = new ByteArrayInputStream(input);

                ByteArrayOutputStream bos;
                byte[] output;
                for(bos = new ByteArrayOutputStream(); length > 0; length -= 16) {
                    output = new byte[16];
                    byte[] out = new byte[16];
                    bins.read(output);
                    this.oneRound(ctx.sk, output, out);
                    bos.write(out);
                }

                output = bos.toByteArray();
                if (ctx.isPadding && ctx.mode == 0) {
                    output = this.padding(output, 0);
                }

                bins.close();
                bos.close();
                return output;
            }
        }
    }

    public static class Sm4Context {
        public int mode = 1;
        public long[] sk = new long[32];
        public boolean isPadding = true;
    }
}
