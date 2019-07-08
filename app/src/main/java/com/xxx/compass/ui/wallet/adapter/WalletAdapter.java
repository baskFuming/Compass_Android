package com.xxx.compass.ui.wallet.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.compass.R;
import com.xxx.compass.model.http.bean.WalletBean;
import com.xxx.compass.model.utils.GlideUtil;

import java.util.List;

public class WalletAdapter extends BaseQuickAdapter<WalletBean.TmpWalletExtBean, BaseViewHolder> {

    public WalletAdapter(@Nullable List<WalletBean.TmpWalletExtBean> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean.TmpWalletExtBean item) {
        helper.setText(R.id.item_wallet_available, item.getBalanceStr())
                .setText(R.id.item_wallet_frozen, item.getFrozenBalanceStr())
                .setText(R.id.item_wallet_name, item.getCoinId())
                .addOnClickListener(R.id.item_wallet_recharge)
                .addOnClickListener(R.id.item_wallet_withdrawal)
                .addOnClickListener(R.id.item_wallet_exchange)
                .addOnClickListener(R.id.item_wallet_deposit);

        GlideUtil.loadBase(mContext, item.getCoinLogo(), (ImageView) helper.getView(R.id.item_wallet_icon));

    }
}
