package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * 修改密码
 * Created by Administrator on 2015/10/30.
 */
public class RevisePassword extends CarPlayBaseActivity implements View.OnClickListener {
    LinearLayout old_layout, new_layout, onceagain_layout;
    Button btn_yes;
    EditText et_password, et_newpassword, et_againpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
    }

    @Override
    public void initView() {
        setTitle("修改密码");
        old_layout = (LinearLayout) findViewById(R.id.old_layout);
        new_layout = (LinearLayout) findViewById(R.id.new_layout);
        onceagain_layout = (LinearLayout) findViewById(R.id.onceagain_layout);
        et_password = (EditText)  findViewById(R.id.et_newpassword);
        et_newpassword = (EditText)  findViewById(R.id.et_password);
        et_againpassword = (EditText)  findViewById(R.id.et_againpassword);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:


                break;
        }
    }
}
