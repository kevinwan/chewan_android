package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.CachePolicy;
import net.duohuo.dhroid.util.Perference;

import org.json.JSONObject;

import android.content.Context;

/**
 * 
 * 用于存Preferences的获取与存储
 * 
 * @author duohuo
 * 
 */
public class CarPlayPerference extends Perference
{
    
    public String uid;
    
    // 用户的密码
    public String pswd;
    
    public String username;
    
    public String headUrl;
    
    public String getUid()
    {
        return uid;
    }
    
    public void setUid(String uid)
    {
        this.uid = uid;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String settingbg;
    
    // 网络更新用户信息
    public void refreshUserInfo(Context context)
    {
        // DhNet net = new DhNet(API.persondetail);
        // net.useCache(CachePolicy.POLICY_NOCACHE);
        // net.doGet(new NetTask(context)
        // {
        // @Override
        // public void doInUI(Response response, Integer transfer)
        // {
        // if (response.isSuccess())
        // {
        // JSONObject jo = response.jSONFromData();
        // String faceurl = JSONUtil.getString(jo, "faceurl");
        // if (faceurl != null)
        // {
        // faceurl = faceurl.replace("_s.jpg", "_m.jpg");
        // }
        // // face = API.ImageBase + faceurl;
        // uid = JSONUtil.getString(jo, "id");
        // username = JSONUtil.getString(jo, "name");
        // commit();
        // notifyDataSetChanged();
        // }
        // }
        // });
    }
    
    public String getPswd()
    {
        return pswd;
    }
    
    public void setPswd(String pswd)
    {
        this.pswd = pswd;
    }
    
}
