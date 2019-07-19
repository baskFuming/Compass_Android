package com.xxx.compass.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.GameBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.app.activity.GameActivity;
import com.xxx.compass.ui.app.adapter.AppGameAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 应用布局
 * @Author xxx
 */
public class AppFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.main_app_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.main_app_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.main_app_not_game)
    LinearLayout mNotGame;

    private List<GameBean> mList = new ArrayList<>();
    private AppGameAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app;
    }

    @Override
    protected void initData() {
        mAdapter = new AppGameAdapter(mList);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        loadGameList();
    }

    @OnClick({R.id.main_app_talk_btc, R.id.main_app_zbg, R.id.main_app_my_token, R.id.main_app_coin_all, R.id.main_app_news_btc, R.id.main_app_eight_btc, R.id.main_app_eth_browser, R.id.main_app_btc_browser, R.id.main_app_to_loan, R.id.main_app_mall})
    public void OnClick(View view) {
        Uri uri = null;
        switch (view.getId()) {
            case R.id.main_app_talk_btc:
                uri = Uri.parse(ConfigClass.APP_TALK_BTC);
                break;
            case R.id.main_app_zbg:
                uri = Uri.parse(ConfigClass.APP_ZBG);
                break;
            case R.id.main_app_my_token:
                uri = Uri.parse(ConfigClass.APP_MY_TOKEN);
                break;
            case R.id.main_app_coin_all:
                uri = Uri.parse(ConfigClass.APP_COIN_ALL);
                break;
            case R.id.main_app_news_btc:
                uri = Uri.parse(ConfigClass.APP_NEWS_BTC);
                break;
            case R.id.main_app_eight_btc:
                uri = Uri.parse(ConfigClass.APP_EIGHT_BTC);
                break;
            case R.id.main_app_eth_browser:
                uri = Uri.parse(ConfigClass.APP_ETH_BROWSER);
                break;
            case R.id.main_app_btc_browser:
                uri = Uri.parse(ConfigClass.APP_BTC_BROWSER);
                break;
            case R.id.main_app_to_loan:
                Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_app_mall:
                Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                break;
        }
        if (uri != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    @Override
    public void onRefresh() {
        loadGameList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        intent.putExtra("data", mList.get(position));
        startActivity(intent);
    }

    /**
     * @Model 获取游戏列表
     */
    private void loadGameList() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getGameList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<GameBean>>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<List<GameBean>> bean) {
                        if (bean == null) {
                            mNotGame.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mRefresh.setRefreshing(false);
                            return;
                        }
                        List<GameBean> list = bean.getData();
                        if (list == null || list.size() == 0) {
                            mNotGame.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                            mRefresh.setRefreshing(false);
                            return;
                        }

                        //移除未开启的游戏
                        for (int i = 0; i < list.size(); i++) {
                            GameBean gameBean = list.get(i);
                            if (!gameBean.isOpen()) {
                                list.remove(i);
                                i--;
                            }
                        }

                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mNotGame.setVisibility(View.GONE);
                        mRecycler.setVisibility(View.VISIBLE);
                        mRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        mNotGame.setVisibility(View.VISIBLE);
                        mRecycler.setVisibility(View.GONE);
                        mRefresh.setRefreshing(false);
                    }
                });
    }

}
