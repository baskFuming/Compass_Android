package com.xxx.compass.ui.rush.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.RushRecordBean;

import java.util.List;

public class RushRecordAdapter extends BaseQuickAdapter<RushRecordBean, BaseViewHolder> {

    public RushRecordAdapter(@Nullable List<RushRecordBean> data) {
        super(R.layout.item_rush_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RushRecordBean item) {
        helper.setText(R.id.item_rush_record_name, item.getCoinId())
                .setText(R.id.item_rush_record_time, item.getFlashsaleTime())
                .setText(R.id.item_rush_record_amount, item.getFlashsaleMount());
    }
}
