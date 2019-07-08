package com.xxx.compass.model.http.bean;

public class LoginBean {

    private String username;
    private LocationBean location;
    private int memberLevel;
    private String token;
    private Object realName;
    private CountryBean country;
    private Object avatar;
    private String promotionCode;
    private int id;
    private int loginCount;
    private String superPartner;
    private String promotionPrefix;
    private boolean signInAbility;
    private boolean signInActivity;
    private Object memberRate;

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public CountryBean getCountry() {
        return country;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public int getId() {
        return id;
    }

    private static class LocationBean {
        private String country;
        private Object province;
        private Object city;
        private Object district;
    }

    public static class CountryBean {

        private String zhName;
        private String enName;
        private String areaCode;
        private String language;
        private String localCurrency;
        private int sort;

        public String getZhName() {
            return zhName;
        }
    }
}
