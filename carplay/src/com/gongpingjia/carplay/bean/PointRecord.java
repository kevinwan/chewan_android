package com.gongpingjia.carplay.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */

public class PointRecord {

    static PointRecord instance;

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

    public List<String> activityDynamicChatList = new ArrayList<>();

    /**
     * 类型点击事件
     */
    public List<String> typeClick = new ArrayList<>();

    /**
     * 活动匹配邀他事件
     */

    public List<String> activityMatchInvitedCountList = new ArrayList<>();


    /**
     * 匹配活动匹配点击事件
     */
    public int activityMatchCount = 0;


    /**
     * 官方活动购票事件
     */
    public List<String> officialActivityBuyTicketList = new ArrayList<>();

    /**
     * 官方活动加入群聊事件
     */

    public List<String> officialActivityChatJoinList = new ArrayList<>();

    /**
     * 打开APP事件
     */

    public int appOpenCount = 0;

    /**
     * 进入动态邀他事件
     */
    public List<String> dynamicNearbyInvitedList = new ArrayList<>();


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

    public List<String> getActivityDynamicCallList() {
        return activityDynamicCallList;
    }

    public void setActivityDynamicCallList(List<String> activityDynamicCallList) {
        this.activityDynamicCallList = activityDynamicCallList;
    }

    public List<String> getActivityDynamicChatList() {
        return activityDynamicChatList;
    }

    public void setActivityDynamicChatList(List<String> activityDynamicChatList) {
        this.activityDynamicChatList = activityDynamicChatList;
    }

    public List<String> getActivityMatchInvitedCountList() {
        return activityMatchInvitedCountList;
    }

    public void setActivityMatchInvitedCountList(List<String> activityMatchInvitedCountList) {
        this.activityMatchInvitedCountList = activityMatchInvitedCountList;
    }

    public int getActivityMatchCount() {
        return activityMatchCount;
    }

    public void setActivityMatchCount(int activityMatchCount) {
        this.activityMatchCount = activityMatchCount;
    }

    public List<String> getOfficialActivityBuyTicketList() {
        return officialActivityBuyTicketList;
    }

    public void setOfficialActivityBuyTicketList(List<String> officialActivityBuyTicketList) {
        this.officialActivityBuyTicketList = officialActivityBuyTicketList;
    }

    public List<String> getOfficialActivityChatJoinList() {
        return officialActivityChatJoinList;
    }

    public void setOfficialActivityChatJoinList(List<String> officialActivityChatJoinList) {
        this.officialActivityChatJoinList = officialActivityChatJoinList;
    }

    public int getAppOpenCount() {
        return appOpenCount;
    }

    public void setAppOpenCount(int appOpenCount) {
        this.appOpenCount = appOpenCount;
    }

    public List<String> getDynamicNearbyInvitedList() {
        return dynamicNearbyInvitedList;
    }

    public void setDynamicNearbyInvitedList(List<String> dynamicNearbyInvitedList) {
        this.dynamicNearbyInvitedList = dynamicNearbyInvitedList;
    }

    public List<String> getTypeClick() {
        return typeClick;
    }

    public void setTypeClick(List<String> typeClick) {
        this.typeClick = typeClick;
    }
}
