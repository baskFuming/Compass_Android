package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class WithdrawalRecordBean {

    private int id;
    private String unit;
    private int orginIndex;
    private String tableSymbol;
    private int transferType;
    private double amount;
    private long time;

    public String getUnit() {
        return unit;
    }

    public String getAmount() {
        return "-" + StringUtil.getMoney(amount);
    }

    public String getTime() {
        return StringUtil.getSimpleDataFormatTime("yyyy-MM-dd HH:mm:ss", time);
    }

}
