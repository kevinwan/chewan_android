package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class AboutUsActivity extends CarPlayBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        
        setTitle("关于我们");
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

}
