package com.gongpingjia.carplay.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.SimplePageAdapter;

/**
 * 引导页 hqh
 * 
 * @author Administrator
 * 
 */
public class GuidanceActivity extends CarPlayBaseActivity {
	SimplePageAdapter pagerAdapter;
	ViewPager pager;
	View firstView, secondView,thirdView;
	LayoutInflater mLayoutInflater;
	//开始畅游
	Button btn_start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidance);
	}

	@Override
	public void initView() {
		mLayoutInflater = LayoutInflater.from(self);
		firstView = mLayoutInflater.inflate(R.layout.vp_first, null);
		secondView = mLayoutInflater.inflate(R.layout.vp_second, null);
		thirdView = mLayoutInflater.inflate(R.layout.vp_third, null);
		pager = (ViewPager) findViewById(R.id.viewpager);
		pagerAdapter = new SimplePageAdapter(firstView, secondView,thirdView);
		
		 pager.setAdapter(pagerAdapter);
	        pager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        btn_start=(Button) thirdView.findViewById(R.id.start);
	        
	        btn_start.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(self,MainActivity.class);
					startActivity(intent);
					self.finish();
				}
			});
	}
}
