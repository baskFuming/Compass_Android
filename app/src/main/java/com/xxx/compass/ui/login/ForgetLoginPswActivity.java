package com.xxx.compass.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
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
 * @Page 忘记密码页
 * @Author xxx
 */
public class ForgetLoginPswActivity extends BaseActivity {

    @BindView(R.id.forget_login_psw_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.forget_login_psw_sms_code_edit)
    EditText mSMSCodeEdit;
    @BindView(R.id.forget_login_psw_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.forget_login_psw_password_again_edit)
    EditText mPasswordAgainEdit;

    @BindView(R.id.forget_login_psw_send_sms_code)
    TextView mSendSMSCode;
    @BindView(R.id.forget_login_psw_selector_phone)
    TextView mSelectorCounty;

    @BindView(R.id.forget_login_psw_password_eye)
    CheckBox mPasswordEye;
    @BindView(R.id.forget_login_psw_password_again_eye)
    CheckBox mPasswordAgainEye;

    private LoadingDialog mLoadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_login_psw;
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.forget_login_psw_return,R.id.forget_login_psw_selector_phone, R.id.forget_login_psw_send_sms_code, R.id.forget_login_psw_btn, R.id.forget_login_psw_password_eye, R.id.forget_login_psw_password_again_eye})
    public void OnClick(View view) {
        KeyBoardUtil.closeKeyBord(this, mAccountEdit);
        switch (view.getId()) {
            case R.id.forget_login_psw_return:
                finish();
                break;
            case R.id.forget_login_psw_selector_phone:
                Intent intent = new Intent(this, SelectCountyActivity.class);
                intent.putExtra(SelectCountyActivity.REQUEST_KRY, SelectCountyActivity.FORGET_PAGE_CODE);
                startActivityForResult(intent, ConfigClass.REQUEST_CODE);
                break;
            case R.id.forget_login_psw_password_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordEye.isChecked(), mPasswordEdit);
                break;
            case R.id.forget_login_psw_password_again_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordAgainEye.isChecked(), mPasswordAgainEdit);
                break;
            case R.id.forget_login_psw_send_sms_code:
                sendSMSCode();
                break;
            case R.id.forget_login_psw_btn:
                updatePsw();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigClass.RESULT_CODE && data != null) {
            mSelectorCounty.setText(data.getStringExtra(SelectCountyActivity.RESULT_CODE_KRY));
        }
    }

    /**
     * @Model 发送忘记密码短信验证码
     */
    private void sendSMSCode() {
        String account = mAccountEdit.getText().toString();
        if (account.isEmpty()) {
            ToastUtil.showToast(R.string.forget_login_psw_error_1);
            return;
        }
        Api.getInstance().sendForgetSMSCode(account,"null","null","null")
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
                                mSendSMSCode.setText(getString(R.string.forget_login_psw_send_sms_code));
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
                            mLoadingDialog = LoadingDialog.getInstance(ForgetLoginPswActivity.this);
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
     * @Model 忘记密码
     */
    private void updatePsw() {
        final String account = mAccountEdit.getText().toString();
        String smsCode = mSMSCodeEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        String passwordAgain = mPasswordAgainEdit.getText().toString();

        if (account.isEmpty()) {
            ToastUtil.showToast(R.string.forget_login_psw_error_1);
            return;
        }
        if (smsCode.isEmpty()) {
            ToastUtil.showToast(R.string.forget_login_psw_error_2);
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast(R.string.forget_login_psw_error_3);
            return;
        }
        if (!password.equals(passwordAgain)) {
            ToastUtil.showToast(R.string.forget_login_psw_error_7);
            return;
        }
        if (!smsCode.matches(ConfigClass.MATCHES_SMS_CODE)) {
            ToastUtil.showToast(R.string.forget_login_psw_error_5);
            return;
        }
        if (!password.matches(ConfigClass.MATCHES_PASSWORD)) {
            ToastUtil.showToast(R.string.forget_login_psw_error_6);
            return;
        }

        Api.getInstance().forgetPsw(account, smsCode, password, "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        ToastUtil.showToast(bean.getMessage());
                        Intent intent = new Intent(ForgetLoginPswActivity.this, LoginActivity.class);
                        intent.putExtra("account", account);
                        setResult(ConfigClass.RESULT_CODE, intent);
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
                            mLoadingDialog = LoadingDialog.getInstance(ForgetLoginPswActivity.this);
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
