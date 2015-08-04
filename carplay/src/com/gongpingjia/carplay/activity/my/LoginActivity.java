package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.util.Utils;

/**
 * 登录页面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends CarPlayBaseActivity {

    // 手机号
    private EditText PhoneNumEditText;

    // 密码
    private EditText PasswordEditText;

    // 登录
    private Button LoginButton;

    // 注册
    private LinearLayout login_register;

    // 忘记密码
    private TextView login_forgetpsw;

    public static final int Register = 1;

    public static LoginCallBack loginCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String action = getIntent().getStringExtra("action");
        if (action != null && action.equals("logout")) {
            ActivityTack.getInstanse().finishOthers(this);
        }
    }

    @Override
    public void initView() {
        PhoneNumEditText = (EditText) findViewById(R.id.ed_login_phone);
        PasswordEditText = (EditText) findViewById(R.id.ed_login_password);
        LoginButton = (Button) findViewById(R.id.button_login);
        LoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Utils.autoCloseKeyboard(self, arg0);
                final String strPhoneNum = PhoneNumEditText.getText().toString();
                final String strPassword = PasswordEditText.getText().toString();
                if (TextUtils.isEmpty(strPhoneNum)) {
                    Toast.makeText(self, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isValidMobilePhoneNumber(strPhoneNum)) {
                    Toast.makeText(self, "手机格式错误", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(strPassword)) {
                    Toast.makeText(self, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                DhNet net = new DhNet(API.login);
                net.addParam("phone", strPhoneNum);
                net.addParam("password", MD5Util.string2MD5(strPassword));
                net.doPost(new NetTask(self) {

                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            JSONObject jo = response.jSONFrom("data");
                            User user = User.getInstance();
                            user.setUserId(JSONUtil.getString(jo, "userId"));
                            user.setToken(JSONUtil.getString(jo, "token"));
                            user.setBrand(JSONUtil.getString(jo, "brand"));
                            user.setBrandLogo(JSONUtil.getString(jo, "brandLogo"));
                            user.setModel(JSONUtil.getString(jo, "model"));
                            user.setSeatNumber(JSONUtil.getInt(jo, "seatNumber"));
                            user.setIsAuthenticated(JSONUtil.getInt(jo, "isAuthenticated"));
                            user.setLogin(true);
                            CarPlayPerference per = IocContainer.getShare().get(CarPlayPerference.class);
                            per.phone = strPhoneNum;
                            per.password = MD5Util.string2MD5(strPassword);
                            per.commit();
                            self.finish();

                        }
                    }
                });
            }
        });
        // 忘记密码
        login_forgetpsw = (TextView) findViewById(R.id.login_forgetpsw);
        login_forgetpsw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(self, ForgetPwdActivity.class);
                startActivity(intent);
            }
        });
        // 注册
        login_register = (LinearLayout) findViewById(R.id.login_register);
        login_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(self, RegisterActivity.class);
                startActivityForResult(intent, Register);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Register) {
                finish();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (loginCall != null) {
            if (User.getInstance().isLogin()) {
                loginCall.onisLogin();
            } else {
                loginCall.onLoginFail();
            }
        }
        loginCall = null;
    }

}
