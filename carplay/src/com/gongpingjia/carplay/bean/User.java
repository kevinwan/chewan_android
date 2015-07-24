package com.gongpingjia.carplay.bean;

public class User
{
    static User user;
    
    String userId = "";
    
    String token = "";
    
    int isAuthenticated;
    
    String brand = "";
    
    String brandLogo = "";
    
    String model = "";
    
    int seatNumber;
    
    public boolean isLogin = false;
    
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
    
    public int getIsAuthenticated()
    {
        return isAuthenticated;
    }
    
    public void setIsAuthenticated(int isAuthenticated)
    {
        this.isAuthenticated = isAuthenticated;
    }
    
    public String getBrand()
    {
        return brand;
    }
    
    public void setBrand(String brand)
    {
        this.brand = brand;
    }
    
    public String getBrandLogo()
    {
        return brandLogo;
    }
    
    public void setBrandLogo(String brandLogo)
    {
        this.brandLogo = brandLogo;
    }
    
    public String getModel()
    {
        return model;
    }
    
    public void setModel(String model)
    {
        this.model = model;
    }
    
    public int getSeatNumber()
    {
        return seatNumber;
    }
    
    public void setSeatNumber(int seatNumber)
    {
        this.seatNumber = seatNumber;
    }

    public boolean isLogin()
    {
        return isLogin;
    }

    public void setLogin(boolean isLogin)
    {
        this.isLogin = isLogin;
    }
    
}
