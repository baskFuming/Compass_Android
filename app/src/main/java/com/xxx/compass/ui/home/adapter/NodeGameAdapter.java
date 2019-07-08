package com.xxx.compass.ui.home.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.NodeGameBean;
import com.xxx.compass.model.utils.StringUtil;

import java.util.List;


public class NodeGameAdapter extends BaseQuickAdapter<NodeGameBean.Top10UserInfoBean, BaseViewHolder> {

    public NodeGameAdapter(@Nullable List<NodeGameBean.Top10UserInfoBean> data) {
        super(R.layout.item_node_game, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NodeGameBean.Top10UserInfoBean item) {
        helper.setText(R.id.item_node_game_id, String.valueOf(helper.getAdapterPosition() + 1))
                .setText(R.id.item_node_game_account, item.getMemberAccount())
                .setText(R.id.item_node_game_number, item.getAsset());
    }
}