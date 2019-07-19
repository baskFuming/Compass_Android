package com.xxx.compass.model.http.bean;

import java.io.Serializable;

public class GameBean implements Serializable{

    private int id;
    private String name;
    private double amount;  //游戏消耗数量
    private int enableFlag; //是否开启
    private String picUrl;  //图片地址
    private String gameUrl; //游戏路径

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isOpen() {
        return enableFlag == 1;
    }

    public String getImage() {
        return picUrl;
    }

    public String getUrl() {
        return gameUrl;
    }

}
