package com.gongpingjia.carplay.activity.main;

import net.duohuo.dhroid.ioc.IocContainer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.util.CarPlayPerference;

public class SplashActivity extends CarPlayBaseActivity {
	
	CarPlayPerference per;
	private final Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void initView() {
		per = IocContainer.getShare().get(CarPlayPerference.class);
		per.load();
		
				if (per.isFirst==0) {
					
					first();
					
				}else{
					notFirst();
				}
		
	}

	private void first() {
		 mHandler.postDelayed(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	            	Intent intent=new Intent(self,GuidanceActivity.class);
					startActivity(intent);
					per.isFirst=1;
					per.commit();
					self.finish();
	            }
	        }, 3000);
	}
	
	private void notFirst() {
		 mHandler.postDelayed(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	            	Intent intent=new Intent(self,MainActivity.class);
					startActivity(intent);
					self.finish();
	            }
	        }, 3000);
	}
}
