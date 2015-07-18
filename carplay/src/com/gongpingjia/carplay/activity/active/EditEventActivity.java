package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

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
