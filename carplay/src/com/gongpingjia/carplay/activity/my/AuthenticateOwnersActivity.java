package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * 
 * @Description 认证车主
 * @author wang
 * @date 2015-7-17 上午10:14:30
 */
public class AuthenticateOwnersActivity extends CarPlayBaseActivity {

	private TextView modelT = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate_owners);
	}

	@Override
	public void initView() {
		setTitle("车主认证");
		setRightAction("跳过", -1, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				noCar();
				
			}
		});
		modelT = (TextView) findViewById(R.id.model);
		modelT.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(self, CarTypeSelectActivity.class);
				startActivity(intent);
			}
		});

		Bundle bundle = (Bundle) getIntent().getExtras().get("data");
		System.out.println(bundle.get("phone") + "-------" + bundle.get("code")
				+ "-------" + bundle.get("pswd") + "-------"
				+ bundle.get("nickname") + "-------" + bundle.get("sex")
				+ "-------" + bundle.get("age") + "-------"
				+ bundle.get("city"));

	}

	/**
	 * 非车主 注册
	 */
	private void noCar(){
		
	}
	
}
