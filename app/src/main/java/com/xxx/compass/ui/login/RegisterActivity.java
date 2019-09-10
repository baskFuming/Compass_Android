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
 * @Page 注册页面
 * @Author xxx
 */
public class RegisterActivity extends BaseTitleActivity {

    @BindView(R.id.register_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.register_sms_code_edit)
    EditText mSMSCodeEdit;
    @BindView(R.id.register_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.register_invite_edit)
    EditText mInviteEdit;

    @BindView(R.id.register_selector_phone)
    TextView mSelectorCounty;
    @BindView(R.id.register_send_sms_code)
    TextView mSMSCodeText;

    @BindView(R.id.register_password_eye)
    CheckBox mPasswordEye;

    private LoadingDialog mLoadingDialog;
    private String phoneName = "中国";    //默认是中国 +86

    @Override
    protected String initTitle() {
        return getString(R.string.register_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.register_account_return, R.id.register_selector_phone, R.id.register_password_eye, R.id.register_send_sms_code, R.id.register_btn})
    public void OnClick(View view) {
        KeyBoardUtil.closeKeyBord(this, mAccountEdit);
        switch (view.getId()) {
            case R.id.register_account_return:
                finish();
                break;
            case R.id.register_selector_phone:
                Intent intent = new Intent(this, SelectCountyActivity.class);
                intent.putExtra(SelectCountyActivity.REQUEST_KRY, SelectCountyActivity.REGISTER_PAGE_CODE);
                startActivityForResult(intent, ConfigClass.REQUEST_CODE);
                break;
            case R.id.register_password_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordEye.isChecked(), mPasswordEdit);
                break;
            case R.id.register_send_sms_code:
                sendSMSCode();
                break;
            case R.id.register_btn:
                register();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigClass.RESULT_CODE && data != null) {
            phoneName = data.getStringExtra(SelectCountyActivity.RESULT_NAME_KRY);
            String phoneCode = data.getStringExtra(SelectCountyActivity.RESULT_CODE_KRY);
            mSelectorCounty.setText(phoneCode);
        }
    }

    /**
     * @Model 发送短信验证码
     */
    private void sendSMSCode() {
        String account = mAccountEdit.getText().toString();
        if (account.isEmpty()) {
            ToastUtil.showToast(R.string.register_error_1);
            return;
        }
        Api.getInstance().sendSMSCode(account, phoneName)
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
                                mSMSCodeText.setText(nowTime + "s");
                            }

                            @Override
                            public void end() {
                                mSMSCodeText.setText(getString(R.string.register_send_sms_code));
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
                            mLoadingDialog = LoadingDialog.getInstance(RegisterActivity.this);
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
     * @Model 注册
     */
    private void register() {
        final String account = mAccountEdit.getText().toString();
        String smsCode = mSMSCodeEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        String inviteCode = mInviteEdit.getText().toString();

        if (account.isEmpty()) {
            ToastUtil.showToast(R.string.register_error_1);
            return;
        }
        if (smsCode.isEmpty()) {
            ToastUtil.showToast(R.string.register_error_2);
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast(R.string.register_error_3);
            return;
        }
        if (!smsCode.matches(ConfigClass.MATCHES_SMS_CODE)) {
            ToastUtil.showToast(R.string.register_error_5);
            return;
        }
        if (!password.matches(ConfigClass.MATCHES_PASSWORD)) {
            ToastUtil.showToast(R.string.register_error_6);
            return;
        }
        if (inviteCode.isEmpty()) {
            ToastUtil.showToast(R.string.register_error_7);
            return;
        }

        Api.getInstance().register(account, account, smsCode, password, phoneName, inviteCode, "null", "null")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        ToastUtil.showToast(bean.getMessage());
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                            mLoadingDialog = LoadingDialog.getInstance(RegisterActivity.this);
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
