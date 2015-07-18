package com.gongpingjia.carplay.activity;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/***
 * 新的留言页面 hqh
 * 
 * @author Administrator
 * 
 */
public class NewMessageActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
	}

	@Override
	public void initView() {
		setTitle("新的留言");
	}
}
