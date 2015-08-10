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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.TermsActivity;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.util.Utils;

/***
 * 注册页面
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends CarPlayBaseActivity implements OnClickListener {
    /** 手机号框 */
    private EditText phoneEt = null;

    /** 验证码框 */
    private EditText codeEt = null;

    /** 密码框 */
    private EditText pswdEt = null;

    /** 验证码按钮 */
    private Button codeBtn = null;

    /** 下一步 */
    private Button nextBtn = null;

    /** 车玩服务条款 */
    private LinearLayout cwfwtk = null;

    /** check选择 */
    private CheckBox ckeck;

    TimeCount time;

    public static final int BasicMessage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    @Override
    public void initView() {
        setTitle("注册");
        phoneEt = (EditText) findViewById(R.id.phone);
        codeEt = (EditText) findViewById(R.id.code);
        pswdEt = (EditText) findViewById(R.id.pswd);
        codeBtn = (Button) findViewById(R.id.btn_code);
        nextBtn = (Button) findViewById(R.id.next);
        cwfwtk = (LinearLayout) findViewById(R.id.cwfwtk);
        ckeck = (CheckBox) findViewById(R.id.checked);

        codeBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        cwfwtk.setOnClickListener(this);

        time = new TimeCount(60000, 1000);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.btn_code:
            getCode();
            break;
        case R.id.next:
            nextStep();
            break;
        case R.id.cwfwtk:
            Intent intent = new Intent(self, TermsActivity.class);
            startActivity(intent);
            break;

        default:
            break;
        }
    }

    private void nextStep() {
        final String strPhone = phoneEt.getText().toString();
        if (TextUtils.isEmpty(strPhone)) {
            showToast("手机号不能为空");
            return;
        } else {
            if (!Utils.isValidMobilePhoneNumber(strPhone)) {
                showToast("手机格式错误");
                return;
            }
        }

        final String strCaptcha = codeEt.getText().toString().trim();
        if (TextUtils.isEmpty(strCaptcha)) {
            showToast("验证码不能为空哦");
            return;
        }

        final String strPassword = pswdEt.getText().toString();
        if (TextUtils.isEmpty(strPassword)) {
            showToast("密码不能为空哦");
            return;
        } else {
            if (strPassword.length() < 6 || strPassword.length() > 16) {
                showToast("密码必须为6-16位数字和字母组合");
                return;
            }
        }

        if (!ckeck.isChecked()) {
            showToast("请阅读并同意车玩服务条款");
            return;
        }
        checkCode(strPhone, strCaptcha);
    }

    private void getCode() {
        codeEt.setText("");
        final String strPhone = phoneEt.getText().toString();
        if (TextUtils.isEmpty(strPhone)) {
            showToast("手机号不能为空");
            return;
        } else {
            if (!Utils.isValidMobilePhoneNumber(strPhone)) {
                showToast("手机格式错误");
                return;
            }
        }

        DhNet net = new DhNet(API.checkCode + strPhone + "/verification");
        net.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    time.start();
                    codeBtn.setEnabled(false);
                } else {
                    codeBtn.setEnabled(true);
                    showToast("手机号已被注册");
                }
            }
        });
    }

    public void checkCode(final String phone, final String code) {
        DhNet net = new DhNet(API.checkCode + phone + "/verification");
        net.addParam("code", code);
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    Intent it = new Intent(self, BasicMessageActivity.class);
                    it.putExtra("phone", phone);
                    it.putExtra("code", code);
                    it.putExtra("pswd", MD5Util.string2MD5(pswdEt.getText().toString()));
                    startActivityForResult(it, BasicMessage);
                } else {
                    showToast("验证码错误!");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == BasicMessage) {
                Intent it = getIntent();
                setResult(Activity.RESULT_OK, it);
                finish();
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            codeBtn.setText("重新发送");
            codeBtn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            codeBtn.setEnabled(false);
            codeBtn.setText(millisUntilFinished / 1000 + "s");
        }
    }

}
