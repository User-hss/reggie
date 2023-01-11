package com.hss.reggie.common;

/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前线程登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getId() {
        return threadLocal.get();
    }

    public static void setId(Long id) {
        threadLocal.set(id);
    }
}
