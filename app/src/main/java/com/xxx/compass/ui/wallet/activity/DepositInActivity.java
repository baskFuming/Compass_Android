package com.xxx.compass.ui.wallet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
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

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.OnClick;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 转入页
 * @Author xxx
 */
public class DepositInActivity extends BaseTitleActivity {

    @BindView(R.id.deposit_in_edit)
    EditText mEdit;

    private double balance;
    private String coinId;
    private LoadingDialog mLoadingDialog;

    @Override
    protected String initTitle() {
        return getString(R.string.deposit_in_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_in;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        coinId = intent.getStringExtra("coinId");
        balance = intent.getDoubleExtra("balance", 0);
        mEdit.setHint(getString(R.string.deposit_in_edit) + " " + balance);

        //限定
        KeyBoardUtil.setFilters(mEdit, ConfigClass.DOUBLE_AMOUNT_NUMBER);
    }

    @OnClick({R.id.deposit_in_record, R.id.deposit_in_all, R.id.deposit_in_btn})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.deposit_in_record:
                Intent intent = new Intent(this, DepositRecordActivity.class);
                intent.putExtra("type", ApiType.DEPOSIT_RECORD_IN_TYPE);
                intent.putExtra("coinId", coinId);
                startActivity(intent);
                break;
            case R.id.deposit_in_all:
                String value = String.valueOf(balance);
                mEdit.setText(value);
                mEdit.setSelection(value.length());
                break;
            case R.id.deposit_in_btn:
                depositIn();
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
        intent.putExtra("inBalance", new BigDecimal(balance).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue());
        setResult(ConfigClass.DEPOSIT_IN_RESULT_CODE, intent);
        super.finish();
    }

    /**
     * @Model 理财转入
     */
    private void depositIn() {
        final double value;
        try {
            String s = mEdit.getText().toString();
            if (s.isEmpty()) {
                ToastUtil.showToast(R.string.deposit_in_error_1);
                return;
            }
            value = Double.parseDouble(s);
            if (value <= 0) {
                ToastUtil.showToast(R.string.deposit_in_error_1);
                return;
            }
            if (value > balance) {
                ToastUtil.showToast(R.string.deposit_out_error_3);
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(R.string.deposit_in_error_2);
            return;
        }
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().depositIn(userId, coinId, value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<BooleanBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<BooleanBean> bean) {
                        if (bean != null) {
                            BooleanBean data = bean.getData();
                            if (data != null) {
                                if (data.isResult()) {
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
                            mLoadingDialog = LoadingDialog.getInstance(DepositInActivity.this);
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
