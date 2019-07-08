package com.xxx.compass.model.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.base.App;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;

import java.util.Locale;


/**
 * 描述: 语言切换工具类
 */

public class LocalManageUtil {

    public static final String LANGUAGE_CN = "zh-CN,zh";   //中文
    public static final String LANGUAGE_US = "en-us,en";   //英式英文
    public static final String LANGUAGE_ENGLISH = "en-US,en";   //美式英文

    /**
     * 初始化语言 方法
     */
    public static Context setLocal(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLanguageLocale();//获取sp里面保存的语言

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            Locale.setDefault(locale);
            return context.createConfigurationContext(config);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
        return context;
    }

    /**
     * 如果不是英文、简体中文，默认返回简体中文
     */
    private static Locale getLanguageLocale() {
        String launcher = SharedPreferencesUtil.getInstance().getString(SharedConst.CONSTANT_LAUNCHER);
        switch (launcher) {
            case LocalManageUtil.LANGUAGE_CN:
                return Locale.SIMPLIFIED_CHINESE;
            case LocalManageUtil.LANGUAGE_US:
                return Locale.US;
            case LocalManageUtil.LANGUAGE_ENGLISH:
                return Locale.ENGLISH;
            default:
                Locale locale = Resources.getSystem().getConfiguration().locale;
                if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                    SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_CN);
                } else if (locale.equals(Locale.ENGLISH)) {
                    SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_ENGLISH);
                } else {
                    if (locale.getLanguage().equals("en")) {
                        SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_US);
                    } else {
                        SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_CN);
                    }
                }
                return locale;
        }
    }

}