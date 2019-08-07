package com.xxx.compass.ui.my.activity;

import android.content.Intent;
import android.view.View;

import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.ui.rush.activity.RushRecordActivity;

import butterknife.OnClick;

/**
 * @Page 收益记录页
 * @Author xxx
 */
public class ProfitRecordActivity extends BaseTitleActivity {

    @Override
    protected String initTitle() {
        return getString(R.string.profit_record_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profit_record;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.profit_record_coin, R.id.conversion_profit_rush, R.id.profit_record_achievement, R.id.profit_record_rush, R.id.profit_record_share, R.id.profit_record_release})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.profit_record_coin:
                startActivity(new Intent(this, DepositProfitActivity.class));
                break;
            case R.id.conversion_profit_rush:
                startActivity(new Intent(this, ConversionProfitActivity.class));
                break;
            case R.id.profit_record_rush:
                startActivity(new Intent(this, RushRecordActivity.class));
                break;
            case R.id.profit_record_achievement:
                startActivity(new Intent(this, AchievementRecordActivity.class));

                break;
            case R.id.profit_record_share:
                startActivity(new Intent(this, ShareRecordActivity.class));
                break;
            case R.id.profit_record_release:
                startActivity(new Intent(this, ReleaseRecordActivity.class));
                break;
        }
    }
}
