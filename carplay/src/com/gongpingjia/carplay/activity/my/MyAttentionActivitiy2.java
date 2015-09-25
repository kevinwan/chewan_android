package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class MyAttentionActivitiy2 extends CarPlayBaseActivity {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private PagerAdapter mAdapter;

    private List<String> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention2);
    }

    @Override
    public void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tl_attention);
        mViewPager = (ViewPager) findViewById(R.id.vp_attention);

        mTitles = new ArrayList<>();
        mTitles.add("互相关注");
        mTitles.add("我的关注");
        mTitles.add("关注我的");

        mAdapter = new PagerAdapter() {

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                // TODO Auto-generated method stub
                TextView tv = new TextView(self);
                tv.setText(String.valueOf(position));
                ((ViewPager) container).addView(tv);
                return tv;
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }

        };

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
