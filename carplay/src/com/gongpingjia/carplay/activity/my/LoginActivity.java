package com.gongpingjia.carplay.activity.my;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录页面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends CarPlayBaseActivity {
	//手机号
	private EditText PhoneNumEditText;
	//密码
	private EditText PasswordEditText;
	//登录
	private Button LoginButton;
	//注册
	private LinearLayout login_register;
	//忘记密码
	private TextView login_forgetpsw;

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
				autoCloseKeyboard(self, arg0);
				final String strPhoneNum = PhoneNumEditText.getText()
						.toString();
				final String strPassword = PasswordEditText.getText()
						.toString();
				if (TextUtils.isEmpty(strPhoneNum)) {
					Toast.makeText(self, "手机号码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!isMobileNum(strPhoneNum)) {
					Toast.makeText(self, "手机格式错误", Toast.LENGTH_SHORT).show();
					return;

				}
				if (TextUtils.isEmpty(strPassword)) {
					Toast.makeText(self, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}

				DhNet net = new DhNet(
						"http://cwapi.gongpingjia.com/v1/user/login");
				net.addParam("phone", strPhoneNum);
				net.addParam("password", MD5Util.string2MD5(strPassword));
				net.doPost(new NetTask(self) {

					@Override
					public void doInUI(Response response, Integer transfer) {
						if (response.isSuccess()) {
							System.out.println("*******" + response.isSuccess());
							JSONObject object = response.jSON();
							User user = User.getInstance();
							user.setUserId(JSONUtil.getString(object, "userId"));
							user.setToken(JSONUtil.getString(object, "token"));
							System.out.println("userID:"
									+ JSONUtil.getString(object, "userId")
									+ "token:"
									+ JSONUtil.getString(object, "token"));

							CarPlayPerference per = IocContainer.getShare()
									.get(CarPlayPerference.class);
							per.phone = strPhoneNum;
							per.password = strPassword;
							per.commit();

							// Intent intent = new
							// Intent(self,MainActivity.class);
							// startActivity(intent);
						} else {
							showToast(response.msg);

						}
					}

					@Override
					public void onErray(Response response) {
						// TODO Auto-generated method stub
						super.onErray(response);
						showToast(response.msg);
					}
				});
			}
		});
		login_forgetpsw = (TextView) findViewById(R.id.login_forgetpsw);
		login_forgetpsw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(self, ForgetPwdActivity.class);
				startActivity(intent);
			}
		});
		login_register = (LinearLayout) findViewById(R.id.login_register);
		login_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(self, RegisterActivity.class);
				startActivity(intent);

			}
		});

	}

	/** 用于判断手机号段是否合法 */
	public static boolean isMobileNum(String num) {
		Pattern p = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

	/**
	 * 如果键盘没有收回 自动关闭键盘
	 * 
	 * @param activity
	 *            Activity
	 * @param v
	 *            控件View
	 */
	public static void autoCloseKeyboard(Activity activity, View v) {
		/** 收起键盘 */
		View view = activity.getWindow().peekDecorView();
		if (view != null && view.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
}
