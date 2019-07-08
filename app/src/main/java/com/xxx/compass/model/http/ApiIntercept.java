package com.xxx.compass.model.http;

import android.support.annotation.NonNull;

import com.xxx.compass.BuildConfig;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.model.log.LogConst;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.log.LogUtil;
import com.xxx.compass.model.utils.LocalManageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class ApiIntercept implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //请求头添加
        Request request = headerIntercept(chain);

        //转化为json格式
//        request = getJSONRequest(request);

        //转化baseUrl
//        request = conversionBaseUrl(request);

        //发起请求
        Response response = chain.proceed(request);

        //日志拦截器
        if (BuildConfig.DEBUG) {
            logIntercept(request, response);
        }

        return response;
    }

    /**
     * 请求头拦截器
     */
    private Request headerIntercept(Interceptor.Chain chain) {
        //拦截请求头添加
        SharedPreferencesUtil instance = SharedPreferencesUtil.getInstance();
        String token1 = instance.getDecryptionString(SharedConst.ENCRYPT_VALUE_TOKEN_1);
        String token2 = instance.getDecryptionString(SharedConst.ENCRYPT_VALUE_TOKEN_2);
        String language = instance.getString(SharedConst.CONSTANT_LAUNCHER);
        return chain.request()
                .newBuilder()
                .addHeader("Accept-Language", language == null || language.equals("") ? LocalManageUtil.LANGUAGE_CN : language)
                .addHeader("Cache-Control", "public,max-age=" + ConfigClass.CACHE_TIME)
                .addHeader("x-auth-token", token1)
                .addHeader("access-auth-token", token2)
                .build();
    }

    /**
     * 转化为json格式
     */
    private Request getJSONRequest(Request request) throws IOException {
        Buffer buffer = new Buffer();
        RequestBody oldBody = request.body();
        String content = "";

        //首先得判断是否是文件上传 如果是就取消所有操作
        if (oldBody instanceof MultipartBody) {
            return request;
        }

        //如果 oldBody不为空 就开始转化操作
        if (oldBody != null) {
            oldBody.writeTo(buffer);
            String params = buffer.readUtf8();
            if (!params.equals("")) {
                JSONObject jsonObject = new JSONObject();
                String[] split = params.split("&");
                for (String aSplit : split) {
                    String[] strings = aSplit.split("=");
                    try {
                        jsonObject.put(strings[0], strings[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                content = jsonObject.toString();
            } else {
                content = "";
            }
        }

        //重新构建出请求体发送
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
        return request.newBuilder()
                .url(request.url().toString())
                .method(request.method(), requestBody)
                .build();
    }

    /**
     * 转化BaseUrl
     * 在需要转化的接口上  加上一个标记
     */
    private Request conversionBaseUrl(Request request) {
        String baseUrl = request.header(ConfigClass.HTTP_CONVERSION);
        //首先验证是否需要转化url
        if (baseUrl != null && !baseUrl.isEmpty()) {
            Request.Builder newBuilder = request.newBuilder();
            newBuilder.removeHeader(ConfigClass.HTTP_CONVERSION);
            HttpUrl newBaseUrl = HttpUrl.parse(baseUrl);
            HttpUrl oldHttpUrl = request.url();
            //重建新的HttpUrl，修改需要修改的url部分
            if (newBaseUrl != null) {
                HttpUrl newHttpUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(newBaseUrl.scheme())
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())
                        .build();
                newBuilder.url(newHttpUrl);
            } else {
                newBuilder.url(oldHttpUrl);
            }
            return newBuilder.build();
        }
        return request;
    }


    /**
     * 日志拦截器
     */
    private void logIntercept(Request request, Response response) throws IOException {
        //获取到请求类型
        String method = request.method();

        //获取到请求Url
        String url = request.url().toString();

        //获取到请求头
        String header = request.headers().toString().replaceAll("\n", " | ");

        //获取到请求参数 转化为json格式
        String params;
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            if (requestBody instanceof MultipartBody) {  //如果是文件 就打印路径即可
                MultipartBody multipartBody = (MultipartBody) requestBody;
                List<MultipartBody.Part> parts = multipartBody.parts();
                StringBuilder sb = new StringBuilder("[");
                for (MultipartBody.Part part : parts) {
                    sb.append(part.headers()).append(",");
                }
                sb.append("]");
                params = sb.toString().replaceAll("\n", "");
            } else { //如果不是 就打印出所有的数据
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                params = buffer.readUtf8();
            }
        } else {
            params = "[]";
        }

        //获取到响应数据
        String body = response.peekBody(1024 * 1024).string().replaceAll("\n", "");

        LogUtil.showLog(LogConst.HTTP_TAG,
                method + "\n" +  //打印方法
                        "url：" + url + "\n" +   //打印url
                        "header：" + header + "\n" +  //打印所有请求头
                        "params：" + params + "\n" +  //打印请求参数
                        "body：" + body + "\n" +  //打印响应内容
                        " \n" + " \n" + " \n" //分割
        );
    }
}
