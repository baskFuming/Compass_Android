package com.xxx.compass.ui.wallet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.WithdrawalRecordBean;
import com.xxx.compass.model.http.utils.ApiCode;
import com.xxx.compass.model.http.utils.ApiError;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.PermissionUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.main.SweepActivity;
import com.xxx.compass.ui.my.activity.SettingPayPswActivity;
import com.xxx.compass.ui.wallet.adapter.WithdrawalRecordAdapter;
import com.xxx.compass.ui.wallet.window.PasswordWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 提现页
 * @Author xxx
 */
public class WithdrawalActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, PasswordWindow.Callback {

    @BindView(R.id.main_not_data)
    LinearLayout mNotData;
    @BindView(R.id.withdrawal_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.withdrawal_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.withdrawal_address)
    EditText mAddress;
    @BindView(R.id.withdrawal_amount)
    EditText mAmount;
    @BindView(R.id.withdrawal_balance)
    TextView mBalance;
    @BindView(R.id.withdrawal_fee)
    TextView mFee;

    private String address;
    private double amount;
    private String coinId;
    private double balance;
    private double fee;
    private int page = ConfigClass.PAGE_DEFAULT;
    private WithdrawalRecordAdapter mAdapter;
    private List<WithdrawalRecordBean> mList = new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    private PasswordWindow mPasswordWindow;

    @Override
    protected String initTitle() {
        return coinId + getString(R.string.withdrawal_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawal;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        balance = intent.getDoubleExtra("balance", 0.0);
        fee = intent.getDoubleExtra("fee", 0.0);
        coinId = getIntent().getStringExtra("coinId");
        mBalance.setText(String.valueOf(balance));
        mFee.setText(getString(R.string.withdrawal_fee) + fee + coinId);

        mAdapter = new WithdrawalRecordAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        //限定
        KeyBoardUtil.setFilters(mAmount, ConfigClass.DOUBLE_AMOUNT_NUMBER);

        loadData();
    }

    @OnClick({R.id.main_return, R.id.withdrawal_btn, R.id.withdrawal_sweep})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.main_return:
                finish();
                break;
            case R.id.withdrawal_btn:
                showWithdrawalDialog();
                break;
            case R.id.withdrawal_sweep:
                if (!PermissionUtil.checkPermission(this, PermissionUtil.READ_PERMISSION, PermissionUtil.WRITE_PERMISSION, PermissionUtil.CAMERA_PERMISSION)) {
                    startActivityForResult(new Intent(this, SweepActivity.class), ConfigClass.REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void finish() {
        KeyBoardUtil.closeKeyBord(this, mAddress);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            String content = data.getStringExtra(Intents.Scan.RESULT);
            mAddress.setText(content);
        }
    }

    @Override
    public void onRefresh() {
        page = ConfigClass.PAGE_DEFAULT;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = PermissionUtil.onRequestPermissionsResult(permissions, grantResults);
        if (result) {
            startActivityForResult(new Intent(this, CaptureActivity.class), ConfigClass.REQUEST_CODE);
        } else {
            if (!PermissionUtil.checkPermission(this, PermissionUtil.READ_PERMISSION, PermissionUtil.WRITE_PERMISSION, PermissionUtil.CAMERA_PERMISSION)) {
                startActivityForResult(new Intent(this, CaptureActivity.class), ConfigClass.REQUEST_CODE);
            }
        }
    }

    /**
     * @Model 获取存币记录列表
     */
    private void loadData() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getWithdrawalRecordList(userId, coinId, page, ConfigClass.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<WithdrawalRecordBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<WithdrawalRecordBean>> bean) {
                        if (bean == null) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }

                        List<WithdrawalRecordBean> list = bean.getData();
                        if (list == null || list.size() == 0 && page == ConfigClass.PAGE_DEFAULT) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }
                        mRecycler.setVisibility(View.VISIBLE);
                        mNotData.setVisibility(View.GONE);
                        if (page == ConfigClass.PAGE_DEFAULT) {
                            mList.clear();
                        }

                        mList.addAll(list);
                        if (list.size() < ConfigClass.PAGE_SIZE) {
                            mAdapter.loadMoreEnd(true);
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mRefresh != null && page == ConfigClass.PAGE_DEFAULT) {
                            mRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mRefresh != null) {
                            mRefresh.setRefreshing(false);
                        }
                    }
                });
    }

    /**
     * 提现验证
     */
    private void showWithdrawalDialog() {
        address = mAddress.getText().toString();
        String amount = mAmount.getText().toString();

        if (address.isEmpty()) {
            ToastUtil.showToast(R.string.withdrawal_error_1);
            return;
        }
        if (amount.isEmpty()) {
            ToastUtil.showToast(R.string.withdrawal_error_2);
            return;
        }
        try {
            if (Double.parseDouble(amount) + fee > balance) {
                ToastUtil.showToast(R.string.withdrawal_error_3);
                return;
            }

        } catch (Exception e) {
            ToastUtil.showToast(R.string.withdrawal_error_4);
            return;
        }

        this.amount = Double.parseDouble(amount);

        if (mPasswordWindow == null || !mPasswordWindow.isShowing()) {
            mPasswordWindow = PasswordWindow.getInstance(this);
            mPasswordWindow.setCallback(this);
            mPasswordWindow.show();
        }
    }

    @Override
    public void callback(String password) {
        if (password.isEmpty()) {
            ToastUtil.showToast(R.string.withdrawal_error_5);
            return;
        }
        withdrawal(password);
    }

    /**
     * @Model 提现
     */
    private void withdrawal(String password) {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().withdrawal(userId, coinId, fee, amount, address, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        if (bean != null) {
                            mPasswordWindow.dismiss();
                            mPasswordWindow = null;
                            balance -= (amount + fee);
                            mBalance.setText(String.valueOf(balance));
                            mAmount.setText("");
                            loadData();
                            ToastUtil.showToast(bean.getMessage());
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        if (errorCode == ApiCode.PAY_NOT_SETTING) {
                            ToastUtil.showToast(getString(R.string.window_exchange_error_4));
                            startActivity(new Intent(WithdrawalActivity.this, SettingPayPswActivity.class));
                            return;
                        }
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(WithdrawalActivity.this);
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
