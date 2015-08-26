package com.gongpingjia.carplay.activity.my;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.data.CityDataManage;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.view.dialog.CityPickDialog;
import com.gongpingjia.carplay.view.dialog.CityPickDialog.OnPickResultListener;
import com.gongpingjia.carplay.view.dialog.DateDialog;
import com.gongpingjia.carplay.view.dialog.DateDialog.OnDateResultListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * 车玩基本信息
 * 
 * @author Administrator
 * 
 */

public class BasicMessageActivity extends CarPlayBaseActivity implements
		OnClickListener {

	/** 头像 */
	private ImageView headI = null;

	/** 昵称 */
	private EditText nicknameT = null;

	/** 性别 */
	private RadioGroup sexR = null;

	/** 选择年龄 */
	private TextView ageT = null;

	/** 选择城市 */
	private TextView cityT = null;

	/** 下一步 */
	private Button nextBtn = null;

	private String mPhotoPath;

	DateDialog dateDialog;

	CityPickDialog cityDialog;

	int mYear, mMonth, mDay;

	String mProvice, mCity, mDistrict;

	String photoUid;

	public static final int AuthenticateOwners = 1;

	// 图片缓存根目录
	private File mCacheDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_message);
	}

	@Override
	public void initView() {
		setTitle("注册");
		mCacheDir = new File(getExternalCacheDir(), "CarPlay");
		mCacheDir.mkdirs();
		dateDialog = new DateDialog();
		dateDialog.setOnDateResultListener(new OnDateResultListener() {

			@Override
			public void result(String date, long datetime, int year, int month,
					int day) {
				ageT.setText(year + "-" + setData(month) + "-" + setData(day));
				mYear = year;
				mMonth = month;
				mDay = day;
			}
		});

		cityDialog = new CityPickDialog(self);
		cityDialog.setOnPickResultListener(new OnPickResultListener() {

			@Override
			public void onResult(String provice, String city, String district) {
				if (provice.equals(city) && provice.equals(district)) {
					cityT.setText(provice);
				} else if (provice.equals(city)) {
					cityT.setText(provice + district);
				} else {
					cityT.setText(provice + city + district);
				}
				mProvice = provice;
				mCity = city;
				mDistrict = district;
			}
		});
		sexR = (RadioGroup) findViewById(R.id.tab);

		headI = (ImageView) findViewById(R.id.head);
		nicknameT = (EditText) findViewById(R.id.nickname);
		ageT = (TextView) findViewById(R.id.age);
		cityT = (TextView) findViewById(R.id.city);
		nextBtn = (Button) findViewById(R.id.next);

		headI.setOnClickListener(this);
		ageT.setOnClickListener(this);
		cityT.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		if (getIntent().getStringExtra("photoId") != null) {
			ImageLoader.getInstance().displayImage(
					getIntent().getStringExtra("photoUrl"), headI);
			nicknameT.setText(getIntent().getStringExtra("nickname"));
			photoUid = getIntent().getStringExtra("photoId");
		}
	}

	private void uploadHead(String path) {
		DhNet net = new DhNet(API.uploadHead);
		net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				hidenProgressDialog();
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					photoUid = JSONUtil.getString(jo, "photoId");
				} else {
					headI.setImageResource(R.drawable.head_icon);
					photoUid = "";
					showToast("上传失败,重新上传");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head:
			mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
					+ ".jpg").getAbsolutePath();
			PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
					new File(mPhotoPath));
			break;

		case R.id.age:
			dateDialog.show(self);
			break;

		case R.id.city:
			cityDialog.show();
			break;

		case R.id.next:
			nextStep();

			break;

		default:
			break;
		}
	}

	private void nextStep() {

		if (TextUtils.isEmpty(photoUid)) {
			showToast("请上传头像");
			return;
		}

		final String strnickname = nicknameT.getText().toString().trim();
		if (TextUtils.isEmpty(strnickname)) {
			showToast("昵称不能为空");
			return;
		}
		if (strnickname.length() > 8) {
			showToast("昵称不能大于8个字符");
			return;
		}
		final String strage = ageT.getText().toString();
		if (TextUtils.isEmpty(strage)) {
			showToast("请设置您的年龄");
			return;
		}
		Calendar a = Calendar.getInstance();
		if (mYear >= a.get(Calendar.YEAR)) {
			showToast("您选择的年龄小于0岁,请选择正确的年龄");
			return;
		}

		final String strcity = cityT.getText().toString();
		if (TextUtils.isEmpty(strcity)) {
			showToast("请设置您所在的城市");
			return;
		}
		Dialog();

		// Bundle bundle = new Bundle();
		// Bundle b = getIntent().getExtras();
		// if (b != null)
		// {
		// bundle.putString("phone", b.getString("phone"));
		// bundle.putString("code", b.getString("code"));
		// bundle.putString("pswd", b.getString("pswd"));
		// }
		// bundle.putString("nickname", strnickname);
		// bundle.putString("gender", sex);
		// bundle.putInt("birthYear", birthYear);
		// bundle.putInt("birthMonth", birthMonth);
		// bundle.putInt("birthday", birthday);
		// bundle.putString("province", province);
		// bundle.putString("city", city);
		// bundle.putString("district", district);
		// bundle.putString("photo", photo);
		//
		// Intent it = new Intent(self, AuthenticateOwnersActivity.class);
		// it.putExtra("data", bundle);
		// startActivity(it);
	}

	private void register() {
		Intent it = getIntent();
		String gender = sexR.getCheckedRadioButtonId() == R.id.tab_left ? "男"
				: "女";
		DhNet net = new DhNet(API.register);

		if (it.getStringExtra("phone") != null) {
			net.addParam("phone", it.getStringExtra("phone"));
			net.addParam("code", it.getStringExtra("code"));
			net.addParam("password", it.getStringExtra("pswd"));
		} else {
			net.addParam("snsUid", it.getStringExtra("uid"));
			net.addParam("snsUserName", it.getStringExtra("nickname"));
			net.addParam("snsChannel", it.getStringExtra("channel"));
		}
		net.addParam("nickname", nicknameT.getText().toString());
		net.addParam("gender", gender);
		net.addParam("birthYear", mYear);
		net.addParam("birthMonth", mMonth);
		net.addParam("birthDay", mDay);
		net.addParam("province", mProvice);
		net.addParam("city", mCity);
		net.addParam("district", mDistrict);
		net.addParam("photo", photoUid);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					Intent oldIntent = getIntent();
					if (oldIntent.getStringExtra("phone") != null) {
						loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
								"userId")), oldIntent.getStringExtra("pswd"),
								jo);
					} else {
						loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
								"userId")), MD5Util.string2MD5(oldIntent
								.getStringExtra("uid")
								+ oldIntent.getStringExtra("channel")
								+ "com.gongpingjia.carplay"), jo);
					}
					// 登录环信

					showToast("注册成功!");

					CarPlayPerference per = IocContainer.getShare().get(
							CarPlayPerference.class);
					per.load();
					Intent it1 = getIntent();
					per.thirdId = it1.getStringExtra("uid");
					per.channel = it1.getStringExtra("channel");
					per.commit();
					Intent it = new Intent(self,
							AuthenticateOwnersActivity.class);
					startActivityForResult(it, AuthenticateOwners);
					setResult(Activity.RESULT_OK, it1);
				}
			}
		});
	}

	private void Dialog() {

		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setCancelable(false);
		dlg.show();

		Window window = dlg.getWindow();
		window.setContentView(R.layout.basicmessage_dialog);

		Button yes = (Button) window.findViewById(R.id.yes);
		yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				register();
			}

		});
		Button cancel = (Button) window.findViewById(R.id.again);

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				dlg.dismiss();

			}

		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.TAKE_PHOTO:
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				String path = PhotoUtil.onPhotoFromCamera(self,
						Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000, newPath);
				mPhotoPath = path;
				break;
			case Constant.PICK_PHOTO:
				PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
						data, 1, 1, 1000);
				break;
			case Constant.ZOOM_PIC:
				Bitmap bmp = PhotoUtil.getLocalImage(new File(mPhotoPath));
				headI.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
				showProgressDialog("上传头像中...");
				uploadHead(mPhotoPath);
				break;

			case AuthenticateOwners:
				Intent it = getIntent();
				setResult(Activity.RESULT_OK, it);
				finish();
				break;
			}
		}
	}

	private void loginHX(String currentUsername, String currentPassword,
			final JSONObject jo) {
		EMChatManager.getInstance().login(currentUsername, currentPassword,
				new EMCallBack() {

					@Override
					public void onSuccess() {

						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							// 处理好友和群组
							initializeContacts();
							User user = User.getInstance();
							user.setToken(JSONUtil.getString(jo, "token"));
							user.setUserId(JSONUtil.getString(jo, "userId"));
							user.setNickName(nicknameT.getText().toString());
							user.setHeadUrl(JSONUtil.getString(jo, "photo"));
							user.setLogin(true);

							LoginEB loginEB = new LoginEB();
							loginEB.setIslogin(true);
							EventBus.getDefault().post(loginEB);
							LoginActivity.asyncFetchGroupsFromServer();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面
							runOnUiThread(new Runnable() {
								public void run() {
									DemoHXSDKHelper.getInstance().logout(true,
											null);
									Toast.makeText(getApplicationContext(),
											R.string.login_failure_failed, 1)
											.show();
								}
							});
							return;
						}
						// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						boolean updatenick = EMChatManager.getInstance()
								.updateCurrentUserNick(
										User.getInstance().getNickName());
						if (!updatenick) {
							Log.e("LoginActivity",
									"update current user nick fail");
						}
						// if (!LoginActivity.this.isFinishing() &&
						// pd.isShowing()) {
						// pd.dismiss();
						// }
						// 进入主页面

					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
						// if (!progressShow) {
						// return;
						// }
						runOnUiThread(new Runnable() {
							public void run() {
								// pd.dismiss();
								if (code == ERROR_EXCEPTION_INVALID_PASSWORD_USERNAME) {
									showToast("用户名或者密码错误!");
								} else {
									showToast("登录失败:" + message);
								}

							}
						});
					}
				});

	}

	private void initializeContacts() {
		Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
		// 添加user"申请与通知"
		ChatUser newFriends = new ChatUser();
		newFriends
				.setUsername(com.gongpingjia.carplay.chat.Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(
				com.gongpingjia.carplay.chat.Constant.NEW_FRIENDS_USERNAME,
				newFriends);
		// 添加"群聊"
		ChatUser groupUser = new ChatUser();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser
				.setUsername(com.gongpingjia.carplay.chat.Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(com.gongpingjia.carplay.chat.Constant.GROUP_USERNAME,
				groupUser);

		// 添加"Robot"
		ChatUser robotUser = new ChatUser();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(com.gongpingjia.carplay.chat.Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(com.gongpingjia.carplay.chat.Constant.CHAT_ROBOT,
				robotUser);

		// 存入内存
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(self);
		List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
		dao.saveContactList(users);
	}

	private String setData(int data) {
		String strdata = String.valueOf(data);
		if (strdata.length() == 1) {
			return "0" + strdata;
		}
		return strdata;

	}

}
