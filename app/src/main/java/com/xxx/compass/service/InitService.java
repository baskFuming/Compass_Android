package com.xxx.compass.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xxx.compass.BuildConfig;
import com.xxx.compass.model.utils.ExceptionUtil;

/**
 * 初始化参数
 */
public class InitService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initException();  //初始化全局异常捕获

                stopSelf(); //自己关闭
            }

        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 初始化全局异常捕获
     */
    private void initException() {
        if (!BuildConfig.DEBUG) {
            ExceptionUtil.init();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
