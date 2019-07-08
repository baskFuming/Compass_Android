package com.xxx.compass.model.http.bean;

public class AppVersionBean {

    private int id;
    private String publishTime;
    private String remark;
    private String version;
    private String downloadUrl;
    private int platform;

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

}