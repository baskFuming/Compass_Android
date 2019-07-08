package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class RushInfoBean {

    private double amount;
    private String coinSymbol;
    private double nextAmount;
    private long nextStarttime;
    private double flashsale_mount;
    private long endtime;
    private int id;
    private long starttime;
    private long current_time;
    private double ratio;

    public double getAmount() {
        return Double.parseDouble(StringUtil.getMoney(amount));
    }

    public double getFlashsale_mount() {
        return Double.parseDouble(StringUtil.getMoney(flashsale_mount));
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public long getNextStarttime() {
        return nextStarttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public long getStarttime() {
        return starttime;
    }

    public long getCurrent_time() {
        return current_time;
    }

    public String getRatio() {
        return StringUtil.getUS(ratio * 100) + "%";
    }

}