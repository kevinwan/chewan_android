package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;

/**
 * 找回密码(设置新密码)页面 hqh
 * 
 * @author Administrator
 * 
 */
public class PwdNextActivity extends CarPlayBaseActivity {
	// 新密码
	private EditText ed_new_pwd;
	// 完成
	private Button button_new_pwd;
	// 手機號
	private String phone;
	// 验证码
	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_next);

		setTitle("找回密码");
	}

	@Override
	public void initView() {
		ed_new_pwd = (EditText) findViewById(R.id.ed_new_pwd);
		button_new_pwd = (Button) findViewById(R.id.button_new_pwd);
		Intent intent = getIntent();
		// 手機號
		phone = intent.getStringExtra("PhoneNum");
		// 验证码
		code = intent.getStringExtra("Verification");
		button_new_pwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String strpwd = ed_new_pwd.getText().toString();
				if (TextUtils.isEmpty(strpwd)) {
					showToast("密码不能为空");
				}
				DhNet net = new DhNet(API.newPassword);
				net.addParam("phone", phone);
				System.out.println(phone+code+strpwd);
				net.addParam("code", code);
				net.addParam("password", MD5Util.string2MD5(strpwd));
				net.doPost(new NetTask(self) {

					@Override
					public void doInUI(Response response, Integer transfer) {
						if (response.isSuccess()) {
							JSONObject jo = response.jSONFrom("data");
							User user = User.getInstance();
							user.setUserId(JSONUtil.getString(jo, "userId"));
							user.setToken(JSONUtil.getString(jo, "token"));

							CarPlayPerference per = IocContainer.getShare()
									.get(CarPlayPerference.class);
							per.phone = phone;
							per.password = strpwd;
							per.commit();
							Intent intent = new Intent(self,
									LoginActivity.class);
							startActivity(intent);
							self.finish();

						} else {
							showToast(response.result);
						}

					}
				});
			}
		});
	}

}
