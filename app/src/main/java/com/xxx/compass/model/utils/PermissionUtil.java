package com.xxx.compass.model.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.xxx.compass.model.log.LogConst;
import com.xxx.compass.model.log.LogUtil;

public class PermissionUtil {

    //授权请求码
    private static final int REQUEST_PERMISSION_CODE = 0;

    @SuppressLint("InlinedApi")
    public static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE; //写入权限
    public static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;   //读取权限
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;  //相机权限

    /**
     * 检查权限
     */
    public static boolean checkPermission(Activity activity, String... params) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        if (activity != null) {
            for (String requestPermission : params) {
                if (ActivityCompat.checkSelfPermission(activity, requestPermission) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(activity, params, REQUEST_PERMISSION_CODE);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 回调方法
     */
    public static boolean onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                LogUtil.showLog(LogConst.PERMISSION_TAG, "用户未授权该权限：" + permissions[i]);
                return false;
            }
        }
        return true;
    }
}
