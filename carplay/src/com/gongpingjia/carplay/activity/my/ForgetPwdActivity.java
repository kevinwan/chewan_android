package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.util.Utils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 找回密码
 * 
 * @author Administrator
 * 
 */
public class ForgetPwdActivity extends CarPlayBaseActivity implements OnClickListener {

    private Button mNext = null;

    EditText ed_forget_phone;

    EditText ed_forget_verification;

    Button button_forget_verification;

//    TimeCount time;

    String strPhone;

    View mVariCodeView;

    public static final int Forgetpwd = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data2);

    }

    @Override
    public void initView() {
        setTitle("找回密码");
//        ed_forget_phone = (EditText) findViewById(R.id.ed_forget_phone);
//        ed_forget_verification = (EditText) findViewById(R.id.ed_forget_verification);
//        button_forget_verification = (Button) findViewById(R.id.button_forget_verification);
//        mNext = (Button) findViewById(R.id.next);
//        time = new TimeCount(60000, 1000);
//        mNext.setOnClickListener(this);
//        button_forget_verification.setOnClickListener(this);
//        mVariCodeView = findViewById(R.id.layout_varification_code);

    }

//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onFinish() {
//            button_forget_verification.setText("重新发送");
//            button_forget_verification.setEnabled(true);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            button_forget_verification.setEnabled(false);
//            button_forget_verification.setText(millisUntilFinished / 1000 + "s");
//        }
//    }

    @Override
    public void onClick(View arg0) {
//        switch (arg0.getId()) {
//        case R.id.next:// 下一步
//            strPhone = ed_forget_phone.getText().toString();
//            final String strVerification = ed_forget_verification.getText().toString();
//            if (TextUtils.isEmpty(strPhone)) {
//                showToast("手机号不能为空");
//                return;
//            }
//            if (!Utils.isValidMobilePhoneNumber(strPhone)) {
//                showToast("手机格式错误");
//                return;
//            }
//            if (TextUtils.isEmpty(strVerification)) {
//                showToast("请输入验证码");
//                return;
//            }
//
//            String url1 = API.CWBaseurl+"/phone/"+ strPhone + "/verification";
//            DhNet net = new DhNet(url1);
//            net.addParam("code", strVerification);
//            net.addParam("type", 1);
//            net.doPostInDialog("验证中...", new NetTask(self) {
//
//                @Override
//                public void doInUI(Response response, Integer transfer) {
//                    if (response.isSuccess()) {
//                        Intent intent = new Intent(self, PwdNextActivity.class);
//                        intent.putExtra("PhoneNum", strPhone);
//                        intent.putExtra("Verification", strVerification);
//                        startActivityForResult(intent, Forgetpwd);
//                    } else {
//                        showToast("验证码错误请重新输入");
//                        AnimatorSet animator = new AnimatorSet();
//                        animator.setDuration(500);
//                        animator.playTogether(ObjectAnimator.ofFloat(mVariCodeView, "translationX", 0, 25, -25, 25,
//                                -25, 15, -15, 6, -6, 0));
//                        animator.start();
//                    }
//                }
//            });
//            break;
//
//        case R.id.button_forget_verification:// 获取验证码
//            ed_forget_verification.setText("");
//            strPhone = ed_forget_phone.getText().toString();
//            if (TextUtils.isEmpty(strPhone)) {
//                showToast("请输入手机号码");
//                return;
//            } else {
//                if (!Utils.isValidMobilePhoneNumber(strPhone)) {
//                    showToast("请输入正确的手机号码");
//                    return;
//                }
//            }
//
//            String url = API.CWBaseurl+"/phone/"+ strPhone + "/verification";
//            DhNet codeNet = new DhNet(url);
//            codeNet.addParam("type", 1);
//            codeNet.doGetInDialog("获取中...", new NetTask(self) {
//
//                @Override
//                public void doInUI(Response response, Integer transfer) {
//                    if (response.isSuccess()) {
//                        time.start();
//                        button_forget_verification.setEnabled(false);
//                    } else {
//                        button_forget_verification.setEnabled(true);
//                        showToast(response.msg);
//                    }
//                }
//            });
//            break;
//
//        default:
//            break;
//        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == Forgetpwd) {
//                Intent intent = new Intent(self, LoginActivity.class);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            }
//        }
//    }

}
