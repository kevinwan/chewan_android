package com.gongpingjia.carplay;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.DaoHelper;
import net.duohuo.dhroid.util.UserLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.gongpingjia.carplay.view.NomalDialog;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CarPlayApplication extends Application
{
    
    private static CarPlayApplication instance;
    
    public IDialog dialoger;
    
    public static ImageLoaderConfiguration imageconfig;
    
    public static CarPlayApplication getInstance()
    {
        return instance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        
        Const.netadapter_page_no = "page";
        Const.netadapter_step = "step";
        Const.response_total = "totalRows";
        Const.response_data = "data";
        Const.netadapter_step_default = 10;
        Const.DATABASE_VERSION = 5;
        Const.response_success = "status";
        IocContainer.getShare().initApplication(this);
        IocContainer.getShare().bind(CarPlayValueFix.class).to(ValueFix.class).scope(InstanceScope.SCOPE_SINGLETON);
        IocContainer.getShare().bind(NomalDialog.class).to(IDialog.class).scope(InstanceScope.SCOPE_SINGLETON);
        IocContainer.getShare()
            .bind(DaoHelper.class)
            .to(OrmLiteSqliteOpenHelper.class)
            .scope(InstanceScope.SCOPE_SINGLETON);
        
        dialoger = IocContainer.getShare().get(IDialog.class);
        // CrashHandler.getInstance().init();
        
        imageconfig =
            new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(1500000)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(imageconfig);
        // UserLocation.getInstance().init(getApplicationContext());
        
    }
    
}
