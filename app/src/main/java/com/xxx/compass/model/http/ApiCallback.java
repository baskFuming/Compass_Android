package com.xxx.compass.model.http;

import android.app.Activity;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.base.ErrorBean;
import com.xxx.compass.model.http.utils.ApiCode;
import com.xxx.compass.model.http.utils.ApiError;
import com.xxx.compass.model.log.LogConst;
import com.xxx.compass.model.log.LogUtil;
import com.xxx.compass.model.utils.SystemUtil;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class ApiCallback<T> implements Observer<BaseBean<T>> {

    //弱引用对象
    private WeakReference<BaseActivity> activity;  //绑定上下文
    private CompositeDisposable compositeDisposable = new CompositeDisposable();    //防止内存泄漏

    //必须使用封装的上下文Activity 否则发起网络请求会报错
    public ApiCallback(Activity activity) {
        if (activity instanceof BaseActivity) {
            BaseActivity activity1 = (BaseActivity) activity;
            this.activity = new WeakReference<>(activity1);
        } else {
            if (activity != null) {
                onError(ApiCode.SERVICE_ERROR_CODE, activity.getResources().getText(R.string.error_http_not_content).toString());
            }
            onComplete();
        }
    }

    //必须重写 请求成功的方法
    public abstract void onSuccess(BaseBean<T> bean);

    //等待重写 请求失败的方法
    public void onError(int errorCode, String errorMessage) {
        LogUtil.showLog(LogConst.HTTP_TAG, null);
    }

    //等待重写 请求开始的方发
    public void onStart(Disposable d) {
        if (activity != null) {
            BaseActivity activity = this.activity.get();
            if (activity != null && activity.isShowData()) {
                if (SystemUtil.isNetworkAvailable(activity)) { //如果没有网络 就停止请求 并且弹出异常
                    onError(ApiCode.SERVICE_ERROR_CODE, activity.getResources().getText(R.string.error_http_net_work_exception).toString());
                    onComplete();
                }
            }
        }

        //开始请求
        compositeDisposable.add(d);
    }

    //等待重写 请求结束的方发
    public void onEnd() {
        //请求结束
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (activity != null) {
            BaseActivity activity = this.activity.get();
            if (activity != null && activity.isShowData()) {
                onStart(d);
            }
        }
    }

    @Override
    public void onNext(BaseBean<T> bean) {
        if (activity != null) {
            BaseActivity activity = this.activity.get();
            if (activity != null && activity.isShowData()) {
                if (bean != null) {
                    int code = bean.getCode();
                    //使用code值做判断
                    if (code == ApiCode.RESPONSE_CODE) {
                        onSuccess(bean);
                    } else {
                        //根据后台返回的code值做特殊异常处理
                        ApiError.ServiceCodeErrorFun(code);
                        //回调错误信息
                        String errorMessage = bean.getMessage() == null ? activity.getResources().getText(R.string.error_http_service_exception).toString() : bean.getMessage();
                        onError(code, errorMessage);
                    }
                } else {
                    //解析失败
                    String errorMessage = activity.getResources().getText(R.string.error_http_json_exception).toString();
                    onError(ApiCode.SERVICE_ERROR_CODE, errorMessage);
                }
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (activity != null) {
            BaseActivity activity = this.activity.get();
            if (activity != null && activity.isShowData()) {
                String errorMessage;
                int code = ApiCode.SERVICE_ERROR_CODE;
                if (e instanceof com.jakewharton.retrofit2.adapter.rxjava2.HttpException) {
                    ResponseBody errorBody = ((HttpException) e).response().errorBody();
                    if (errorBody != null) {
                        try {
                            String string = errorBody.string();
                            ErrorBean bean = new Gson().fromJson(string, ErrorBean.class);
                            code = bean.getCode();
                            ApiError.ServiceCodeErrorFun(code);
                            errorMessage = bean.getMessage();
                        } catch (Exception e1) {
                            errorMessage = activity.getResources().getText(R.string.error_http_json_exception).toString();
                        }
                    } else {
                        errorMessage = activity.getResources().getText(R.string.error_http_json_exception).toString();
                    }
                } else if (e instanceof UnknownHostException) {    //首先判断地址是否正确
                    errorMessage = activity.getResources().getText(R.string.error_http_url_exception).toString();
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {  //再判断是否是链接超时
                    errorMessage = activity.getResources().getText(R.string.error_http_time_out_exception).toString();
                } else {
                    errorMessage = e == null ? activity.getResources().getText(R.string.error_http_not).toString() : e.getMessage();
                }
                onError(code, errorMessage);
                onComplete();
            }
        }
    }

    @Override
    public void onComplete() {
        onEnd();
    }
}