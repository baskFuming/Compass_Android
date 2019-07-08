package com.xxx.compass.model.utils;

import android.os.Handler;
import android.os.Message;

import com.xxx.compass.R;
import com.xxx.compass.base.activity.ActivityManager;

public class ExitAppUtil {

    private final MyHandler mHandler;

    private ExitAppUtil() {
        mHandler = new MyHandler();
    }

    public static ExitAppUtil getInstance() {
        return new ExitAppUtil();
    }

    //二次点击
    private static class MyHandler extends Handler {

        private boolean backCount = false;

        //此处不可使用 弱引用对象
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                backCount = false;
            }
        }

        boolean isBackCount() {
            return backCount;
        }

        void setBackCount() {
            this.backCount = true;
        }
    }

    public void onBackPressed() {
        if (mHandler.isBackCount()) {
            mHandler.removeMessages(1);
            ActivityManager.getInstance().AppExit();
            return;
        }
        mHandler.setBackCount();
        mHandler.sendEmptyMessageDelayed(1, 1500);
        ToastUtil.showToast(R.string.second_exit_app);
    }
}