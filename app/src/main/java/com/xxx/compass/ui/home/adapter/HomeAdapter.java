package com.xxx.compass.ui.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.HomeBean;
import com.xxx.compass.model.utils.GlideUtil;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {

    public HomeAdapter(@Nullable List<HomeBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean item) {
        helper.setText(R.id.item_home_name, item.getCoinQuotesName())
                .setText(R.id.item_home_simple, item.getCoinChineseName())
                .setText(R.id.item_home_range, item.getCoinFluctuation())
                .setText(R.id.item_home_usa, item.getCoinPriceUsdt())
                .setText(R.id.item_home_cny, item.getCoinPriceRmb());

        GlideUtil.loadBase(mContext, item.getCoinUrl(), (ImageView) helper.getView(R.id.item_home_icon));

        if (helper.getAdapterPosition() == 0) {
            helper.setBackgroundRes(R.id.item_home_back, R.drawable.shape_home_item_selection);
            helper.setBackgroundRes(R.id.item_home_range, R.color.colorTransparent);
        } else {
            if ("+".equals(String.valueOf(item.getCoinFluctuation().charAt(0)))) {
                helper.setBackgroundRes(R.id.item_home_range, R.drawable.shape_home_item_rise);
            } else {
                helper.setBackgroundRes(R.id.item_home_range, R.drawable.shape_home_item_fall);
            }
            helper.setBackgroundRes(R.id.item_home_back, R.drawable.shape_home_item_default);
        }
    }
}
