package com.itfly.commons.util;

/**
 * Created by zhoufeiyu on 19/02/2017.
 */
public class ThreadUtils {
    public static void sleepQuietly(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
