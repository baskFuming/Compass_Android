package com.xxx.compass.ui.my.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.ReleaseRecordBean;

import java.util.List;

public class ReleaseRecordAdapter extends BaseQuickAdapter<ReleaseRecordBean, BaseViewHolder> {

    public ReleaseRecordAdapter(@Nullable List<ReleaseRecordBean> data) {
        super(R.layout.item_release_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReleaseRecordBean item) {
        helper.setText(R.id.item_release_record_time, item.getCreateTime())
                .setText(R.id.item_release_record_amount, item.getAmount());
    }
}
