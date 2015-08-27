package com.gongpingjia.carplay;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.GlobalCodeHandler;
import net.duohuo.dhroid.util.UserLocation;
import android.app.Application;
import android.content.Intent;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.gongpingjia.carplay.activity.main.SplashActivity;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.data.CityDataManage;
import com.gongpingjia.carplay.db.DaoHelper;
import com.gongpingjia.carplay.service.MsgService;
import com.gongpingjia.carplay.view.NomalDialog;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CarPlayApplication extends Application implements
		Thread.UncaughtExceptionHandler {

	private static CarPlayApplication instance;

	public IDialog dialoger;

	public static ImageLoaderConfiguration imageconfig;

	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public static CarPlayApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		Thread.setDefaultUncaughtExceptionHandler(this);
		hxSDKHelper.onInit(getApplicationContext());
		instance = this;
		Const.netadapter_page_no = "page";
		Const.netadapter_step = "step";
		Const.response_total = "totalRows";
		Const.response_data = "data";
		Const.netadapter_step_default = 10;
		Const.DATABASE_VERSION = 9;
		Const.response_success = "result";
		Const.response_msg = "errmsg";
		Const.response_result_status = "0";
		Const.netadapter_no_more = "已加载全部数据";
		Const.postType = 2;
		IocContainer.getShare().initApplication(this);
		IocContainer.getShare().bind(CarPlayValueFix.class).to(ValueFix.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(NomalDialog.class).to(IDialog.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(DaoHelper.class)
				.to(OrmLiteSqliteOpenHelper.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		IocContainer.getShare().bind(KmlGlobalCodeHandler.class)
				.to(GlobalCodeHandler.class)
				.scope(InstanceScope.SCOPE_SINGLETON);
		dialoger = IocContainer.getShare().get(IDialog.class);
		// CrashHandler.getInstance().init();

		imageconfig = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1800000).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		ImageLoader.getInstance().init(imageconfig);

		UserLocation.getInstance().init(getApplicationContext());
	}

	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM, final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(isGCM, emCallBack);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// System.out.println("奔溃.................");
		Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
