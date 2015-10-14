package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class RegisterActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private EditText mEditNum, mEditPassword, mEditValidation;
    private Button mBtnRegister;
    private TextView mTextGetValidation;

    private CountTimer mCountTimer;

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

        mCountTimer = new CountTimer(60000, 1000);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String num = mEditNum.getText().toString().trim();
        String verification = mEditValidation.getText().toString().trim();
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
                register(num, verification);
                break;
            case R.id.tv_get_code:
                getValidation(num);
                break;
        }
    }

    /**
     * 验证码的验证，成功跳转下一页
     *
     * @param num
     * @param code
     */
    private void register(final String num, final String code) {
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空");
            return;
        }

        DhNet dhNet = new DhNet(API2.verifyCode);
        dhNet.addParam("code", code);
        dhNet.doPostInDialog("验证中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    String password = mEditPassword.getText().toString().trim();
                    Intent it = new Intent(self, BasicInformationActivity2.class);
                    it.putExtra("phone", num);
                    it.putExtra("password", password);
                    it.putExtra("code", code);
                    startActivity(it);
                } else {
                    showToast("验证失败,请重试");
                }
            }
        });


    }

    /**
     * 获取验证码
     *
     * @param num
     */
    private void getValidation(String num) {
        DhNet dhNet = new DhNet(API2.getVarifacation + num + "/verification");
        dhNet.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    mCountTimer.start();
                    mTextGetValidation.setEnabled(false);
                } else {
                    showToast("获取验证码失败");
                    mTextGetValidation.setEnabled(true);
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
            mTextGetValidation.setEnabled(false);
            mTextGetValidation.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mTextGetValidation.setEnabled(true);
            mTextGetValidation.setText("重新发送");
        }
    }
}
