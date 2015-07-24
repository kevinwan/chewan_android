package com.gongpingjia.carplay.manage;

import android.app.Activity;
import android.content.Intent;

import com.gongpingjia.carplay.activity.my.LoginActivity;
import com.gongpingjia.carplay.bean.User;

public class UserInfoManage
{
    
    static UserInfoManage instance;
    
    public static UserInfoManage getInstance()
    {
        if (instance == null)
        {
            instance = new UserInfoManage();
        }
        return instance;
    }
    
    public boolean checkLogin(Activity context, LoginCallBack loginCallBack)
    {
        boolean islogin = User.getInstance().isLogin();
        if (!islogin)
        {
            if (context != null)
            {
                LoginActivity.loginCall = loginCallBack;
                Intent it = new Intent(context, LoginActivity.class);
                context.startActivity(it);
                return false;
            }
        }
        else
        {
            if (loginCallBack != null)
            {
                loginCallBack.onisLogin();
                
            }
        }
        return islogin;
    }
    
    public interface LoginCallBack
    {
        public void onisLogin();
        
        public void onLoginFail();
    }
}
