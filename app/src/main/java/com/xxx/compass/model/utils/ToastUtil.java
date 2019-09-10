package com.xxx.compass.model.utils;

import android.app.Application;
import android.content.Context;
import android.text.style.TtsSpan;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.xxx.compass.base.App;

/**
 * Toast工具类
 */
public class ToastUtil {

    private static Toast toast = null;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    public static void showToast(String s) {
        if (s == null || s.equals("")) return;

        if (toast == null) {
            toast = Toast.makeText(App.getContext(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(int resId) {
        showToast(App.getApplication().getResources().getString(resId));
    }
}