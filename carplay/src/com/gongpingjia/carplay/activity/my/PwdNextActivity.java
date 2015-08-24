package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 找回密码(设置新密码)页面 hqh
 * 
 * @author Administrator
 * 
 */
public class PwdNextActivity extends CarPlayBaseActivity {
    // 新密码
    private EditText ed_new_pwd, mConfirmEdit;

    // 完成
    private Button button_new_pwd;

    // 手機號
    private String phone;

    // 验证码
    private String code;

    private View mLayoutPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_next);

        setTitle("找回密码");
    }

    @Override
    public void initView() {
        ed_new_pwd = (EditText) findViewById(R.id.ed_new_pwd);
        mConfirmEdit = (EditText) findViewById(R.id.et_confirm_passwd);
        button_new_pwd = (Button) findViewById(R.id.button_new_pwd);
        mLayoutPasswd = findViewById(R.id.layout_passwd);
        Intent intent = getIntent();
        // 手機號
        phone = intent.getStringExtra("PhoneNum");
        // 验证码
        code = intent.getStringExtra("Verification");
        button_new_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String strpwd = ed_new_pwd.getText().toString();
                final String confirmPwd = mConfirmEdit.getText().toString();
                if (TextUtils.isEmpty(strpwd) || TextUtils.isEmpty(confirmPwd)) {
                    showToast("密码不能为空");
                }
                if (strpwd.length() < 6 || strpwd.length() > 16 || confirmPwd.length() < 6 || confirmPwd.length() > 16) {
                    showToast("密码长度应在6-16之间，请重新输入");
                    return;
                }
                if (!confirmPwd.equals(strpwd)) {
                    showToast("两次输入密码不一致，请重新输入");
                    AnimatorSet animator = new AnimatorSet();
                    animator.setDuration(500);
                    animator.playTogether(ObjectAnimator.ofFloat(mLayoutPasswd, "translationX", 0, 25, -25, 25, -25,
                            15, -15, 6, -6, 0));
                    animator.start();
                    return;
                }
                DhNet net = new DhNet(API.newPassword);
                net.addParam("phone", phone);
                System.out.println(phone + code + strpwd);
                net.addParam("code", code);
                net.addParam("password", MD5Util.string2MD5(strpwd));
                net.doPost(new NetTask(self) {

                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                        	showToast("密码修改成功!");
                            JSONObject jo = response.jSONFrom("data");
                            User user = User.getInstance();
                            // JSONUtil.getString(jo, "userId")
                            // JSONUtil.getString(jo, "token")
                            user.setUserId("");
                            user.setToken("");

                            CarPlayPerference per = IocContainer.getShare().get(CarPlayPerference.class);
                            per.phone = phone;
                            per.password = MD5Util.string2MD5(confirmPwd);
                            per.commit();

                            Intent intent = new Intent(self, ForgetPwdActivity.class);

                            setResult(Activity.RESULT_OK, intent);

                            Intent it = new Intent(self, LoginActivity.class);
                            it.putExtra("action", "logout");
                            startActivity(it);
                            finish();
                        } else {
                            showToast("该用户不存在");
                        }

                    }
                });
            }
        });
    }
}
