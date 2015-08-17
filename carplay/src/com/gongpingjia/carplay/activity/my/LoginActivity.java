package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.CarPlayApplication;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.util.Utils;

import de.greenrobot.event.EventBus;

/**
 * 登录页面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends CarPlayBaseActivity {

	// 手机号
	private EditText PhoneNumEditText;

	// 密码
	private EditText PasswordEditText;

	// 登录
	private Button LoginButton;

	// 注册
	private LinearLayout login_register;

	// 忘记密码
	private TextView login_forgetpsw;

	public static final int Register = 1;
	public static final int Forgetpwd = 2;

	public static LoginCallBack loginCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		String action = getIntent().getStringExtra("action");
		if (action != null && action.equals("logout")) {
			ActivityTack.getInstanse().finishOthers(this);
		}
	}

	@Override
	public void initView() {
		PhoneNumEditText = (EditText) findViewById(R.id.ed_login_phone);
		PasswordEditText = (EditText) findViewById(R.id.ed_login_password);
		LoginButton = (Button) findViewById(R.id.button_login);
		LoginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Utils.autoCloseKeyboard(self, arg0);
				final String strPhoneNum = PhoneNumEditText.getText()
						.toString();
				final String strPassword = PasswordEditText.getText()
						.toString();
				if (TextUtils.isEmpty(strPhoneNum)) {
					showToast("手机号码不能为空");
					return;
				}
				if (!Utils.isValidMobilePhoneNumber(strPhoneNum)) {
					showToast("手机格式错误");
					return;

				}
				if (TextUtils.isEmpty(strPassword)) {
					showToast("密码不能为空哦");
					return;
				}

				DhNet net = new DhNet(API.login);
				net.addParam("phone", strPhoneNum);
				net.addParam("password", MD5Util.string2MD5(strPassword));
				net.doPostInDialog("登录中...", new NetTask(self) {

					@Override
					public void doInUI(Response response, Integer transfer) {
						if (response.isSuccess()) {
							JSONObject jo = response.jSONFrom("data");

							// 登录环信
							loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
									"userId")),
									MD5Util.string2MD5(strPassword), jo,
									strPhoneNum);

						}
					}
				});
			}
		});
		// 忘记密码
		login_forgetpsw = (TextView) findViewById(R.id.login_forgetpsw);
		login_forgetpsw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(self, ForgetPwdActivity.class);
				startActivityForResult(intent, Forgetpwd);
			}
		});
		// 注册
		login_register = (LinearLayout) findViewById(R.id.login_register);
		login_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(self, RegisterActivity.class);
				startActivityForResult(intent, Register);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Register) {
				finish();
			}
			if (requestCode == Forgetpwd) {
				finish();
			}
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (loginCall != null) {
			if (User.getInstance().isLogin()) {
				loginCall.onisLogin();
			} else {
				loginCall.onLoginFail();
			}
		}
		loginCall = null;
	}

	private void loginHX(final String currentUsername,
			final String currentPassword, final JSONObject jo,
			final String phone) {
		EMChatManager.getInstance().login(currentUsername, currentPassword,
				new EMCallBack() {

					@Override
					public void onSuccess() {
						CarPlayApplication.getInstance().setUserName(
								currentUsername);
						CarPlayApplication.getInstance().setPassword(
								currentPassword);
						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and

							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							// 处理好友和群组
							initializeContacts();
							User user = User.getInstance();
							user.setUserId(JSONUtil.getString(jo, "userId"));
							user.setToken(JSONUtil.getString(jo, "token"));
							user.setBrand(JSONUtil.getString(jo, "brand"));
							user.setBrandLogo(JSONUtil.getString(jo,
									"brandLogo"));
							user.setModel(JSONUtil.getString(jo, "model"));
							user.setPsaaword(currentPassword);
							user.setSeatNumber(JSONUtil
									.getInt(jo, "seatNumber"));
							user.setIsAuthenticated(JSONUtil.getInt(jo,
									"isAuthenticated"));
							CarPlayPerference per = IocContainer.getShare()
									.get(CarPlayPerference.class);
							per.phone = phone;
							per.password = currentPassword;
							per.commit();
							String action = getIntent()
									.getStringExtra("action");
							if (action != null && action.equals("logout")) {
								Intent it = new Intent(self, MainActivity.class);
								startActivity(it);
							} else {
								self.finish();
							}
							User.getInstance().setLogin(true);
							LoginEB loginEB = new LoginEB();
							loginEB.setIslogin(true);
							EventBus.getDefault().post(loginEB);
							asyncFetchGroupsFromServer();
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
						// boolean updatenick =
						// EMChatManager.getInstance().updateCurrentUserNick(
						// DemoApplication.currentUserNick.trim());
						// if (!updatenick) {
						// Log.e("LoginActivity",
						// "update current user nick fail");
						// }
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
								showToast("登录失败:" + message);
								// Toast.makeText(
								// getApplicationContext(),
								// getString(R.string.Login_failed)
								// + message, Toast.LENGTH_SHORT)
								// .show();
							}
						});
					}
				});

	}

	private void initializeContacts() {
		Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
		// 添加user"申请与通知"
		ChatUser newFriends = new ChatUser();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		ChatUser groupUser = new ChatUser();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		// 添加"Robot"
		ChatUser robotUser = new ChatUser();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);

		// 存入内存
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
		dao.saveContactList(users);
	}

	static void asyncFetchGroupsFromServer() {
		HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

			@Override
			public void onSuccess() {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

				if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}

			@Override
			public void onError(int code, String message) {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

		});
	}
}
