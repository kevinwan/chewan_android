package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.MySubscribePagerAdapter2;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/10/16.
 * 我的关注页面
 */
public class SubscribeActivity2 extends CarPlayListActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MySubscribePagerAdapter2 mAdapter;
    //相互关注,我关注的人,关注我的人
    private Fragment mEachSubscribe, mMySubscriber, mBeSubscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follower2);
        setTitle("我的关注");
        View decor = getWindow().getDecorView();
        mViewPager = find(decor, R.id.vp_follower);
        mTabLayout = find(decor, R.id.tab_layout);

        mMySubscriber = new MySubscriberFragment();
        mBeSubscribed = new BeSubscribedFragment();
        mEachSubscribe = new SubscribeEachOtherFragment();

        mAdapter = new MySubscribePagerAdapter2(getSupportFragmentManager(), Arrays.asList(mEachSubscribe, mMySubscriber, mBeSubscribed));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
    }

    public <T extends View> T find(View view, int id) {
        if (null == view) {
            throw new RuntimeException("container view can not be null");
        }
        return (T) view.findViewById(id);
    }
}
