package com.xxx.compass.ui.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.NodeGameBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.home.adapter.NodeGameAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 节点竞猜页
 * @Author xxx
 */
public class NodeGameActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.note_game_number)
    TextView mNowNumber;
    @BindView(R.id.note_game_first_name)
    TextView mFirstPhone;
    @BindView(R.id.note_game_first_number)
    TextView mFirstNumber;
    @BindView(R.id.note_game_my_number)
    TextView mMyNumber;
    @BindView(R.id.note_game_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.node_game_refresh)
    SwipeRefreshLayout mRefresh;

    private List<NodeGameBean.Top10UserInfoBean> mList = new ArrayList<>();
    private NodeGameAdapter mAdapter;

    @Override
    protected String initTitle() {
        return getString(R.string.note_game_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_node_game;
    }

    @Override
    protected void initData() {
        mAdapter = new NodeGameAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this);

        loadData();
    }

    @OnClick({R.id.node_game_rule})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.node_game_rule:
                startActivity(new Intent(this, NodeGameRuleActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }


    /**
     * @Model 获取节点大赛名次
     */
    private void loadData() {
        Api.getInstance().getNodeGameList(SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<NodeGameBean>(this) {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(BaseBean<NodeGameBean> bean) {
                        if (bean != null) {
                            NodeGameBean data = bean.getData();
                            if (data != null) {
                                List<NodeGameBean.Top10UserInfoBean> list = data.getTop10UserInfo();
                                if (list != null && list.size() != 0) {
                                    mNowNumber.setText(data.getAllBonuseAsset());
                                    mMyNumber.setText(getString(R.string.note_game_my_number) + (data.getMyIndex() == -1 ? getString(R.string.note_game_my_not) : data.getMyIndex()));
                                    mFirstNumber.setText(getString(R.string.note_game_first_number) + data.getChampionBonuse());
                                    mFirstPhone.setText(getString(R.string.note_game_first_name) + data.getChampionAccount());

                                    mList.clear();
                                    mList.addAll(list);
                                    mAdapter.notifyDataSetChanged();
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

}
