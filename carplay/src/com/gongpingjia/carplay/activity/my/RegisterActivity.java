package com.gongpingjia.carplay.activity.my;

import java.util.HashMap;
import java.util.Map;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.util.Utils;

/***
 * 注册页面
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends CarPlayBaseActivity implements
		OnClickListener {
	/** 手机号框 */
	private EditText phoneEt = null;
	/** 验证码框 */
	private EditText codeEt = null;
	/** 密码框 */
	private EditText pswdEt = null;
	/** 验证码按钮 */
	private Button codeBtn = null;
	/** 下一步 */
	private Button nextBtn = null;
	/** 车玩服务条款 */
	private LinearLayout cwfwtk = null;
	/** check选择 */
	private CheckBox ckeck;

	TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

	}

	@Override
	public void initView() {
		setTitle("注册");

		viewInit();

		CarPlayPerference per = IocContainer.getShare().get(
				CarPlayPerference.class);
	}

	/** 控件初始化 */
	private void viewInit() {
		phoneEt = (EditText) findViewById(R.id.phone);
		codeEt = (EditText) findViewById(R.id.code);
		pswdEt = (EditText) findViewById(R.id.pswd);
		codeBtn = (Button) findViewById(R.id.btn_code);
		nextBtn = (Button) findViewById(R.id.next);
		cwfwtk = (LinearLayout) findViewById(R.id.cwfwtk);
		ckeck = (CheckBox) findViewById(R.id.checked);

		codeBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		cwfwtk.setOnClickListener(this);

		time = new TimeCount(60000, 1000);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_code:
			getCode();
			break;
		case R.id.next:
			nextStep();
			break;
		case R.id.cwfwtk:

			break;

		default:
			break;
		}
	}

	/** 下一步 */
	private void nextStep() {
		final String strPhone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(strPhone)) {
			showToast("请输入手机号码");
			return;
		} else {
			if (!Utils.isValidMobilePhoneNumber(strPhone)) {
				showToast("请输入正确的手机号码");
				return;
			}
		}

		final String strCaptcha = codeEt.getText().toString();
		if (TextUtils.isEmpty(strCaptcha)) {
			showToast("请输入验证码");
			return;
		}

		final String strPassword = pswdEt.getText().toString();
		if (TextUtils.isEmpty(strPassword)) {
			showToast("请输入密码");
			return;
		} else {
			if (strPassword.length() < 6 || strPassword.length() > 16) {
				showToast("密码长度应在6-16之间，请重新输入");
				return;
			}
		}

		if (!ckeck.isChecked()) {
			showToast("请阅读并同意车玩服务条款");
			return;
		}
//		
//		/** 校验验证码  */
//		DhNet net = new DhNet("http://cwapi.gongpingjia.com/v1/phone/"
//				+ strPhone + "/verification");
//		net.addParam("code",strCaptcha);
//		net.doPostInDialog(new NetTask(self) {
//
//			@Override
//			public void doInUI(Response response, Integer transfer) {
//				if (response.isSuccess()) {
					Intent it=new Intent(self,BasicMessageActivity.class);
					it.putExtra("phone", strPhone);
					it.putExtra("code", strCaptcha);
					it.putExtra("pswd", MD5Util.string2MD5(strPassword));
					startActivity(it);
//				} else {
//				}
//			}
//		});
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		codeEt.setText("");
		final String strPhone = phoneEt.getText().toString();
		if (TextUtils.isEmpty(strPhone)) {
			showToast("请输入手机号码");
			return;
		} else {
			if (!Utils.isValidMobilePhoneNumber(strPhone)) {
				showToast("请输入正确的手机号码");
				return;
			}
		}

		DhNet net = new DhNet("http://cwapi.gongpingjia.com/v1/phone/"
				+ strPhone + "/verification");
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					time.start();
					codeBtn.setEnabled(false);
				} else {
					codeBtn.setEnabled(true);
				}
			}
		});
	}

	/**
	 * 发送验证码定时器
	 * 
	 * @author Administrator
	 * 
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			codeBtn.setText("重新发送");
			codeBtn.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			codeBtn.setEnabled(false);
			codeBtn.setText(millisUntilFinished / 1000 + "秒");
		}
	}

}
