package com.pfh.promiselist.utils;

import com.orhanobut.logger.Logger;

/**
 * 封装logger的log类
 */

public class L {

    private L() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static boolean isDebug = true;
    private static final String TAG = "LogUtil";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Logger.i(msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Logger.d(msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Logger.e(msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Logger.v(msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).i(msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).d(msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).e(msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Logger.t(tag).v(msg);
    }

    public static void json(String jsonContent){
        if (isDebug)
            Logger.json(jsonContent);
    }
}
