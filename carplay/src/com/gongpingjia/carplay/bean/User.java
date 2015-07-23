package com.gongpingjia.carplay.bean;

public class User
{
    static User user;
    
    String userId = "";
    
    String token = "";
    
    public static User getInstance()
    {
        if (user == null)
        {
            user = new User();
        }
        return user;
    }
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
    
}
