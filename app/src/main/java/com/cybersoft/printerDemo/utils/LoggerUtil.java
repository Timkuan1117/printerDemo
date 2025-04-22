package com.cybersoft.printerDemo.utils;

import android.util.Log;

import java.util.logging.Level;

public class LoggerUtil {
    public static void d(String className, String msg) {
        log(Level.INFO, className + "(" + Thread.currentThread().getId() + ")", msg, null);
    }
    public static void log(Level level, String className, String msg, Throwable tr) {
        if (level == Level.SEVERE) {
            Log.e(className, msg, tr);
        } else {
            Log.w(className, msg);
        }
    }
}
