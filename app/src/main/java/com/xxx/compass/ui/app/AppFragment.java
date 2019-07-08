package com.xxx.compass.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * @Page 应用布局
 * @Author xxx
 */
public class AppFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.main_app_talk_btc, R.id.main_app_zbg,R.id.main_app_my_token, R.id.main_app_coin_all,R.id.main_app_news_btc, R.id.main_app_eight_btc, R.id.main_app_eth_browser, R.id.main_app_btc_browser, R.id.main_app_to_loan, R.id.main_app_mall})
    public void OnClick(View view) {
        Uri uri = null;
        switch (view.getId()) {
            case R.id.main_app_talk_btc:
                uri = Uri.parse(ConfigClass.APP_TALK_BTC);
                break;
            case R.id.main_app_zbg:
                uri = Uri.parse(ConfigClass.APP_ZBG);
                break;
            case R.id.main_app_my_token:
                uri = Uri.parse(ConfigClass.APP_MY_TOKEN);
                break;
            case R.id.main_app_coin_all:
                uri = Uri.parse(ConfigClass.APP_COIN_ALL);
                break;
            case R.id.main_app_news_btc:
                uri = Uri.parse(ConfigClass.APP_NEWS_BTC);
                break;
            case R.id.main_app_eight_btc:
                uri = Uri.parse(ConfigClass.APP_EIGHT_BTC);
                break;
            case R.id.main_app_eth_browser:
                uri = Uri.parse(ConfigClass.APP_ETH_BROWSER);
                break;
            case R.id.main_app_btc_browser:
                uri = Uri.parse(ConfigClass.APP_BTC_BROWSER);
                break;
            case R.id.main_app_to_loan:
                Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_app_mall:
                Toast.makeText(getContext(), getString(R.string.main_app_not_1), Toast.LENGTH_SHORT).show();
                break;
        }
        if (uri != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}
