package com.xxx.compass.ui.wallet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.DepositInfoBean;
import com.xxx.compass.model.http.bean.DepositProfitBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.ui.my.adapter.DepositProfitAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 理财页
 * @Author xxx
 */
public class DepositActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.main_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;

    @BindView(R.id.deposit_asset)
    TextView mTotalAsset;
    @BindView(R.id.deposit_asset_text)
    TextView mTotalAssetText;
    @BindView(R.id.deposit_profit)
    TextView mTotalProfit;

    private int page = ConfigClass.PAGE_DEFAULT;
    private DepositProfitAdapter mAdapter;
    private List<DepositProfitBean> mList = new ArrayList<>();

    private String coinId;
    private double inBalance;
    private double outBalance;

    @Override
    protected String initTitle() {
        return getString(R.string.deposit_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        coinId = intent.getStringExtra("coinId").toUpperCase();
        inBalance = intent.getDoubleExtra("inBalance", 0);
        outBalance = intent.getDoubleExtra("outBalance", 0);
        mTotalAssetText.setText(getString(R.string.deposit_total_asset) + "(" + coinId + ")");

        mAdapter = new DepositProfitAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        loadList();
    }

    @OnClick({R.id.deposit_in, R.id.deposit_out})
    public void OnClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.deposit_in:
                intent = new Intent(this, DepositInActivity.class);
                intent.putExtra("balance", inBalance);
                break;
            case R.id.deposit_out:
                intent = new Intent(this, DepositOutActivity.class);
                intent.putExtra("balance", outBalance);
                break;
        }
        if (intent != null) {
            intent.putExtra("coinId", coinId);
            startActivityForResult(intent, ConfigClass.REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }

    @Override
    public void onRefresh() {
        page = ConfigClass.PAGE_DEFAULT;
        loadList();
        loadInfo();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigClass.DEPOSIT_IN_RESULT_CODE && data != null) {
            double in = data.getDoubleExtra("inBalance", inBalance);
            if (in != inBalance) {
                outBalance += new BigDecimal(inBalance).subtract(new BigDecimal(in)).setScale(ConfigClass.DOUBLE_AMOUNT_NUMBER, RoundingMode.DOWN).doubleValue();
                inBalance = in;
            }
        }
        if (resultCode == ConfigClass.DEPOSIT_OUT_RESULT_CODE && data != null) {
            double out = data.getDoubleExtra("outBalance", outBalance);
            if (out != outBalance) {
                inBalance += new BigDecimal(outBalance).subtract(new BigDecimal(out)).setScale(ConfigClass.DOUBLE_AMOUNT_NUMBER, RoundingMode.DOWN).doubleValue();
                outBalance = out;
            }
        }
    }

    /**
     * @Model 获取理财总收益列表
     */
    private void loadList() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getDepositProfitList(userId, coinId, page, ConfigClass.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<DepositProfitBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<DepositProfitBean>> bean) {
                        if (bean == null) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }

                        List<DepositProfitBean> list = bean.getData();
                        if (list == null || list.size() == 0 && page == ConfigClass.PAGE_DEFAULT) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }

                        mNotData.setVisibility(View.GONE);
                        mRecycler.setVisibility(View.VISIBLE);
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
     * @Model 获取理财总收益信息
     */
    private void loadInfo() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getDepositProfitInfo(userId, coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<DepositInfoBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<DepositInfoBean> bean) {
                        if (bean != null) {
                            DepositInfoBean data = bean.getData();
                            if (data != null) {
                                mTotalAsset.setText(data.getAllFinancingsAsset(false));
                                mTotalProfit.setText(data.getAllFinancingsIncome());
                            }
                        }
                    }
                });
    }
}
