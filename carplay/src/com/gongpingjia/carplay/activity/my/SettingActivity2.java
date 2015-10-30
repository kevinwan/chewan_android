package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.FileUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.ioc.IocContainer;

import java.io.File;

public class SettingActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    File mCacheDir;
    TextView mTextCacheSize;
    RelativeLayout setting_about_us, setting_versions,layout_modifypwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
    }

    /**
     *
     */
    @Override
    public void initView() {
        setTitle("设置");
        Button btnLogout = (Button) findViewById(R.id.btn_logout);
        View layoutClearCache = findViewById(R.id.layout_cache_clear);
        setting_about_us = (RelativeLayout) findViewById(R.id.setting_about_us);
        setting_versions = (RelativeLayout) findViewById(R.id.setting_versions);
        layout_modifypwd = (RelativeLayout) findViewById(R.id.layout_modifypwd);
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mTextCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        mTextCacheSize.setText(String.valueOf(FileUtil.getFileOrDirSize(mCacheDir,
                FileUtil.UNIT_SACLE.M)) + " M");
        setting_about_us.setOnClickListener(this);
        layoutClearCache.setOnClickListener(this);
        setting_versions.setOnClickListener(this);
        layout_modifypwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        if (!User.getInstance().isLogin()) {
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.btn_logout:
                logout();
                break;
            case R.id.setting_about_us:
                it = new Intent(this, AboutUsActivity.class);
                startActivity(it);
                break;
            case R.id.setting_versions:
//                it = new Intent(this, VersionIntroActivity.class);
//                startActivity(it);
                break;
            case R.id.layout_modifypwd:
                it = new Intent(this, RevisePassword.class);
                startActivity(it);
                break;
            case R.id.layout_cache_clear:
                ImageLoader.getInstance().getMemoryCache().clear();
                ImageLoader.getInstance().getDiskCache().clear();
                if (FileUtil.deleteFileOrDir(mCacheDir)) {
                    showToast("缓存清理成功");
                    mTextCacheSize.setText("0 M");
                } else {
                    showToast("不需要清理了哦");
                }
                break;
        }
    }

    private void logout() {
        showProgressDialog("退出登录中...");
        DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        hidenProgressDialog();
                        Intent it = new Intent(self, LoginActivity2.class);
                        it.putExtra("action", "logout");
                        startActivity(it);
                        CarPlayPerference preference = IocContainer
                                .getShare().get(CarPlayPerference.class);
                        preference.load();
                        preference.setPassword("");
                        preference.setChannel("");
                        preference.commit();
                        User user = User.getInstance();
                        user.setLogin(false);
                        user.setUserId("");
                        user.setToken("");
                        user.setHasAlbum(false);
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
    }
}
