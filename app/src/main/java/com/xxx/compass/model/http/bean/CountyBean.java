package com.xxx.compass.model.http.bean;

public class CountyBean {

    private String zhName;
    private String enName;
    private String areaCode;
    private String language;
    private String localCurrency;
    private int sort;

    public String getZhName() {
        return zhName;
    }

    public String getAreaCode() {
        return "+" + areaCode;
    }

}
