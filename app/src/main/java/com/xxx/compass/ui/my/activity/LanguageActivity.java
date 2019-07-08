package com.xxx.compass.ui.my.activity;

import android.view.View;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.LocalManageUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseTitleActivity {

    @BindView(R.id.language_simple_zh_check)
    TextView mSimpleZh;
    @BindView(R.id.language_en_check)
    TextView mEn;

    @Override
    protected String initTitle() {
        return getString(R.string.language_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initData() {
        String nowLanguage = SharedPreferencesUtil.getInstance().getString(SharedConst.CONSTANT_LAUNCHER);
        switch (nowLanguage) {
            case LocalManageUtil.LANGUAGE_CN:
                mSimpleZh.setText("√");
                mEn.setText("");
                break;
            case LocalManageUtil.LANGUAGE_US:
                mSimpleZh.setText("");
                mEn.setText("√");
                break;
        }
    }

    @OnClick({R.id.language_simple_zh, R.id.language_en})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.language_simple_zh:
                mSimpleZh.setText("√");
                mEn.setText("");
                SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_CN);
                EventBus.getDefault().post(ConfigClass.EVENT_LANGUAGE_TAG);
                break;
            case R.id.language_en:
                mSimpleZh.setText("");
                mEn.setText("√");
                SharedPreferencesUtil.getInstance().saveString(SharedConst.CONSTANT_LAUNCHER, LocalManageUtil.LANGUAGE_US);
                EventBus.getDefault().post(ConfigClass.EVENT_LANGUAGE_TAG);
                break;
        }
    }
}
