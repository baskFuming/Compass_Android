package com.xxx.compass.ui.wallet.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.DepositRecordBean;
import com.xxx.compass.model.utils.StringUtil;

import java.util.List;

public class DepositRecordAdapter extends BaseQuickAdapter<DepositRecordBean, BaseViewHolder> {

    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DepositRecordAdapter(@Nullable List<DepositRecordBean> data) {
        super(R.layout.item_deposit_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepositRecordBean item) {
        helper.setText(R.id.item_deposit_record_name, item.getUnit())
                .setText(R.id.item_deposit_record_time, item.getTime())
                .setText(R.id.item_deposit_record_amount, item.getAmount(tag));
    }
}
