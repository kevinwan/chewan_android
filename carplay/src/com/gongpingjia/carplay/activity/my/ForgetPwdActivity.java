package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
/**
 * 找回密码
 * @author Administrator
 *
 */
public class ForgetPwdActivity extends CarPlayBaseActivity {

	private Button mNext=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		
		viewInit();
	}

	private void viewInit() {
		setTitle("找回密码");
		mNext=(Button) findViewById(R.id.next);
		mNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(ForgetPwdActivity.this,PwdNextActivity.class);
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
