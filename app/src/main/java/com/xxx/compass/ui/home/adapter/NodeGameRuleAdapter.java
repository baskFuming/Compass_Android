package com.xxx.compass.ui.home.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.NodeGameRuleBean;
import com.xxx.compass.model.utils.StringUtil;

import java.util.List;

public class NodeGameRuleAdapter extends BaseQuickAdapter<NodeGameRuleBean, BaseViewHolder> {

    private Activity activity;

    public NodeGameRuleAdapter(@Nullable List<NodeGameRuleBean> data, Activity activity) {
        super(R.layout.item_node_game_rule, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, NodeGameRuleBean item) {
        helper.setText(R.id.item_node_game_rule_grade, item.getLevel(activity) + mContext.getString(R.string.node_game_rule_level_text))
                .setText(R.id.item_node_game_rule_percentage, item.getBonusesRatio())
                .setText(R.id.item_node_game_rule_ranking, item.getIndex());
    }
}
