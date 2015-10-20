package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2015/10/13.
 */
public class LoginActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private EditText mEditNum, mEditPassword;
    private Button mBtnLogin, mBtnRegister;
    private TextView mTextForgetPasswd;
    private ImageView mImgWeixin, mImgQQ, mImgWeibo;
    private CheckBox mImgShowOrHidePassword;


    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    // 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
    private UMQQSsoHandler qqSsoHandler;

    // 微信平台
    private UMWXHandler wxHandler;

    private String mUid, mAvatarUrl, mNickName, mChannel = "wechat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    @Override
    public void initView() {
        mEditNum = (EditText) findViewById(R.id.et_phone_num);
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mImgWeibo = (ImageView) findViewById(R.id.iv_weibo);
        mImgQQ = (ImageView) findViewById(R.id.iv_qq);
        mImgWeixin = (ImageView) findViewById(R.id.iv_weixin);
        mImgShowOrHidePassword = (CheckBox) findViewById(R.id.iv_switch);
        mTextForgetPasswd = (TextView) findViewById(R.id.tv_forget_password);

        mBtnRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTextForgetPasswd.setOnClickListener(this);

        mImgWeibo.setOnClickListener(this);
        mImgWeixin.setOnClickListener(this);
        mImgQQ.setOnClickListener(this);

        mImgShowOrHidePassword.setOnClickListener(this);


        wxHandler = new UMWXHandler(self, com.gongpingjia.carplay.api.Constant.WX_APP_KEY,
                com.gongpingjia.carplay.api.Constant.WX_APP_SECRET);
        qqSsoHandler = new UMQQSsoHandler(self, com.gongpingjia.carplay.api.Constant.QQ_APP_ID,
                com.gongpingjia.carplay.api.Constant.QQ_APP_KEY);

        qqSsoHandler.addToSocialSDK();
        wxHandler.addToSocialSDK();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;

            case R.id.iv_qq:
                doOauthVerify(SHARE_MEDIA.QQ);
                break;
            case R.id.iv_weibo:
                doOauthVerify(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_weixin:
                doOauthVerify(SHARE_MEDIA.WEIXIN);
                break;

            case R.id.tv_forget_password:
                Intent it = new Intent(self, ForgetPasswordActivity2.class);
                startActivity(it);
                break;

            case R.id.iv_switch:
                changePasswordState();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    //直接注册
    private void register() {
        Intent it = new Intent(this, RegisterActivity2.class);
        startActivity(it);
    }

    //手机号码登陆
    private void login() {
//        String num = mEditNum.getText().toString().trim();
//        String password = mEditPassword.getText().toString().trim();
//        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(password)) {
//            showToast("手机号或密码不能为空");
//            return;
//        }

        DhNet dhNet = new DhNet(API2.login);
        dhNet.addParam("phone", "18000000000");
        dhNet.addParam("password", MD5Util.string2MD5("123456"));
        dhNet.doPostInDialog("登陆中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("登陆成功");
                    JSONObject json = response.jSONFrom("data");
                    User user = User.getInstance();
                    try {
                        user.setUserId(json.getString("userId"));
                        user.setToken(json.getString("token"));
                        user.setBrand(json.getString("brand"));
                        user.setBrandLogo(json.getString("brandLogo"));
                        user.setHeadUrl(json.getString("avatar"));
                        user.setNickName(json.getString("nickname"));
                        user.setModel(json.getString("model"));
                        User.getInstance().setLogin(true);

//                            LoginEB loginEB = new LoginEB();
//                            loginEB.setIslogin(true);
//                            EventBus.getDefault().post(loginEB);

//                            loginHX(MD5Util.string2MD5(JSONUtil.getString(json, "userId")),
//                                    MD5Util.string2MD5(per.thirdId + per.channel + "com.gongpingjia.carplay"),
//                                    json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                } else {
                    showToast("登陆失败");
                }
            }
        });
    }

    //三方登陆
    private void loginSns() {
        DhNet net = new DhNet(API2.snsLogin);
        String password = MD5Util.string2MD5(mUid + mChannel + "com.gongpingjia.carplay");
        net.addParam("nickname", mNickName);
        net.addParam("avatar", mAvatarUrl);
        net.addParam("uid", mUid);
        net.addParam("channel", mChannel);
        net.addParam("password", password);
        net.doPostInDialog("跳转中...", new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject json = response.jSONFrom("data");
                    if (!json.has("token")) {
                        //数据库没有该用户
                        Intent it = new Intent(self, BasicInformationActivity2.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("avatarUrl", mAvatarUrl);
                        bundle.putString("nickname", mNickName);
                        bundle.putString("uid", mUid);
                        bundle.putString("channel", mChannel);
                        try {
                            bundle.putString("avatar", json.getString("avatar"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        it.putExtras(bundle);
                        startActivity(it);
                    } else {
                        //数据库中有该用户
                        showToast("登录成功");
                        CarPlayPerference per = IocContainer.getShare().get(CarPlayPerference.class);
                        per.load();
                        per.thirdId = mUid;
                        per.channel = mChannel;
                        per.commit();

                        User user = User.getInstance();
                        try {
                            user.setUserId(json.getString("userId"));
                            user.setToken(json.getString("token"));
                            user.setBrand(json.getString("brand"));
                            user.setBrandLogo(json.getString("brandLogo"));
                            user.setHeadUrl(json.getString("avatar"));
                            user.setNickName(json.getString("nickname"));
                            user.setModel(json.getString("model"));
                            User.getInstance().setLogin(true);

//                            LoginEB loginEB = new LoginEB();
//                            loginEB.setIslogin(true);
//                            EventBus.getDefault().post(loginEB);

//                            loginHX(MD5Util.string2MD5(JSONUtil.getString(json, "userId")),
//                                    MD5Util.string2MD5(per.thirdId + per.channel + "com.gongpingjia.carplay"),
//                                    json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        从退出登陆过来
//                        if (isFromLogout) {
//                            Intent it = new Intent(self, MainActivity.class);
//                            startActivity(it);
//                        }
                        Intent it = new Intent(self, MainActivity2.class);
                        startActivity(it);
                        self.finish();
                    }
                } else {
                    showToast("登录失败");
                }
            }
        });
    }


    //改变密码的状态
    private void changePasswordState() {
        if (!mImgShowOrHidePassword.isChecked()) {
            //显示密码
            mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mImgShowOrHidePassword.setBackgroundResource(R.drawable.switch_right);
        } else {
            //隐藏密码
            mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgShowOrHidePassword.setBackgroundResource(R.drawable.switch_left);
        }
    }


    //三方登陆按钮,如果注册过则直接登陆，否则跳转到注册页面
    private void doOauthVerify(SHARE_MEDIA media) {
        mController.doOauthVerify(self, media, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA arg0) {
                showProgressDialog("加载中...");
            }

            @Override
            public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
                hidenProgressDialog();
                showToast("授权失败");
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA media) {
                mUid = value.getString("uid");
                hidenProgressDialog();
                getUserInfo(media);
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                hidenProgressDialog();
                showToast("授权取消");
            }
        });
    }

    private void getUserInfo(final SHARE_MEDIA media) {
        mController.getPlatformInfo(self, media, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {
                showProgressDialog("正在读取信息...");
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                hidenProgressDialog();
                if (status == 200 && info != null) {
                    switch (media) {
                        case WEIXIN:
                            mNickName = (String) info.get("nickname");
                            mAvatarUrl = (String) info.get("headimgurl");
                            mChannel = "wechat";
                            break;

                        case SINA:
                            mNickName = (String) info.get("screen_name");
                            mAvatarUrl = (String) info.get("profile_image_url");
                            mChannel = "sinaWeibo";
                            break;

                        case QQ:
                            mNickName = (String) info.get("screen_name");
                            mAvatarUrl = (String) info.get("profile_image_url");
                            mChannel = "qq";
                            break;

                        default:
                            break;
                    }
                    loginSns();
                } else {
                    showToast("获取用户信息失败" + status);
                }
            }
        });
    }

}
