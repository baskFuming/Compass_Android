package com.xxx.compass.ui.app.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.GameBean;
import com.xxx.compass.model.http.bean.RushRecordBean;
import com.xxx.compass.model.utils.GlideUtil;

import java.util.List;

public class AppGameAdapter extends BaseQuickAdapter<GameBean, BaseViewHolder> {

    public AppGameAdapter(@Nullable List<GameBean> data) {
        super(R.layout.item_app_game, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean item) {
        helper.setText(R.id.item_app_game_name, item.getName());

        GlideUtil.loadBaseCircle(mContext, item.getImage(), (ImageView) helper.getView(R.id.item_app_game_icon));
    }
}
