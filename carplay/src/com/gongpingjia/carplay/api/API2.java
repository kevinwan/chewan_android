package com.gongpingjia.carplay.api;

/**
 * Created by Administrator on 2015/10/13.
 */
public class API2 {

    public static int ImageHeight = 0;

    //测试版API前缀
    public static final String CWBaseurl = "http://cwapi.gongpingjia.com:8080/v2/";
//    public static final String CWBaseurl = "http://192.168.1.162:8080/v2/";


    //获取验证码
    public static final String getVerification = CWBaseurl + "phone/";
    //官方活动详情
    public static final String ActiveDetails = CWBaseurl + "official/activity/";

    public static final String verifyCode = CWBaseurl + "phone/";
    //手机号登陆
    public static final String login = CWBaseurl + "user/login";
    //三方登陆
    public static final String snsLogin = CWBaseurl + "sns/login";
    //注册
    public static final String register = CWBaseurl + "user/register";
    //头像上传
    public static final String uploadAvatar = CWBaseurl + "avatar/upload";

    //忘记密码
    public static final String forgetPassword = CWBaseurl + "user/password";

    public static final String allCarBrands = CWBaseurl + "/car/brand";

    public static final String carDetails = CWBaseurl + "/car/model";


    //获取我关注的信息
    public static String getSubscribe(String userId, String token) {
        return String.format(CWBaseurl + "user/%s/subscribe?token=%s", userId, token);
    }

    public static final String recommendList = CWBaseurl + "/official/activity/list";

    //获取地点信息
    public static String getPlaces(String id) {
        return "http://cwapi.gongpingjia.com:8080/v2/area/list?parentId=" + id;
    }

    //匹配活动
    public static String getMatchUrl(String userId, String token) {
        return String.format(CWBaseurl + "activity/register?userId=%s&token=%s", userId, token);
    }

    //报名参加
    public static final String joinActive = CWBaseurl + "official/activity/";

    //约她同去参加官方活动
    public static final String joinTogether = CWBaseurl + "official/activity/";

    public static String getPersonProfile(String userId1, String userId2, String token) {
        return String.format(CWBaseurl + "user/%s/info?viewUser=%s&token=%s", userId1, userId2, token);
    }

    public static String getProfileFromHx(String userId, String token, String chatName) {
        return String.format(CWBaseurl + "user/emchatInfo?userId=%s&token=%s&emchatName=%s", userId, token, chatName);
    }

    //取消关注某人
    public static String getUnfollowPerson(String userId, String token) {
        return String.format(CWBaseurl + "user/%s/unlisten?token=%s", userId, token);
    }

    //关注某人
    public static String getFollowPerson(String userId, String token) {
        return String.format(CWBaseurl + "user/%s/listen?token=%s", userId, token);
    }

    //获取谁关注的信息
    public static String getSubscribeMy(String userId, String token) {
        return String.format(CWBaseurl + "user/%s/subscribe/history?token=%s", userId, token);
    }


    //上传经纬度
    public static String sendLocation(String userId, String token) {
        return String.format(CWBaseurl + "user/%s/location?token=%s", userId, token);
    }

    public static String updateVersion = CWBaseurl + "version?product=android";

}
