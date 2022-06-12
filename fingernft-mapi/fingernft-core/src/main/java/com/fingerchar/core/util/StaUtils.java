package com.fingerchar.core.util;

/**
 * @Author： Zjm
 * @Date：2022/3/15 17:26
 */
public class StaUtils {
    /**
     * 获取整点时间戳（秒）
     * @return
     */
    public static Long getStaTime() {
        return (System.currentTimeMillis() / 1000 / 3600) * 3600;
    }
}
