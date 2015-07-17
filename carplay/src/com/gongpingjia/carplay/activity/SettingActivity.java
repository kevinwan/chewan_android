package com.gongpingjia.carplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;

/**
 * 
 * @Description 设置界面
 * @author wang
 * @date 2015-7-16 上午9:21:17
 */
public class SettingActivity extends CarPlayBaseActivity implements OnClickListener
{
    Button setting_btn;
    
    RelativeLayout setting_clear, setting_like, setting_about_us, setting_versions, setting_at_versions;
    
    TextView title;
    
    private SettingActivity mySelf = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting_clear = (RelativeLayout)findViewById(R.id.setting_clear);
        setting_like = (RelativeLayout)findViewById(R.id.setting_like);
        setting_about_us = (RelativeLayout)findViewById(R.id.setting_about_us);
        setting_versions = (RelativeLayout)findViewById(R.id.setting_versions);
        setting_at_versions = (RelativeLayout)findViewById(R.id.setting_at_versions);
        setting_btn = (Button)findViewById(R.id.setting_quit);
        title = (TextView)findViewById(R.id.title);
        title.setText("设置");
        setting_clear.setOnClickListener(this);
        setting_like.setOnClickListener(this);
        setting_about_us.setOnClickListener(this);
        setting_versions.setOnClickListener(this);
        setting_at_versions.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.setting_clear:
                Toast.makeText(mySelf, "清理缓存", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_like:
                Toast.makeText(mySelf, "喜欢鼓励", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_about_us:
                Toast.makeText(mySelf, "关于我们", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_versions:
                Toast.makeText(mySelf, "版本介绍", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_at_versions:
                Toast.makeText(mySelf, "当前版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_quit:
                Toast.makeText(mySelf, "退出登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mySelf, PersonalDataActivity.class);
                startActivity(intent);
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }
    
}
