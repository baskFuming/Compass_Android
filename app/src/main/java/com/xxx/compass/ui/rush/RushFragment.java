package com.xxx.compass.ui.rush;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.fragment.BaseFragment;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.FlashSaleBean;
import com.xxx.compass.model.http.bean.RushInfoBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.MyCountDownTimer;
import com.xxx.compass.model.utils.ToastUtil;
import com.xxx.compass.ui.rush.activity.RushRecordActivity;
import com.xxx.compass.ui.rush.window.RushWindow;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 抢购布局
 * @Author xxx
 */
public class RushFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RushWindow.Callback {

    @BindView(R.id.main_rush_title)
    TextView mTitle;
    @BindView(R.id.main_rush_time_h)
    TextView mHText;
    @BindView(R.id.main_rush_time_m)
    TextView mMText;
    @BindView(R.id.main_rush_time_s)
    TextView mSText;
    @BindView(R.id.main_rush_button)
    ImageView mBtn;
    @BindView(R.id.main_rush_total_amount)
    TextView mTotalAmount;
    @BindView(R.id.main_rush_progress)
    ProgressBar mProgress;
    @BindView(R.id.main_rush_now_amount)
    TextView mNowAmount;
    @BindView(R.id.main_rush_now_amount_rule)
    TextView mNowAmountRule;
    @BindView(R.id.main_rush_refresh)
    SwipeRefreshLayout mRefresh;

    private String coinId;   //币种名称
    private LoadingDialog mLoadingDialog;
    private MyCountDownTimer mCount;

    private boolean isOpenRush; //是否开启抢购
    private String rushWindowString;    //抢购弹窗文字
    private RushWindow mRushWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rush;
    }

    @Override
    protected void initData() {
        mRefresh.setOnRefreshListener(this);

        getRushInfo(false);
    }

    @OnClick({R.id.main_rush_button, R.id.main_rush_record})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.main_rush_button:
//                if (rushWindowString == null) {
//                    getRushInfo(false);
//                    return;
//                }
                if (isOpenRush) {
                    mRushWindow = RushWindow.getInstance(getActivity());
                    mRushWindow.setCallback(this);
                    mRushWindow.setContent(rushWindowString);
                    mRushWindow.show();
                } else {
                    ToastUtil.showToast(R.string.main_rush_error_1);
                }
                break;
            case R.id.main_rush_record:
                startActivity(new Intent(getContext(), RushRecordActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        getRushInfo(false);
    }

    /**
     * @Model 启动定时器
     */
    @SuppressLint("SetTextI18n")
    private void startCountDownTimer(final long time) {
        if (mCount != null) {
            mCount.cancel();
        }

        mCount = new MyCountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setUI(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog.getInstance(getContext());
                    mLoadingDialog.show();
                }
                getRushInfo(true);
            }

            private void setUI(long total) {
                //第一步计算小时
                long h = total / 3600;
                if (mHText != null) {
                    mHText.setText(h >= 10 ? "" + h : "0" + h);
                }

                //第二步计算分钟
                long m = total % 3600 / 60;
                if (mMText != null) {
                    mMText.setText(m >= 10 ? "" + m : "0" + m);
                }

                //第三步计算秒
                long s = total % 3600 % 60;
                if (mSText != null) {
                    mSText.setText(s >= 10 ? "" + s : "0" + s);
                }
            }
        };
        mCount.start();
    }

    @Override
    public void callback() {
        doFlashSale();
    }

    /**
     * @Model 获取抢购信息
     */
    private void getRushInfo(final boolean isRefresh) {
        Api.getInstance().getRushInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<RushInfoBean>(getActivity()) {

                    @SuppressLint({"SetTextI18n", "StringFormatMatches"})
                    @Override
                    public void onSuccess(BaseBean<RushInfoBean> bean) {
                        if (bean != null) {
                            RushInfoBean data = bean.getData();
                            if (data != null) {
                                coinId = data.getCoinSymbol();

                                if (true) {
                                    rushWindowString = String.format(getString(R.string.window_rush_content), 1100.00, 1100.00);
                                } else {
                                    rushWindowString = String.format(getString(R.string.window_rush_content1), 1001, 1001, 9.235, 1001);
                                }

                                double totalAmount = data.getAmount();
                                double nowAmount = data.getFlashsale_mount();
                                mProgress.setMax((int) totalAmount);
                                mProgress.setProgress((int) nowAmount);
                                mTotalAmount.setText(getString(R.string.main_rush_total_amount) + totalAmount + " " + data.getCoinSymbol());
                                mNowAmount.setText(getString(R.string.main_rush_now_amount) + nowAmount + " " + data.getCoinSymbol());
                                mNowAmountRule.setText(data.getRatio());

                                long nowTime = data.getCurrent_time();
                                long startTime = data.getStarttime() - nowTime;
                                long endTime = data.getEndtime() - nowTime;
                                long nextTime = data.getNextStarttime() - nowTime;

                                if (startTime > 0) {    //如果开抢时间没过 就开抢倒计时
                                    isOpenRush = false;
                                    mBtn.setImageResource(R.mipmap.rush_open);
                                    mTitle.setText(R.string.main_rush_open_down);
                                    startCountDownTimer(startTime);
                                } else {    //如果开抢过了 就说明抢购开始倒计时
                                    if (endTime > 0) {  //如果抢购时间没过 就说明抢购开始倒计时
                                        isOpenRush = true;
                                        mBtn.setImageResource(R.mipmap.rush_finish);
                                        mTitle.setText(R.string.main_rush_close_down);
                                        startCountDownTimer(endTime);
                                    } else {    //如果抢购时间过了 就说明抢购已经结束了 开始下一次
                                        isOpenRush = false;
                                        mBtn.setImageResource(R.mipmap.rush_open);
                                        mTitle.setText(R.string.main_rush_open_down);
                                        if (nextTime > 0) { //如果第二天抢购开始时间过了 就停止倒计时
                                            startCountDownTimer(nextTime);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (!isRefresh) {
                            if (mRefresh != null) {
                                mRefresh.setRefreshing(true);
                            }
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (isRefresh) {
                            if (mLoadingDialog != null) {
                                mLoadingDialog.dismiss();
                                mLoadingDialog = null;
                            }
                        } else {
                            if (mRefresh != null) {
                                mRefresh.setRefreshing(false);
                            }
                        }
                    }
                });
    }


    /**
     * @Model 抢购
     */
    private void doFlashSale() {
        String userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().flashSale(userId, coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<FlashSaleBean>(getActivity()) {

                    @Override
                    public void onSuccess(BaseBean<FlashSaleBean> bean) {
                        if (bean != null) {
                            FlashSaleBean data = bean.getData();
                            if (data != null) {
                                double result = data.getResult();
                                ToastUtil.showToast(getString(R.string.main_rush_success) + result);
                                mBtn.setImageResource(R.mipmap.rush_finish);
                            }
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        ToastUtil.showToast(errorMessage);
                    }


                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(getContext());
                            mLoadingDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    }
                });
    }


}
