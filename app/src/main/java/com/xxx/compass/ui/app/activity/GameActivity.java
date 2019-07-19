package com.xxx.compass.ui.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.model.http.bean.GameBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.ui.wallet.window.PasswordWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Page 游戏页面
 */
public class GameActivity extends BaseActivity {

    @BindView(R.id.game_web)
    WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        GameBean bean = (GameBean) intent.getSerializableExtra("data");
        String url = bean.getUrl();

        final WebSettings webSetting;
        webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);  //支持js
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);  //支持弹窗
        webSetting.setBlockNetworkImage(false);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                webSetting.setTextZoom(getWindow().getDecorView().getWidth() / 375 * 100);
            }
        });
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String funName = "";
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    view.loadUrl(funName);
                } else {
                    view.evaluateJavascript(funName, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                        }
                    });
                }
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
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });
    }
}
