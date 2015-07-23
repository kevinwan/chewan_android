package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.util.MD5Util;

/**
 * 
 * @Description 认证车主
 * @author wang
 * @date 2015-7-17 上午10:14:30
 */
public class AuthenticateOwnersActivity extends CarPlayBaseActivity {

	
	private TextView modelT = null;

	//注册数据
	Bundle data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate_owners);
	}

	@Override
	public void initView() {
		data = (Bundle) getIntent().getExtras().get("data");
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

		

	}

	/**
	 * 非车主 注册
	 */
	private void noCar(){
		DhNet net = new DhNet("http://cwapi.gongpingjia.com/v1/user/register");
		net.addParam("phone",data.get("phone"));
		net.addParam("code",data.get("code"));
		net.addParam("password",data.get("password"));
		net.addParam("nickname",data.get("nickname"));
		net.addParam("gender",data.get("gender"));
		net.addParam("birthYear",data.get("birthYear"));
		net.addParam("birthMonth",data.get("birthMonth"));
		net.addParam("birthday",data.get("birthday"));
		net.addParam("province",data.get("province"));
		net.addParam("city",data.get("city"));
		net.addParam("district",data.get("district"));
		net.addParam("photo",data.get("photo"));
		
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("注册成功");
				} else {
					showToast(response.result);
				}
			}
		});
	}
	
}
