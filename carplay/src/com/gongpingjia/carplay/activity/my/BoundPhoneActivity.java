package com.gongpingjia.carplay.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.view.dialog.BindPhoneDialog;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 绑定手机号
 * <p/>
 * Created by Administrator on 2015/11/5.
 */
public class BoundPhoneActivity extends CarPlayBaseActivity implements View.OnClickListener {
    private EditText mEditPhone, mEditVerification, mEditPassword;
    private CountTimer mCountTimer;
    private Button mBtnFinish, mBtnGetVerification;
    LinearLayout password_layout;
    TextView txt;

    boolean phoneisRegister = true;

    EditText bphone_passwordE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_phone);
        mCountTimer = new CountTimer(60 * 1000, 1000);
        //BoundPhoneDialog   手机已被注册后弹出dialog
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
        bphone_passwordE = (EditText) findViewById(R.id.bphone_password);
        mBtnGetVerification.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
        TextWatcher mEditText = new TextWatcher() {
            private CharSequence temp;


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditPhone.getText().length() == 11) {
                    getPhoneIsRegister(mEditPhone.getText().toString());
                }

            }
        };
        mEditPhone.addTextChangedListener(mEditText);


    }


    @Override
    public void onClick(View view) {
        String phone = mEditPhone.getText().toString().trim();
        String verification = mEditVerification.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.bphone_get_verification:
                if (TextUtils.isEmpty(phone)) {
                    showToast("手机号不能为空");
                    return;
                }

                if (phone.length() != 11) {
                    showToast("手机号不合法");
                    return;
                }
                getCode(phone);
//                getVerification(phone);
                break;
            case R.id.btn_finish:
                if (TextUtils.isEmpty(verification)) {
                    showToast("请输入验证码!");
                    return;
                }
                if (!phoneisRegister) {
                    if (TextUtils.isEmpty(password)) {
                        showToast("请输入密码!");
                        return;
                    }

                    if (password.length() < 6 || password.length() > 15) {
                        showToast("密码为6-15位字母和数字的组合");
                        return;
                    }

                    if (!CarPlayUtil.isValidPassword(password)) {
                        showToast("密码为6-15位字母和数字的组合");
                        return;
                    }

                }
                showProgressDialog("绑定中...");
                checkCode(verification, phone);


                break;
        }

    }

    public void getVerification(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }

        if (phone.length() != 11) {
            showToast("手机号不合法");
            return;
        }
        getPhoneIsRegister(phone);

    }

    class CountTimer extends CountDownTimer {

        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetVerification.setEnabled(false);
            mBtnGetVerification.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mBtnGetVerification.setEnabled(true);
            mBtnGetVerification.setText("重新发送");
        }
    }


    private void getPhoneIsRegister(final String phone) {
        DhNet net = new DhNet(API2.phoneisRegister(phone));
        net.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    phoneisRegister = JSONUtil.getBoolean(jo, "exist");
                    findViewById(R.id.password_layout).setVisibility(phoneisRegister ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    private void getCode(String phone) {
        DhNet dhNet = new DhNet(API2.getVerification + phone + "/verification");
        if (phoneisRegister) {
            dhNet.addParam("type", 1);
        } else {
            dhNet.addParam("type", 0);
        }
        dhNet.doGetInDialog("获取中...", new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    mCountTimer.start();
                    showToast("验证码发送成功");
                } else {
                    mCountTimer.cancel();
//                    showToast(response.msg);
                }
            }
        });
    }

    private void bindPhone(String phone, String code) {
        DhNet net = new DhNet(API2.bindPhone);
        Intent it = getIntent();
        net.addParam("uid", it.getStringExtra("uid"));
        net.addParam("channel", it.getStringExtra("channel"));
        net.addParam("snsPassword", MD5Util.string2MD5(getIntent()
                .getStringExtra("uid")
                + getIntent().getStringExtra("channel")
                + "com.gongpingjia.carplay"));
        net.addParam("phone", phone);
        net.addParam("code", code);

        net.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    //三方登录完善信息
                    JSONObject jo = response.jSONFromData();
                    loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
                            "userId")), JSONUtil.getString(jo, "password"), jo, false);


                } else {
                    hidenProgressDialog();
                }
            }
        });
    }

    private void checkCode(final String code, final String phone) {
        String url = API2.CWBaseurl + "/phone/" + phone + "/verification";
        DhNet net = new DhNet(url);
        net.addParam("code", code);
        if (phoneisRegister) {
            net.addParam("type", 1);
        }
        net.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    if (phoneisRegister) {
                        String des = null;
                        String chanel = getIntent().getStringExtra("channel");
                        if (chanel.equals("wechat")) {
                            des = "绑定后可用微信号登录";
                        } else if (chanel.equals("sinaWeibo")) {
                            des = "绑定后可用新浪微博号登录";
                        } else if (chanel.equals("qq")) {
                            des = "绑定后可用QQ号登录";
                        }
                        BindPhoneDialog dialog = new BindPhoneDialog(self, des);
                        dialog.setOnCLickResult(new BindPhoneDialog.OnCLickResult() {
                            @Override
                            public void clickResult() {
                                showProgressDialog("绑定中...");
                                bindPhone(phone, mEditVerification.getText().toString());
                            }
                        });
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                hidenProgressDialog();
                            }
                        });
                        dialog.show();
                    } else {
                        hidenProgressDialog();
                        Intent it = new Intent(self, BasicInformationActivity2.class);
                        Intent oldit = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phone);
                        bundle.putString("code", code);
                        bundle.putString("password", mEditPassword.getText().toString());
                        bundle.putString("type", "thirdlogin");


                        bundle.putString("avatarUrl", oldit.getStringExtra("avatarUrl"));
                        bundle.putString("nickname", oldit.getStringExtra("nickname"));
                        bundle.putString("uid", oldit.getStringExtra("uid"));
                        bundle.putString("channel", oldit.getStringExtra("channel"));
                        bundle.putString("avatar", oldit.getStringExtra("avatar"));
                        it.putExtras(bundle);
                        startActivity(it);
                    }
                } else {
                    hidenProgressDialog();
                }
            }
        });
    }


    private void loginHX(String currentUsername, String currentPassword,
                         final JSONObject jo, final boolean isphonelogin) {
        EMChatManager.getInstance().login(currentUsername, currentPassword,
                new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        hidenProgressDialog();
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
                            user.setLicenseAuthStatus("认证通过".equals(jo.getString("licenseAuthStatus")));
                            user.setPhotoAuthStatus("认证通过".equals(jo.getString("photoAuthStatus")));
                            user.setEmName(jo.getString("emchatName"));
                            JSONArray jsa = JSONUtil.getJSONArray(jo, "album");
                            user.setHasAlbum(jsa != null && jsa.length() != 0);
//                    user.setHasAlbum(jsa.length() == 0 ? false : true);
                            user.setGender(JSONUtil.getString(jo, "gender"));
                            user.setAge(JSONUtil.getInt(jo, "age"));


                            user.setToken(JSONUtil.getString(jo, "token"));
                            user.setUserId(JSONUtil.getString(jo, "userId"));
                            user.setNickName(JSONUtil.getString(jo, "nickname"));
                            user.setHeadUrl(JSONUtil.getString(jo, "avatar"));
                            user.setLogin(true);

                            LoginEB loginEB = new LoginEB();
                            loginEB.setIslogin(true);
                            EventBus.getDefault().post(loginEB);
                            LoginActivity2.asyncFetchGroupsFromServer();

                            CarPlayPerference per = IocContainer.getShare().get(
                                    CarPlayPerference.class);
                            per.load();
                            per.thirdId = getIntent().getStringExtra("uid");
                            per.channel = getIntent().getStringExtra("channel");
                            per.nickname = getIntent().getStringExtra("nickname");
                            per.commit();
                            Intent it = new Intent(self, BindPhoneInfoActivity.class);
                            it.putExtra("birthday", JSONUtil.getLong(jo, "birthday"));
                            startActivity(it);
                            LoginActivity2.asyncFetchGroupsFromServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            hidenProgressDialog();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    DemoHXSDKHelper.getInstance().logout(true,
                                            null);
                                    Toast.makeText(getApplicationContext(),
                                            R.string.login_failure_failed, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                            return;
                        }
                        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                        boolean updatenick = EMChatManager.getInstance()
                                .updateCurrentUserNick(
                                        User.getInstance().getNickName());
                        if (!updatenick) {
                            Log.e("LoginActivity",
                                    "update current user nick fail");
                        }
                        // if (!LoginActivity.this.isFinishing() &&
                        // pd.isShowing()) {
                        // pd.dismiss();
                        // }
                        // 进入主页面

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        // if (!progressShow) {
                        // return;
                        // }
                        hidenProgressDialog();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // pd.dismiss();
                                if (code == ERROR_EXCEPTION_INVALID_PASSWORD_USERNAME) {
                                    showToast("用户名或者密码错误!");
                                } else {
                                    showToast("登录失败:" + message);
                                }

                            }
                        });
                    }
                });

    }

    private void initializeContacts() {
        Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
        // 添加user"申请与通知"
        ChatUser newFriends = new ChatUser();
        newFriends
                .setUsername(com.gongpingjia.carplay.chat.Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(
                com.gongpingjia.carplay.chat.Constant.NEW_FRIENDS_USERNAME,
                newFriends);
        // 添加"群聊"
        ChatUser groupUser = new ChatUser();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser
                .setUsername(com.gongpingjia.carplay.chat.Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(com.gongpingjia.carplay.chat.Constant.GROUP_USERNAME,
                groupUser);

        // 添加"Robot"
        ChatUser robotUser = new ChatUser();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(com.gongpingjia.carplay.chat.Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(com.gongpingjia.carplay.chat.Constant.CHAT_ROBOT,
                robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(self);
        List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
        dao.saveContactList(users);
    }


}
