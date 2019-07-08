package com.xxx.compass.model.http.bean;

import android.app.Activity;

import com.xxx.compass.R;
import com.xxx.compass.base.App;
import com.xxx.compass.model.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class AchievementRecordBean implements Serializable {

    private int referCnt;
    private int rootId;
    private String rootPhone;
    private int rootStar;
    private int maxLevelCnt;
    private double referTeamAsset;
    private List<CommunityInfoListBean> communityInfoList;

    public String getReferTeamAsset() {
        return StringUtil.getMoney(referTeamAsset);
    }

    public List<CommunityInfoListBean> getCommunityInfoList() {
        return communityInfoList;
    }

    public static class CommunityInfoListBean implements Serializable {

        private double directReferAsset;
        private int referCnt;
        private int level;
        private double referTeamAsset;
        private List<TeamListBean> teamList;

        public String getDirectReferAsset() {
            return StringUtil.getMoney(directReferAsset);
        }

        public String getReferTeamAsset() {
            return StringUtil.getMoney(referTeamAsset);
        }

        public String getReferCnt() {
            return String.valueOf(referCnt);
        }

        public String getLevel(Activity activity) {
            return String.valueOf(level + 1) + activity.getString(R.string.generation);
        }

        public List<TeamListBean> getTeamList() {
            return teamList;
        }

        public static class TeamListBean implements Serializable {

            private int userStar;
            private int referCnt;
            private String phone;
            private double referTeamAsset;

            public String getUserStar(Activity activity) {
                return userStar == 0 ? activity.getString(R.string.item_achievement_record_amount_1) : userStar + activity.getString(R.string.item_achievement_record_amount_2);
            }

            public String getPhone() {
                return StringUtil.getPhoneCode(phone);
            }

            public String getReferTeamAsset() {
                return StringUtil.getMoney(referTeamAsset);
            }

        }
    }
}
