package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

import java.util.List;

public class NodeGameBean {

    private double allBonuseAsset;
    private String championAccount;
    private double championBonuse;
    private int myIndex;
    private List<Top10UserInfoBean> top10UserInfo;

    public String getAllBonuseAsset() {
        return StringUtil.getMoney(allBonuseAsset) + "CT";
    }

    public String getChampionAccount() {
        return championAccount;
    }

    public String getChampionBonuse() {
        return StringUtil.getMoney(championBonuse) + "CT";
    }

    public int getMyIndex() {
        return myIndex;
    }

    public List<Top10UserInfoBean> getTop10UserInfo() {
        return top10UserInfo;
    }

    public static class Top10UserInfoBean {

        private int id;
        private String memberAccount;
        private double asset;
        private long time;

        public String getMemberAccount() {
            return StringUtil.getPhoneCode(memberAccount);
        }

        public String getAsset() {
            return StringUtil.getMoney(asset);
        }

        public long getTime() {
            return time;
        }

    }
}