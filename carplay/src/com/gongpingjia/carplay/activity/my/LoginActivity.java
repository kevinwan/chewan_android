package com.gongpingjia.carplay.activity.my;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 登录页面
 * @author Administrator
 *
 */
public class LoginActivity extends CarPlayBaseActivity{
	private EditText PhoneNumEditText;
	private EditText PasswordEditText;
	private Button LoginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	@Override
	public void initView() {
		 PhoneNumEditText = (EditText) findViewById(R.id.ed_login_phone);
		 PasswordEditText = (EditText) findViewById(R.id.ed_login_password);
		 LoginButton = (Button) findViewById(R.id.button_login);
		 LoginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 String strPhoneNum = PhoneNumEditText.getText().toString();
				 String strPassword = PasswordEditText.getText().toString();
			}
		});
	}
}
