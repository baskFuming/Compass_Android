package com.xxx.compass.model.utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.xxx.compass.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 手机号脱敏
     */
    public static String getPhoneCode(String phone) {
        if (phone != null && phone.trim().length() == 11) {
            StringBuilder sb = new StringBuilder(phone.trim());
            sb.replace(3, 7, "****");
            return sb.toString();
        }
        return phone;
    }

    /**
     * 获取时间
     */
    public static String getSimpleDataFormatTime(String pattern, long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    /**
     * 获取数字转化汉字
     */
    public static String getChinese(Activity activity,int key) {
        switch (key) {
            case 1:
                return activity.getString(R.string.first);
            case 2:
                return activity.getString(R.string.second);
            case 3:
                return activity.getString(R.string.three);
            case 4:
                return activity.getString(R.string.four);
            case 5:
                return activity.getString(R.string.five);
            case 6:
                return activity.getString(R.string.six);
            case 7:
                return activity.getString(R.string.seven);
            case 8:
                return activity.getString(R.string.eight);
            case 9:
                return activity.getString(R.string.nine);
        }
        return activity.getString(R.string.zero);
    }

    /**
     * 获取币
     */
    public static String getMoney(double money) {
        if (money == 0) {
            return "0";
        }
        return new BigDecimal(String.valueOf(money)).setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
    }

    /**
     * 获取美元
     */
    public static String getUS(double money) {
        if (money == 0) {
            return "0";
        }
        return new BigDecimal(String.valueOf(money)).setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
    }

}
