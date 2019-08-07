package com.xxx.compass.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.xxx.compass.model.utils.LocalManageUtil;
import com.xxx.compass.service.InitService;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    //上下文
    public static Activity activity;

    @Override
    protected void attachBaseContext(Context base) {
        context = base;
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //启动初始化
        startService(new Intent(this, InitService.class));
    }

    public static Context getContext() {
        return context;
    }

    //设置不跟随系统的字体变大而变大
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) getResources();
        // 当切换横竖屏 重置语言
        LocalManageUtil.setLocal(getApplicationContext());
        super.onConfigurationChanged(newConfig);
    }

//    //设置不跟随系统的字体变大而变大
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (res.getConfiguration().fontScale != 1) {
//            Configuration newConfig = new Configuration();
//            newConfig.setToDefaults();
//            res.updateConfiguration(newConfig, res.getDisplayMetrics());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                createConfigurationContext(newConfig);
//            } else {
//                res.updateConfiguration(newConfig, res.getDisplayMetrics());
//            }
//        }
//        return res;
//    }

}
