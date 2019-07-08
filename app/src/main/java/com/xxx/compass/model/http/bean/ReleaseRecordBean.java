package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class ReleaseRecordBean {

    private int id;
    private double mount;
    private double flashsaleMount;
    private long createTime;
    private long flashsaleTime;
    private int flag;

    public String getAmount() {
        return "+" + StringUtil.getMoney(mount) + "CT";
    }

    public String getCreateTime() {
        return StringUtil.getSimpleDataFormatTime("yyyy-MM-dd HH:mm:ss", createTime);
    }

}
