package com.gongpingjia.carplay.manage;

import android.app.Activity;
import android.content.Intent;

import com.gongpingjia.carplay.activity.my.LoginActivity2;
import com.gongpingjia.carplay.bean.User;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;

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
//				IocContainer.getShare().get(IDialog.class)
//						.showToastShort(context, "请先登录!");
                LoginActivity2.loginCall = loginCallBack;
                Intent it = new Intent(context, LoginActivity2.class);
                context.startActivity(it);
                return false;
            }
        } else {
            if (loginCallBack != null) {
                loginCallBack.onisLogin();

            }
        }
        return islogin;
    }

    public boolean checkLogin(Activity context, String from,
                              LoginCallBack loginCallBack) {
        boolean islogin = User.getInstance().isLogin();
        if (!islogin) {
            if (context != null) {
                IocContainer.getShare().get(IDialog.class)
                        .showToastShort(context, "请先登录!");
                LoginActivity2.loginCall = loginCallBack;
                Intent it = new Intent(context, LoginActivity2.class);
                // 是否从用户头像点击注册
                it.putExtra("from", from);
                context.startActivity(it);
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
