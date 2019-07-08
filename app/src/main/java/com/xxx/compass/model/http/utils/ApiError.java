package com.xxx.compass.model.http.utils;

import android.app.Activity;
import android.content.Intent;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.base.activity.ActivityManager;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.ui.login.LoginActivity;
import com.xxx.compass.ui.main.MainActivity;

public class ApiError {

    /**
     * 服务器返回的错误码处理
     *
     * @param code 状态码
     */
    public static void ServiceCodeErrorFun(int code) {
        switch (code) {
            case ApiCode.CT_TOKEN_INVALID:
                startActivity();
                break;
            case ApiCode.UC_TOKEN_INVALID:
                startActivity();
                break;
        }
    }


    /**
     * Token失效跳转页面
     */
    public static void startActivity() {
        MainActivity activity1 = (MainActivity) ActivityManager.getInstance().getActivity(MainActivity.class.getSimpleName());
        Activity loginActivity = ActivityManager.getInstance().getActivity(LoginActivity.class.getSimpleName());
        if (loginActivity == null) {
            if (activity1 != null) {
                SharedPreferencesUtil.getInstance().cleanAll();
                activity1.startActivityForResult(new Intent(activity1, LoginActivity.class), ConfigClass.REQUEST_CODE);
            }
        }
    }
}
