package com.xxx.compass.ui.my.activity;

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
import com.xxx.compass.ui.wallet.adapter.DepositProfitAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 存币收益页
 * @Author xxx
 */
public class DepositProfitActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.main_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;

    @BindView(R.id.deposit_profit_asset)
    TextView mTotalAsset;
    @BindView(R.id.deposit_profit_profit)
    TextView mTotalProfit;

    private int page = ConfigClass.PAGE_DEFAULT;
    private DepositProfitAdapter mAdapter;
    private List<DepositProfitBean> mList = new ArrayList<>();

    @Override
    protected String initTitle() {
        return getString(R.string.deposit_profit_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_profit;
    }

    @Override
    protected void initData() {
        mAdapter = new DepositProfitAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        loadInfo();
        loadList();
    }

    @Override
    public void onRefresh() {
        page = ConfigClass.PAGE_DEFAULT;
        loadInfo();
        loadList();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadList();
    }

    /**
     * @Model 获取理财总收益列表
     */
    private void loadList() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getDepositTotalProfitList(userId, page, ConfigClass.PAGE_SIZE)
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
        Api.getInstance().getDepositTotalProfitInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<DepositInfoBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<DepositInfoBean> bean) {
                        if (bean != null){
                            DepositInfoBean data = bean.getData();
                            if (data != null){
                                mTotalAsset.setText(data.getAllFinancingsAsset(true));
                                mTotalProfit.setText(data.getAllFinancingsIncome());
                            }
                        }
                    }
                });
    }
}
