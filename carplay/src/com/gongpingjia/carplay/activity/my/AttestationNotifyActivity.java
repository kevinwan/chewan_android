package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.color;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.TabEB;
import com.gongpingjia.carplay.bean.User;

import de.greenrobot.event.EventBus;

/**
 * 车主认证通知
 * 
 * @author Administrator
 * 
 */
public class AttestationNotifyActivity extends CarPlayBaseActivity {
	TextView contentpassT, contentfailT, reasonT;
	Button nextBtn;
	LinearLayout notpassL, passL;
	String result;

	// 是否认证车主成功 (默认0:未成功)
	int isAuthenticated = 0;
	int drivingyears = 0;
	String carModel = "";
	String license = "";

	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attestation_notify);
	}

	@Override
	public void initView() {
		user = User.getInstance();
		setTitle("车主认证通知");
		contentpassT = (TextView) findViewById(R.id.contentpass);
		contentfailT = (TextView) findViewById(R.id.contentfail);
		nextBtn = (Button) findViewById(R.id.next);
		notpassL = (LinearLayout) findViewById(R.id.notpass);
		passL = (LinearLayout) findViewById(R.id.pass);
		reasonT = (TextView) findViewById(R.id.reason);

		Intent it = getIntent();
		String model = it.getExtras().getString("carModel");
		result = it.getExtras().getString("result");

		if (result.equals("0")) {
			pass(model);
		} else {
			String remarks = it.getStringExtra("remarks");
			fail(model, remarks);
		}

		DhNet verifyNet = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/authentication?token=" + user.getToken());
		verifyNet.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					isAuthenticated = JSONUtil.getInt(jo, "isAuthenticated");
					drivingyears = JSONUtil.getInt(jo, "drivingExperience");
					carModel = JSONUtil.getString(jo, "carModel");
					license = JSONUtil.getString(jo, "licensePhoto");
				}
			}
		});

		nextBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (result.equals("0")) {
					Intent it = new Intent(self, MainActivity.class);
					it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(it);
					finish();
					EventBus.getDefault().post(new TabEB());
				} else {
					// 已认证 则不跳转
					if (isAuthenticated != 1) {
						Intent intent = new Intent(self,
								AuthenticateOwnersActivity.class);
						intent.putExtra("type", "attest");
						intent.putExtra("isAuthenticated", isAuthenticated);
						intent.putExtra("drivingyears", drivingyears);
						intent.putExtra("carModel", carModel);
						intent.putExtra("license", license);
						startActivity(intent);
					}
					finish();
				}
			}
		});

	}

	/** 通过 */
	private void pass(String model) {
		notpassL.setVisibility(View.GONE);
		passL.setVisibility(View.VISIBLE);
		nextBtn.setText("开启我的车旅程");

		String newcontent = null;
		String a = "您申请的";
		String b = model;
		String c = "身份认证已经审核通过";
		newcontent = a + b + c;

		contentpassT.setText(strColor(newcontent, b));
	}

	/** 未通过 */
	private void fail(String model, String remarks) {
		notpassL.setVisibility(View.VISIBLE);
		passL.setVisibility(View.GONE);
		nextBtn.setText("重新认证");

		String newcontent = null;
		String a = "您申请的";
		String b = model;
		String c = "身份认证未通过审核";
		newcontent = a + b + c;

		contentfailT.setText(strColor(newcontent, b));
		reasonT.setText(remarks);
	}

	private SpannableStringBuilder strColor(String newcontent, String str) {
		int start = newcontent.indexOf(str);
		SpannableStringBuilder style = new SpannableStringBuilder(newcontent);
		style.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.theme_bg)), start, start + str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

}
