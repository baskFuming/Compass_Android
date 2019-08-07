package com.xxx.compass.base.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.base.App;
import com.xxx.compass.model.utils.LocalManageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 语言切换模块
 */
public abstract class BaseLanguageActivity extends AppCompatActivity{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //上下文
        App.activity = this;
        //注册多语言切换的EventBus
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(String eventFlag){
        switch (eventFlag){
            case ConfigClass.EVENT_LANGUAGE_TAG:
                recreate();//刷新界面
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.activity = null;
        //注销多语言切换EventBus
        EventBus.getDefault().unregister(this);
    }
}
