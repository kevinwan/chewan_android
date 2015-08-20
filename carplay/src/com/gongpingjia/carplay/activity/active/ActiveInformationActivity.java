package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.my.MyPerSonDetailActivity;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
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
	private static final int REQUEST_DESCRIPTION = 1;

	User user;
	// 活动ID
	String activityId;

	LinearLayout headlayoutV, activity_edit,acitvity_layout_head;
	// 是否为创建者
	int id;

	JSONObject headJo = new JSONObject();

	int headlayoutWidth;

	int piclayoutWidth;

	// 活动介绍 组织者名字
	TextView activity_name, activity_Introduction;

	// 组织者头像
	RoundImageView head;

	boolean isJoin = false;

	String status = null;

	long startTime;

	String userid;

	private DhNet mDhNet;

	String type, intro, location, address, province, city, district, pay;

	Double longitude, latitude;

	long start, end;

	JSONArray picJsa;

	Button quiteB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_information);
		headlayoutV = (LinearLayout) findViewById(R.id.headlayout);
		acitvity_layout_head = (LinearLayout) findViewById(R.id.acitvity_layout_head);
		activity_edit = (LinearLayout) findViewById(R.id.activity_edit);
		activity_Introduction = (TextView) findViewById(R.id.activity_Introduction);
		activity_name = (TextView) findViewById(R.id.activity_name);
		head = (RoundImageView) findViewById(R.id.activity_head);
		quiteB = (Button) findViewById(R.id.quite);
		quiteB.setOnClickListener(this);
		headlayoutV.setOnClickListener(this);
		activity_edit.setOnClickListener(this);
//		head.setOnClickListener(this);
		acitvity_layout_head.setOnClickListener(this);
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
				showToast("举报");
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
					headJo = response.jSONFromData();
					id = JSONUtil.getInt(headJo, "isOrganizer");
					// type = JSONUtil.getString(headJo, "type");
					// intro = JSONUtil.getString(headJo, "introduction");
					// location = JSONUtil.getString(headJo, "location");
					// address = JSONUtil.getString(headJo, "address");
					// province = JSONUtil.getString(headJo, "province");
					// city = JSONUtil.getString(headJo, "city");
					// district = JSONUtil.getString(headJo, "district");
					// pay = JSONUtil.getString(headJo, "pay");
					// longitude = JSONUtil.getDouble(headJo, "longitude");
					// latitude = JSONUtil.getDouble(headJo, "latitude");
					// start = JSONUtil.getLong(headJo, "start");
					// end = JSONUtil.getLong(headJo, "end");
					// picJsa = JSONUtil.getJSONArray(headJo, "cover");
					// JSONArray picJsa = JSONUtil.getJSONArray(headJo,
					// "cover");
					bindHeadView(headJo);

				}
			}
		});
	}

	private void bindHeadView(final JSONObject headJo) {
		JSONObject createrJo = JSONUtil.getJSONObject(headJo, "organizer");
		userid = JSONUtil.getString(createrJo, "userId");
		activeRelative(headJo);
		ViewUtil.bindNetImage(head, JSONUtil.getString(createrJo, "photo"),
				"head");
		ViewUtil.bindView(activity_Introduction,
				JSONUtil.getString(headJo, "introduction"));
		ViewUtil.bindView(activity_name,
				JSONUtil.getString(createrJo, "nickname"));
		startTime = JSONUtil.getLong(headJo, "start");
		JSONArray headJsa = JSONUtil.getJSONArray(headJo, "members");
		headlayoutV.removeAllViews();
		PicLayoutUtil headUtil = new PicLayoutUtil(self, headJsa, 5,
				headlayoutV, headlayoutWidth);
		headUtil.setHeadMaxCount(6);
		headUtil.AddChild();

	}

	private void activeRelative(JSONObject jo) {
		int isOrganizer = JSONUtil.getInt(jo, "isOrganizer");
		int isMember = JSONUtil.getInt(jo, "isMember");
		if (isOrganizer == 1) {
			status = "管理";
			isJoin = true;
		} else {
			if (isMember == 1) {
				status = "已加入";
				isJoin = true;
			} else if (isMember == 0) {
				status = "我要去玩";
				isJoin = false;
			} else {
				status = "申请中";
				isJoin = false;
			}
		}
	}

	public JSONObject getShareContent(JSONObject jo) {
		JSONArray picJsa = JSONUtil.getJSONArray(jo, "cover");
		JSONObject picjo;
		JSONObject shareJo = new JSONObject();
		try {
			picjo = picJsa.getJSONObject(0);
			String imgUrl = JSONUtil.getString(picjo, "thumbnail_pic");

			JSONObject userJo = JSONUtil.getJSONObject(jo, "organizer");
			String shareTitle = JSONUtil.getString(userJo, "nickname")
					+ "邀请您参加" + JSONUtil.getString(jo, "introduction") + "活动";
			long time = JSONUtil.getLong(jo, "start");
			String startTime = CarPlayValueFix.getStandardTime(time, "MM月dd日 ");
			String shareContent = "开始时间: " + startTime + "\n目的地: "
					+ JSONUtil.getString(jo, "location") + "\n费用: "
					+ JSONUtil.getString(jo, "pay");
			String shareUrl = API.share + "share.html?code="
					+ JSONUtil.getString(jo, "activityId");

			shareJo.put("imgUrl", imgUrl);
			shareJo.put("shareTitle", shareTitle);
			shareJo.put("shareContent", shareContent);
			shareJo.put("shareUrl", shareUrl);
			shareJo.put("activityId", JSONUtil.getString(jo, "activityId"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shareJo;
	}

	@Override
	public void onClick(View arg0) {
		Intent it;
		switch (arg0.getId()) {
		case R.id.headlayout:
			if (status.equals("管理")) {
				JSONObject shareJo = getShareContent(headJo);
				it = new Intent(self, MyActiveMembersManageActivity.class);
				it.putExtra("activityId", activityId);
				it.putExtra("shareContent", shareJo.toString());
				it.putExtra("isJoin", isJoin);
				startActivity(it);
			} else {
				it = new Intent(self, ActiveMembersActivity.class);
				it.putExtra("activityId", activityId);
				it.putExtra("startTime", startTime);
				it.putExtra("isJoin", isJoin);
				startActivity(it);
			}
			break;
		case R.id.acitvity_layout_head:
			if (id != 0) {
				it = new Intent(self, MyPerSonDetailActivity.class);
				it.putExtra("userId", userid);
				startActivity(it);
			} else {
				it = new Intent(self, PersonDetailActivity.class);
				it.putExtra("userId", userid);
				startActivity(it);
			}
			break;
		case R.id.activity_edit:
//			if (id != 0) {
//				it = new Intent(self, ActiveDescriptionActivity.class);
//				it.putExtra("intr", activity_Introduction.getText().toString());
//				startActivityForResult(it, REQUEST_DESCRIPTION);
//			}
			break;

		case R.id.quite:
			showProgressDialog("退出中...");
			exitGrop();
			break;

		default:
			break;
		}

	}

	private void exitGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Intent it1 = getIntent();
					EMGroupManager.getInstance().exitFromGroup(
							it1.getStringExtra("groupId"));
					runOnUiThread(new Runnable() {
						public void run() {
							hidenProgressDialog();
							setResult(RESULT_OK);
							finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							hidenProgressDialog();
							showToast(getResources().getString(
									R.string.Exit_the_group_chat_failure)
									+ " " + e.getMessage());
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_DESCRIPTION:
				activity_Introduction.setText(data.getStringExtra("des"));
				break;

			default:
				break;
			}
		}

	}

}
