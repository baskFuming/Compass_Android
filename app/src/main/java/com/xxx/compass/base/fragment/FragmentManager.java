package com.xxx.compass.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.xxx.compass.base.fragment.BaseFragment;

import java.util.List;

/**
 * Fragment管理类
 */
public class FragmentManager {

    /**
     * 替换fragment
     */
    public static void replaceFragment(AppCompatActivity activity, Class<? extends BaseFragment> fragmentClass, int FrameLayoutId) {
        if (activity != null) {
            android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            String fragmentName = fragmentClass.getSimpleName();
            BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(fragmentName);
            try {
                if (fragment == null) {
                    fragment = fragmentClass.newInstance();
                    transaction.add(FrameLayoutId, fragment, fragmentName);
                }
                List<Fragment> list = fragmentManager.getFragments();
                for (Fragment fragment1 : list) {   //
                    transaction.hide(fragment1);
                }
                transaction.show(fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.commit();
        }
    }

    /**
     * 获取Fragment
     */
    public static Fragment getFragment(AppCompatActivity activity, String fragmentName) {
        if (activity != null) {
            android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
            return fragmentManager.findFragmentByTag(fragmentName);
        }
        return null;
    }
}
