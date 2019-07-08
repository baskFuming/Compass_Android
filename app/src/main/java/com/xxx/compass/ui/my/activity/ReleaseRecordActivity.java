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
import com.xxx.compass.model.http.bean.ReleaseInfoBean;
import com.xxx.compass.model.http.bean.ReleaseRecordBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.ui.my.adapter.ReleaseRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 释放记录页
 * @Author xxx
 */
public class ReleaseRecordActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.main_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;
    @BindView(R.id.release_total_balance)
    TextView mTotalBalance;
    @BindView(R.id.release_total_out)
    TextView mTotalOut;
    @BindView(R.id.release_today_balance)
    TextView mTodayBalance;
    @BindView(R.id.release_today_asset)
    TextView mTodayAsset;
    @BindView(R.id.release_total_asset)
    TextView mTotalAsset;

    private int page = ConfigClass.PAGE_DEFAULT;
    private ReleaseRecordAdapter mAdapter;
    private List<ReleaseRecordBean> mList = new ArrayList<>();

    @Override
    protected String initTitle() {
        return getString(R.string.activity_release_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_record;
    }

    @Override
    protected void initData() {
        mAdapter = new ReleaseRecordAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        loadData();
        loadInfo();
    }

    @Override
    public void onRefresh() {
        page = ConfigClass.PAGE_DEFAULT;
        loadData();
        loadInfo();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadData();
    }


    /**
     * @Model 获取释放记录列表
     */
    private void loadData() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getReleaseRecordList(userId, page, ConfigClass.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<ReleaseRecordBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<ReleaseRecordBean>> bean) {
                        if (bean == null) {
                            mNotData.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mAdapter.loadMoreEnd(true);
                            return;
                        }

                        List<ReleaseRecordBean> list = bean.getData();
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
     * @Model 获取释放记录列表
     */
    private void loadInfo() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getReleaseRecordInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<ReleaseInfoBean>(this) {

                    @Override
                    public void onSuccess(BaseBean<ReleaseInfoBean> bean) {
                        if (bean != null) {
                            ReleaseInfoBean data = bean.getData();
                            if (data != null) {
                                mTotalBalance.setText(data.getAllRemainAsset());
                                mTotalOut.setText(data.getAllReleaseAsset());
                                mTodayBalance.setText(data.getTodayFlashsaleAsset());
                                mTodayAsset.setText(data.getTodayReleaseAsset());
                                mTotalAsset.setText(data.getAllFlashsaleAsset());
                            }
                        }
                    }
                });
    }
}
