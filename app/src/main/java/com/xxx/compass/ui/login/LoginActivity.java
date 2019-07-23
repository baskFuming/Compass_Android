package com.xxx.compass.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.ActivityManager;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.LoginBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.MD5Util;
import com.xxx.compass.model.utils.SystemUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 登录页面
 * @Author xxx
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.login_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.login_password_eye)
    CheckBox mPasswordEye;

    private LoadingDialog mLoadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        //access-token
        String accessToken = MD5Util.getMD5(SystemUtil.getSerialNumber() + SystemUtil.getUUID());
        SharedPreferencesUtil util = SharedPreferencesUtil.getInstance();
        util.saveEncryptString(SharedConst.ENCRYPT_VALUE_TOKEN_2, accessToken);


        //保存记录
        String phone = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_PHONE);
        mAccountEdit.setText(phone);
    }

    @OnClick({R.id.login_password_eye, R.id.login_register, R.id.login_forger_password, R.id.login_btn})
    public void OnClick(View view) {
        KeyBoardUtil.closeKeyBord(this, mAccountEdit);
        switch (view.getId()) {
            case R.id.login_password_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordEye.isChecked(), mPasswordEdit);
                break;
            case R.id.login_forger_password:
                startActivityForResult(new Intent(this, ForgetLoginPswActivity.class), ConfigClass.REQUEST_CODE);
                break;
            case R.id.login_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), ConfigClass.REQUEST_CODE);
                break;
            case R.id.login_btn:
                login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        KeyBoardUtil.closeKeyBord(this, mAccountEdit, mPasswordEdit);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigClass.RESULT_CODE && data != null) {
            String account = data.getStringExtra("account");
            mAccountEdit.setText(account);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getInstance().AppExit();
    }

    /**
     * @Model 登录
     */
    private void login() {
        final String account = mAccountEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();

        if (account.isEmpty()) {
            ToastUtil.showToast(R.string.login_error_1);
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast(R.string.login_error_2);
            return;
        }

        Api.getInstance().login(account, password, "null", "null", "null")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<LoginBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<LoginBean> bean) {
                        if (bean != null) {
                            LoginBean data = bean.getData();
                            if (data != null) {
                                ToastUtil.showToast(bean.getMessage());

                                SharedPreferencesUtil util = SharedPreferencesUtil.getInstance();
                                util.saveString(SharedConst.VALUE_USER_PHONE, account);
                                util.saveString(SharedConst.VALUE_USER_NAME, data.getUsername());
                                util.saveString(SharedConst.VALUE_INVITE_CODE, data.getPromotionCode());
                                util.saveString(SharedConst.VALUE_USER_ID, String.valueOf(data.getId()));
                                util.saveBoolean(SharedConst.IS_LOGIN, true);
                                LoginBean.CountryBean country = data.getCountry();
                                if (country != null) {
                                    util.saveString(SharedConst.VALUE_COUNTY_CITY, country.getZhName());
                                }
                                //x-token
                                util.saveEncryptString(SharedConst.ENCRYPT_VALUE_TOKEN_1, data.getToken());

                                //判断下是否进入过首页
                                Activity activity = ActivityManager.getInstance().getActivity(MainActivity.class.getSimpleName());
                                if (activity != null) {
                                    setResult(ConfigClass.LOGIN_RESULT_CODE);
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(LoginActivity.this);
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
