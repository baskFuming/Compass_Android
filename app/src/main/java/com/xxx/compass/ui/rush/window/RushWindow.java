package com.xxx.compass.ui.rush.window;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.dialog.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class RushWindow extends BaseDialog {

    private Callback callback;
    private String content;

    public static RushWindow getInstance(Activity activity) {
        return new RushWindow(activity);
    }

    @BindView(R.id.window_rush_content)
    public TextView mContent;

    private RushWindow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.window_rush;
    }

    @Override
    protected void initData() {
        mContent.setText(content);
    }

    @Override
    protected double setWidth() {
        return 0.8;
    }

    @OnClick({R.id.window_rush_btn, R.id.window_rush_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_rush_btn:
                if (callback != null) callback.callback();
                dismiss();
                break;
            case R.id.window_rush_return:
                dismiss();
                break;
        }
    }

    public interface Callback {
        void callback();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
