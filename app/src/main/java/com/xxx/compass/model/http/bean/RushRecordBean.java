package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class RushRecordBean {

    private int id;
    private double mount;
    private double flashsaleMount;
    private long createTime;
    private long flashsaleTime;
    private int flag;

    public String getCoinId(){
        return "CT";
    }

    public String getFlashsaleMount() {
        return "+" + flashsaleMount;
    }

    public String getFlashsaleTime() {
        return StringUtil.getSimpleDataFormatTime("yyyy-MM-dd HH:mm:ss", flashsaleTime);
    }
}
