package com.gongpingjia.carplay.bean;

import com.gongpingjia.carplay.util.MD5Util;

public class User {
    static User user;

    String userId = "";

    String token = "";


    String phone = "";

    // 为1代表已认证
    boolean licenseAuthStatus = false;


    boolean photoAuthStatus = false;

    String brand = "";

    String brandLogo = "";

    String model = "";

    public String nickName;

    public String headUrl;

    public boolean isLogin = false;
    public String psaaword;

    public String emName;

    private boolean isDisconnect = false;

    private boolean hasAlbum = false;

    private String gender;

    public int age;


    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getChatUserId() {
        return MD5Util.string2MD5(userId);
    }

    public String getPsaaword() {
        return psaaword;
    }

    public void setPsaaword(String psaaword) {
        this.psaaword = psaaword;
    }

    public String getChatUserPassword() {
        return psaaword;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public boolean isDisconnect() {
        return isDisconnect;
    }

    public void setDisconnect(boolean isDisconnect) {
        this.isDisconnect = isDisconnect;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isLicenseAuthStatus() {
        return licenseAuthStatus;
    }

    public void setLicenseAuthStatus(boolean licenseAuthStatus) {
        this.licenseAuthStatus = licenseAuthStatus;
    }

    public boolean isPhotoAuthStatus() {
        return photoAuthStatus;
    }

    public void setPhotoAuthStatus(boolean photoAuthStatus) {
        this.photoAuthStatus = photoAuthStatus;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setIsDisconnect(boolean isDisconnect) {
        this.isDisconnect = isDisconnect;
    }


    public String getEmName() {
        return emName;
    }

    public void setEmName(String emName) {
        this.emName = emName;
    }


    public boolean isHasAlbum() {
        return hasAlbum;
    }

    public void setHasAlbum(boolean hasAlbum) {
        this.hasAlbum = hasAlbum;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
