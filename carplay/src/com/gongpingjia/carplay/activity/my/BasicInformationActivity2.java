package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.db.UserDao;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.view.dialog.DateTimerDialog2;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.UserLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/13.
 */
public class BasicInformationActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private RadioGroup mGroupSex;
    private EditText mEditNickname;
    private TextView mTextBirthday;
    private ImageView mImgAvatar;

    private String mPhotoPath;
    private File mCacheDir;
    private String photoUid;
    private long mBirthday = 0;


    CarPlayPerference per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data2);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

    }

    @Override
    public void initView() {
        setTitle("个人信息");
        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();
        if (per.isshowPersonGuide == 0) {
            findViewById(R.id.guide).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                per.isshowPersonGuide = 1;
                per.commit();
                findViewById(R.id.guide).setVisibility(View.GONE);
            }
        });
        setLeftAction(R.drawable.action_cancel, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setRightAction("完成", 1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mGroupSex = (RadioGroup) findViewById(R.id.rg_sex);
        mEditNickname = (EditText) findViewById(R.id.et_nickname);
        mTextBirthday = (TextView) findViewById(R.id.tv_birthday);
        mImgAvatar = (ImageView) findViewById(R.id.iv_avatar);


        mImgAvatar.setOnClickListener(this);
        mTextBirthday.setOnClickListener(this);

        if (getIntent().getStringExtra("avatar") != null) {
            ImageLoader.getInstance().displayImage(getIntent().getStringExtra("avatarUrl"), mImgAvatar);
            mEditNickname.setText(getIntent().getStringExtra("nickname"));
            photoUid = getIntent().getStringExtra("avatar");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_avatar:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
                        new File(mPhotoPath));
                break;

            case R.id.tv_birthday:
                DateTimerDialog2 dateTimerDialog2 = new DateTimerDialog2(self);
                dateTimerDialog2.setOnDateTimerResultListener(new DateTimerDialog2.OnDateTimerResultListener() {
                    @Override
                    public void onResult(String year, String month, String day) {
                        String result = year + "/" + month + "/" + day;
                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = null;
                        try {
                            date = df.parse(result);
                            mBirthday = date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mTextBirthday.setText(year + "年" + month + "月" + day + "日");
                    }
                });
                dateTimerDialog2.show();
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.TAKE_PHOTO:
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    String path = PhotoUtil.onPhotoFromCamera(self,
                            Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000, newPath);
                    mPhotoPath = path;
                    break;
                case Constant.PICK_PHOTO:
                    PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
                            data, 1, 1, 1000);
                    break;
                case Constant.ZOOM_PIC:
                    Bitmap bmp = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    mImgAvatar.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
                    showProgressDialog("上传头像中...");
                    uploadHead(mPhotoPath);
                    break;
            }
        }
    }

    private void uploadHead(String path) {
        DhNet net = new DhNet(API2.uploadAvatar);
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    showToast("头像上传成功");
                    JSONObject jo = response.jSONFromData();
                    photoUid = JSONUtil.getString(jo, "photoId");
                } else {
                    mImgAvatar.setImageResource(R.drawable.head_icon);
                    photoUid = "";
//                    showToast("上传失败,重新上传");
                }
            }
        });
    }


    private void register() {

        if (TextUtils.isEmpty(mEditNickname.getText().toString())) {
            showToast("请输入昵称");
            return;
        }

        if (mEditNickname.length()>7){
            showToast("昵称不能大于7个字符");
            return;
        }

        if (mBirthday == 0) {
            showToast("请选择生日");
            return;
        }

        if (photoUid == null) {
            showToast("请上传头像");
            return;
        }
        DhNet net = new DhNet(API2.register);
        String gender = mGroupSex.getCheckedRadioButtonId() == R.id.rb_female ? "女" : "男";
        if (getIntent().getStringExtra("phone") != null) {
            //手机号登陆
            net.addParam("phone", getIntent().getStringExtra("phone"));
            net.addParam("code", getIntent().getStringExtra("code"));
            net.addParam("password", MD5Util.string2MD5(getIntent().getStringExtra("password")));
        } else {
            //三方登陆
            net.addParam("uid", getIntent().getStringExtra("uid"));
            net.addParam("channel", getIntent().getStringExtra("channel"));
        }


        net.addParam("nickname", mEditNickname.getText().toString());
        net.addParam("gender", gender);
        net.addParam("birthday", mBirthday);
        net.addParam("avatar", photoUid);

        Map<String, Object> landmark = new HashMap<>();
        landmark.put("longitude", UserLocation.getInstance().getLongitude());
        landmark.put("latitude", UserLocation.getInstance().getLatitude());
        net.addParam("landmark", landmark);
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("注册成功!");
                    CarPlayPerference per = IocContainer.getShare().get(
                            CarPlayPerference.class);
                    per.load();

                    JSONObject jo = response.jSONFromData();
                    if (getIntent().getStringExtra("phone") != null) {
                        //手机号完善信息
                        loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
                                "userId")), getIntent().getStringExtra("password"), jo);
//                        loginHX(JSONUtil.getString(jo,
//                                "userId"), MD5Util.string2MD5(getIntent().getStringExtra("password")), jo);
                        per.phone = getIntent().getStringExtra("phone");
                        per.password = getIntent().getStringExtra("password");
                    } else {
                        //三方登录完善信息
                        loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
                                "userId")), MD5Util.string2MD5(getIntent()
                                .getStringExtra("uid")
                                + getIntent().getStringExtra("channel")
                                + "com.gongpingjia.carplay"), jo);
                        per.thirdId = getIntent().getStringExtra("uid");
                        per.channel = getIntent().getStringExtra("channel");
                        per.headUrl = getIntent().getStringExtra("avatarUrl");
                        per.nickname = getIntent().getStringExtra("nickname");
                    }
                    per.commit();
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                    LoginActivity2.asyncFetchGroupsFromServer();
                }
            }

        });
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
                            user.setNickName(mEditNickname.getText().toString());
                            user.setHeadUrl(JSONUtil.getString(jo, "avatar"));
                            user.setLogin(true);

                            LoginEB loginEB = new LoginEB();
                            loginEB.setIslogin(true);
                            EventBus.getDefault().post(loginEB);
                            LoginActivity2.asyncFetchGroupsFromServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
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

    private String setData(int data) {
        String strdata = String.valueOf(data);
        if (strdata.length() == 1) {
            return "0" + strdata;
        }
        return strdata;

    }
}
