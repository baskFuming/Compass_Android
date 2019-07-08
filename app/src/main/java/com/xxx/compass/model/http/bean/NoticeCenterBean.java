package com.xxx.compass.model.http.bean;

import java.util.List;

public class NoticeCenterBean {

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public static class ContentBean {

        private int id;
        private String title;
        private String content;
        private String createTime;
        private boolean isShow;
        private Object isTop;
        private Object imgUrl;
        private int sort;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getCreateTime() {
            return createTime;
        }

    }
}
