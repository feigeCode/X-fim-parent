
package com.feig.utils;

import java.lang.management.ManagementFactory;

/**
 * Util class providing pid of current process.
 */
public final class PidUtil {

    /**
     * Resolve and get current process ID.
     *
     * @return current process ID
     */
    public static int getPid() {
        // Note: this will trigger local host resolve, which might be slow.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(name.split("@")[0]);
    }

    private PidUtil() {}
}
