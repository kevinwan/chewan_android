package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * Created by Administrator on 2015/10/14.
 */
public class ForgetPasswordActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private EditText mEditPhone, mEditVerification, mEditPassword;
    private CountTimer mCountTimer;
    private Button mBtnFinish, mBtnGetVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd2);
        mCountTimer = new CountTimer(60 * 1000, 1000);
    }

    @Override
    public void initView() {
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mEditPhone = (EditText) findViewById(R.id.et_phone);
        mEditVerification = (EditText) findViewById(R.id.et_verification);

        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnGetVerification = (Button) findViewById(R.id.btn_get_verification);

        mBtnGetVerification.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String phone = mEditPhone.getText().toString().trim();
        String verification = mEditVerification.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        switch (id) {
            case R.id.btn_get_verification:
                getVerification(phone);
                break;
            case R.id.btn_finish:
                resetPassword(phone, verification, password);
                break;
        }
    }

    private void resetPassword(final String phone, String verification, final String password) {

        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(verification)) {
            showToast("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }

        DhNet dhNet = new DhNet(API2.forgetPassword);
        dhNet.addParam("phone", phone);
        dhNet.addParam("code", verification);
        dhNet.addParam("password", MD5Util.string2MD5(password));
        dhNet.doPostInDialog("修改中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("修改成功");
                    CarPlayPerference per = IocContainer.getShare().get(CarPlayPerference.class);
                    per.load();
                    per.phone = phone;
                    per.password = password;
                    per.commit();
                    finish();
                } else {
                    showToast("修改失败");
                }
            }
        });
    }

    public void getVerification(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }
        DhNet dhNet = new DhNet(API2.getVerification + phone + "/verification");
        dhNet.addParam("type", 1);
        dhNet.doGetInDialog("获取中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    mCountTimer.start();
                    showToast("验证码发送成功");
                } else {
                    mCountTimer.cancel();
                    showToast("验证码获取失败，请重试");
                }
            }
        });
    }


    class CountTimer extends CountDownTimer {

        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetVerification.setEnabled(false);
            mBtnGetVerification.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mBtnGetVerification.setEnabled(true);
            mBtnGetVerification.setText("重新发送");
        }
    }
}
