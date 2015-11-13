package com.gongpingjia.carplay.bean;

import net.duohuo.dhroid.util.Perference;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FilterPreference extends Perference {

    private String first;

    private String province;

    private String city;

    private String district;

    private String type;

    private String gender;

    private int isAuthenticate;

    private String carLevel;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getType() {
        return type;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsAuthenticate() {
        return isAuthenticate;
    }

    public void setIsAuthenticate(int isAuthenticate) {
        this.isAuthenticate = isAuthenticate;
    }

    public String getCarLevel() {
        return carLevel;
    }

    public void setCarLevel(String carLevel) {
        this.carLevel = carLevel;
    }

}
