package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class ReleaseInfoBean {

    private double allReleaseAsset;
    private double allFlashsaleAsset;
    private double todayFlashsaleAsset;
    private double todayReleaseAsset;
    private double allRemainAsset;
    private double allDynamicReleaseAsset;

    public String getAllDynamicReleaseAsset() {
        return StringUtil.getMoney(allDynamicReleaseAsset) + " CT";
    }


    public String getAllReleaseAsset() {
        return StringUtil.getMoney(allReleaseAsset) + " CT";
    }

    public String getAllFlashsaleAsset() {
        return StringUtil.getMoney(allFlashsaleAsset) + " CT";
    }

    public String getTodayFlashsaleAsset() {
        return StringUtil.getMoney(todayFlashsaleAsset) + " CT";
    }

    public String getTodayReleaseAsset() {
        return StringUtil.getMoney(todayReleaseAsset) + " CT";
    }

    public String getAllRemainAsset() {
        return StringUtil.getMoney(allRemainAsset) + " CT";
    }


}
