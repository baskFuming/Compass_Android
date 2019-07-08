package com.xxx.compass.model.utils;

import android.os.Looper;

import com.xxx.compass.R;
import com.xxx.compass.base.App;

/**
 * 全局异常捕获
 */
public class ExceptionUtil implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * CrashHandler实例
     */
    private static ExceptionUtil INSTANCE;

    /**
     * 被锁的对象
     */
    private static final Object synObject = new Object();

    /**
     * 保证只有一个CrashHandler实例 并且初始化异常
     */
    private ExceptionUtil() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static ExceptionUtil init() {
        if (INSTANCE == null) {
            synchronized (synObject) {
                if (INSTANCE == null) {
                    INSTANCE = new ExceptionUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //Sleep一会后结束程序
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {

            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex != null) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    showError(ex);
                    Looper.loop();
                }
            }.start();
        }
        return true;
    }

    private static void showError(Throwable ex) {
        ToastUtil.showToast(R.string.exit_exception);
    }
}