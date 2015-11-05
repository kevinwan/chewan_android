package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * 绑定手机号
 *
 * Created by Administrator on 2015/11/5.
 */
public class BoundPhoneActivity extends CarPlayBaseActivity implements View.OnClickListener{
    private EditText mEditPhone, mEditVerification, mEditPassword;
//    private CountTimer mCountTimer;
    private Button mBtnFinish, mBtnGetVerification;
    LinearLayout password_layout;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data2);
//        mCountTimer = new CountTimer(60 * 1000, 1000);
    }

    @Override
    public void initView() {
        setTitle("绑定手机号");
        mEditPassword = (EditText) findViewById(R.id.bphone_password);
        mEditPhone = (EditText) findViewById(R.id.bphone_phone);
        mEditVerification = (EditText) findViewById(R.id.bphone_verification);
         password_layout = (LinearLayout) findViewById(R.id.password_layout);
         txt = (TextView) findViewById(R.id.txt);
        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnGetVerification = (Button) findViewById(R.id.bphone_get_verification);

        mBtnGetVerification.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phone = mEditPhone.getText().toString().trim();
        String verification = mEditVerification.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        switch (view.getId()){
            case R.id.bphone_get_verification:
                getVerification(phone);
                break;
            case  R.id.btn_finish:

                break;
        }

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
//                    mCountTimer.start();
                    showToast("验证码发送成功");
                } else {
//                    mCountTimer.cancel();
//                    showToast(response.msg);
                }
            }
        });
    }

}
