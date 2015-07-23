package com.gongpingjia.carplay.activity.my;

import java.io.File;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

public class MyFragment extends Fragment implements OnClickListener {
	public static final int LOGINCODE = 1;
	View mainV;

	NetRefreshAndMoreListView listV;

	ActiveAdapter adapter;

	static MyFragment instance;

	/** 我的关注,我的发布,我的参与 三个点击区域的View */
	View my_attentionV, my_releaseV, my_participationV;

	LinearLayout carchat, owners_certification, people_concerned;

	User user;

	/** 已登录,未登录 */
	LinearLayout loginedLl, notloginLl;
	Button loginBtn;

	/** 头像,车logo */
	ImageView headI, carBrandLogoI;
	/** 昵称,年龄,车型+车龄 */
	TextView nicknameT, ageT, carModelT;
	/** 性别 */
	RelativeLayout genderR;

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

		headI = (ImageView) mainV.findViewById(R.id.head);
		carBrandLogoI = (ImageView) mainV.findViewById(R.id.carBrandLogo);
		nicknameT = (TextView) mainV.findViewById(R.id.nickname);
		ageT = (TextView) mainV.findViewById(R.id.age);
		carModelT = (TextView) mainV.findViewById(R.id.carModel);
		genderR = (RelativeLayout) mainV.findViewById(R.id.gender);

		if (TextUtils.isEmpty(user.getUserId())) {
			loginedLl.setVisibility(View.GONE);
			notloginLl.setVisibility(View.VISIBLE);

			// 已登录
		} else {
			loginedLl.setVisibility(View.VISIBLE);
			notloginLl.setVisibility(View.GONE);
			getMyDetails();
		}

		loginBtn = (Button) mainV.findViewById(R.id.login);
		my_attentionV = mainV.findViewById(R.id.my_attention);
		my_releaseV = mainV.findViewById(R.id.my_release);
		my_participationV = mainV.findViewById(R.id.my_participation);
		carchat = (LinearLayout) mainV.findViewById(R.id.carchat);
		people_concerned = (LinearLayout) mainV
				.findViewById(R.id.people_concerned);
		owners_certification = (LinearLayout) mainV
				.findViewById(R.id.owners_certification);

		loginBtn.setOnClickListener(this);
		my_attentionV.setOnClickListener(this);
		my_releaseV.setOnClickListener(this);
		my_participationV.setOnClickListener(this);
		carchat.setOnClickListener(this);
		people_concerned.setOnClickListener(this);
		owners_certification.setOnClickListener(this);
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
				
				nicknameT.setText(nickname);
				ageT.setText(age);

				ViewUtil.bindNetImage(headI, photo, CarPlayValueFix.optionsDefault.toString());
				
				if (TextUtils.isEmpty(carModel)) {
					carModelT.setText("带我飞~");
				}else{
					carModelT.setText(carModel + "  " + drivingExperience + "年");
				}
				


				if (TextUtils.isEmpty(carBrandLogo)
						|| carBrandLogo.equals("null")) {
					carBrandLogoI.setVisibility(View.INVISIBLE);
				} else {
					Bitmap carlogo = PhotoUtil.getLocalImage(new File(
							carBrandLogo));
					carBrandLogoI.setImageBitmap(ImageUtil.toRoundCorner(
							carlogo, 1000));
				}

				if (gender.equals("男"))
					genderR.setBackgroundResource(R.drawable.man);
				else
					genderR.setBackgroundResource(R.drawable.woman);

			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent it;

		switch (v.getId()) {
		case R.id.my_attention:
			it = new Intent(getActivity(), MyAttentionActiveActivity.class);
			startActivity(it);
			break;

		case R.id.my_release:
			it = new Intent(getActivity(), MyReleaseActiveActivity.class);
			startActivity(it);
			break;

		case R.id.my_participation:
			it = new Intent(getActivity(), MyParticipationActiveActivity.class);
			startActivity(it);
			break;
		case R.id.carchat:
			it = new Intent(getActivity(), PlayCarChatActivity.class);
			startActivity(it);
			break;
		case R.id.people_concerned:
			it = new Intent(getActivity(), AttentionPersonActivity.class);
			startActivity(it);
			break;
		case R.id.owners_certification:
			it = new Intent(getActivity(), AuthenticateOwnersActivity.class);
			startActivity(it);
			break;
		case R.id.login:
			it = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(it, LOGINCODE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == LOGINCODE) {
				loginedLl.setVisibility(View.VISIBLE);
				notloginLl.setVisibility(View.GONE);
				getMyDetails();
			}
		}
	}

}