package com.gongpingjia.carplay.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.SimplePageAdapter;
import com.gongpingjia.carplay.view.CirclePageIndicator;

public class GuidanceActivity2 extends CarPlayBaseActivity {

    SimplePageAdapter pagerAdapter;
    ViewPager pager;
    View firstView, secondView,thirdView,fouthView,fifthView;
    LayoutInflater mLayoutInflater;
    //开始畅游
    ImageView btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance2);
    }

    @Override
    public void initView() {
        mLayoutInflater = LayoutInflater.from(self);
        firstView = mLayoutInflater.inflate(R.layout.item_guidance_page_first, null);
        secondView = mLayoutInflater.inflate(R.layout.item_guidance_page_second, null);
        thirdView = mLayoutInflater.inflate(R.layout.item_guidance_page_third, null);
        fouthView = mLayoutInflater.inflate(R.layout.item_guidance_page_fourth, null);
        fifthView = mLayoutInflater.inflate(R.layout.item_guidance_page_fifth, null);
        pager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new SimplePageAdapter(firstView, secondView,thirdView,fouthView,fifthView);

        pager.setAdapter(pagerAdapter);
        CirclePageIndicator mIndicator  = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        btn_start=(ImageView) fifthView.findViewById(R.id.start);

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(self, MainActivity2.class);
                startActivity(intent);
                finishWithoutAnim();
            }
        });
    }
}
