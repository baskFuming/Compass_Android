package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

import java.util.List;

public class ShareRecordBean {

    private double totalCnt;
    private List<RestListBean> restList;

    public String getTotalCnt() {
        return StringUtil.getMoney(totalCnt);
    }

    public List<RestListBean> getRestList() {
        return restList;
    }

    public static class RestListBean {

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
            return StringUtil.getMoney(amount);
        }

        public String getTime() {
            return StringUtil.getSimpleDataFormatTime("yyyy-MM-dd HH:mm:ss", time);
        }

    }
}
