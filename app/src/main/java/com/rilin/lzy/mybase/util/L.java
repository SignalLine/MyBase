package com.rilin.lzy.mybase.util;

/**
 * Created by YangLei on 2016/1/8.
 */

import android.util.Log;

import com.rilin.lzy.mybase.application.MyApplication;


/**
 * Logcat统一管理类
 */
public class L {
    private L() {
        /**
         * 不能实例化
         */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 是否需要打印bug，在application的onCreate函数里面初始化
    public static boolean isDebug = MyApplication.isDebug;
    private static final String TAG = "log";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }
}
