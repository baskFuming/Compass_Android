package com.xxx.compass.ui.my;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.MyLevelBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.LinearGradientUtil;
import com.xxx.compass.ui.login.LoginActivity;
import com.xxx.compass.ui.my.activity.AccountSettingActivity;
import com.xxx.compass.ui.my.activity.CallMeActivity;
import com.xxx.compass.ui.my.activity.FeedBackActivity;
import com.xxx.compass.ui.my.activity.InviteFriendActivity;
import com.xxx.compass.ui.my.activity.NoticeCenterActivity;
import com.xxx.compass.ui.my.activity.ProfitRecordActivity;

import butterknife.BindView;
import butterknife.OnClick;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 我的布局
 * @Author xxx
 */
public class MyFragment extends BaseFragment {

    @BindView(R.id.main_my_icon)
    ImageView mIcon;
    @BindView(R.id.main_my_name)
    TextView mName;
    @BindView(R.id.main_my_invited_text)
    TextView mInvitedText;
    @BindView(R.id.main_my_level_1)
    ImageView mMyLevel1;
    @BindView(R.id.main_my_level_2)
    ImageView mMyLevel2;
    @BindView(R.id.main_my_level_3)
    ImageView mMyLevel3;
    @BindView(R.id.main_my_level_4)
    ImageView mMyLevel4;
    @BindView(R.id.main_my_level_5)
    ImageView mMyLevel5;

    @BindView(R.id.main_my_invited_icon_text_1)
    TextView mToast1;

    private boolean isLogin;
    private String inviteCode;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
    }

    @OnClick({R.id.main_my_name, R.id.main_my_call_my, R.id.main_my_record, R.id.main_my_invited, R.id.main_my_invited_text, R.id.main_my_notice, R.id.main_my_feedback, R.id.main_my_setting})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.main_my_name:
                if (!isLogin) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.main_my_record:
                startActivity(new Intent(getContext(), ProfitRecordActivity.class));
                break;
            case R.id.main_my_invited:
                startActivity(new Intent(getContext(), InviteFriendActivity.class));
                break;
            case R.id.main_my_invited_text:
                KeyBoardUtil.copy(getContext(), inviteCode);
                break;
            case R.id.main_my_notice:
                startActivity(new Intent(getContext(), NoticeCenterActivity.class));
                break;
            case R.id.main_my_feedback:
                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;
            case R.id.main_my_call_my:
                startActivity(new Intent(getContext(), CallMeActivity.class));
                break;
            case R.id.main_my_setting:
                startActivity(new Intent(getContext(), AccountSettingActivity.class));
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        isLogin = SharedPreferencesUtil.getInstance().getBoolean(SharedConst.IS_LOGIN);
        if (isLogin) {
            mName.setText(SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_NAME));
            inviteCode = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_INVITE_CODE);
            mInvitedText.setText(getString(R.string.main_my_invited_text) + "：" + inviteCode);
            getUserInfo();
        }
    }

    /**
     * @Model 加载用户信息
     */
    private void getUserInfo() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getUserStar(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<MyLevelBean>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<MyLevelBean> bean) {
                        if (bean != null) {
                            MyLevelBean data = bean.getData();
                            if (data != null) {
                                int userStar = data.getUserStar();
                                if (userStar >= 1) {
                                    mMyLevel1.setImageResource(R.mipmap.level_true);
                                } else {
                                    mMyLevel1.setImageResource(R.mipmap.level_false);
                                }
                                if (userStar >= 2) {
                                    mMyLevel2.setImageResource(R.mipmap.level_true);
                                } else {
                                    mMyLevel2.setImageResource(R.mipmap.level_false);
                                }
                                if (userStar >= 3) {
                                    mMyLevel3.setImageResource(R.mipmap.level_true);
                                } else {
                                    mMyLevel3.setImageResource(R.mipmap.level_false);
                                }
                                if (userStar >= 4) {
                                    mMyLevel4.setImageResource(R.mipmap.level_true);
                                } else {
                                    mMyLevel4.setImageResource(R.mipmap.level_false);
                                }
                                if (userStar >= 5) {
                                    mMyLevel5.setImageResource(R.mipmap.level_true);
                                } else {
                                    mMyLevel5.setImageResource(R.mipmap.level_false);
                                }
                            }
                        }
                    }
                });
    }

}
