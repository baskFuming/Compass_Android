package com.xxx.compass.model.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.xxx.compass.base.App;
import com.xxx.compass.ConfigClass;

public class SharedPreferencesUtil {

    private static final Object SYC = new Object();

    private static SharedPreferencesUtil sharedPreferencesUtils;

    private SharedPreferences sharedPreferences;

    private SharedPreferencesUtil() {
        sharedPreferences = App.getContext().getSharedPreferences(ConfigClass.SP_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance() {
        if (sharedPreferencesUtils == null) {
            synchronized (SYC) {
                if (sharedPreferencesUtils == null) {
                    sharedPreferencesUtils = new SharedPreferencesUtil();
                }
            }
        }
        return sharedPreferencesUtils;
    }


    /**
     * 保存
     */
    public void saveEncryptString(String tag, String content) {
        saveString(tag, SpEncryption.getInstance().encryptString(content, ConfigClass.SP_KEY));
    }

    public void saveString(String tag, String content) {
        if (tag != null && content != null) {
            sharedPreferences.edit().putString(tag, content).apply();
        }
    }

    public void saveBoolean(String tag, Boolean boo) {
        if (tag != null && boo != null) {
            sharedPreferences.edit().putBoolean(tag, boo).apply();
        }
    }

    /**
     * 获取
     */
    public String getDecryptionString(String tag) {
        return SpEncryption.getInstance().decryptString(getString(tag), ConfigClass.SP_KEY);
    }

    public String getString(String tag) {
        String data = "";
        if (tag != null) {
            data = sharedPreferences.getString(tag, "");
        }
        return data;
    }

    public Boolean getBoolean(String tag) {
        boolean data = false;
        if (tag != null) {
            data = sharedPreferences.getBoolean(tag, false);
        }
        return data;
    }

    /**
     * 清除
     */
    public void cleanAll() {
        sharedPreferences.edit()
                .remove(SharedConst.ENCRYPT_VALUE_TOKEN_1)
                .remove(SharedConst.ENCRYPT_VALUE_TOKEN_2)
                .remove(SharedConst.IS_LOGIN)
                .remove(SharedConst.IS_SETTING_PAY_PSW)
                .remove(SharedConst.VALUE_COUNTY_CITY)
                .remove(SharedConst.VALUE_COUNTY_CODE)
                .remove(SharedConst.VALUE_INVITE_CODE)
                .remove(SharedConst.VALUE_USER_ID)
                .remove(SharedConst.VALUE_USER_NAME)
                .remove(SharedConst.VALUE_USER_PHONE)
                .apply();
    }
}
