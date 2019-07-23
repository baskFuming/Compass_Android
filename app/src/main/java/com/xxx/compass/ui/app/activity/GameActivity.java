package com.xxx.compass.ui.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.GameBean;
import com.xxx.compass.model.http.bean.MemberAssetBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.base.BooleanBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 游戏页面
 */
public class GameActivity extends BaseActivity {

    @BindView(R.id.game_web)
    WebView mWebView;
    private String Url;
    private String userId;
    private String restart = null;
    private String score = null;
    private int gameid;
    private String funName;
    private String gameName;
    @BindView(R.id.main_title)
    TextView title;
    private String amount;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData() {
        userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Intent intent = getIntent();
        GameBean bean = (GameBean) intent.getSerializableExtra("data");
        gameid = bean.getId();
        gameName = bean.getName();
        title.setText(gameName);
//        Url = bean.getUrl();
        Url = bean.getUrl() + "?" + "userId=" + userId + "&gameId=" + bean.getId() + "&baseUrl=" + ConfigClass.BASE_URL;
        final WebSettings webSetting;
        webSetting = mWebView.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        //设置支持缩放
        webSetting.setUseWideViewPort(true); //设置加载进来的页面自适应手机屏幕（可缩放）
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);  //支持js
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);  //支持弹窗
        webSetting.setBlockNetworkImage(false);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(Url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  //接受所有证书
            }
        });
        //接收相应事件
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    getMemberAsset();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                JSONObject json = null;
                try {
                    json = new JSONObject(message);
                    restart = json.getString("restart");
                    score = json.getString("score");
                    if (score.equals("0")) {
                        //开始
                        if (restart.equals("0")) {
                            funName = "startOn";
                        } else {
                            funName = "again";
                        }
                        getStartGame();
                    } else if (score.equals("no")) {
                        //一次没打
                        score = "0";
                        getUpdateScore();
                    } else {
                        //结果
                        getUpdateScore();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }
        });
    }

    /**
     * @model 获取用用户CT
     */
    private void getMemberAsset() {
        userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getMemberAsset(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<MemberAssetBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<MemberAssetBean> bean) {
                        amount = String.valueOf(bean.getData().getAmount());
                        String funCount = "javascript:ctNumber('" + amount + "')";
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            mWebView.loadUrl(funCount);
                        } else {
                            mWebView.evaluateJavascript(funCount, new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    //此处为 js 返回的结果
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);

                    }
                });
    }

    /**
     * @model 开始游戏
     */
    private void getStartGame() {
        userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getStartGame(userId, gameid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<BooleanBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<BooleanBean> bean) {
                        if (bean.getData().isResult()) {
                            String funName;
                            if (restart.equals("1")) {
                                funName = "startOn";
                            } else {
                                funName = "again";
                            }
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                mWebView.loadUrl("javascript:" + funName + "(" + true + ")");
                            } else {
                                mWebView.evaluateJavascript("javascript:" + funName + "(" + true + ")", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        //此处为 js 返回的结果
                                    }
                                });
                            }
                        } else {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                mWebView.loadUrl("javascript:" + funName + "(" + false + ")");
                            } else {
                                mWebView.evaluateJavascript("javascript:" + funName + "(" + false + ")", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        //此处为 js 返回的结果
                                    }
                                });
                            }
                        }
                        getMemberAsset();
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                        if (errorCode == -1079) {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                mWebView.loadUrl("javascript:" + funName + "(" + false + ")");
                            } else {
                                mWebView.evaluateJavascript("javascript:" + funName + "(" + false + ")", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        //此处为 js 返回的结果
                                    }
                                });
                            }
                        }
                        ToastUtil.showToast(errorMessage);
                    }
                });
    }

    /**
     * 更新比赛得分
     */
    private void getUpdateScore() {
        userId = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_USER_ID);
        Api.getInstance().getUpdateScore(userId, gameid, score)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<BooleanBean>(this) {
                    @Override
                    public void onSuccess(BaseBean<BooleanBean> bean) {
                    }
                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        super.onError(errorCode, errorMessage);
                    }
                });
    }

    @Override
    protected void onResume() {
        if (mWebView != null) {
            mWebView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mWebView.reload();
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
        if (mWebView != null) {
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearCache(true);
            mWebView.stopLoading();
            mWebView.destroy();
        }
    }

    @OnClick({R.id.main_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_return:
                onBackPressed();
                break;
        }
    }
}
