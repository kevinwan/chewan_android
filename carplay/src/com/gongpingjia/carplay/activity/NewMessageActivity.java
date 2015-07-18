package com.gongpingjia.carplay.activity;

import android.os.Bundle;

import com.gongpingjia.carplay.R;

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
