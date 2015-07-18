package com.gongpingjia.carplay.activity;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
