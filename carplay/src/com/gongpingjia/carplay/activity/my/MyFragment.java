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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;

import de.greenrobot.event.EventBus;

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
	RoundImageView headI, carBrandLogoI, carlogo;

	/** 昵称,年龄,车型+车龄,是非认证 */
	TextView nicknameT, ageT, carModelT, attestation_txt;

	/** 性别 */
	RelativeLayout genderR;

	/** 发布数,关注数,参与数 */
	TextView postNumberT, subscribeNumberT, joinNumberT;

	ImageView unLogheadI;

	DotLinLayout dotLinLayout;

	CarPlayGallery gallery;

	Timer galleryTimer;

	int currentPosition = 200;

	int galleryCount = 0;

	boolean isfirst;
	
	//是否认证车主成功 (默认0:未成功) 
	int isAuthenticated=0;
	int drivingyears=0;
	String carModel="";
	String license="";
	
	

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
		EventBus.getDefault().register(this);
		initView();
		return mainV;
	}

	private void initView() {
		isfirst = true;
		user = User.getInstance();
		loginedLl = (LinearLayout) mainV.findViewById(R.id.logined);
		notloginLl = (LinearLayout) mainV.findViewById(R.id.notlogin);
		loginBtn = (Button) mainV.findViewById(R.id.login);
		headI = (RoundImageView) mainV.findViewById(R.id.head);
		carBrandLogoI = (RoundImageView) mainV.findViewById(R.id.carBrandLogo);
		carlogo = (RoundImageView) mainV.findViewById(R.id.carlogo);
		nicknameT = (TextView) mainV.findViewById(R.id.nickname);
		ageT = (TextView) mainV.findViewById(R.id.age);
		carModelT = (TextView) mainV.findViewById(R.id.carModel);
		genderR = (RelativeLayout) mainV.findViewById(R.id.gender);
		attestation_txt = (TextView) mainV.findViewById(R.id.attestation_txt);
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
		unLogheadI = (ImageView) mainV.findViewById(R.id.notlogin_head);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(it, LOGINCODE);
			}
		});
		unLogheadI.setOnClickListener(this);
		my_attentionV.setOnClickListener(this);
		my_releaseV.setOnClickListener(this);
		my_participationV.setOnClickListener(this);
		carchat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(getActivity(), PlayCarChatActivity.class);
				startActivity(it);

			}
		});
		headI.setOnClickListener(this);
		people_concerned.setOnClickListener(this);
		owners_certification.setOnClickListener(this);
		editdata.setOnClickListener(this);
		feedback_layoutV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(getActivity(), FeedBackActivity.class);
				startActivity(it);
			}
		});

		gallery = (CarPlayGallery) mainV.findViewById(R.id.gallery);
		dotLinLayout = (DotLinLayout) mainV.findViewById(R.id.dots);
		dotLinLayout.setDotImage(R.drawable.dot_n, R.drawable.dot_f);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(getActivity(), ManageAlbumActivity.class);
				startActivity(it);
			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {

				if (position >= galleryCount) {
					if (galleryCount != 0) {
						position = position % galleryCount;
					}
				}
				dotLinLayout.setCurrentFocus(position);
				// ImageView img = (ImageView) view.findViewById(R.id.pic);
				// img.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				// R.anim.home_res_video_gallery_in));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// 未登录
		if (TextUtils.isEmpty(user.getUserId())) {
			loginedLl.setVisibility(View.GONE);
			notloginLl.setVisibility(View.VISIBLE);
			postNumberT.setText("0");
			subscribeNumberT.setText("0");
			joinNumberT.setText("0");
			isShowDefaultBg(true);
		}
	}

	/** 获取个人资料 */
	private void getMyDetails() {
		if (isfirst) {
			MainActivity activity = (MainActivity) getActivity();
			activity.showProgressDialog("加载中");
		}
		
		DhNet verifyNet = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/authentication?token="
				+ user.getToken());
		verifyNet.doGet(new NetTask(getActivity()) {
			
			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					isAuthenticated = JSONUtil.getInt(jo, "isAuthenticated");
					drivingyears=JSONUtil.getInt(jo, "drivingExperience");
					carModel=JSONUtil.getString(jo, "carModel");
					license=JSONUtil.getString(jo, "licensePhoto");
				}
			}
		});
		
		
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/info?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.doGet(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (isfirst) {
					MainActivity activity = (MainActivity) getActivity();
					activity.hidenProgressDialog();
				}
				if (response.isSuccess()) {
					isfirst = false;
					JSONObject jo = response.jSONFromData();

					String nickname = JSONUtil.getString(jo, "nickname");
					String age = JSONUtil.getString(jo, "age");
					String carModel = JSONUtil.getString(jo, "carModel");
					String drivingExperience = JSONUtil.getString(jo,
							"drivingExperience");
					String photo = JSONUtil.getString(jo, "photo");
					String carBrandLogo = JSONUtil
							.getString(jo, "carBrandLogo");
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
						carModelT.setText(carModel + ",  " + drivingExperience
								+ "年车龄");
					}

					if (TextUtils.isEmpty(carBrandLogo)
							|| carBrandLogo.equals("null")) {
						carBrandLogoI.setVisibility(View.GONE);
					} else {
						carBrandLogoI.setVisibility(View.VISIBLE);
						ViewUtil.bindNetImage(carBrandLogoI, carBrandLogo,
								CarPlayValueFix.optionsDefault.toString());
					}
					
					switch (isAuthenticated) {
					//未认证
					case 0:
						attestation_txt.setVisibility(View.GONE);
						carlogo.setVisibility(View.GONE);
						break;
					//已认证
					case 1:
						carlogo.setVisibility(View.VISIBLE);
						attestation_txt.setVisibility(View.VISIBLE);
						attestation_txt.setText("已认证");
						ViewUtil.bindNetImage(carlogo, carBrandLogo,
								CarPlayValueFix.optionsDefault.toString());
						break;
					//认证中
					case 2:
						attestation_txt.setVisibility(View.VISIBLE);
						carlogo.setVisibility(View.GONE);
						attestation_txt.setText("认证中");
						break;

					default:
						break;
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
		if (jsa.length() > 1) {
			gallery.setSelection(200);
			currentPosition = 200;
		}

		if (jsa.length() != 0) {
			isShowDefaultBg(false);
		} else {
			isShowDefaultBg(true);
		}

	}

	public void isShowDefaultBg(boolean flag) {
		if (!flag) {
			unLogheadI.setVisibility(View.GONE);
			gallery.setVisibility(View.VISIBLE);
			dotLinLayout.setVisibility(View.VISIBLE);
		} else {
			unLogheadI.setVisibility(View.VISIBLE);
			gallery.setVisibility(View.GONE);
			dotLinLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(final View v) {
		UserInfoManage.getInstance().checkLogin(getActivity(),
				new LoginCallBack() {

					@Override
					public void onisLogin() {
						Intent it;
						switch (v.getId()) {

						case R.id.head:
							it = new Intent(getActivity(),
									EditPersonalInfoActivity.class);
							startActivity(it);
							break;
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
						case R.id.people_concerned:
							it = new Intent(getActivity(),
									AttentionPersonActivity.class);
							startActivity(it);
							break;
						case R.id.owners_certification:
							//已认证 则不跳转
							if (isAuthenticated!=1) {
								it = new Intent(getActivity(),
										AuthenticateOwnersActivity.class);
								it.putExtra("type", "my");
								it.putExtra("isAuthenticated", isAuthenticated);
								it.putExtra("drivingyears", drivingyears);
								it.putExtra("carModel", carModel);
								it.putExtra("license", license);
								startActivity(it);
							}
							break;

						case R.id.editdata:
							it = new Intent(getActivity(),
									EditPersonalInfoActivity.class);
							startActivity(it);
							break;
						case R.id.notlogin_head:
							it = new Intent(getActivity(),
									ManageAlbumActivity.class);
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

	public void onEventMainThread(LoginEB login) {
		if (login.islogin) {
			loginedLl.setVisibility(View.VISIBLE);
			notloginLl.setVisibility(View.GONE);
			isfirst = true;
			getMyDetails();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 已登录
		if (user.isLogin()) {
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
		}, 3 * 1000, 10 * 1000);
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

		EventBus.getDefault().unregister(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		};
	};

}