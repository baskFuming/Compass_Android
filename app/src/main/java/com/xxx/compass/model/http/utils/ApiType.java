package com.xxx.compass.model.http.utils;

/**
 * 网络请求 返回值type
 */
public class ApiType {

    public static final String HOME_LOCATION = "0";   //轮播图位置

    //钱包列表是否开启兑换
    public static final int CLOSE_EXCHANGE_TYPE = 0;
    public static final int OPEN_EXCHANGE_TYPE = 1;

    //钱包列表是否开启理财
    public static final int CLOSE_FINANC_TYPE = 0;
    public static final int OPEN_FINANC_TYPE = 1;


    //理财记录type
    public static final int DEPOSIT_RECORD_IN_TYPE = 0;
    public static final int DEPOSIT_RECORD_OUT_TYPE = 1;
}