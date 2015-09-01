package com.gongpingjia.carplay.manage;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.Activity;
import android.content.Intent;

import com.gongpingjia.carplay.activity.my.LoginActivity;
import com.gongpingjia.carplay.bean.User;

public class UserInfoManage {

	static UserInfoManage instance;

	public static UserInfoManage getInstance() {
		if (instance == null) {
			instance = new UserInfoManage();
		}
		return instance;
	}

	public boolean checkLogin(Activity context, LoginCallBack loginCallBack) {
		boolean islogin = User.getInstance().isLogin();
		if (!islogin) {
			if (context != null) {
				IocContainer.getShare().get(IDialog.class)
						.showToastShort(context, "请先登录!");
				// LoginActivity.loginCall = loginCallBack;
				// Intent it = new Intent(context, LoginActivity.class);
				// context.startActivity(it);
				return false;
			}
		} else {
			if (loginCallBack != null) {
				loginCallBack.onisLogin();

			}
		}
		return islogin;
	}

	public boolean checkLogin(Activity context, boolean isFromAvatar,
			LoginCallBack loginCallBack) {
		boolean islogin = User.getInstance().isLogin();
		if (!islogin) {
			if (context != null) {
				IocContainer.getShare().get(IDialog.class)
						.showToastShort(context, "请先登录!");
				// LoginActivity.loginCall = loginCallBack;
				// Intent it = new Intent(context, LoginActivity.class);
				// // 是否从用户头像点击注册
				// it.putExtra("isFromAvatar", isFromAvatar);
				// context.startActivity(it);
				return false;
			}
		} else {
			if (loginCallBack != null) {
				loginCallBack.onisLogin();
			}
		}
		return islogin;
	}

	public interface LoginCallBack {
		public void onisLogin();

		public void onLoginFail();
	}
}
