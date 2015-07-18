package com.gongpingjia.carplay.activity;

import android.os.Bundle;

import com.gongpingjia.carplay.R;

/***
 * 编辑活动页面
 * 
 * @author Administrator
 * 
 */
public class EditEventActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
	}

	@Override
	public void initView() {
		setTitle("编辑活动");
	}
}
