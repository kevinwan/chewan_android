package com.gongpingjia.carplay.activity.my;

import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.DotLinLayout;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;

/**
 * 我的详情
 * 
 * @author Administrator
 * 
 */
public class MyPerSonDetailActivity extends CarPlayBaseActivity implements
		OnClickListener {
	public static final int LOGINCODE = 1;

	NetRefreshAndMoreListView listV;

	ActiveAdapter adapter;

	/** 我的关注,我的发布,我的参与 三个点击区域的View */
	View my_attentionV, my_releaseV, my_participationV;

	LinearLayout carchat, owners_certification, people_concerned, editdata,
			feedback_layoutV;

	User user;

	/** 已登录,未登录 */
	LinearLayout loginedLl, notloginLl;

	Button loginBtn;

	/** 头像,车logo */
	RoundImageView headI, carBrandLogoI;

	/** 昵称,年龄,车型+车龄 */
	TextView nicknameT, ageT, carModelT;

	/** 性别 */
	RelativeLayout genderR;

	/** 发布数,关注数,参与数 */
	TextView postNumberT, subscribeNumberT, joinNumberT;

	DotLinLayout dotLinLayout;

	CarPlayGallery gallery;

	Timer mTimer;

	int currentPosition = 200;

	int galleryCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myperson_detail_activity);
	}

	@Override
	public void initView() {

		setTitle("我的详情");
		loginedLl = (LinearLayout) findViewById(R.id.logined);
		notloginLl = (LinearLayout) findViewById(R.id.notlogin);
		loginBtn = (Button) findViewById(R.id.login);
		headI = (RoundImageView) findViewById(R.id.head);
		carBrandLogoI = (RoundImageView) findViewById(R.id.carBrandLogo);
		nicknameT = (TextView) findViewById(R.id.nickname);
		ageT = (TextView) findViewById(R.id.age);
		carModelT = (TextView) findViewById(R.id.carModel);
		genderR = (RelativeLayout) findViewById(R.id.gender);

		postNumberT = (TextView) findViewById(R.id.postNumber);
		subscribeNumberT = (TextView) findViewById(R.id.subscribeNumber);
		joinNumberT = (TextView) findViewById(R.id.joinNumber);

		my_attentionV = findViewById(R.id.my_attention);
		my_releaseV = findViewById(R.id.my_release);
		my_participationV = findViewById(R.id.my_participation);
		carchat = (LinearLayout) findViewById(R.id.carchat);
		people_concerned = (LinearLayout) findViewById(R.id.people_concerned);
		owners_certification = (LinearLayout) findViewById(R.id.owners_certification);
		editdata = (LinearLayout) findViewById(R.id.editdata);
		feedback_layoutV = (LinearLayout) findViewById(R.id.feedback_layout);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(self, LoginActivity.class);
				startActivityForResult(it, LOGINCODE);
			}
		});
		my_attentionV.setOnClickListener(this);
		my_releaseV.setOnClickListener(this);
		my_participationV.setOnClickListener(this);
		carchat.setOnClickListener(this);
		people_concerned.setOnClickListener(this);
		owners_certification.setOnClickListener(this);
		editdata.setOnClickListener(this);
		feedback_layoutV.setOnClickListener(this);
		user = User.getInstance();
		loginedLl.setVisibility(View.VISIBLE);
		notloginLl.setVisibility(View.GONE);

		gallery = (CarPlayGallery) findViewById(R.id.gallery);
		dotLinLayout = (DotLinLayout) findViewById(R.id.dots);
		dotLinLayout.setDotImage(R.drawable.dot_n, R.drawable.dot_f);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position >= galleryCount) {
					position = position % galleryCount;
				}
				dotLinLayout.setCurrentFocus(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		getMyDetails();
		// 未登录
	}

	@Override
	public void onResume() {
		super.onResume();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 0, 10 * 1000);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	/** 获取个人资料 */
	private void getMyDetails() {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/info?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				JSONObject jo = response.jSONFromData();

				String nickname = JSONUtil.getString(jo, "nickname");
				String age = JSONUtil.getString(jo, "age");
				String carModel = JSONUtil.getString(jo, "carModel");
				String drivingExperience = JSONUtil.getString(jo,
						"drivingExperience");
				String photo = JSONUtil.getString(jo, "photo");
				String carBrandLogo = JSONUtil.getString(jo, "carBrandLogo");
				String gender = JSONUtil.getString(jo, "gender");
				String postNumber = JSONUtil.getString(jo, "postNumber");
				String subscribeNumber = JSONUtil.getString(jo,
						"subscribeNumber");
				String joinNumber = JSONUtil.getString(jo, "joinNumber");

				nicknameT.setText(nickname);
				ageT.setText(age);

				ViewUtil.bindNetImage(headI, photo, "head");

				if (TextUtils.isEmpty(carModel)) {
					carModelT.setText("带我飞~");
				} else {
					carModelT
							.setText(carModel + "  " + drivingExperience + "年");
				}

				if (TextUtils.isEmpty(carBrandLogo)
						|| carBrandLogo.equals("null")) {
					carBrandLogoI.setVisibility(View.GONE);
				} else {
					carBrandLogoI.setVisibility(View.VISIBLE);
					ViewUtil.bindNetImage(carBrandLogoI, carBrandLogo,
							"carlogo");
				}

				if (gender.equals("男"))
					genderR.setBackgroundResource(R.drawable.man);
				else
					genderR.setBackgroundResource(R.drawable.woman);

				postNumberT.setText(postNumber);
				subscribeNumberT.setText(subscribeNumber);
				joinNumberT.setText(joinNumber);
				JSONArray albumPhotosJsa = JSONUtil.getJSONArray(jo,
						"albumPhotos");
				bingGallery(albumPhotosJsa);
			}
		});

	}

	private void bingGallery(JSONArray jsa) {
		galleryCount = jsa.length();
		if (galleryCount > 0) {
			dotLinLayout.setDotCount(galleryCount);
			dotLinLayout.setCurrentFocus(galleryCount / 2);
			gallery.setSelection(galleryCount / 2);
		}
		GalleryAdapter adapter = new GalleryAdapter(self, jsa);
		gallery.setAdapter(adapter);
		gallery.setSelection(200);
		currentPosition = 200;
	}

	@Override
	public void onClick(final View v) {
		UserInfoManage.getInstance().checkLogin(self, new LoginCallBack() {

			@Override
			public void onisLogin() {
				Intent it;
				switch (v.getId()) {

				// case R.id.login:
				// it = new Intent(self, LoginActivity.class);
				// startActivityForResult(it, LOGINCODE);
				// break;

				case R.id.my_attention:
					it = new Intent(self, MyAttentionActiveActivity.class);
					startActivity(it);
					break;

				case R.id.my_release:
					it = new Intent(self, MyReleaseActiveActivity.class);
					startActivity(it);
					break;

				case R.id.my_participation:
					it = new Intent(self, MyParticipationActiveActivity.class);
					startActivity(it);
					break;
				case R.id.carchat:
					it = new Intent(self, PlayCarChatActivity.class);
					startActivity(it);
					break;
				case R.id.people_concerned:
					it = new Intent(self, AttentionPersonActivity.class);
					startActivity(it);
					break;
				case R.id.owners_certification:
					it = new Intent(self, AuthenticateOwnersActivity.class);
					startActivity(it);
					break;

				case R.id.feedback_layout:
					it = new Intent(self, FeedBackActivity.class);
					startActivity(it);
					break;
				case R.id.editdata:
					it = new Intent(self, EditPersonalInfoActivity.class);
					startActivity(it);

					break;
				default:
					break;
				}

			}

			@Override
			public void onLoginFail() {

			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			gallery.setSelection(currentPosition);
		};
	};

}
