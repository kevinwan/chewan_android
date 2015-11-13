package com.gongpingjia.carplay.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.Utils;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;


/**
 *
 * 活动动态dialog
 * Created by Administrator on 2015/10/19.
 */
public class ActivityDialog implements View.OnClickListener {
    private Context mContext;
    private AlertDialog mDialog;
    String appointmentId;
    EditText edit_phone,edit_pwd;
    OnPickResultListener onPickResultListener;
    Button btn_finish,btn_get_verification;
//    private CountTimer mCountTimer;
    User user = User.getInstance();
    public ActivityDialog(Context context,String appointmentId) {
        this.mContext = context;
        this.appointmentId = appointmentId;

    }
    public void show() {
//        mCountTimer = new CountTimer(60 * 1000, 1000);
        AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.activity_dynamic, null);

         edit_phone  = (EditText) view.findViewById(R.id.edit_phone);
         edit_pwd  = (EditText) view.findViewById(R.id.edit_pwd);
         btn_get_verification = (Button) view.findViewById(R.id.btn_get_verification);
         btn_finish = (Button) view.findViewById(R.id.btn_finish);


        btn_get_verification.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        dlg.setView(view);
        mDialog = dlg.create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setWindowAnimations(R.style.mystyle);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.show();
    }


    private void dismiss() {
        mDialog.dismiss();
    }

    public void getVerification(String phone) {
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(mContext,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidMobilePhoneNumber(phone)) {
            Toast.makeText(mContext,"手机格式错误",Toast.LENGTH_SHORT).show();
            return;
        }
        DhNet dhNet = new DhNet(API2.getVerification+phone+"/verification");
        dhNet.addParam("type", 0);
        dhNet.doGetInDialog("获取中...", new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
//                    mCountTimer.start();
                    Toast.makeText(mContext,"验证码发送成功",Toast.LENGTH_SHORT).show();
                } else {
//                    mCountTimer.cancel();
                    Toast.makeText(mContext,"验证码获取失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finish:
                if (TextUtils.isEmpty(edit_phone.getText().toString())) {
                    Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                DhNet net = new DhNet(API2.CWBaseurl+"/application/"+appointmentId+"/process?userId="+user.getUserId()+"&token="+user.getToken());
                net.addParam("accept", "true");
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
                break;
            case R.id.btn_get_verification:
                getVerification(edit_phone.getText().toString());
                break;
        }
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
