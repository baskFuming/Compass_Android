package com.xxx.compass.ui.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.DoExceptionBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.base.BooleanBean;
import com.xxx.compass.model.http.bean.WalletBean;
import com.xxx.compass.model.http.utils.ApiType;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.wallet.activity.DepositActivity;
import com.xxx.compass.ui.wallet.activity.RechargeActivity;
import com.xxx.compass.ui.wallet.activity.WithdrawalActivity;
import com.xxx.compass.ui.wallet.adapter.WalletAdapter;
import com.xxx.compass.ui.wallet.window.WalletExchangeWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 钱包布局
 * @Author xxx
 */
public class WalletFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, WalletExchangeWindow.Callback {

    @BindView(R.id.main_wallet_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;
    @BindView(R.id.main_wallet_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_wallet_total_asset)
    TextView mTotalAsset;
    @BindView(R.id.main_wallet_available_asset)
    TextView mAvailableAsset;

    private WalletAdapter mAdapter;
    private List<WalletBean.TmpWalletExtBean> mList = new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    private WalletExchangeWindow mExchangerWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void initData() {
        mAdapter = new WalletAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnItemChildClickListener(this);

        loadData(true);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WalletBean.TmpWalletExtBean bean = mList.get(position);
        switch (view.getId()) {
            case R.id.item_wallet_recharge:
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra("coinId", bean.getCoinId());
                intent.putExtra("address", bean.getAddress());
                startActivity(intent);
                break;
            case R.id.item_wallet_withdrawal:
                Intent intent1 = new Intent(getContext(), WithdrawalActivity.class);
                intent1.putExtra("coinId", bean.getCoinId());
                intent1.putExtra("balance", bean.getBalance());
                intent1.putExtra("fee", bean.getFee());
                startActivity(intent1);
                break;
            case R.id.item_wallet_exchange:
                if (mList.get(position).getSupportExchange() == ApiType.OPEN_EXCHANGE_TYPE) {
                    if (mExchangerWindow == null || !mExchangerWindow.isShowing()) {
                        mExchangerWindow = WalletExchangeWindow.getInstance(getActivity());
                        mExchangerWindow.setCallback(this);
                        mExchangerWindow.setCoinId(bean.getCoinId());
                        mExchangerWindow.setBalance(bean.getBalance());
                        mExchangerWindow.show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_wallet_deposit:
                if (mList.get(position).getSupportFinanc() == ApiType.OPEN_FINANC_TYPE) {
                    Intent intent2 = new Intent(getContext(), DepositActivity.class);
                    intent2.putExtra("coinId", bean.getCoinId());
                    intent2.putExtra("inBalance", bean.getBalance());
                    intent2.putExtra("outBalance", bean.getFinancingCnt());
                    startActivity(intent2);
                } else {
                    ToastUtil.showToast(getString(R.string.main_app_not_1));
//                    Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void callback(String coinId, double value) {
        exchanger(coinId, value);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(false);
    }

    /**
     * @param isRefresh 是否刷新
     * @Model 获取钱包列表
     */
    private void loadData(final boolean isRefresh) {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getWalletList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<WalletBean>(getActivity()) {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(BaseBean<WalletBean> bean) {
                        if (bean != null) {
                            WalletBean data = bean.getData();
                            if (data != null) {
                                mTotalAsset.setText("$" + new BigDecimal(data.getAllAsset()).setScale(2, RoundingMode.DOWN).toPlainString());
                                mAvailableAsset.setText("$" + new BigDecimal(data.getAllAvailableAsset()).setScale(2, RoundingMode.DOWN).toPlainString());
                                List<WalletBean.TmpWalletExtBean> list = data.getTmpWalletExt();
                                if (list == null || list.size() == 0) {
                                    mNotData.setVisibility(View.VISIBLE);
                                    mRecycler.setVisibility(View.GONE);
                                    return;
                                }

                                mNotData.setVisibility(View.GONE);
                                mRecycler.setVisibility(View.VISIBLE);
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mRefresh != null && isRefresh) {
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
     * @Model 兑换
     */
    private void exchanger(String coinId, double value) {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().exchange(userId, coinId, value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<DoExceptionBean>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<DoExceptionBean> bean) {
                        if (bean != null) {
                            ToastUtil.showToast(bean.getMessage());
                            DoExceptionBean data = bean.getData();
                            if (data != null && mExchangerWindow != null) {
                                if (data.isResult()) {
                                    mExchangerWindow.dismiss();
                                    mExchangerWindow = null;
                                    loadData(false);
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
                            mLoadingDialog = LoadingDialog.getInstance(getContext());
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
