package com.xxx.compass.ui.wallet.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.DepositRecordBean;
import com.xxx.compass.model.http.utils.ApiType;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.ui.wallet.adapter.DepositRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DepositRecordActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.main_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;

    private int page = ConfigClass.PAGE_DEFAULT;
    private DepositRecordAdapter mAdapter;
    private List<DepositRecordBean> mList = new ArrayList<>();
    private int type;
    private String coinId;

    @Override
    protected String initTitle() {
        if (type == ApiType.DEPOSIT_RECORD_IN_TYPE) {
            return getString(R.string.deposit_record_in_title);
        }
        if (type == ApiType.DEPOSIT_RECORD_OUT_TYPE) {
            return getString(R.string.deposit_record_out_title);
        }
        return "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_record;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        coinId = intent.getStringExtra("coinId");
        type = intent.getIntExtra("type", 0);

        mAdapter = new DepositRecordAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        if (type == ApiType.DEPOSIT_RECORD_IN_TYPE) {
            mAdapter.setTag("+");
        }
        if (type == ApiType.DEPOSIT_RECORD_OUT_TYPE) {
            mAdapter.setTag("-");
        }

        loadData();
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


    /**
     * @Model 获取存币记录列表
     */
    private void loadData() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Observable<BaseBean<List<DepositRecordBean>>> observable = null;
        if (type == ApiType.DEPOSIT_RECORD_IN_TYPE) {
            observable = Api.getInstance().getDepositInRecordList(userId, coinId, page, ConfigClass.PAGE_SIZE);
        }
        if (type == ApiType.DEPOSIT_RECORD_OUT_TYPE) {
            observable = Api.getInstance().getDepositOutRecordList(userId, coinId, page, ConfigClass.PAGE_SIZE);
        }
        if (observable == null) return;

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<DepositRecordBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<DepositRecordBean>> bean) {
                        if (bean == null) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }

                        List<DepositRecordBean> list = bean.getData();
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
}
