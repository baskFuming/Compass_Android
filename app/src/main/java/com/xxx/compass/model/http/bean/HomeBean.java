package com.xxx.compass.model.http.bean;

import com.xxx.compass.model.utils.StringUtil;

public class HomeBean {

    private int coinId;
    private String coinEnglishName;
    private String coinSymbol;
    private String coinUrl;
    private String coinChineseName;
    private String coinContractAddress;
    private double coinDecimal;
    private double coinDecimalUsed;
    private String coinQuotesName;
    private String coinLocation;
    private double coinPriceRmb;
    private String coinPriceUsdt;
    private String coinFluctuation;
    private long coinPriceUpdatetime;
    private long coinFluctuationUpdatetime;
    private String coinPriceRequestpath;
    private String coinPriceJsonStructure;
    private String coinPriceRequestmethod;
    private String coinPriceRequestbody;
    private String coinFluctuationJsonStructure;
    private String coinFluctuationRequestpath;
    private String coinFluctuationRequestmethod;
    private int isdisplay;
    private String coinFluctuationRequestbody;
    private int coinSort;

    public String getCoinUrl() {
        return coinUrl;
    }

    public String getCoinChineseName() {
        return coinChineseName  == null ? coinEnglishName : coinChineseName;
    }

    public String getCoinQuotesName() {
        return coinQuotesName;
    }

    public String getCoinPriceRmb() {
        return "￥" + StringUtil.getUS(coinPriceRmb);
    }

    public String getCoinPriceUsdt() {
        return " $" + StringUtil.getMoney(coinPriceUsdt == null || coinPriceUsdt.equals("") ? 0 : Double.parseDouble(coinPriceUsdt));
    }

    public String getCoinFluctuation() {
        return coinFluctuation;
    }

}
