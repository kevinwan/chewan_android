package com.gongpingjia.carplay.api;

public class API {
    public static final String Baseurl = "http://api7.gongpingjia.com/";

    public static final String CWBaseurl = "http://cwapi.gongpingjia.com/v1";

    public static final String allCarData = Baseurl + "/mobile/buy/car/search/";

    public static final String allCarBrands = "http://cwapi.gongpingjia.com/v1/car/brand";

    public static final String carDetails = " http://cwapi.gongpingjia.com/v1/car/model";

    // 登录
    public static final String login = CWBaseurl + "/user/login";

    // 重设密码
    public static final String newPassword = CWBaseurl + "/user/password";

    public static final String checkCode = CWBaseurl + "/phone/";

    public static final String uploadHead = CWBaseurl + "/avatar/upload";

    public static final String register = CWBaseurl + "/user/register";

    // 上传图片
    public static final String uploadPictures = CWBaseurl + "/activity/cover/upload?";

    public static final String activeList = CWBaseurl + "/activity/list";

    public static final String createActive = CWBaseurl + "/activity/register?";

    public static final String availableSeat = CWBaseurl + "/user/";

}
