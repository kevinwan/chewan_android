package com.gongpingjia.carplay.activity.my;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 找回密码(设置新密码)页面 hqh
 * 
 * @author Administrator
 * 
 */
public class PwdNextActivity extends CarPlayBaseActivity {
	//新密码
	private EditText editText;
	//完成
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_next);

		setTitle("找回密码");
	}

	@Override
	public void initView() {
		editText = (EditText) findViewById(R.id.ed_new_pwd);
		button = (Button) findViewById(R.id.button_new_pwd);
		Intent intent = getIntent();
		//手機號
		String phone = intent.getStringExtra("PhoneNum");
		//验证码
		String verification = intent.getStringExtra("Verification");
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
