package com.gongpingjia.carplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
/***
 * 注册页面
 * @author Administrator
 *
 */
public class RegisterActivity extends CarPlayBaseActivity {

	private Button mNext=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		viewInit();
	}

	private void viewInit() {
		setTitle("注册");
		mNext=(Button) findViewById(R.id.next);
		mNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(RegisterActivity.this,ForgetPwdActivity.class);
				startActivity(intent);
			}
		});
	}

    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }

}
