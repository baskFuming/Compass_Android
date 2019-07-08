package com.xxx.compass.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.service.InitService;
import com.xxx.compass.ui.login.LoginActivity;

import java.lang.ref.WeakReference;

/**
 * @Page 闪屏页
 * @Author xxx
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * 延迟时间
     */
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View decorView = getWindow().getDecorView();
        decorView.post(new Runnable() {
            @Override
            public void run() {
                double width = decorView.getWidth();
                double height = decorView.getHeight();
                if (height / width >= 1.8) {
                    decorView.setBackgroundResource(R.mipmap.splash_big);
                }
                // 利用消息处理器实现延迟跳转到登录窗口
                mHandler.sendEmptyMessageAtTime(0, ConfigClass.SPLASH_DELAY_TIME);
            }
        });

        //初始化
        startService(new Intent(this, InitService.class));
    }


    private static class MyHandler extends Handler {

        //弱引用对象
        private final WeakReference<SplashActivity> mActivity;

        MyHandler(SplashActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                SplashActivity activity = mActivity.get();
                if (activity != null) {
                    Intent intent;
                    if (SharedPreferencesUtil.getInstance().getBoolean(SharedConst.IS_LOGIN)) {
                        intent = new Intent(activity, MainActivity.class);
                    } else {
                        intent = new Intent(activity, LoginActivity.class);
                    }
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        }
    }
}
