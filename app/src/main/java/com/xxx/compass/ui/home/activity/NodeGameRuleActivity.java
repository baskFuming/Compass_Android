package com.xxx.compass.ui.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.NodeGameRuleBean;
import com.xxx.compass.ui.home.adapter.NodeGameRuleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 节点竞猜规则页
 * @Author xxx
 */
public class NodeGameRuleActivity extends BaseTitleActivity {

    @BindView(R.id.node_game_rule_open)
    TextView mOpenBtn;
    @BindView(R.id.node_game_open_content_1)
    TextView mOpenContent1;
    @BindView(R.id.node_game_open_content_2)
    TextView mOpenContent2;

    @BindView(R.id.node_game_rule_close)
    TextView mCloseBtn;
    @BindView(R.id.node_game_close_content)
    TextView mCloseContent;

    @BindView(R.id.award_rules_recycler)
    RecyclerView mRecycler;

    private List<NodeGameRuleBean> mList = new ArrayList<>();
    private NodeGameRuleAdapter mAdapter;

    @Override
    protected String initTitle() {
        return getString(R.string.node_game_rule_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_node_game_rule;
    }


    @Override
    protected void initData() {
        mAdapter = new NodeGameRuleAdapter(mList, this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);

        loadData();
    }

    @OnClick({R.id.node_game_rule_open, R.id.node_game_rule_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.node_game_rule_open:
                mOpenBtn.setVisibility(View.GONE);
                mCloseBtn.setVisibility(View.VISIBLE);

                mCloseContent.setVisibility(View.GONE);
                mOpenContent1.setVisibility(View.VISIBLE);
                mOpenContent2.setVisibility(View.VISIBLE);
                break;
            case R.id.node_game_rule_close:
                mCloseBtn.setVisibility(View.GONE);
                mOpenBtn.setVisibility(View.VISIBLE);

                mCloseContent.setVisibility(View.VISIBLE);
                mOpenContent1.setVisibility(View.GONE);
                mOpenContent2.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * @Model 获取节点大赛奖励规则
     */
    private void loadData() {
        Api.getInstance().getNodeGameRuleList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<NodeGameRuleBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<NodeGameRuleBean>> bean) {
                        if (bean != null) {
                            List<NodeGameRuleBean> list = bean.getData();
                            if (list != null && list.size() != 0) {
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
