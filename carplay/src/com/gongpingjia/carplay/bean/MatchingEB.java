package com.gongpingjia.carplay.bean;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/23.
 */
public class MatchingEB {

    private String majorType;

    private String pay;

    private String type;

    private boolean transfer;

    private Map<String, String> destination;

    Map<String, Double> estabPoint;

    Map<String, String> establish;


    public String getMajorType() {
        return majorType;
    }

    public void setMajorType(String majorType) {
        this.majorType = majorType;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public Map<String, String> getDestination() {
        return destination;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public void setDestination(Map<String, String> destination) {
        this.destination = destination;
    }

    public Map<String, Double> getEstabPoint() {
        return estabPoint;
    }

    public void setEstabPoint(Map<String, Double> estabPoint) {
        this.estabPoint = estabPoint;
    }

    public Map<String, String> getEstablish() {
        return establish;
    }

    public void setEstablish(Map<String, String> establish) {
        this.establish = establish;
    }
}
