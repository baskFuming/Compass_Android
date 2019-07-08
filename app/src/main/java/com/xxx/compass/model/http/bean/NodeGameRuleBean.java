package com.xxx.compass.model.http.bean;

import android.app.Activity;

import com.xxx.compass.model.utils.StringUtil;

public class NodeGameRuleBean {

    private int id;
    private int level;
    private int nimIndex;
    private int maxInde;
    private int bonusesCnt;
    private double bonusesRatio;

    public String getLevel(Activity activity) {
        return StringUtil.getChinese(activity, level);
    }

    public String getIndex() {
        return nimIndex + " - " + maxInde;
    }

    public String getBonusesRatio() {
        return (bonusesRatio * 100) + "%";
    }

}