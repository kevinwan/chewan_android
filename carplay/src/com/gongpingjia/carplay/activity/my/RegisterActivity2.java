package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    private Button mBtnGetVerification;

    private CountTimer mCountTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    }

    @Override
    public void initView() {
        setTitle("注册");
        ImageView close = (ImageView) findViewById(R.id.back);
        close.setImageResource(R.drawable.icon_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEditNum = (EditText) findViewById(R.id.et_phone_num);
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnGetVerification = (Button) findViewById(R.id.tv_get_code);
        mEditValidation = (EditText) findViewById(R.id.et_verification_code);

        mBtnRegister.setOnClickListener(this);
        mBtnGetVerification.setOnClickListener(this);

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
        final String password = mEditPassword.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }
        if (password.length()<6||password.length()>15) {
            showToast("密码为6-15位字母和数字的组合");
            return;
        }
        if (!isValidPassword(password)) {
            showToast("密码为6-15位字母和数字的组合");
            return;
        }

        DhNet dhNet = new DhNet(API2.verifyCode + "/" + num + "/verification");
        dhNet.addParam("code", code);
        dhNet.doPostInDialog("验证中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    Intent it = new Intent(self, BasicInformationActivity2.class);
                    it.putExtra("phone", num);
                    it.putExtra("password", password);
                    it.putExtra("code", code);
                    startActivity(it);
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
        DhNet dhNet = new DhNet(API2.getVerification + num + "/verification");
        dhNet.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    mCountTimer.start();
                    mBtnGetVerification.setEnabled(false);
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

    public boolean isValidPassword(String str){
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for(int i=0 ; i<str.length() ; i++) { //循环遍历字符串
            if (Character.isDigit(str.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        if (isDigit&&isLetter)
        return true;
        return false;
    }
}
