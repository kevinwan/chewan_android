package com.gongpingjia.carplay.activity;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
/**
 * 找回密码(设置新密码)页面
 * hqh
 * @author Administrator
 *
 */
public class PwdNextActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_next);
		
		setTitle("找回密码");
	}

}
