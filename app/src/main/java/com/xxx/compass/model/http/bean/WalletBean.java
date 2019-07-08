package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

import java.util.List;

public class WalletBean {

    private double allAsset;
    private double allAvailableAsset;
    private List<TmpWalletExtBean> tmpWalletExt;

    public String getAllAsset() {
        return StringUtil.getUS(allAsset);
    }

    public String getAllAvailableAsset() {
        return StringUtil.getUS(allAvailableAsset);
    }

    public List<TmpWalletExtBean> getTmpWalletExt() {
        return tmpWalletExt;
    }

    public static class TmpWalletExtBean {

        private int id;
        private String address;
        private double balance;
        private double frozenBalance;
        private int isLock;
        private double toReleased;
        private int version;
        private String coinId;
        private String fullName;
        private String coinLogo;
        private double fee;
        private int supportExchange;
        private int supportFinanc;
        private double financingCnt;

        public String getAddress() {
            return address;
        }

        public double getBalance() {
            return Double.parseDouble(StringUtil.getMoney(balance));
        }

        public String getBalanceStr() {
            return StringUtil.getMoney(balance);
        }

        public double getFrozenBalance() {
            return Double.parseDouble(StringUtil.getMoney(frozenBalance));
        }

        public String getFrozenBalanceStr() {
            return StringUtil.getMoney(frozenBalance);
        }

        public String getCoinId() {
            return coinId == null ? "" : coinId.toUpperCase();
        }

        public String getCoinLogo() {
            return coinLogo;
        }

        public double getFee() {
            return fee;
        }

        public int getSupportExchange() {
            return supportExchange;
        }

        public int getSupportFinanc() {
            return supportFinanc;
        }

        public double getFinancingCnt() {
            return financingCnt;
        }
    }
}
