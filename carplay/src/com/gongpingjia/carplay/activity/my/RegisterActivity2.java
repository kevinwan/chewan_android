package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * Created by Administrator on 2015/10/13.
 */
public class RegisterActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private EditText mEditNum, mEditPassword, mEditValidation;
    private Button mBtnRegister;
    private TextView mTextGetValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    }

    @Override
    public void initView() {
        mEditNum = (EditText) findViewById(R.id.et_phone_num);
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mTextGetValidation = (TextView) findViewById(R.id.tv_get_code);

        mBtnRegister.setOnClickListener(this);
        mTextGetValidation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String num = mEditNum.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            showToast("手机号不能为空");
            return;
        }
        if (num.length() != 11) {
            showToast("手机号不合法");
            return;
        }
        switch (id) {
            case R.id.btn_register:
                break;
            case R.id.tv_get_code:
                getValidation(num);
                break;
        }
    }

    private void getValidation(String num) {
        
    }
}
