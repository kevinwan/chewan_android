package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
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

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

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

    private boolean isFromLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        String action = getIntent().getStringExtra("action");
        if (action != null && action.equals("logout")) {
            isFromLogout = true;
            ActivityTack.getInstanse().finishOthers(this);
        }
        User.getInstance().setLogin(false);
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
        final String num = mEditNum.getText().toString().trim();
        final String password = mEditPassword.getText().toString().trim();
        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(password)) {
            showToast("手机号或密码不能为空");
            return;
        }


//        final String num = "18000000001";
//        final String password = "123456";

//        final String num = "18000000001";
//      //final Strin//password = "123456";
//        final Strin//num = mEditNum.getText().toString().trim();
//        final String num = mEditNum.getText().toString().trim();
//        final String password = mEditPassword.getText().toString().trim();
//        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(password)) {
//            showToast("手机号或密码不能为空");
//            return;
//        }
//        final String num = "18000000001";
//        final String password = "123456";
//        num = "18362971169";


        DhNet dhNet = new DhNet(API2.login);
        dhNet.addParam("phone", num);
        dhNet.addParam("password", MD5Util.string2MD5(password));
        showProgressDialog("登录中...");
        dhNet.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject json = response.jSONFrom("data");

                    loginHX(MD5Util.string2MD5(JSONUtil.getString(json, "userId")),
                            MD5Util.string2MD5(password),
                            json, num);
                    //                        user.setUserId(json.getString("userId"));
//                        user.setToken(json.getString("token"));
//                        user.setBrand(json.getString("brand"));
//                        user.setBrandLogo(json.getString("brandLogo"));
//                        user.setHeadUrl(json.getString("avatar"));
//                        user.setNickName(json.getString("nickname"));
//                        user.setModel(json.getString("model"));
//                        User.getInstance().setLogin(true);

//                            LoginEB loginEB = new LoginEB();
//                            loginEB.setIslogin(true);
//                            EventBus.getDefault().post(loginEB);

//                            loginHX(MD5Util.string2MD5(JSONUtil.getString(json, "userId")),
//                                    MD5Util.string2MD5(per.thirdId + per.channel + "com.gongpingjia.carplay"),
//                                    json);
                } else {
                    hidenProgressDialog();
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
        showProgressDialog("跳转中...");
        net.doPost(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject json = response.jSONFrom("data");
                    if (!json.has("token")) {
                        hidenProgressDialog();
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
                        loginHX(MD5Util.string2MD5(JSONUtil.getString(json, "userId")),
                                MD5Util.string2MD5(mUid + mChannel + "com.gongpingjia.carplay"),
                                json, null);


                        //                        从退出登陆过来
//                        if (isFromLogout) {
//                            Intent it = new Intent(self, MainActivity.class);
//                            startActivity(it);
//                        }
                    }
                } else {
                    hidenProgressDialog();
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


    private void loginHX(String currentUsername, String currentPassword, final JSONObject jo, final String phone) {
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            /**
             *
             */
            @Override
            public void onSuccess() {

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    initializeContacts();


                    User user = User.getInstance();

                    user.setUserId(jo.getString("userId"));
                    user.setToken(jo.getString("token"));
                    user.setHeadUrl(jo.getString("avatar"));
                    user.setNickName(jo.getString("nickname"));
                    user.setLicenseAuthStatus("认证通过".equals(jo.getString("licenseAuthStatus")));
                    user.setPhotoAuthStatus("认证通过".equals(jo.getString("photoAuthStatus")));
                    user.setEmName(jo.getString("emchatName"));
                    user.setPhone(mEditNum.getText().toString().trim());
                    JSONArray jsa = JSONUtil.getJSONArray(jo, "album");
                    user.setHasAlbum(jsa != null && jsa.length() != 0);
//                    user.setHasAlbum(jsa.length() == 0 ? false : true);
                    User.getInstance().setLogin(true);

                    LoginEB loginEB = new LoginEB();
                    loginEB.setIslogin(true);
                    EventBus.getDefault().post(loginEB);
                    LoginActivity.asyncFetchGroupsFromServer();
                    CarPlayPerference per = IocContainer.getShare().get(CarPlayPerference.class);
                    per.load();
                    per.thirdId = mUid;
                    per.channel = mChannel;
                    if (phone != null) {
                        per.phone = phone;
                    }
                    per.commit();
                    hidenProgressDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showToast("登录成功!");
                        }
                    });
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                    self.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    hidenProgressDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            DemoHXSDKHelper.getInstance().logout(true, null);
                            showToast(getString(R.string.login_failure_failed));

                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance()
                        .updateCurrentUserNick(User.getInstance().getNickName());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                // if (!LoginActivity.this.isFinishing() &&
                // pd.isShowing()) {
                // pd.dismiss();
                // }
                // 进入主页面

            }

            @Override
            public void onProgress(int progress, String status) {
                hidenProgressDialog();
            }

            @Override
            public void onError(final int code, final String message) {
                hidenProgressDialog();
            }
        });

    }


    private void initializeContacts() {
        Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
        // 添加user"申请与通知"
        ChatUser newFriends = new ChatUser();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        ChatUser groupUser = new ChatUser();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        ChatUser robotUser = new ChatUser();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(LoginActivity2.this);
        List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
        dao.saveContactList(users);
    }
}
