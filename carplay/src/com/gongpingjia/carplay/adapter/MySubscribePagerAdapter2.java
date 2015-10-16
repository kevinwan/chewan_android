package com.gongpingjia.carplay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 * 我的关注页面ViewPager adapter
 */
public class MySubscribePagerAdapter2 extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MySubscribePagerAdapter2(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;
        switch (position) {
            case 0:
                title = "互相关注";
                break;
            case 1:
                title = "我的关注";
                break;
            case 2:
                title = "关注我的";
                break;
            default:
                title = "互相关注";
        }
        return title;
    }
}
