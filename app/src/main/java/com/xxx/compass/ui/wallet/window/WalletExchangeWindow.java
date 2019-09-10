package com.xxx.compass.ui.wallet.window;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.dialog.BaseDialog;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletExchangeWindow extends BaseDialog {

    private String coinId;
    private double balance;
    private Callback callback;

    public static WalletExchangeWindow getInstance(Activity context) {
        return new WalletExchangeWindow(context);
    }

    @BindView(R.id.window_exchange_edit)
    public EditText mAccountEdit;
    @BindView(R.id.window_exchange_title)
    public TextView mTitle;

    private WalletExchangeWindow(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.window_exchange;
    }

    @Override
    protected void initData() {
        setCancelable(true); // 是否可以按“返回键”消失
        mAccountEdit.setHint(getContext().getString(R.string.window_exchange_edit_hint) + balance);
        mTitle.setText(coinId + getContext().getString(R.string.window_exchange_title));

        //限定
        KeyBoardUtil.setFilters(mAccountEdit, ConfigClass.DOUBLE_AMOUNT_NUMBER);
    }

    @OnClick({R.id.window_exchange_btn, R.id.window_exchange_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_exchange_btn:
                try {
                    String s = mAccountEdit.getText().toString();
                    if (s.isEmpty()) {
                        ToastUtil.showToast(R.string.window_exchange_error_1);
                        return;
                    }
                    double value = Double.parseDouble(s);
                    if (value == 0) {
                        ToastUtil.showToast(R.string.window_exchange_error_1);
                        return;
                    }
                    if (value > balance) {
                        ToastUtil.showToast(R.string.window_exchange_error_3);
                        return;
                    }
                    if (callback != null) callback.callback(coinId, value);
                } catch (Exception e) {
                    ToastUtil.showToast(R.string.window_exchange_error_2);
                }
                break;
            case R.id.window_exchange_return:
                KeyBoardUtil.closeKeyBord(getContext(), mAccountEdit);
                dismiss();
                break;
        }
    }

    @Override
    protected double setWidth() {
        return 0.8;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public interface Callback {
        void callback(String coinId, double value);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
