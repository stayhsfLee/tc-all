package com.thenorthw.tc.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hongjiao.hj on 2015/8/11.
 */
public class ShortUUIDUtil {

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");

    private ShortUUIDUtil() {}

    public static String nameUUIDFromBytes(byte[] bytes) {
        return toShortString(UUID.nameUUIDFromBytes(bytes));
    }

    public static String randomUUID() {
        return toShortString(UUID.randomUUID());
    }

    private static String toShortString(UUID u) {
        return UUIDtoString(u);
    }

    private static String UUIDtoString(UUID u) {
        long mostSigBits = u.getMostSignificantBits();
        long leastSigBits = u.getLeastSignificantBits();
        return digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4)
                + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toString(hi | (val & (hi - 1)), 36).substring(1);
    }

    public static String  generateRequestId() {
        String datePrefix = fmt.format(new Date());

        String shortUUID = randomUUID().substring(0, 10).toUpperCase();
        return datePrefix + "-" + shortUUID;
    }

}
