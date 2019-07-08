package com.xxx.compass.ui.home;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.HomeBannerBean;
import com.xxx.compass.model.http.bean.HomeBean;
import com.xxx.compass.model.http.bean.NoticeCenterBean;
import com.xxx.compass.model.http.utils.ApiType;
import com.xxx.compass.model.utils.BannerUtil;
import com.xxx.compass.ui.home.activity.NodeGameActivity;
import com.xxx.compass.ui.home.adapter.HomeAdapter;
import com.xxx.compass.ui.my.activity.NoticeCenterActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 首页布局
 * @Author xxx
 */
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.main_home_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_home_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_home_app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.main_not_data)
    LinearLayout mNotData;
    @BindView(R.id.main_home_banner)
    Banner mBanner;
    @BindView(R.id.main_home_view_flipper)
    ViewFlipper mViewFlipper;

    private HomeAdapter mAdapter;
    private List<HomeBean> mRecyclerList = new ArrayList<>();
    private List<NoticeCenterBean.ContentBean> mNoticeList = new ArrayList<>();

    private boolean isLoadBanner;   //是否加载完毕轮播图
    private boolean isLoadFlipper;   //是否加载文字公告

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        mAdapter = new HomeAdapter(mRecyclerList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mRefresh.setEnabled(true);
                } else {
                    mRefresh.setEnabled(false);
                }
            }
        });
        mBanner.stopAutoPlay();

        loadData();
        loadBanner();
        loadNotice();
    }

    @OnClick(R.id.home_node_game)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.home_node_game:
                startActivity(new Intent(getContext(), NodeGameActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
        if (!isLoadFlipper) {
            loadNotice();
        }
        if (!isLoadBanner) {
            loadBanner();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mBanner != null) {
            if (hidden) {
                mBanner.stopAutoPlay();
            } else {
                mBanner.startAutoPlay();
            }
        }
    }

    /**
     * @Model 获取首页列表
     */
    private void loadData() {
        Api.getInstance().getHomeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<HomeBean>>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<List<HomeBean>> bean) {
                        if (bean != null) {
                            List<HomeBean> list = bean.getData();
                            if (list == null || list.size() == 0) {
                                mNotData.setVisibility(View.VISIBLE);
                                mRecycler.setVisibility(View.GONE);
                                return;
                            }
                            mNotData.setVisibility(View.GONE);
                            mRecycler.setVisibility(View.VISIBLE);
                            mRecyclerList.clear();
                            mRecyclerList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mRefresh != null) {
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
     * @Model 获取轮播图
     */
    private void loadBanner() {
        Api.getInstance().getHomeBannerList(ApiType.HOME_LOCATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<HomeBannerBean>>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<List<HomeBannerBean>> bean) {
                        if (bean != null) {
                            List<HomeBannerBean> list = bean.getData();
                            if (list != null) {
                                isLoadBanner = true;
                                BannerUtil.init(mBanner, list, new OnBannerListener() {
                                    @Override
                                    public void OnBannerClick(int position) {

                                    }
                                });
                            } else {
                                isLoadBanner = false;
                            }
                        } else {
                            isLoadBanner = false;
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        isLoadBanner = false;
                    }
                });
    }

    /**
     * @Model 获取消息中心数据
     */
    private void loadNotice() {
        Api.getInstance().getNoticeCenterList(1, ConfigClass.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<NoticeCenterBean>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<NoticeCenterBean> bean) {
                        if (bean != null) {
                            NoticeCenterBean data = bean.getData();
                            if (data != null) {
                                isLoadFlipper = true;

                                List<NoticeCenterBean.ContentBean> list = data.getContent();
                                for (int i = 0; i < list.size(); i++) {
                                    NoticeCenterBean.ContentBean noticeCenterBean = list.get(i);
                                    if (noticeCenterBean != null) {
                                        addView(noticeCenterBean);
                                    }
                                }
                                if (list.size() == 1) {
                                    addView(list.get(0));
                                }
                                addOnClick();
                            } else {
                                isLoadFlipper = false;
                            }
                        } else {
                            isLoadFlipper = false;
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        isLoadFlipper = false;
                    }

                    private void addView(NoticeCenterBean.ContentBean noticeCenterBean) {
                        View inflate = View.inflate(getContext(), R.layout.weight_view_flipper, null);
                        TextView mContext = inflate.findViewById(R.id.main_home_notice_content);
                        TextView mTime = inflate.findViewById(R.id.main_home_notice_time);
                        mContext.setText(noticeCenterBean.getTitle());
                        mTime.setText(noticeCenterBean.getCreateTime());
                        mViewFlipper.addView(inflate);
                        mNoticeList.add(noticeCenterBean);
                    }

                    private void addOnClick() {
                        mViewFlipper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int child = mViewFlipper.getDisplayedChild();   //点击的条目下标
                                if (mNoticeList.size() != 0) {
                                    NoticeCenterBean.ContentBean bean = mNoticeList.get(child);
                                }

                                //目前版本统一跳转到列表
                                startActivity(new Intent(getContext(), NoticeCenterActivity.class));
                            }
                        });
                    }
                });
    }
}
