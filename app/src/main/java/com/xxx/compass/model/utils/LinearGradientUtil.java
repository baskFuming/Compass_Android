package com.xxx.compass.model.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.TextView;

public class LinearGradientUtil {

    /**
     * 设置textView 的颜色渐变
     *
     * @param text 设置textView
     */
    public static void setTextViewStyles(TextView text) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0,
                text.getPaint().getTextSize(),
                Color.parseColor("#6FFFEF"),
                Color.parseColor("#6FFFEF"),
                Shader.TileMode.CLAMP);
        text.getPaint().setShader(mLinearGradient);
        text.invalidate();
    }
}
