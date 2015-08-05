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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;

public class MyFragment extends Fragment implements OnClickListener {
	public static final int LOGINCODE = 1;

	View mainV;

	NetRefreshAndMoreListView listV;

	ActiveAdapter adapter;

	static MyFragment instance;

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

	Timer galleryTimer;

	int currentPosition = 200;

	int galleryCount = 0;

	public static MyFragment getInstance() {
		if (instance == null) {
			instance = new MyFragment();
		}

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainV = inflater.inflate(R.layout.fragment_my, null);
		initView();
		return mainV;
	}

	private void initView() {
		user = User.getInstance();
		loginedLl = (LinearLayout) mainV.findViewById(R.id.logined);
		notloginLl = (LinearLayout) mainV.findViewById(R.id.notlogin);
		loginBtn = (Button) mainV.findViewById(R.id.login);
		headI = (RoundImageView) mainV.findViewById(R.id.head);
		carBrandLogoI = (RoundImageView) mainV.findViewById(R.id.carBrandLogo);
		nicknameT = (TextView) mainV.findViewById(R.id.nickname);
		ageT = (TextView) mainV.findViewById(R.id.age);
		carModelT = (TextView) mainV.findViewById(R.id.carModel);
		genderR = (RelativeLayout) mainV.findViewById(R.id.gender);

		postNumberT = (TextView) mainV.findViewById(R.id.postNumber);
		subscribeNumberT = (TextView) mainV.findViewById(R.id.subscribeNumber);
		joinNumberT = (TextView) mainV.findViewById(R.id.joinNumber);

		my_attentionV = mainV.findViewById(R.id.my_attention);
		my_releaseV = mainV.findViewById(R.id.my_release);
		my_participationV = mainV.findViewById(R.id.my_participation);
		carchat = (LinearLayout) mainV.findViewById(R.id.carchat);
		people_concerned = (LinearLayout) mainV
				.findViewById(R.id.people_concerned);
		owners_certification = (LinearLayout) mainV
				.findViewById(R.id.owners_certification);
		editdata = (LinearLayout) mainV.findViewById(R.id.editdata);
		feedback_layoutV = (LinearLayout) mainV
				.findViewById(R.id.feedback_layout);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(getActivity(), LoginActivity.class);
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

		// 未登录
		if (TextUtils.isEmpty(user.getUserId())) {
			loginedLl.setVisibility(View.GONE);
			notloginLl.setVisibility(View.VISIBLE);
			postNumberT.setText("0");
			subscribeNumberT.setText("0");
			joinNumberT.setText("0");
		}

		gallery = (CarPlayGallery) mainV.findViewById(R.id.gallery);
		dotLinLayout = (DotLinLayout) mainV.findViewById(R.id.dots);
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
	}

	/** 获取个人资料 */
	private void getMyDetails() {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/info?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.doGetInDialog(new NetTask(getActivity()) {

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

				ViewUtil.bindNetImage(headI, photo,
						CarPlayValueFix.optionsDefault.toString());

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
							CarPlayValueFix.optionsDefault.toString());
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
		GalleryAdapter adapter = new GalleryAdapter(getActivity(), jsa);
		gallery.setAdapter(adapter);
		gallery.setSelection(200);
		currentPosition = 200;
	}

	@Override
	public void onClick(final View v) {
		UserInfoManage.getInstance().checkLogin(getActivity(),
				new LoginCallBack() {

					@Override
					public void onisLogin() {
						Intent it;
						switch (v.getId()) {

						// case R.id.login:
						// it = new Intent(getActivity(), LoginActivity.class);
						// startActivityForResult(it, LOGINCODE);
						// break;

						case R.id.my_attention:
							it = new Intent(getActivity(),
									MyAttentionActiveActivity.class);
							startActivity(it);
							break;

						case R.id.my_release:
							it = new Intent(getActivity(),
									MyReleaseActiveActivity.class);
							startActivity(it);
							break;

						case R.id.my_participation:
							it = new Intent(getActivity(),
									MyParticipationActiveActivity.class);
							startActivity(it);
							break;
						case R.id.carchat:
							it = new Intent(getActivity(),
									PlayCarChatActivity.class);
							startActivity(it);
							break;
						case R.id.people_concerned:
							it = new Intent(getActivity(),
									AttentionPersonActivity.class);
							startActivity(it);
							break;
						case R.id.owners_certification:
							it = new Intent(getActivity(),
									AuthenticateOwnersActivity.class);
							startActivity(it);
							break;

						case R.id.feedback_layout:
							it = new Intent(getActivity(),
									FeedBackActivity.class);
							startActivity(it);
							break;
						case R.id.editdata:
							it = new Intent(getActivity(),
									EditPersonalInfoActivity.class);
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

	@Override
	public void onResume() {
		super.onResume();
		// 已登录
		if (!TextUtils.isEmpty(user.getUserId())) {
			loginedLl.setVisibility(View.VISIBLE);
			notloginLl.setVisibility(View.GONE);
			getMyDetails();
		}

		galleryTimer = new Timer();
		galleryTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 0, 10 * 1000);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (galleryTimer != null) {
			galleryTimer.cancel();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (galleryTimer != null) {
			galleryTimer.cancel();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			gallery.setSelection(currentPosition);
		};
	};

}