package com.gongpingjia.carplay.bean;

import net.duohuo.dhroid.util.Perference;

/**
 * Created by Administrator on 2015/10/19.
 */
public class FilterPreference2 extends Perference {
    //活动类型
    private String type="";
    //付费类型
    private String pay="";
    //性别
    private String gender="";
    //是否包接送
    private boolean transfer=false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }
}
