package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

/**
 * 修改密码
 * Created by Administrator on 2015/10/30.
 */
public class RevisePassword extends CarPlayBaseActivity implements View.OnClickListener {
    LinearLayout old_layout, new_layout, onceagain_layout;
    Button btn_yes;
    EditText et_password, et_newpassword, et_againpassword;
    CarPlayPerference per;
    String pwd;
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();
        pwd = per.password;
        System.out.println("旧密码+++++++++++++++"+pwd);
    }

    @Override
    public void initView() {
        setTitle("修改密码");
        old_layout = (LinearLayout) findViewById(R.id.old_layout);
        new_layout = (LinearLayout) findViewById(R.id.new_layout);
        onceagain_layout = (LinearLayout) findViewById(R.id.onceagain_layout);
        et_password = (EditText) findViewById(R.id.et_password);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_againpassword = (EditText) findViewById(R.id.et_againpassword);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String oldPassword = MD5Util.string2MD5(et_password.getText().toString().trim());
        String newPassword = et_newpassword.getText().toString();
        String againPassword = et_againpassword.getText().toString();
        switch (view.getId()) {
            case R.id.btn_yes:
                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(againPassword)) {
                    showToast("请输入就密码");
                    return;
                }
                if (!oldPassword.equals(pwd)) {
                    System.out.println("密码"+MD5Util.string2MD5(oldPassword));
                    showToast("旧密码不正确，请重新输入");
                    return;
                }
                if (oldPassword.equals(newPassword)) {
                    showToast("新密码和旧密码不能一致，请重新输入");
                    return;
                }
                if (!newPassword.equals(againPassword)) {
                    showToast("新密码两次输入不一致，请重新输入");
                    return;
                }
                if ( newPassword.length() < 6 || newPassword.length() > 20 || againPassword.length() < 6 || againPassword.length() > 20) {
                    showToast("密码长度应在6-20之间，请重新输入");
                    return;
                }
                DhNet net = new DhNet(API2.CWBaseurl+"user/"+user.getUserId()+"/password?token="+user.getToken());
                net.addParam("old",MD5Util.string2MD5(oldPassword));
                net.addParam("new",MD5Util.string2MD5(newPassword));
                net.doPostInDialog(new NetTask(self) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()){
                            JSONObject js = response.jSONFromData();
                            String phone = JSONUtil.getString(js, "phone");
                            String password = JSONUtil.getString(js,"password");
                            per.phone = phone;
                            per.password = password;
                            per.commit();
                        }

                    }
                });
                break;
        }
    }
}
