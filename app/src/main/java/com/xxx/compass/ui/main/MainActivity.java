package com.xxx.compass.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.base.fragment.FragmentManager;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.AppVersionBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.IsSettingPayPswBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ExitAppUtil;
import com.xxx.compass.model.utils.PermissionUtil;
import com.xxx.compass.model.utils.SystemUtil;
import com.xxx.compass.ui.app.AppFragment;
import com.xxx.compass.ui.home.HomeFragment;
import com.xxx.compass.ui.my.MyFragment;
import com.xxx.compass.ui.rush.RushFragment;
import com.xxx.compass.ui.wallet.WalletFragment;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 主页
 * @Author xxx
 */
public class MainActivity extends BaseActivity {

    //页面下标
    private static final int HOME_TYPE = R.id.main_home;     //首页
    private static final int WALLET_TYPE = R.id.main_wallet; //钱包
    private static final int RUSH_TYPE = R.id.main_rush_btn;     //抢购
    private static final int APP_TYPE = R.id.main_app;       //应用
    private static final int MY_TYPE = R.id.main_my;         //我的

    @BindView(R.id.main_home_image)
    ImageView mHomeImage;
    @BindView(R.id.main_wallet_image)
    ImageView mWalletImage;
    @BindView(R.id.main_app_image)
    ImageView mAppImage;
    @BindView(R.id.main_my_image)
    ImageView mMyImage;

    @BindView(R.id.main_home_text)
    TextView mHomeText;
    @BindView(R.id.main_wallet_text)
    TextView mWalletText;
    @BindView(R.id.main_app_text)
    TextView mAppText;
    @BindView(R.id.main_my_text)
    TextView mMyText;

    @BindView(R.id.main_rush_btn)
    ImageView mRushBtn;

    private int nowType = HOME_TYPE;   //当前选中下标
    private int lastType = HOME_TYPE;   //上一个下标

    private ExitAppUtil exitAppUtil;    //双击退出

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        //初始化读写权限
        PermissionUtil.checkPermission(this, PermissionUtil.READ_PERMISSION, PermissionUtil.WRITE_PERMISSION);

        //初始化双击退出
        exitAppUtil = ExitAppUtil.getInstance();

        //首先检查是否还在登陆状态
        checkIsSettingPayPassword();

        //加载首页数据
        selectorItem();

    }

    @OnClick({R.id.main_home, R.id.main_wallet, R.id.main_rush_btn, R.id.main_app, R.id.main_my})
    public void onClick(View v) {
        nowType = v.getId();
        if (nowType != lastType) {
            defaultItem();
            selectorItem();
            lastType = nowType;
        }
    }

    @Override
    public void onBackPressed() {
        exitAppUtil.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConfigClass.LOGIN_RESULT_CODE) {
            checkAppVersion();
            checkIsSettingPayPassword();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nowType = savedInstanceState.getInt("type", HOME_TYPE);

        //切换数据
        selectorItem();
        defaultItem();
        lastType = nowType;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", nowType);
    }

    //选中的
    public void selectorItem() {
        switch (nowType) {
            case HOME_TYPE:
                mHomeImage.setImageResource(R.mipmap.main_home_selection);
                mHomeText.setTextColor(getResources().getColor(R.color.colorMainBottomSelector));
                FragmentManager.replaceFragment(this, HomeFragment.class, R.id.main_frame);
                break;
            case WALLET_TYPE:
                mWalletImage.setImageResource(R.mipmap.main_wallet_selection);
                mWalletText.setTextColor(getResources().getColor(R.color.colorMainBottomSelector));
                FragmentManager.replaceFragment(this, WalletFragment.class, R.id.main_frame);
                break;
            case RUSH_TYPE:
                FragmentManager.replaceFragment(this, RushFragment.class, R.id.main_frame);
                break;
            case APP_TYPE:
                mAppImage.setImageResource(R.mipmap.main_app_selection);
                mAppText.setTextColor(getResources().getColor(R.color.colorMainBottomSelector));
                FragmentManager.replaceFragment(this, AppFragment.class, R.id.main_frame);
                break;
            case MY_TYPE:
                mMyImage.setImageResource(R.mipmap.main_my_selection);
                mMyText.setTextColor(getResources().getColor(R.color.colorMainBottomSelector));
                FragmentManager.replaceFragment(this, MyFragment.class, R.id.main_frame);
                break;
        }
    }

    //撤销上次选中的
    public void defaultItem() {
        switch (lastType) {
            case HOME_TYPE:
                mHomeImage.setImageResource(R.mipmap.main_home_default);
                mHomeText.setTextColor(getResources().getColor(R.color.colorMainBottomDefault));
                break;
            case WALLET_TYPE:
                mWalletImage.setImageResource(R.mipmap.main_wallet_default);
                mWalletText.setTextColor(getResources().getColor(R.color.colorMainBottomDefault));
                break;
            case RUSH_TYPE:
                break;
            case APP_TYPE:
                mAppImage.setImageResource(R.mipmap.main_app_default);
                mAppText.setTextColor(getResources().getColor(R.color.colorMainBottomDefault));
                break;
            case MY_TYPE:
                mMyImage.setImageResource(R.mipmap.main_my_default);
                mMyText.setTextColor(getResources().getColor(R.color.colorMainBottomDefault));
                break;
        }
    }

    /**
     * @Model 检查App版本
     */
    private void checkAppVersion() {
        Api.getInstance().checkAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<AppVersionBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<AppVersionBean> bean) {
                        if (bean != null) {
                            AppVersionBean data = bean.getData();
                            if (data != null) {
                                String version = data.getVersion();
                                if (!SystemUtil.getVersionName(MainActivity.this).equals(version)) {
                                    UpdateWindow.getInstance(MainActivity.this, data.getDownloadUrl());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * @Model 检查是否设置支付密码
     */
    private void checkIsSettingPayPassword() {
        Api.getInstance().checkIsSettingPayPassword()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<IsSettingPayPswBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<IsSettingPayPswBean> bean) {
                        if (bean != null) {
                            IsSettingPayPswBean data = bean.getData();
                            if (data != null) {
                                SharedPreferencesUtil.getInstance().saveBoolean(SharedConst.IS_SETTING_PAY_PSW, data.getFundsVerified());
                            }
                        }

                        //检查版本号
                        checkAppVersion();
                    }
                });
    }
}