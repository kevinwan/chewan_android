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
    private boolean transfer=true;

    //记录上一次选择的位置id
    //活动类型id
    private int type_id=-1;
    //付费类型id
    private int pay_id=-1;
    //性别类型id
    private int gender_id=-1;


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

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getPay_id() {
        return pay_id;
    }

    public void setPay_id(int pay_id) {
        this.pay_id = pay_id;
    }

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }
}
