package com.xxx.compass.ui.wallet.window;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.xxx.compass.R;
import com.xxx.compass.base.dialog.BaseDialog;
import com.xxx.compass.model.utils.KeyBoardUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordWindow extends BaseDialog {

    private Callback callback;

    public static PasswordWindow getInstance(Activity context) {
        return new PasswordWindow(context);
    }

    @BindView(R.id.window_password_edit)
    public EditText mPasswordEdit;

    private PasswordWindow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.window_password;
    }

    @Override
    protected void initData() {
        setCancelable(true); // 是否可以按“返回键”消失
    }

    @Override
    protected double setWidth() {
        return 0.8;
    }

    @OnClick({R.id.window_password_btn, R.id.window_password_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_password_btn:
                String password = mPasswordEdit.getText().toString();
                if (callback != null) callback.callback(password);
                break;
            case R.id.window_password_return:
                KeyBoardUtil.closeKeyBord(getContext(), mPasswordEdit);
                dismiss();
                break;
        }
    }

    public interface Callback {
        void callback(String password);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
