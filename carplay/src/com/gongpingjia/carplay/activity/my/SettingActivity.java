package com.gongpingjia.carplay.activity.my;

import java.io.File;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.IocContainer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
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

    TextView mSizeText;

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
            Toast.makeText(mySelf, "喜欢鼓励", Toast.LENGTH_SHORT).show();
            break;
        case R.id.setting_about_us:
            it = new Intent(this, AboutUsActivity.class);
            startActivity(it);
            break;
        case R.id.setting_versions:
            Toast.makeText(mySelf, "版本介绍", Toast.LENGTH_SHORT).show();
            break;
        case R.id.setting_at_versions:
            Toast.makeText(mySelf, "当前版本", Toast.LENGTH_SHORT).show();
            break;
        case R.id.setting_quit:
            it = new Intent(this, LoginActivity.class);
            it.putExtra("action", "logout");
            startActivity(it);
            CarPlayPerference preference = IocContainer.getShare().get(CarPlayPerference.class);
            preference.load();
            preference.setPassword("");
            preference.commit();
            Log.e("tag", "update user info");
            break;

        default:
            break;
        }
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
    }

}
