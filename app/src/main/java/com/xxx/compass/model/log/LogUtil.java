package com.xxx.compass.model.log;

import android.util.Log;

import com.xxx.compass.BuildConfig;

/**
 * 自定义Log
 */
public class LogUtil {

    private static final int LOG_E = 5;    //E级别
    private static final int LOG_D = 4;    //D级别
    private static final int LOG_I = 3;    //I级别
    private static final int LOG_W = 2;    //W级别
    private static final int LOG_V = 1;    //V级别

    private static final int LOG_LEVEL = LOG_E;    //当前Log日志输出等级

    public static void showLog(String tag, String errorMessage) {
        errorMessage = errorMessage == null || errorMessage.isEmpty() ? "未知错误" : errorMessage;
        if (BuildConfig.DEBUG) {
            switch (LOG_LEVEL) {
                case LOG_E:
                    Log.e(tag, errorMessage);
                    break;
                case LOG_D:
                    Log.d(tag, errorMessage);
                    break;
                case LOG_I:
                    Log.i(tag, errorMessage);
                    break;
                case LOG_W:
                    Log.w(tag, errorMessage);
                    break;
                case LOG_V:
                    Log.v(tag, errorMessage);
                    break;
            }
        }
    }
}