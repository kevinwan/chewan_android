package com.gongpingjia.carplay.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */

public class PointRecord {

    static PointRecord instance;
    private int activityDynamicCall;

    public static PointRecord getInstance() {
        if (instance == null) {
            instance = new PointRecord();
        }
        return instance;
    }

    /**
     * 未注册附近邀他事件
     */
    public int unRegisterNearbyInvited = 0;


    /**
     * 未注册匹配邀请事件
     */
    public int unRegisterMatchInvited = 0;

    /**
     * 用户注册事件
     */

    public int userRegister = 0;

    /**
     * 活动动态打电话事件
     */
    public List<String> activityDynamicCallList = new ArrayList<>();

    
    /**
     * 活动动态聊天事件
     */

    public int activityDynamicChat = 0;

    /**
     * 活动匹配邀他事件
     */

    public int activityMatchInvitedCount = 0;


    /**
     * 匹配活动匹配点击事件
     */
    public int activityMatchCount = 0;


    /**
     * 官方活动购票事件
     */
    public int officialActivityBuyTicket = 0;

    /**
     * 官方活动加入群聊事件
     */

    public int officialActivityChatJoin = 0;

    /**
     * 打开APP事件
     */

    public int appOpenCount = 0;

    /**
     * 进入动态邀他事件
     */
    public int dynamicNearbyInvited = 0;

    public int getUnRegisterNearbyInvited() {
        return unRegisterNearbyInvited;
    }

    public void setUnRegisterNearbyInvited(int unRegisterNearbyInvited) {
        this.unRegisterNearbyInvited = unRegisterNearbyInvited;
    }

    public int getUnRegisterMatchInvited() {
        return unRegisterMatchInvited;
    }

    public void setUnRegisterMatchInvited(int unRegisterMatchInvited) {
        this.unRegisterMatchInvited = unRegisterMatchInvited;
    }

    public int getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(int userRegister) {
        this.userRegister = userRegister;
    }

    public int getActivityDynamicCall() {

        return activityDynamicCall;
    }

    public void setActivityDynamicCall(int activityDynamicCall) {
        this.activityDynamicCall = activityDynamicCall;
    }

    public int getActivityDynamicChat() {
        return activityDynamicChat;
    }

    public void setActivityDynamicChat(int activityDynamicChat) {
        this.activityDynamicChat = activityDynamicChat;
    }

    public int getActivityMatchInvitedCount() {
        return activityMatchInvitedCount;
    }

    public void setActivityMatchInvitedCount(int activityMatchInvitedCount) {
        this.activityMatchInvitedCount = activityMatchInvitedCount;
    }

    public int getOfficialActivityBuyTicket() {
        return officialActivityBuyTicket;
    }

    public void setOfficialActivityBuyTicket(int officialActivityBuyTicket) {
        this.officialActivityBuyTicket = officialActivityBuyTicket;
    }

    public int getOfficialActivityChatJoin() {
        return officialActivityChatJoin;
    }

    public void setOfficialActivityChatJoin(int officialActivityChatJoin) {
        this.officialActivityChatJoin = officialActivityChatJoin;
    }

    public int getAppOpenCount() {
        return appOpenCount;
    }

    public void setAppOpenCount(int appOpenCount) {
        this.appOpenCount = appOpenCount;
    }

    public int getDynamicNearbyInvited() {
        return dynamicNearbyInvited;
    }

    public void setDynamicNearbyInvited(int dynamicNearbyInvited) {
        this.dynamicNearbyInvited = dynamicNearbyInvited;
    }

    public int getActivityMatchCount() {
        return activityMatchCount;
    }

    public void setActivityMatchCount(int activityMatchCount) {
        this.activityMatchCount = activityMatchCount;
    }
}
