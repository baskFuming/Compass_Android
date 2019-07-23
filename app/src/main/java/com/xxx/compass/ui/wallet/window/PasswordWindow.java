package com.xxx.compass.ui.wallet.window;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.base.dialog.BaseDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.DownTimeUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PasswordWindow extends BaseDialog {

    private Callback callback;
    private BaseActivity activity;

    public static PasswordWindow getInstance(BaseActivity context) {
        return new PasswordWindow(context);
    }

    @BindView(R.id.window_password_edit)
    public EditText mPasswordEdit;
    @BindView(R.id.window_password_send)
    public EditText mPasswordSend;
    @BindView(R.id.window_send_sms_code)
    public TextView sendCode;
    public TextView smsCode;
    private String password;
    private String code;


    private PasswordWindow(BaseActivity context) {
        super(context);
        this.activity = context;
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

    @OnClick({R.id.window_password_btn, R.id.window_password_return,R.id.window_send_sms_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_password_btn:
                password = mPasswordEdit.getText().toString();
                code = mPasswordSend.getText().toString();
                if (callback != null) callback.callback(password, code);
                break;
            case R.id.window_password_return:
                KeyBoardUtil.closeKeyBord(getContext(), mPasswordEdit, mPasswordSend);
                dismiss();
                break;
            case R.id.window_send_sms_code:
//                if (password.isEmpty()) {
//                    ToastUtil.showToast(R.string.withdrawal_error_5);
//                    return;
//                }
//                if (code.isEmpty()) {
//                    ToastUtil.showToast(R.string.forget_login_psw_error_2);
//                    return;
//                }
                sendSMSCode();
                break;
        }
    }

    private void sendSMSCode() {
        String phone = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_PHONE);
        String phoneName = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_COUNTY_CITY);

        Api.getInstance().sendWithdrawal(phone, phoneName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(activity) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        ToastUtil.showToast(bean.getMessage());
                        DownTimeUtil.getInstance().openDownTime(ConfigClass.SMS_CODE_DOWN_TIME, new DownTimeUtil.Callback() {
                            @Override
                            public void run(int nowTime) {
                                sendCode.setText(nowTime + "s");
                            }

                            @Override
                            public void end() {
                                sendCode.setText(R.string.register_send_sms_code);
                            }
                        });
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        ToastUtil.showToast(errorMessage);
                    }
                });
    }

    public interface Callback {
        void callback(String password, String code);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
