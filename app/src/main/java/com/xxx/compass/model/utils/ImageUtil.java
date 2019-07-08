package com.xxx.compass.model.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.xxx.compass.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageUtil {

    //授权请求码
    private static final int REQUEST_PERMISSION_CODE = 100;

    /**
     * 保存图片到图库
     */
    public static void saveImage(Activity context, Bitmap bmp) {
        if (checkPermission(context)) return;

        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        //创建出目录
        File file1 = new File(galleryPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        //创建出图片文件
        File file = new File(galleryPath,UUID.randomUUID() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, file.getName(), null);
            ToastUtil.showToast(context.getString(R.string.save_image_success));
        } catch (Exception e) {
            ToastUtil.showToast(context.getString(R.string.save_image_fail));
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查权限
     *
     * @return
     */
    private static boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;

        String[] requestPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,  //读写权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String requestPermission : requestPermissions) {
            if (ActivityCompat.checkSelfPermission(activity, requestPermission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity, requestPermissions, REQUEST_PERMISSION_CODE);
                return true;
            }
        }
        return false;
    }

    /**
     * 压缩图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100;  //100是压缩等级 最高100  最低0
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
