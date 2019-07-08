package com.xxx.compass.ui.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.AppVersionBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.LocalManageUtil;
import com.xxx.compass.model.utils.SystemUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.main.UpdateWindow;
import com.xxx.compass.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 账户设置页
 * @Author xxx
 */
public class AccountSettingActivity extends BaseTitleActivity {

    @BindView(R.id.account_setting_version_code)
    TextView mVersionCode;
    @BindView(R.id.account_setting_switch_language_code)
    TextView mLanguageCode;

    private String versionName;
    private LoadingDialog mLoadDialog;

    @Override
    protected String initTitle() {
        return getString(R.string.account_setting_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_setting;
    }

    @Override
    protected void initData() {
        versionName = SystemUtil.getVersionName(this);
        mVersionCode.setText(versionName);

        String languageCode = SharedPreferencesUtil.getInstance().getString(SharedConst.CONSTANT_LAUNCHER);
        switch (languageCode) {
            case LocalManageUtil.LANGUAGE_CN:
                mLanguageCode.setText(getString(R.string.language_simple_zh));
                break;
            case LocalManageUtil.LANGUAGE_US:
                mLanguageCode.setText(getString(R.string.language_en));
                break;
            default:
                mLanguageCode.setText(getString(R.string.language_simple_zh));
                break;
        }
    }

    @OnClick({R.id.account_setting_set_password, R.id.account_setting_use_help, R.id.account_setting_switch_language, R.id.account_setting_check_version, R.id.account_setting_out_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_setting_set_password:
                startActivity(new Intent(AccountSettingActivity.this, PasswordSettingActivity.class));
                break;
            case R.id.account_setting_use_help:
                startActivity(new Intent(AccountSettingActivity.this, UseHelpActivity.class));
                break;
            case R.id.account_setting_switch_language:
                startActivity(new Intent(AccountSettingActivity.this, LanguageActivity.class));
                break;
            case R.id.account_setting_check_version:
                checkAppVersion();
                break;
            case R.id.account_setting_out_login:
                outLogin();
                break;
        }
    }

    /**
     * @Model 检查App版本
     */
    private void checkAppVersion() {
        Api.getInstance().checkAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<AppVersionBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<AppVersionBean> bean) {
                        if (bean != null) {
                            AppVersionBean data = bean.getData();
                            if (data != null) {
                                String version = data.getVersion();
                                if (versionName.equals(version)) {
                                    ToastUtil.showToast(R.string.check_version_not);
                                } else {
                                    UpdateWindow.getInstance(AccountSettingActivity.this, data.getDownloadUrl());
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
                        if (mLoadDialog == null) {
                            mLoadDialog = LoadingDialog.getInstance(AccountSettingActivity.this);
                            mLoadDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadDialog != null) {
                            mLoadDialog.dismiss();
                            mLoadDialog = null;
                        }
                    }
                });
    }

    /**
     * @Model 退出登录
     */
    private void outLogin() {
        Api.getInstance().outLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        if (bean != null) {
                            ToastUtil.showToast(bean.getMessage());
                            SharedPreferencesUtil.getInstance().cleanAll(); //清空所有数据
                            startActivity(new Intent(AccountSettingActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadDialog == null) {
                            mLoadDialog = LoadingDialog.getInstance(AccountSettingActivity.this);
                            mLoadDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadDialog != null) {
                            mLoadDialog.dismiss();
                            mLoadDialog = null;
                        }
                    }
                });
    }

}
