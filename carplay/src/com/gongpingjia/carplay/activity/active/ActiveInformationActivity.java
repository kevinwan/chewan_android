package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.PicLayoutUtil;
import com.gongpingjia.carplay.view.RoundImageView;

/**
 * 
 * @Description 活动信息
 * @author wang
 * @date 2015-7-17 上午11:09:03
 */
public class ActiveInformationActivity extends CarPlayBaseActivity implements
		OnClickListener {
	User user;
	String activityId;
	LinearLayout headlayoutV;
	int id;
	JSONObject object = new JSONObject();
	int headlayoutWidth;
	int piclayoutWidth;
	// 活动介绍 组织者名字
	TextView activity_Introduction, activity_name;
	// 组织者头像
	RoundImageView head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_information);
		headlayoutV = (LinearLayout) findViewById(R.id.headlayout);
		activity_Introduction = (TextView) findViewById(R.id.activity_Introduction);
		activity_name = (TextView) findViewById(R.id.activity_name);
		 head = (RoundImageView) findViewById(R.id.activity_head);
	}

	@Override
	public void initView() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		piclayoutWidth = width - DhUtil.dip2px(self, 10 + 10);
		headlayoutWidth = piclayoutWidth - DhUtil.dip2px(self, 80 + 8 * 2 + 10);
		setTitle("活动信息");
		user = User.getInstance();
		activityId = getIntent().getStringExtra("activityId");
		setRightAction("举报", -1, new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		getData();

	}

	public void getData() {
		DhNet net = new DhNet(API.CWBaseurl + "/activity/" + activityId
				+ "/info?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					object = response.jSONFromData();
					id = JSONUtil.getInt(object, "isOrganizer");
					bindHeadView(object);

				}
			}
		});
	}

	private void bindHeadView(final JSONObject headJo) {
		JSONObject createrJo = JSONUtil.getJSONObject(headJo, "organizer");
		
		ViewUtil.bindNetImage(head, JSONUtil.getString(createrJo, "photo"),
				"head");
		ViewUtil.bindView(activity_Introduction,
				JSONUtil.getString(headJo, "introduction"));
		ViewUtil.bindView(activity_name,
				JSONUtil.getString(createrJo, "nickname"));

		JSONArray headJsa = JSONUtil.getJSONArray(headJo, "members");
		System.out.println("111111111"+ JSONUtil.getJSONArray(headJo, "members"));
		headlayoutV.removeAllViews();
		PicLayoutUtil headUtil = new PicLayoutUtil(self, headJsa, 5,
				headlayoutV, headlayoutWidth);
		headUtil.setHeadMaxCount(6);
		headUtil.AddChild();

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.headlayout:

			break;
		case R.id.activity_head:
			break;

		default:
			break;
		}

	}

}
