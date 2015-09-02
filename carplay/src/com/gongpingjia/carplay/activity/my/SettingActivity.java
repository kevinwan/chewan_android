package com.gongpingjia.carplay.activity.my;

import java.io.File;

import net.duohuo.dhroid.ioc.IocContainer;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.bean.FilterPreference;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.FileUtil;
import com.gongpingjia.carplay.util.FileUtil.UNIT_SACLE;

/**
 * 
 * @Description 设置界面
 * @author wang
 * @date 2015-7-16 上午9:21:17
 */
public class SettingActivity extends CarPlayBaseActivity implements OnClickListener {
    Button setting_btn;

    RelativeLayout setting_clear, setting_like, setting_about_us, setting_versions, setting_at_versions;

    TextView title;

    TextView mSizeText, mVersionText;

    private SettingActivity mySelf = this;

    private File mCacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting_clear = (RelativeLayout) findViewById(R.id.setting_clear);
        setting_like = (RelativeLayout) findViewById(R.id.setting_like);
        setting_about_us = (RelativeLayout) findViewById(R.id.setting_about_us);
        setting_versions = (RelativeLayout) findViewById(R.id.setting_versions);
        setting_at_versions = (RelativeLayout) findViewById(R.id.setting_at_versions);
        setting_btn = (Button) findViewById(R.id.setting_quit);
        title = (TextView) findViewById(R.id.title);
        title.setText("设置");

        setting_btn.setVisibility(User.getInstance().isLogin() ? View.VISIBLE : View.GONE);

        mVersionText = (TextView) findViewById(R.id.tv_version);
        mVersionText.setText(getAppVersion());

        mSizeText = (TextView) findViewById(R.id.tv_cache_size);
        setting_clear.setOnClickListener(this);
        setting_like.setOnClickListener(this);
        setting_about_us.setOnClickListener(this);
        setting_versions.setOnClickListener(this);
        setting_at_versions.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
        mCacheFile = new File(getExternalCacheDir(), "CarPlay");
        mSizeText.setText(String.valueOf(FileUtil.getFileOrDirSize(mCacheFile, UNIT_SACLE.M)) + " M");
    }

    @Override
    public void onClick(View arg0) {
        Intent it = null;
        switch (arg0.getId()) {
        case R.id.setting_clear:
            if (FileUtil.deleteFileOrDir(mCacheFile)) {
                showToast("缓存清理成功");
                mSizeText.setText("0 M");
            } else {
                showToast("不需要清理了哦");
            }
            break;
        case R.id.setting_like:
            Intent viewIntent = new Intent("android.intent.action.VIEW",
                    Uri.parse("market://details?id=com.gongpingjia.carplay"));
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // viewIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            //
            // viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            // viewIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(viewIntent);
            break;
        case R.id.setting_about_us:
            it = new Intent(this, AboutUsActivity.class);
            startActivity(it);
            break;
        case R.id.setting_versions:
            it = new Intent(this, VersionIntroActivity.class);
            startActivity(it);
            break;
        case R.id.setting_at_versions:
            break;
        case R.id.setting_quit:
            Log.e("tag", "update user info");
            showProgressDialog("退出登录中...");
            DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hidenProgressDialog();
                            Intent it = new Intent(self, LoginActivity.class);
                            it.putExtra("action", "logout");
                            startActivity(it);
                            CarPlayPerference preference = IocContainer.getShare().get(CarPlayPerference.class);
                            preference.load();
                            preference.setPassword("");
                            preference.setChannel("");
                            preference.commit();
                            User user = User.getInstance();
                            user.setLogin(false);
                            user.setUserId("");
                            user.setToken("");

                            FilterPreference pre = IocContainer.getShare().get(FilterPreference.class);
                            pre.load();
                            pre.setFirst(null);
                            pre.commit();
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    hidenProgressDialog();
                }
            });
            break;

        default:
            break;
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
    }

    public String getAppVersion() {
        String version = "获取失败";
        PackageManager pkgManager = getPackageManager();
        try {
            PackageInfo pkgInfo = pkgManager.getPackageInfo(this.getPackageName(), 0);
            version = pkgInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;

    }

}
