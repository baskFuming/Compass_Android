package com.xxx.compass.ui.login;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.CountyBean;

import java.util.List;

public class SelectCountyAdapter extends BaseQuickAdapter<CountyBean, BaseViewHolder> {

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    SelectCountyAdapter(@Nullable List<CountyBean> data) {
        super(R.layout.item_select_county, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CountyBean item) {
        helper.setText(R.id.item_select_county_name, item.getZhName())
                .setText(R.id.item_select_county_code, item.getAreaCode());

        if (helper.getAdapterPosition() == position) {
            helper.setTextColor(R.id.item_select_county_name, Color.parseColor("@color/colorMain"))
                    .setTextColor(R.id.item_select_county_code, Color.parseColor("@color/colorMain"));
        } else {
            helper.setTextColor(R.id.item_select_county_name, Color.parseColor("@color/colorText"))
                    .setTextColor(R.id.item_select_county_code, Color.parseColor("@color/colorText"));
        }
    }
}
