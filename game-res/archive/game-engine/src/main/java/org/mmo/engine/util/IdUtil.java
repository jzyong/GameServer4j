package org.mmo.engine.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 唯一id 生成工具
 * @author jzy
 */
public class IdUtil {

    private static final AtomicLong id = new AtomicLong(0);
    // 当前服务器id
    public static int SERVER_ID = 0;

    public static long getId() {
        return (((long) SERVER_ID) & 0xFFFF) << 48 | (TimeUtil.currentTimeMillis() / 1000L & 0xFFFFFFFF) << 16
                | id.addAndGet(1) & 0xFFFF;
    }

    public static long getId(int key) {
        return (((long) key) & 0xFFFF) << 48 | (TimeUtil.currentTimeMillis() / 1000L & 0xFFFFFFFF) << 16
                | id.addAndGet(1) & 0xFFFF;
    }
}
