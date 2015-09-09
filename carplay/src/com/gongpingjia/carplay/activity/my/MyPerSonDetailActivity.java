package com.gongpingjia.carplay.activity.my;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.DotLinLayout;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
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
	RoundImageView headI, carBrandLogoI, person_carlogo;

	/** 昵称,年龄,车型+车龄 */
	TextView nicknameT, ageT, carModelT;

	/** 性别 */
	RelativeLayout genderR;

	/** 发布数,关注数,参与数 */
	TextView postNumberT, subscribeNumberT, joinNumberT;
	/** 是否已认证 */
	TextView person_txt;

	ImageView unLogheadI;

	DotLinLayout dotLinLayout;

	CarPlayGallery gallery;

	Timer mTimer;

	int currentPosition = 200;

	int galleryCount;

	File mCacheDir;

	String tempPath;

	// 是否认证车主成功 (默认0:未成功)
	int isAuthenticated = 0;
	int drivingyears = 0;
	String carModel = "";
	String license = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myperson_detail_activity);
	}

	@Override
	public void initView() {

		setTitle("我的详情");
		setRightAction(null, R.drawable.icon_camera, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated
				// method stub
				mCacheDir = new File(getExternalCacheDir(), "CarPlay");
				mCacheDir.mkdirs();
				tempPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO,
						Constant.PICK_PHOTO, new File(tempPath));

			}
		});
		loginedLl = (LinearLayout) findViewById(R.id.logined);
		notloginLl = (LinearLayout) findViewById(R.id.notlogin);
		loginBtn = (Button) findViewById(R.id.login);
		headI = (RoundImageView) findViewById(R.id.head);
		person_carlogo = (RoundImageView) findViewById(R.id.person_carlogo);
		carBrandLogoI = (RoundImageView) findViewById(R.id.carBrandLogo);
		nicknameT = (TextView) findViewById(R.id.nickname);
		ageT = (TextView) findViewById(R.id.age);
		carModelT = (TextView) findViewById(R.id.carModel);
		genderR = (RelativeLayout) findViewById(R.id.gender);
		person_txt = (TextView) findViewById(R.id.person_txt);
		postNumberT = (TextView) findViewById(R.id.postNumber);
		subscribeNumberT = (TextView) findViewById(R.id.subscribeNumber);
		joinNumberT = (TextView) findViewById(R.id.joinNumber);
		unLogheadI = (ImageView) findViewById(R.id.notlogin_head);

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
		headI.setOnClickListener(this);
		my_attentionV.setOnClickListener(this);
		my_releaseV.setOnClickListener(this);
		my_participationV.setOnClickListener(this);
		carchat.setOnClickListener(this);
		people_concerned.setOnClickListener(this);
		owners_certification.setOnClickListener(this);
		editdata.setOnClickListener(this);
		unLogheadI.setOnClickListener(this);
		feedback_layoutV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(self, FeedBackActivity.class);
				startActivity(it);
			}
		});
		user = User.getInstance();
		loginedLl.setVisibility(View.VISIBLE);
		notloginLl.setVisibility(View.GONE);

		gallery = (CarPlayGallery) findViewById(R.id.gallery);
		dotLinLayout = (DotLinLayout) findViewById(R.id.dots);
		dotLinLayout.setDotImage(R.drawable.dot_n, R.drawable.dot_f);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(self, ManageAlbumActivity.class);
				startActivity(it);
			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position >= galleryCount) {
					if (galleryCount != 0) {
						position = position % galleryCount;
					}
				}
				dotLinLayout.setCurrentFocus(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// 未登录
	}

	@Override
	public void onResume() {
		super.onResume();
		getMyDetails();
		
		if (mTimer != null) {
			mTimer.cancel();
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 3 * 1000, 10 * 1000);
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
		DhNet verifyNet = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/authentication?token="
				+ user.getToken());
		verifyNet.doGet(new NetTask(self) {
			
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
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
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
						carModelT.setText(carModel + "  " + drivingExperience
								+ "年");
					}

					if (TextUtils.isEmpty(carBrandLogo)
							|| carBrandLogo.equals("null")) {
						carBrandLogoI.setVisibility(View.GONE);
					} else {
						carBrandLogoI.setVisibility(View.VISIBLE);
						ViewUtil.bindNetImage(carBrandLogoI, carBrandLogo,
								"carlogo");
					}
					
					switch (isAuthenticated) {
					//未认证
					case 0:
						person_txt.setVisibility(View.GONE);
						person_carlogo.setVisibility(View.GONE);
						break;
					//已认证
					case 1:
						person_carlogo.setVisibility(View.VISIBLE);
						person_txt.setVisibility(View.VISIBLE);
						person_txt.setText("已认证");
						ViewUtil.bindNetImage(person_carlogo, carBrandLogo,
								CarPlayValueFix.optionsDefault.toString());
						findViewById(R.id.icon).setVisibility(
								View.GONE);
						break;
					//认证中
					case 2:
						person_txt.setVisibility(View.VISIBLE);
						person_carlogo.setVisibility(View.GONE);
						person_txt.setText("认证中");
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
		GalleryAdapter adapter = new GalleryAdapter(self, jsa);
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
		UserInfoManage.getInstance().checkLogin(self, new LoginCallBack() {

			@Override
			public void onisLogin() {
				Intent it;
				switch (v.getId()) {
				case R.id.head:
					it = new Intent(self, EditPersonalInfoActivity.class);
					startActivity(it);
					break;
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
					//已认证 则不跳转
					if (isAuthenticated!=1) {
						it = new Intent(self, AuthenticateOwnersActivity.class);
						it.putExtra("type", "my");
						it.putExtra("isAuthenticated", isAuthenticated);
						it.putExtra("drivingyears", drivingyears);
						it.putExtra("carModel", carModel);
						it.putExtra("license", license);
						startActivity(it);
					}
					break;

				case R.id.editdata:
					it = new Intent(self, EditPersonalInfoActivity.class);
					startActivity(it);

					break;
				case R.id.notlogin_head:
					it = new Intent(self, ManageAlbumActivity.class);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.TAKE_PHOTO:
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				String path = PhotoUtil.onPhotoFromCamera(self,
						Constant.ZOOM_PIC, tempPath, 3, 2, 1000, newPath);
				tempPath = path;
				break;
			case Constant.PICK_PHOTO:
				PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, tempPath,
						data, 3, 2, 1000);
				break;
			case Constant.ZOOM_PIC:
				upLoadPic(tempPath);
				break;

			// case Constant.PICK_PHOTO:
			// Bitmap btp = PhotoUtil.checkImage(self, data);
			// PhotoUtil.saveLocalImage(btp, new File(mCurPath));
			// btp.recycle();
			// upLoadPic(mCurPath);
			// break;
			// case Constant.TAKE_PHOTO:
			// Bitmap btp1 = PhotoUtil.getLocalImage(new File(mCurPath));
			// String newPath = new File(mCacheDir, System.currentTimeMillis()
			// + ".jpg").getAbsolutePath();
			// int degree = PhotoUtil.getBitmapDegree(mCurPath);
			// PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
			// btp1.recycle();
			// upLoadPic(newPath);
			// break;
			}
		}
	}

	private void upLoadPic(String path) {
		User user = User.getInstance();
		DhNet net = new DhNet(API.uploadAlbum + user.getUserId()
				+ "/album/upload?token=" + user.getToken());
		Log.e("url", net.getUrl());
		net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					IocContainer.getShare().get(IDialog.class)
							.showToastShort(self, "图片上传成功!");
					Intent it = new Intent(self, ManageAlbumActivity.class);
					it.putExtra("tempPath", tempPath);
					startActivity(it);
				}
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			// gallery.setSelection(currentPosition);
			gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		};
	};

}
