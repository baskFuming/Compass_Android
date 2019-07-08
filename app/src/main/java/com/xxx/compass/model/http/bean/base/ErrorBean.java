package com.xxx.compass.model.http.bean.base;

public class ErrorBean {
    /**
     * code : 0
     * message : 操作成功
     * data : null
     */
    private int code;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
