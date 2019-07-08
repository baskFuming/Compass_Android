package com.xxx.compass.model.http.bean;

public class IsSettingPayPswBean {

    private String username;
    private int id;
    private String createTime;
    private int realVerified;
    private int emailVerified;
    private int phoneVerified;
    private int loginVerified;
    private int fundsVerified;
    private int realAuditing;
    private String mobilePhone;
    private Object email;
    private Object realName;
    private Object realNameRejectReason;
    private Object idCard;
    private Object avatar;
    private int accountVerified;
    private Object googleStatus;

    public boolean getFundsVerified() {
        return fundsVerified != 0;
    }
}
