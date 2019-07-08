package com.xxx.compass.base.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

public abstract class BaseDialog extends AlertDialog {

    protected BaseDialog(Context context) {
        super(context);
    }

    /**
     * 展示Dialog
     */
    public void show() {
        setCancelable(true); // 是否可以按“返回键”消失
        setCanceledOnTouchOutside(false); // 点击框以外的区域

        //渲染布局
        View view = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        super.show();   //展示
        setContentView(view);

        //设置大小
        final Window window = getWindow();
        if (window != null) {
            final WindowManager.LayoutParams lp = window.getAttributes();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    double width = setWidth();
                    if (width != 0) {
                        lp.width = (int) (window.getDecorView().getWidth() * width);
                        window.setGravity(Gravity.CLIP_HORIZONTAL);
                        window.setAttributes(lp);
                    }
                    initData();
                }
            });
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract double setWidth();
}
