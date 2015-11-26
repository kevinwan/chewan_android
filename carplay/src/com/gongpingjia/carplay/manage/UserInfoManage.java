package com.gongpingjia.carplay.manage;

import android.app.Activity;
import android.content.Intent;

import com.gongpingjia.carplay.activity.my.LoginActivity2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.dialog.DynamicDelDialog;

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

    public boolean checkLogin(final Activity context, final LoginCallBack loginCallBack) {
        boolean islogin = User.getInstance().isLogin();
        if (!islogin) {
            DynamicDelDialog dialog = new DynamicDelDialog(context, "您还未登录,现在就去登录么");
            dialog.setOnCLickResult(new DynamicDelDialog.OnCLickResult() {
                @Override
                public void clickResult() {
                    checkToLogin(context, loginCallBack);
                }
            });

            dialog.show();
        }else {
            if (loginCallBack != null) {
                loginCallBack.onisLogin();
            }
        }
        return islogin;
    }

    public boolean checkLogin(final Activity context, final String from, final LoginCallBack loginCallBack) {
        boolean islogin = User.getInstance().isLogin();
        if (!islogin) {
        DynamicDelDialog dialog = new DynamicDelDialog(context, "您还未登录,现在就去登录么");
        dialog.setOnCLickResult(new DynamicDelDialog.OnCLickResult() {
            @Override
            public void clickResult() {
                checkToLogin(context, from, loginCallBack);
            }
        });

        dialog.show();
        }else {
            if (loginCallBack != null) {
                loginCallBack.onisLogin();
            }
        }
        return islogin;
    }

    public void checkToLogin(Activity context, LoginCallBack loginCallBack) {
//        boolean islogin = User.getInstance().isLogin();
//        if (!islogin) {
            if (context != null) {
                IocContainer.getShare().get(IDialog.class)
                        .showToastShort(context, "");
                LoginActivity2.loginCall = loginCallBack;
                Intent it = new Intent(context, LoginActivity2.class);
                context.startActivity(it);
//                return false;
            }
//        } else {
//            if (loginCallBack != null) {
//                loginCallBack.onisLogin();
//
//            }
//        }
//        return islogin;
    }

    public void checkToLogin(Activity context, String from,
                                LoginCallBack loginCallBack) {
//        boolean islogin = User.getInstance().isLogin();
//        if (!islogin) {
            if (context != null) {
                IocContainer.getShare().get(IDialog.class)
                        .showToastShort(context, "");
                LoginActivity2.loginCall = loginCallBack;
                Intent it = new Intent(context, LoginActivity2.class);
                // 是否从用户头像点击注册
                it.putExtra("from", from);
                context.startActivity(it);
//                return false;
            }
//        } else {
//            if (loginCallBack != null) {
//                loginCallBack.onisLogin();
//            }
//        }
//        return islogin;
    }

    public interface LoginCallBack {
        public void onisLogin();

        public void onLoginFail();
    }
}
