package com.gongpingjia.carplay.activity.main;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;

public class SplashActivity extends CarPlayBaseActivity
{
    
    CarPlayPerference per;
    
    private final Handler mHandler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    
    @Override
    public void initView()
    {
        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();
        
        if (!TextUtils.isEmpty(per.phone) && !TextUtils.isEmpty(per.password))
        {
            login();
        }
        else
        {
            if (per.isFirst == 0)
            {
                first();
                
            }
            else
            {
                notFirst();
            }
        }
        
    }
    
    private void login()
    {
        DhNet net = new DhNet(API.login);
        net.addParam("phone", per.phone);
        net.addParam("password", per.password);
        net.doPost(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    JSONObject jo = response.jSONFrom("data");
                    User user = User.getInstance();
                    user.setUserId(JSONUtil.getString(jo, "userId"));
                    user.setToken(JSONUtil.getString(jo, "token"));
                    user.setBrand(JSONUtil.getString(jo, "brand"));
                    user.setBrandLogo(JSONUtil.getString(jo, "brandLogo"));
                    user.setModel(JSONUtil.getString(jo, "model"));
                    user.setSeatNumber(JSONUtil.getInt(jo, "seatNumber"));
                    user.setIsAuthenticated(JSONUtil.getInt(jo, "isAuthenticated"));
                    user.setLogin(true);
                }
                
                if (per.isFirst == 0)
                {
                    first();
                    
                }
                else
                {
                    notFirst();
                }
            }
        });
    }
    
    private void first()
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(self, GuidanceActivity.class);
                startActivity(intent);
                per.isFirst = 1;
                per.commit();
                self.finish();
            }
        }, 3000);
    }
    
    private void notFirst()
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(self, MainActivity.class);
                startActivity(intent);
                self.finish();
            }
        }, 3000);
    }
}
