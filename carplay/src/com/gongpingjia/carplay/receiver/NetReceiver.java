package com.gongpingjia.carplay.receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;

public class NetReceiver extends BroadcastReceiver {

	CarPlayPerference per;

	Context context;

	Dialog progressdialog;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, intent.getAction(), 1).show();
		this.context = context;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobileInfo.isConnected() || wifiInfo.isConnected()) {
			per = IocContainer.getShare().get(CarPlayPerference.class);
			per.load();
			User user = User.getInstance();
			if (!TextUtils.isEmpty(per.channel)
					&& !TextUtils.isEmpty(user.getUserId())
					&& user.isDisconnect()) {
				// 三方登陆
				loginHX(MD5Util.string2MD5(user.getUserId()),
						MD5Util.string2MD5(per.thirdId + per.channel
								+ "com.gongpingjia.carplay"), null);
			} else if (!TextUtils.isEmpty(per.phone)
					&& !TextUtils.isEmpty(per.password)
					&& !TextUtils.isEmpty(user.getUserId())
					&& user.isDisconnect()) {
				// 正常登陆
				loginHX(MD5Util.string2MD5(user.getUserId()), per.password,
						null);
			} else {
			}
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				IocContainer.getShare().get(IDialog.class)
						.showToastShort(context, msg.obj.toString());
				break;
			case 1:
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.hide();
				}
				break;
			default:
				break;
			}
		}
	};

	private void loginThirdParty() {
		String api = API.CWBaseurl + "/sns/login";
		DhNet net = new DhNet(api);
		net.addParam("uid", per.thirdId);
		net.addParam("channel", per.channel);
		net.addParam(
				"sign",
				MD5Util.string2MD5(per.thirdId + per.channel
						+ "com.gongpingjia.carplay"));
		net.doPost(new NetTask(context) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject json = response.jSONFrom("data");
					if (json.has("snsUid")) {

					} else if (json.has("userId")) {

						loginHX(MD5Util.string2MD5(JSONUtil.getString(json,
								"userId")), MD5Util.string2MD5(per.thirdId
								+ per.channel + "com.gongpingjia.carplay"),
								json);
					}
				} else {

				}
			}
		});
	}

	private void loginHX(String currentUsername, String currentPassword,
			final JSONObject jo) {
		progressdialog = IocContainer.getShare().get(IDialog.class)
				.showProgressDialog(context, "重新登录中...");
		EMChatManager.getInstance().login(currentUsername, currentPassword,
				new EMCallBack() {

					@Override
					public void onSuccess() {

						hideProgress();
						Message msg = mHandler.obtainMessage();
						msg.what = 0;
						msg.obj = "登录成功!";
						mHandler.sendMessage(msg);
						User.getInstance().setDisconnect(false);
						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							// EMGroupManager.getInstance().loadAllGroups();
							// EMChatManager.getInstance().loadAllConversations();
							// // 处理好友和群组
							// initializeContacts();
							// User user = User.getInstance();
							// user.setUserId(JSONUtil.getString(jo, "userId"));
							// user.setToken(JSONUtil.getString(jo, "token"));
							// user.setBrand(JSONUtil.getString(jo, "brand"));
							// user.setBrandLogo(JSONUtil.getString(jo,
							// "brandLogo"));
							// user.setModel(JSONUtil.getString(jo, "model"));
							// user.setSeatNumber(JSONUtil
							// .getInt(jo, "seatNumber"));
							// user.setIsAuthenticated(JSONUtil.getInt(jo,
							// "isAuthenticated"));
							// user.setNickName(JSONUtil.getString(jo,
							// "nickname"));
							// user.setHeadUrl(JSONUtil.getString(jo, "photo"));
							User.getInstance().setLogin(true);

							// LoginEB loginEB = new LoginEB();
							// EventBus.getDefault().post(loginEB);
							// LoginActivity.asyncFetchGroupsFromServer();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面
							hideProgress();
							Message msg1 = mHandler.obtainMessage();
							msg1.what = 0;
							msg1.obj = "登录失败!";
							mHandler.sendMessage(msg);
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
						hideProgress();
						Message msg = mHandler.obtainMessage();
						msg.what = 0;
						msg.obj = "登录失败:" + message;
						mHandler.sendMessage(msg);
					}
				});

	}

	private void hideProgress() {
		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}

	private void login() {
		DhNet net = new DhNet(API.login);
		net.addParam("phone", per.phone);
		net.addParam("password", per.password);
		net.doPost(new NetTask(context) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFrom("data");
					loginHX(MD5Util
							.string2MD5(JSONUtil.getString(jo, "userId")),
							per.password, jo);
				}

			}

			@Override
			public void onErray(Response response) {
				// TODO Auto-generated method stub
				super.onErray(response);

			}
		});
	}

	private void initializeContacts() {
		Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
		// 添加user"申请与通知"
		ChatUser newFriends = new ChatUser();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = context.getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		ChatUser groupUser = new ChatUser();
		String strGroup = context.getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		// 添加"Robot"
		ChatUser robotUser = new ChatUser();
		String strRobot = context.getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);

		// 存入内存
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(context);
		List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
		dao.saveContactList(users);
	}

}