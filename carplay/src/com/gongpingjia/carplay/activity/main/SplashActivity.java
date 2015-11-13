package com.gongpingjia.carplay.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.my.LoginActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.MD5Util;

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

public class SplashActivity extends CarPlayBaseActivity {

    CarPlayPerference per;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initView() {
        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();

        if (per.lastLoginTime ==(CarPlayUtil.getTodayDate()-(1000*60*60*24))){
            per.setIsTodayFirst(true);
//            System.out.println(per.lastLoginTime+"-----------------------");
        }else {
//            System.out.println(per.lastLoginTime+"----++++++++++++++---"+(CarPlayUtil.getTodayDate()-(1000*60*60*24)));
            per.setIsTodayFirst(false);
        }
        per.setLastLoginTime(CarPlayUtil.getTodayDate());
        per.commit();

        if (!TextUtils.isEmpty(per.channel)) {
            // 三方登陆
            loginThirdParty();
        } else if (!TextUtils.isEmpty(per.phone)
                && !TextUtils.isEmpty(per.password)) {
            // 正常登陆
            login();
        } else {
            if (per.isFirst == 0) {
                first();
            } else {
                notFirst();
            }
        }
    }

    // 三方登陆
    private void loginThirdParty() {
        String api = API2.snsLogin;
        DhNet net = new DhNet(api);
        String password = MD5Util.string2MD5(per.thirdId + per.channel + "com.gongpingjia.carplay");
        net.addParam("channel", per.channel);
        net.addParam("nickname", per.nickname);
        net.addParam("avatar", per.headUrl);
        net.addParam("uid", per.thirdId);
        net.addParam("password", password);
        net.doPost(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject json = response.jSONFrom("data");
                    if (json.has("snsUid")) {
                        // 登陆失败
                        Intent it = new Intent(self, MainActivity2.class);
                        startActivity(it);
                    } else if (json.has("userId")) {

                        loginHX(MD5Util.string2MD5(JSONUtil.getString(json,
                                        "userId")), JSONUtil.getString(json, "password"),
                                json);
                        // 登陆成功
                        // User user = User.getInstance();
                        // try {
                        // user.setUserId(json.getString("userId"));
                        // user.setToken(json.getString("token"));
                        // user.setBrand(json.getString("brand"));
                        // user.setBrandLogo(json.getString("brandLogo"));
                        // user.setNickName(json.getString("nickname"));
                        // user.setSeatNumber(json.getInt("seatNumber"));
                        // user.setModel(json.getString("model"));
                        // User.getInstance().setLogin(true);
                        // LoginEB loginEB = new LoginEB();
                        // loginEB.setIslogin(true);
                        // EventBus.getDefault().post(loginEB);
                        // } catch (JSONException e) {
                        // e.printStackTrace();
                        // }
                    }
                } else {
                    // 登陆
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                }
            }

            @Override
            public void onErray(Response response) {
                super.onErray(response);
                notFirst();
            }
        });
    }

    private void login() {
        DhNet net = new DhNet(API2.login);
        net.addParam("phone", per.phone);
        net.addParam("password", per.password);
        net.doPost(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFrom("data");
                    loginHX(MD5Util
                                    .string2MD5(JSONUtil.getString(jo, "userId")),
                            per.password, jo);
                } else {
                    if (per.isFirst == 0) {
                        first();

                    } else {
                        notFirst();
                    }
                }

            }

            @Override
            public void onErray(Response response) {
                // TODO Auto-generated method stub
                super.onErray(response);

                if (per.isFirst == 0) {
                    first();

                } else {
                    notFirst();
                }
            }
        });
    }

    private void first() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(self, GuidanceActivity2.class);
                startActivity(intent);
                per.isFirst = 1;
                per.commit();
                finishWithoutAnim();
            }
        }, 2000);
    }

    private void notFirst() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(self, MainActivity2.class);
                startActivity(intent);
                finishWithoutAnim();
            }
        }, 2000);

    }

    private void loginHX(String currentUsername, String currentPassword,
                         final JSONObject jo) {
        EMChatManager.getInstance().login(currentUsername, currentPassword,
                new EMCallBack() {

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
                            user.setPhone(per.phone);
                            JSONArray jsa = JSONUtil.getJSONArray(jo, "album");
                            user.setHasAlbum(jsa != null && jsa.length() > 1);
                            user.setLogin(true);
                            user.setGender(JSONUtil.getString(jo, "gender"));
                            user.setAge(JSONUtil.getInt(jo, "age"));
                            notFirst();
                            LoginActivity2.asyncFetchGroupsFromServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    DemoHXSDKHelper.getInstance().logout(true,
                                            null);
//									Toast.makeText(getApplicationContext(),
//											R.string.login_failure_failed, 1)
//											.show();
                                }
                            });
                            notFirst();
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
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // pd.dismiss();
                                showToast("登录失败:" + message);

                                notFirst();
                                // Toast.makeText(
                                // getApplicationContext(),
                                // getString(R.string.Login_failed)
                                // + message, Toast.LENGTH_SHORT)
                                // .show();
                            }
                        });
                    }
                });

    }

    private void initializeContacts() {
        Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
        // 添加user"申请与通知"
        ChatUser newFriends = new ChatUser();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
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
        UserDao dao = new UserDao(self);
        List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
        dao.saveContactList(users);
    }
}
