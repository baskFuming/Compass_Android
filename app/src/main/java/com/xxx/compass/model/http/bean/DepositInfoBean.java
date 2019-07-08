package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class DepositInfoBean {

    private double allFinancingsAsset;
    private double allFinancingsIncome;

    public String getAllFinancingsAsset(boolean isTotal) {
        if (isTotal) {
            return StringUtil.getUS(allFinancingsAsset);
        } else {
            return StringUtil.getMoney(allFinancingsAsset);
        }
    }

    public String getAllFinancingsIncome() {
        return StringUtil.getMoney(allFinancingsIncome);
    }
}
