package com.xxx.compass.ui.my.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.http.bean.AchievementRecordBean;
import com.xxx.compass.ui.my.adapter.AchievementRecordItemAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @Page 分享收益详情页
 * @Author xxx
 */
public class AchievementRecordItemActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.achievement_record_item_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.achievement_record_item_refresh)
    SwipeRefreshLayout mRefresh;

    @Override
    protected String initTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_achievement_record_item;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        AchievementRecordBean.CommunityInfoListBean data = (AchievementRecordBean.CommunityInfoListBean) intent.getSerializableExtra("data");
        List<AchievementRecordBean.CommunityInfoListBean.TeamListBean> list = data.getTeamList();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(new AchievementRecordItemAdapter(list, this));
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mRefresh.setRefreshing(false);
    }
}
