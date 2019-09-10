package com.xxx.compass.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.blankj.utilcode.util.Utils;
import com.xxx.compass.model.utils.LocalManageUtil;
import com.xxx.compass.service.InitService;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static App application;

    @Override
    protected void attachBaseContext(Context base) {
        context = base;
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        //启动初始化
        startService(new Intent(this, InitService.class));
    }

    public static Context getContext() {
        return context;
    }

    public static App getApplication() {
        if (application == null) {
            application = new App();
        }
        return application;
    }

    //设置不跟随系统的字体变大而变大
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) getResources();
        // 当切换横竖屏 重置语言
        LocalManageUtil.setLocal(getApplicationContext());
        super.onConfigurationChanged(newConfig);
    }
}
