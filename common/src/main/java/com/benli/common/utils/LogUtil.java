
package com.benli.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import android.util.Log;

import com.benli.common.BuildConfig;

/**
 * 日志工具
 */
public class LogUtil {

    public static boolean enableLog = BuildConfig.DEBUG;

    public static int v(String tag, String msg) {
        if (enableLog) {
            return Log.v(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    public static int i(String tag, String msg) {
        if (enableLog) {
            return Log.i(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (enableLog) {
            return Log.w(tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (enableLog) {
            return Log.d(tag, msg);
        }
        return 0;
    }
    
    /**
     * 
     * @param tag
     * @param msg
     * @return
     */
    public static int v(String tag, Object...msg) {
        String message = null;
        if (ArrayUtils.isEmpty(msg)) {
            message = "null or empty msg!";
        }else{
            message = String.format(msg[0].toString(), ArrayUtils.subarray(msg, 1, msg.length));
        }
        
        return v(tag, message);
    }
    
    public static int e(String tag, Object...msg) {
        String message = null;
        if (ArrayUtils.isEmpty(msg)) {
            message = "null or empty msg!";
        }else{
            message = String.format(msg[0].toString(), ArrayUtils.subarray(msg, 1, msg.length));
        }
        
        return e(tag, message);
    }

    public static int i(String tag, Object...msg) {
        String message = null;
        if (ArrayUtils.isEmpty(msg)) {
            message = "null or empty msg!";
        }else{
            message = String.format(msg[0].toString(), ArrayUtils.subarray(msg, 1, msg.length));
        }
        
        return i(tag, message);
    }

    public static int w(String tag, Object...msg) {
        String message = null;
        if (ArrayUtils.isEmpty(msg)) {
            message = "null or empty msg!";
        }else{
            message = String.format(msg[0].toString(), ArrayUtils.subarray(msg, 1, msg.length));
        }
        
        return w(tag, message);
    }

    public static int d(String tag, Object...msg) {
        String message = null;
        if (ArrayUtils.isEmpty(msg)) {
            message = "null or empty msg!";
        }else{
            message = String.format(msg[0].toString(), ArrayUtils.subarray(msg, 1, msg.length));
        }
        
        return d(tag, message);
    }

    private static final int LOG_LEVEL = Log.VERBOSE;

    private static final int EXCEPTION_STACK_INDEX = 2;


    public static void verbose(String... msg) {
        if (Log.VERBOSE >= LOG_LEVEL) {
            v(getTag(), msg);
        }
    }

    public static void debug(String... msg) {
        if (Log.DEBUG >= LOG_LEVEL) {
            d(getTag(), msg);
        }
    }

    public static void info(String... msg) {
        if (Log.INFO >= LOG_LEVEL) {
            i(getTag(), msg);
        }
    }

    public static void warn(String... msg) {
        if (Log.WARN >= LOG_LEVEL) {
            w(getTag(), msg);
        }
    }

    public static void error(String... msg) {
        if (Log.ERROR >= LOG_LEVEL) {
            e(getTag(), msg);
        }
    }

    private static String getTag() throws StackOverflowError {
        StackTraceElement element = new LogException().getStackTrace()[EXCEPTION_STACK_INDEX];

        String className = element.getClassName();

        int index = className.lastIndexOf(".");
        if (index > 0) {
            className = className.substring(index + 1);
        }

        return className + "_" + element.getMethodName() + "_" + element.getLineNumber();
    }

    private static class LogException extends Exception {

        private static final long serialVersionUID = 1L;
    }
}
