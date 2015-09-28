package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.INetRefreshAndMorelistView.OnRefreshListener;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class MyAttentionActivitiy2 extends CarPlayBaseActivity {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private PagerAdapter mAdapter;

    private List<String> mTitles;

    private View mRoot;

    private NetRefreshAndMoreListView mFollowEachOther, mFollowing, mFollower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention2);
    }

    @Override
    public void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tl_attention);
        mViewPager = (ViewPager) findViewById(R.id.vp_attention);

        mTitles = new ArrayList<String>();
        mTitles.add("互相关注");
        mTitles.add("我的关注");
        mTitles.add("关注我的");

        mRoot = LayoutInflater.from(this).inflate(R.layout.include_refresh_listview, null);
        mFollowEachOther = (NetRefreshAndMoreListView) mRoot.findViewById(R.id.listview);
        mFollowing = (NetRefreshAndMoreListView) mRoot.findViewById(R.id.listview);
        mFollower = (NetRefreshAndMoreListView) mRoot.findViewById(R.id.listview);
        NetJSONAdapter adapter = new NetJSONAdapter(API.activeList, this, R.layout.item_follower_mutual);
        adapter.addparam("key", "latest");
        mFollowEachOther.setAdapter(adapter);
        adapter.showNext();

        mFollowEachOther.setOnEmptyDataListener(new OnEmptyDataListener() {

            @Override
            public void onEmpty(boolean showeEptyView) {
                mRoot.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            }
        });

        mFollowEachOther.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                showToast("refresh");
            }
        });

        mAdapter = new PagerAdapter() {

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                switch (position) {
                case 0:
                    ((ViewPager) container).addView(mFollowEachOther);
                    break;
                case 1:
                    ((ViewPager) container).addView(mFollowing);
                    break;
                case 2:
                    ((ViewPager) container).addView(mFollower);
                    break;
                default:
                    return mFollowEachOther;
                }
                return mFollowEachOther;
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
