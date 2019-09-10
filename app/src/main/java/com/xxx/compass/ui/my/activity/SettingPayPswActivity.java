package com.xxx.compass.ui.my.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 设置支付密码页
 * @Author xxx
 */
public class SettingPayPswActivity extends BaseTitleActivity {

    @BindView(R.id.setting_pay_psw_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.setting_pay_psw_password_again_edit)
    EditText mPasswordAgainEdit;

    @BindView(R.id.setting_pay_psw_password_eye)
    CheckBox mPasswordEye;
    @BindView(R.id.setting_pay_psw_password_again_eye)
    CheckBox mPasswordAgainEye;

    private LoadingDialog mLoadingDialog;

    @Override
    protected String initTitle() {
        return getString(R.string.setting_pay_psw_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_pay_psw;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.setting_pay_psw_btn, R.id.setting_pay_psw_password_eye, R.id.setting_pay_psw_password_again_eye})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.setting_pay_psw_password_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordEye.isChecked(), mPasswordEdit);
                break;
            case R.id.setting_pay_psw_password_again_eye:
                KeyBoardUtil.setInputTypePassword(mPasswordAgainEye.isChecked(), mPasswordAgainEdit);
                break;
            case R.id.setting_pay_psw_btn:
                updatePsw();
                break;
        }
    }

    @Override
    public void finish() {
        KeyBoardUtil.closeKeyBord(this, mPasswordEdit);
        super.finish();
    }

    /**
     * @Model 设置支付密码
     */
    private void updatePsw() {
        String newPassword = mPasswordEdit.getText().toString();
        String newPasswordAgain = mPasswordAgainEdit.getText().toString();

        if (newPassword.isEmpty()) {
            ToastUtil.showToast(R.string.setting_pay_psw_error_1);
            return;
        }
        if (!newPassword.equals(newPasswordAgain)) {
            ToastUtil.showToast(R.string.setting_pay_psw_error_2);
            return;
        }
        if (!newPassword.matches(ConfigClass.MATCHES_PASSWORD)) {
            ToastUtil.showToast(R.string.setting_pay_psw_error_3);
            return;
        }

        Api.getInstance().settingPayPsw(newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        if (bean != null) {
                            SharedPreferencesUtil.getInstance().saveBoolean(SharedConst.IS_SETTING_PAY_PSW, true);
                            ToastUtil.showToast(bean.getMessage());
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
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(SettingPayPswActivity.this);
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
