package com.gongpingjia.carplay.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gongpingjia.carplay.R;

/***
 * 
 * 创建活动
 * @author Administrator
 *
 */
public class CreatEventActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_event);
		
		setTitle("创建活动");
	}

}
