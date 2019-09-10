package com.xxx.compass.ui.my.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.utils.DownTimeUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 修改支付密码页
 * @Author xxx
 */
public class ModifyPayPswActivity extends BaseTitleActivity {

    @BindView(R.id.modify_pay_psw_sms_code_edit)
    EditText mSMSCodeEdit;
    @BindView(R.id.modify_pay_psw_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.modify_pay_psw_password_again_edit)
    EditText mPasswordAgainEdit;

    @BindView(R.id.modify_pay_psw_send_sms_code)
    TextView mSendSMSCode;

    @BindView(R.id.modify_pay_psw_password_eye)
    CheckBox mPasswordEye;
    @BindView(R.id.modify_pay_psw_password_again_eye)
    CheckBox mPasswordAgainEye;

    private LoadingDialog mLoadingDialog;

    @Override
    protected String initTitle() {
        return getString(R.string.modify_pay_psw_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pay_psw;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.modify_pay_psw_send_sms_code, R.id.modify_pay_psw_btn, R.id.modify_pay_psw_password_eye, R.id.modify_pay_psw_password_again_eye})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.modify_pay_psw_password_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordEye.isChecked(), mPasswordEdit);
                break;
            case R.id.modify_pay_psw_password_again_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordAgainEye.isChecked(), mPasswordAgainEdit);
                break;
            case R.id.modify_pay_psw_send_sms_code:
                sendSMSCode();
                break;
            case R.id.modify_pay_psw_btn:
                updatePsw();
                break;
        }
    }

    @Override
    public void finish() {
        KeyBoardUtil.closeKeyBord(this, mSMSCodeEdit);
        super.finish();
    }

    /**
     * @Model 发送修改密码短信验证码
     */
    private void sendSMSCode() {
        Api.getInstance().sendModifyPaySMSCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        ToastUtil.showToast(bean.getMessage());
                        DownTimeUtil.getInstance().openDownTime(ConfigClass.SMS_CODE_DOWN_TIME, new DownTimeUtil.Callback() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run(int nowTime) {
                                mSendSMSCode.setText(nowTime + "s");
                            }

                            @Override
                            public void end() {
                                mSendSMSCode.setText(getString(R.string.modify_pay_psw_send_sms_code));
                            }
                        });
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(ModifyPayPswActivity.this);
                            mLoadingDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    }
                });
    }


    /**
     * @Model 修改支付密码
     */
    private void updatePsw() {
        String smsCode = mSMSCodeEdit.getText().toString();
        String newPassword = mPasswordEdit.getText().toString();
        String newPasswordAgain = mPasswordAgainEdit.getText().toString();

        if (smsCode.isEmpty()) {
            ToastUtil.showToast(R.string.modify_pay_psw_error_1);
            return;
        }
        if (newPassword.isEmpty()) {
            ToastUtil.showToast(R.string.modify_pay_psw_error_2);
            return;
        }
        if (!newPassword.equals(newPasswordAgain)) {
            ToastUtil.showToast(R.string.modify_pay_psw_error_3);
            return;
        }
        if (!smsCode.matches(ConfigClass.MATCHES_SMS_CODE)) {
            ToastUtil.showToast(R.string.modify_pay_psw_error_4);
            return;
        }
        if (!newPassword.matches(ConfigClass.MATCHES_PASSWORD)) {
            ToastUtil.showToast(R.string.modify_pay_psw_error_5);
            return;
        }

        Api.getInstance().modifyPayPsw(smsCode, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        ToastUtil.showToast(bean.getMessage());
                        finish();
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(ModifyPayPswActivity.this);
                            mLoadingDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    }
                });
    }
}
