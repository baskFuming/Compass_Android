package com.xxx.compass.base.activity;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理类
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
        activityStack = new Stack<>();
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 获取到指定Activity
     */
    public Activity getActivity(String activityName) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity != null) {
                if (activity.getClass().getSimpleName().equals(activityName)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            int size = activityStack.size();
            for (int i = 0; i < size; i++) {
                Activity activity = activityStack.get(0);
                if (null != activity) {
                    activity.finish();
                }
            }
            activityStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
