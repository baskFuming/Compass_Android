package com.xxx.compass.ui.my.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.ConversionProfitBean;
import com.xxx.compass.model.http.bean.DepositProfitBean;

import java.util.List;

import retrofit2.Converter;

public class ConversionProfitAdapter extends BaseQuickAdapter<ConversionProfitBean, BaseViewHolder> {

    public ConversionProfitAdapter(@Nullable List<ConversionProfitBean> data) {
        super(R.layout.item_conversion_profit, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConversionProfitBean item) {
        helper.setText(R.id.item_conversion_profit_name, item.getUnit())
                .setText(R.id.item_conversion_profit_time, item.getTime())
                .setText(R.id.item_conversion_profit_amount, item.getAmount());
    }
}
