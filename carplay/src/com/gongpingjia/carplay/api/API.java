package com.gongpingjia.carplay.api;

public class API {

	public static final String update = "http://chewanapi.gongpingjia.com/version";

	// // 正式
	// public static final String share = "http://chewanapi.gongpingjia.com/";
	//
	// public static final String CWBaseurl =
	// "http://chewanapi.gongpingjia.com/v1";

	// 测试
	public static final String share = "http://cwapi.gongpingjia.com/";
	public static final String CWBaseurl = "http://cwapi.gongpingjia.com/v1";

	public static final String allCarBrands = CWBaseurl + "/car/brand";

	public static final String carDetails = CWBaseurl + "/car/model";

	// 登录
	public static final String login = CWBaseurl + "/user/login";

	// 重设密码
	public static final String newPassword = CWBaseurl + "/user/password";

	public static final String checkCode = CWBaseurl + "/phone/";

	public static final String uploadHead = CWBaseurl + "/avatar/upload";

	public static final String register = CWBaseurl + "/user/register";

	// 上传图片
	public static final String uploadPictures = CWBaseurl
			+ "/activity/cover/upload?";

	public static final String activeList = CWBaseurl + "/activity/list";

	public static final String createActive = CWBaseurl + "/activity/register?";

	public static final String availableSeat = CWBaseurl + "/user/";

	public static final String editActive = CWBaseurl + "/activity/";

	// 上传到相册
	public static final String uploadAlbum = CWBaseurl + "/user/";

	public static final String editAlbum = CWBaseurl + "/user/";

	// 官方活动
	public static final String official = CWBaseurl + "/official/activity/list";

	// 获取群聊头像
	public static final String groupChatHead = CWBaseurl + "/chatgroup/photos?";

}
