package com.gongpingjia.carplay.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.chat.AlertDialog;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.Utils;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * Created by Administrator on 2015/10/20.
 */
public class ActiveDialog extends Dialog {
    private Context mContext;
    String appointmentId;
    EditText edit_phone, edit_pwd;
    OnPickResultListener onPickResultListener;
    Button btn_finish, btn_get_verification;
    // private CountTimer mCountTimer;
    User user = User.getInstance();

    public ActiveDialog(Context context, String appointmentId) {
        super(context);
        this.mContext = context;
        this.appointmentId = appointmentId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dynamic);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        btn_get_verification = (Button) findViewById(R.id.btn_get_verification);
        btn_finish = (Button) findViewById(R.id.btn_finish);

        btn_get_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerification(edit_phone.getText().toString());
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_pwd.getText().toString())) {
                    Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                DhNet dnet = new DhNet(API2.CWBaseurl + "userId=" + user.getUserId() + "/binding?&token=" + user.getToken());
                dnet.addParam("phone", edit_phone.getText().toString());
                dnet.addParam("code", edit_pwd.getText().toString());
                dnet.doPostInDialog(new NetTask(mContext) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                              DhNet net = new DhNet(API2.CWBaseurl+"/application/"+appointmentId+"/process?userId="+user.getUserId() + "&token=" + user.getToken());
//                            DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb6d0cf224e7d878f695&token=a767ead8-7c00-4b90-b6de-9dcdb4d5bc41");
                            net.addParam("accept", true);
                            net.doPostInDialog(new NetTask(mContext) {
                                @Override
                                public void doInUI(Response response, Integer transfer) {
                                    if (response.isSuccess()) {
                                        showSelectedResult(1);
                                        dismiss();
                                        System.out.println("应邀：" + response.isSuccess());
                                    }
                                }
                            });
                        }
                    }
                });


            }
        });

    }

    public void getVerification(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidMobilePhoneNumber(phone)) {
            Toast.makeText(mContext, "手机格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        DhNet dhNet = new DhNet(API2.getVerification + phone + "/verification");
        dhNet.addParam("type", 0);
        dhNet.doGetInDialog("获取中...", new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
//                    mCountTimer.start();
                    Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_SHORT).show();
                } else {
//                    mCountTimer.cancel();
                    Toast.makeText(mContext, "验证码获取失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSelectedResult(int result) {
        if (onPickResultListener != null) {
            onPickResultListener.onResult(result);
        }
    }

    public interface OnPickResultListener {
        void onResult(int result);
    }

    public OnPickResultListener getOnPickResultListener() {
        return onPickResultListener;
    }

    public void setOnPickResultListener(
            OnPickResultListener onPickResultListener) {
        this.onPickResultListener = onPickResultListener;
    }

}
