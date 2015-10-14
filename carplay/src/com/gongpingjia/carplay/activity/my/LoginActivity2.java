package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * Created by Administrator on 2015/10/13.
 */
public class LoginActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private EditText mEditNum, mEditPassword;
    private Button mBtnLogin, mBtnRegister;
    private TextView mTextForgetPasswd;
    private ImageView mImgWeixin, mImgQQ, mImgWeibo;
    private ImageView mImgShowOrHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    @Override
    public void initView() {
        mEditNum = (EditText) findViewById(R.id.et_phone_num);
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mImgWeibo = (ImageView) findViewById(R.id.iv_weibo);
        mImgQQ = (ImageView) findViewById(R.id.iv_qq);
        mImgWeixin = (ImageView) findViewById(R.id.iv_weixin);
        mImgShowOrHidePassword = (ImageView) findViewById(R.id.iv_switch);

        mBtnRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        mImgWeibo.setOnClickListener(this);
        mImgWeixin.setOnClickListener(this);
        mImgQQ.setOnClickListener(this);

        mImgShowOrHidePassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;

            case R.id.iv_qq:
                break;
            case R.id.iv_weibo:
                break;
            case R.id.iv_weixin:
                break;

            case R.id.iv_switch:
                changePasswordState();
                break;
        }
    }

    private void register() {
        Intent it = new Intent(this, RegisterActivity2.class);
        startActivity(it);
    }

    private void login() {
        String num = mEditNum.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(password)) {
            showToast("手机号或密码不能为空");
            return;
        }

        DhNet dhNet = new DhNet(API2.login);
        dhNet.doPostInDialog("登陆中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("登陆成功");
                } else {
                    showToast("登陆失败");
                }
            }
        });

    }

    private void changePasswordState() {
        if (mEditPassword.getTransformationMethod() != null) {
            //显示密码
            mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //隐藏密码
            mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
