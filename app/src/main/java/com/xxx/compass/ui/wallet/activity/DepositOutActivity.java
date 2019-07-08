package com.xxx.compass.ui.wallet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.base.BooleanBean;
import com.xxx.compass.model.http.utils.ApiType;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.my.activity.SettingPayPswActivity;
import com.xxx.compass.ui.wallet.window.PasswordWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 转出页
 * @Author xxx
 */
public class DepositOutActivity extends BaseTitleActivity implements PasswordWindow.Callback {

    @BindView(R.id.deposit_out_edit)
    EditText mEdit;

    private double value;
    private double balance;
    private String coinId;
    private LoadingDialog mLoadingDialog;
    private PasswordWindow mPasswordWindow;

    @Override
    protected String initTitle() {
        return getString(R.string.deposit_out_record);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_out;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        coinId = intent.getStringExtra("coinId");
        balance = intent.getDoubleExtra("balance", 0);
        mEdit.setHint(getString(R.string.deposit_out_edit) + " " + balance);

        //限定
        KeyBoardUtil.setFilters(mEdit, ConfigClass.DOUBLE_AMOUNT_NUMBER);
    }

    @OnClick({R.id.deposit_out_record, R.id.deposit_out_all, R.id.deposit_out_btn})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.deposit_out_record:
                Intent intent = new Intent(this, DepositRecordActivity.class);
                intent.putExtra("type", ApiType.DEPOSIT_RECORD_OUT_TYPE);
                intent.putExtra("coinId", coinId);
                startActivity(intent);
                break;
            case R.id.deposit_out_all:
                String value = String.valueOf(balance);
                mEdit.setText(value);
                mEdit.setSelection(value.length());
                break;
            case R.id.deposit_out_btn:
                showDepositOutDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        KeyBoardUtil.closeKeyBord(this, mEdit);
        Intent intent = new Intent(this, DepositActivity.class);
        intent.putExtra("outBalance", new BigDecimal(balance).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue());
        setResult(ConfigClass.DEPOSIT_OUT_RESULT_CODE, intent);
        super.finish();
    }

    /**
     * 理财转出验证
     */
    private void showDepositOutDialog() {
        try {
            String s = mEdit.getText().toString();
            if (s.isEmpty()) {
                ToastUtil.showToast(R.string.deposit_out_error_1);
                return;
            }
            value = Double.parseDouble(s);
            if (value <= 0) {
                ToastUtil.showToast(R.string.deposit_out_error_1);
                return;
            }
            if (value > balance) {
                ToastUtil.showToast(R.string.deposit_out_error_3);
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(R.string.deposit_out_error_2);
            return;
        }

        if (!SharedPreferencesUtil.getInstance().getBoolean(SharedConst.IS_SETTING_PAY_PSW)) {
            ToastUtil.showToast(R.string.deposit_out_error_3);
            startActivity(new Intent(this, SettingPayPswActivity.class));
            return;
        }

        if (mPasswordWindow == null || !mPasswordWindow.isShowing()) {
            mPasswordWindow = PasswordWindow.getInstance(this);
            mPasswordWindow.setCallback(this);
            mPasswordWindow.show();
        }
    }

    @Override
    public void callback(String password) {
        if (password.isEmpty()) {
            ToastUtil.showToast(R.string.deposit_out_error_5);
            return;
        }
        depositOut(password);
    }

    /**
     * @Model 理财转出
     */
    private void depositOut(String password) {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().depositOut(userId, coinId, value, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<BooleanBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<BooleanBean> bean) {
                        if (bean != null) {
                            BooleanBean data = bean.getData();
                            if (data != null) {
                                if (data.isResult()) {
                                    mPasswordWindow.dismiss();
                                    mPasswordWindow = null;
                                    balance = new BigDecimal(balance - value).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue();
                                    mEdit.setHint(getString(R.string.deposit_out_edit) + " " + balance);
                                    mEdit.setText("");
                                    ToastUtil.showToast(bean.getMessage());
                                    return;
                                }
                            }
                            ToastUtil.showToast(bean.getMessage());
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(DepositOutActivity.this);
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
