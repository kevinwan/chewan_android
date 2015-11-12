package com.gongpingjia.carplay.activity.main;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.MatchingListFragment;
import com.gongpingjia.carplay.activity.active.NearListFragment;
import com.gongpingjia.carplay.activity.active.RecommendListFragment;
import com.gongpingjia.carplay.activity.dynamic.DynamicListFragment;
import com.gongpingjia.carplay.activity.my.LoginActivity2;
import com.gongpingjia.carplay.activity.my.MyFragment2;
import com.gongpingjia.carplay.activity.my.SettingActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.FilterPreference2;
import com.gongpingjia.carplay.bean.PointRecord;
import com.gongpingjia.carplay.bean.TabEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.GroupEB;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.view.dialog.NearbyFilterDialog;
import com.gongpingjia.carplay.view.pop.MatePop;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Stack;
import java.util.Timer;

import de.greenrobot.event.EventBus;

public class MainActivity2 extends BaseFragmentActivity implements
        EMEventListener {
    LinearLayout layout;

    LinearLayout tabV;

    FragmentManager fm;

    // Fragment 的栈
    Stack<Fragment> slist;

    View titleBar;

    CarPlayPerference per;

    String mPhotoPath;

    File mCacheDir;

    ImageView msgT;

    ImageView chatPointI;

    Timer mTimer;

    // 消息数据
    JSONObject dataJo;

    private static boolean isExit = false;

    Handler mHandler;

    List<EMGroup> groupList;

    private MyConnectionListener connectionListener = null;

    public boolean isConflict = false;

    private MyGroupChangeListener groupChangeListener = null;

    ImageView appointmentI;

    private RotateAnimation mRotateAnimation;
    ImageView imgCenter;

    TextView rightT;

    FilterPreference2 pre;
    private ImageView right_icon;

    //上传图片总数
    private int uploadPhotoCount = 0;

    //已上传的图片
    private int uploadedCount = 0;

    User user;
//    RelativeLayout free_layout;
//    CheckBox free_ck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmain);
        EventBus.getDefault().register(this);
        initView();
//        isAuthen();
        updateApp();

        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getCmdMessageBroadcastAction());
        registerReceiver(cmdMessageReceiver, cmdIntentFilter);


        updateUnreadLabel();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        Intent it = new Intent(this, MsgService.class);
//        startService(it);
        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[]{
                        EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventNewCMDMessage,
                        EMNotifierEvent.Event.EventDeliveryAck,
                        EMNotifierEvent.Event.EventReadAck,
                        EMNotifierEvent.Event.EventConversationListChanged});

        // asyncFetchGroupsFromServer();
        // ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager()
        // .asyncGetCurrentUserInfo();
    }

    public void initView() {
        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();
        if (per.isShowMainGuilde == 0) {
            findViewById(R.id.guide).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.guide_yun).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                per.isShowMainGuilde = 1;
                per.commit();
                findViewById(R.id.guide).setVisibility(View.GONE);
            }
        });


        //不能去掉
        findViewById(R.id.main_bg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //不能去掉
        findViewById(R.id.msg_bg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        user = User.getInstance();
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mHandler = new Handler();
        slist = new Stack<Fragment>();
        fm = getSupportFragmentManager();
        tabV = (LinearLayout) findViewById(R.id.tab);
        titleBar = findViewById(R.id.titlebar);
        rightT = (TextView) findViewById(R.id.right_text);

        //筛选
        rightT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NearbyFilterDialog dialog = new NearbyFilterDialog(self);
                dialog.show();
                dialog.setOnNearbyFilterResultListener(new NearbyFilterDialog.OnNearbyFilterResultListener() {
                    @Override
                    public void onResult(String type, String pay, String gender, boolean transfer) {
                        pre = IocContainer.getShare().get(FilterPreference2.class);
                        pre.setType(type);
                        pre.setPay(pay);
                        pre.setGender(gender);
                        pre.setTransfer(transfer);
                        pre.commit();
                        EventBus.getDefault().post(pre);
                    }
                });
            }
        });
        right_icon = (ImageView) findViewById(R.id.right_icon);
        right_icon.setImageResource(R.drawable.setting);
        right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置
                Intent it = new Intent(self, SettingActivity2.class);
                startActivity(it);

            }
        });
        initTab();
        setTab(0);
        msgT = (ImageView) findViewById(R.id.msg_point);
        chatPointI = (ImageView) findViewById(R.id.chat_point);
        appointmentI = (ImageView) findViewById(R.id.center);
        appointmentI.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setTab(2);
            }
        });
        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        groupChangeListener = new MyGroupChangeListener();
        // 注册群聊相关的listener
        EMGroupManager.getInstance()
                .addGroupChangeListener(groupChangeListener);

        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        appointmentI.setAnimation(rotateAnimation);
        rotateAnimation.start();
//        sendLoaction(20.12, 120.12);


        //图片模糊处理
//        Blurry.with(context).capture(view).into(imageView);
    }

    private void initTab() {
        for (int i = 0; i < tabV.getChildCount(); i++) {
            final int index = i;
            View childV = tabV.getChildAt(i);
            childV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    setTab(index);
                }
            });
        }
    }

    private void setTab(final int index) {

        if (index == 4 || index == 3) {
            if (!User.getInstance().isLogin()) {
                UserInfoManage.getInstance().checkLogin(self,
                        new UserInfoManage.LoginCallBack() {
                            @Override
                            public void onisLogin() {
                                if (index == 4) {
                                    setTab(4);
                                } else {
                                    setTab(3);
                                }
                            }

                            @Override
                            public void onLoginFail() {
                            }
                        });
            }
        }

        if (index == 4 && !User.getInstance().isLogin()) {
            return;
        }


        rightT.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
//        free_layout.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        right_icon.setVisibility(index == 4 ? View.VISIBLE : View.GONE);

        for (int i = 0; i < tabV.getChildCount(); i++) {
            View childV = tabV.getChildAt(i);
            View imgLayout = childV.findViewById(R.id.img_layout);
            final ImageView img = (ImageView) imgLayout.findViewById(R.id.img);
            TextView text = (TextView) childV.findViewById(R.id.text);
            if (index == i) {
                text.setTextColor(getResources().getColor(
                        R.color.xiangce_rad));
                switch (index) {
                    case 0:
                        setTitle("附近");
                        img.setImageResource(R.drawable.icon_nav_near_f);
                        switchContent(NearListFragment.getInstance());
                        break;

                    case 1:
                        setTitle("推荐");
                        img.setImageResource(R.drawable.icon_nav_tuijian_f);
                        switchContent(RecommendListFragment.getInstance());
                        break;
                    case 2:
                        MatePop.getInstance(self).show();
//                        switchContent(MateFragment.getInstance());
                        break;
                    case 3:

                        setTitle("动态");
                        img.setImageResource(R.drawable.icon_nav_dongtai_f);
                        switchContent(DynamicListFragment.getInstance());
//                        if (per.isShowMessageGuilde == 0) {
//                            findViewById(R.id.main_msg_guide).setVisibility(View.VISIBLE);
//                        }
                        break;
                    case 4:
                        setTitle("我的");
                        switchContent(MyFragment2.getInstance());
                        img.setImageResource(R.drawable.icon_nav_mine_f);

                        break;


                    default:
                        break;

                }
            } else {
                text.setTextColor(getResources().getColor(R.color.text_grey));
                switch (i) {
                    case 0:
                        img.setImageResource(R.drawable.icon_nav_near);
                        break;

                    case 1:
                        img.setImageResource(R.drawable.icon_nav_tuijian);
                        break;

                    case 3:
                        img.setImageResource(R.drawable.icon_nav_dongtai);
                        break;
                    case 4:
                        img.setImageResource(R.drawable.icon_nav_mine);


                        break;


                    default:
                        break;
                }
            }
        }
    }

    public void switchContent(Fragment fragment) {
        try {
            FragmentTransaction t = fm.beginTransaction();
            List<Fragment> flist = fm.getFragments();
            if (flist == null) {
                t.add(R.id.main_content, fragment);
            } else {
                if (!flist.contains(fragment)) {
                    t.add(R.id.main_content, fragment);
                }
                t.hide(slist.get(slist.size() - 1));
                t.show(fragment);
            }

            if (slist.contains(fragment)) {
                slist.remove(fragment);
            }

            slist.add(fragment);

            t.commitAllowingStateLoss();

        } catch (Exception e) {
        }
    }

    private void isAuthen() {
        User user = User.getInstance();
        if (user.isLogin()) {
//            DhNet mDhNet = new DhNet(API.availableSeat + user.getUserId()
//                    + "/seats?token=" + user.getToken());
//            mDhNet.doGet(new NetTask(self) {
//
//                @Override
//                public void doInUI(Response response, Integer transfer) {
//                    // TODO Auto-generated method stub
//                    if (response.isSuccess()) {
//                        JSONObject json = response.jSONFrom("data");
////                        try {
////                            User user = User.getInstance();
////                            user.setIsAuthenticated(json
////                                    .getInt("isAuthenticated"));
////                            // 认证车主
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//                    }
//                }
//            });
        }
    }

    public void updateApp() {
        final String mCurrentVersion = getAppVersion();
        DhNet net = new DhNet(API2.updateVersion);
        net.doGet(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    String version = JSONUtil.getString(jo, "version");
                    if (0 < version.compareTo(mCurrentVersion)) {
                        showUpdateDialog(jo);
                    }
                }
            }
        });
    }

    private String getAppVersion() {
        String versionName = null;
        try {
            String pkName = this.getPackageName();
            versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;

        } catch (Exception e) {
            return null;
        }
        return versionName;
    }

    private void showUpdateDialog(final JSONObject jo) {
        Builder builder = new Builder(this);
        builder.setTitle("发现新版本 " + JSONUtil.getString(jo, "version"));
        builder.setMessage(JSONUtil.getString(jo, "remarks"));
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent it = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(JSONUtil.getString(jo, "url"));
                        it.setData(uri);
                        startActivity(it);
                    }

                });
        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (JSONUtil.getInt(jo, "forceUpgrade") == 1) {
                            finish();
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PICK_PHOTO:
                    showProgressDialog("图片上传中...");
                    if (data != null && data.getExtras() != null) {
                        @SuppressWarnings("unchecked")
                        List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
                        if (photos == null || photos.isEmpty()) {
                            showToast("没有选择图片!");
                        } else {
                            uploadPhotoCount = photos.size();
                            for (int i = 0; i < photos.size(); i++) {
                                String newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
                                        .getAbsolutePath();
                                Bitmap btp = PhotoUtil.getLocalImage(new File(photos.get(i).getOriginalPath()));
                                PhotoUtil.saveLocalImage(btp, new File(newPhotoPath));
                                uploadHead(newPhotoPath);
                            }
                        }
                    }
                    break;
                case Constant.TAKE_PHOTO:
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    showProgressDialog("上传头像中...");
                    uploadPhotoCount = 1;
                    uploadHead(newPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path) {

        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
//        addPhoto.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/album/upload?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    user.setHasAlbum(true);         //设置相册状态
                    uploadedCount = uploadedCount + 1;
                    JSONObject jo = response.jSONFromData();
                    String success = "上传成功";
                    if (uploadPhotoCount == uploadedCount) {
                        showToast("上传成功");
                        //控制附近列表刷新
                        EventBus.getDefault().post(new String("刷新附近列表"));
                        EventBus.getDefault().post(success);
                        uploadedCount = 0;

                        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/photoCount?token=" + user.getToken());
                        net.addParam("count", uploadPhotoCount);
                        net.doPost(new NetTask(self) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {

                            }
                        });
                    }
                }
            }
        });
    }

    public void onEventMainThread(JSONObject jo) {
        dataJo = jo;
        JSONObject commentJo = JSONUtil.getJSONObject(jo, "comment");
        int commentCount = JSONUtil.getInt(commentJo, "count");

        JSONObject applicationJo = JSONUtil.getJSONObject(jo, "application");
        int applicationCount = JSONUtil.getInt(applicationJo, "count");

        if (commentCount != 0 || applicationCount != 0) {
            // msgT.setText(commentCount + applicationCount + "");
            msgT.setVisibility(View.VISIBLE);
        } else {
            msgT.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(TabEB tab) {
        if (tab.getIndex() == 2) {
            //切换到匹配意向,从匹配意向发过来的消息,tab.getParams()代表匹配的条件
            switchContent(MatchingListFragment.getInstance());
            MatchingListFragment.getInstance().setParams(tab.getParams());
            setTitle("匹配意向结果");
            return;
        } else if (tab.getIndex() == 4) {
            setTab(4);
            return;
        }
        setTab(0);
    }

    //附近adapter,随便看看dialog
    public void onEventMainThread(Integer photo) {
//        showToast(photo+"photo");
        switch (photo) {
            case Constant.TAKE_PHOTO:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                Intent getImageByCamera = new Intent(
                        "android.media.action.IMAGE_CAPTURE");
                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(mPhotoPath)));
                startActivityForResult(getImageByCamera,
                        Constant.TAKE_PHOTO);
                break;
            case Constant.PICK_PHOTO:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                Intent intent = new Intent(self,
                        PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX,
                        10);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, Constant.PICK_PHOTO);
                break;
        }
    }

    public static void asyncFetchGroupsFromServer() {
        HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

            @Override
            public void onSuccess() {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

                if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }

            }

            @Override
            public void onError(int code, String message) {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

        });
    }

    /**
     * 连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (User.getInstance().isLogin()) {
                        if (error == EMError.USER_REMOVED) {
                            // 显示帐号已经被移除
                            showToast("账号被移除!");
                            // showAccountRemovedDialog();
                        } else if (error == EMError.CONNECTION_CONFLICT) {
                            // 显示帐号在其他设备登陆dialog
                            showToast("账号在另一地点登录!");
                            // showConflictDialog();
                        } else {
                            showToast("网络异常,请重新连接!");
                            // chatHistoryFragment.errorItem
                            // .setVisibility(View.VISIBLE);
                            // if (NetUtils.hasNetwork(MainActivity.this))
                            // chatHistoryFragment.errorText.setText(st1);
                            // else
                            // chatHistoryFragment.errorText.setText(st2);

                        }
                        isConflict = true;
                        User.getInstance().setDisconnect(false);
                        User.getInstance().setLogin(false);
                        User.getInstance().setDisconnect(true);
                        DemoHXSDKHelper.getInstance().logout(true, null);
                        Intent it = new Intent(self, LoginActivity2.class);
                        it.putExtra("action", "logout");
                        startActivity(it);
                        finish();
                    }
                }

            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("msg", "onDestroy");
        EventBus.getDefault().unregister(this);
        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(
                    connectionListener);
        }


        if (groupChangeListener != null) {
            EMGroupManager.getInstance().removeGroupChangeListener(
                    groupChangeListener);
        }

        unregisterReceiver(cmdMessageReceiver);

//        PointRecord record = PointRecord.getInstance();
//
//        Log.d("msg", record.getActivityDynamicCallList().toString());
//        Log.d("msg", record.getActivityDynamicChatList().toString());
//        Log.d("msg", record.getTypeClick().toString());
//
//        Log.d("msg", record.getActivityMatchCount() + "");
//        Log.d("msg", record.getActivityMatchInvitedCountList().toString());
//        Log.d("msg", record.getDynamicNearbyInvitedList().toString());
//
//        Log.d("msg", record.getOfficialActivityChatJoinList().toString());
//
//        Log.d("msg", record.getOfficialActivityBuyTicketList().toString());
//
//        Log.d("msg", record.getUserRegister() + "");
//        Log.d("msg", record.getUnRegisterNearbyInvited() + "");
//
//
//        Log.d("msg", record.getUnRegisterMatchInvited() + "");
        uploadPointRecord();


    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
                .getInstance();

        super.onStop();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.d("msg", "onPause");
        Toast toast = IocContainer.getShare().get(Toast.class);
        toast.cancel();
    }

    static public class ExitRunnable implements Runnable {
        @Override
        public void run() {
            isExit = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                IocContainer.getShare().get(IDialog.class)
                        .showToastShort(getApplicationContext(), "再按一次退出程序");
                mHandler.postDelayed(new ExitRunnable(), 2000);
            } else {
//                Intent it = new Intent(self, MsgService.class);
//                stopService(it);
                ActivityTack.getInstanse().exit(self);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEvent(EMNotifierEvent event) {

        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {

                EMMessage message = (EMMessage) event.getData();


                final int type = message.getIntAttribute("type", -1);

                if (type != -1) {
                    EventBus.getDefault().post("上传成功");
                }


                runOnUiThread(new Runnable() {
                    public void run() {
                        updateUnreadLabel();
                    }
                });
                // 提示新消息
                // HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                EventBus.getDefault().post(message);
                break;
            }

            case EventOfflineMessage: {
                EMMessage message = (EMMessage) event.getData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        updateUnreadLabel();
                    }
                });
                // 提示新消息
                // HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                EventBus.getDefault().post(message);
                break;
            }

            case EventConversationListChanged: {
                break;
            }

            default:
                break;
        }
    }

    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        chatPointI.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        int chatGroupCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance()
                .getAllConversations().values()) {
//            if (conversation.getType() == EMConversationType.GroupChat)
            chatroomUnreadMsgCount = chatroomUnreadMsgCount
                    + conversation.getUnreadMsgCount();
            chatGroupCount = chatGroupCount + conversation.getUnreadMsgCount();
        }
        return chatGroupCount;
    }


    public class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,
                                         String inviter, String reason) {

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter,
                                        String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {

            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        updateUnreadLabel();
                        GroupEB eb = new GroupEB();
                        EventBus.getDefault().post(eb);

                    } catch (Exception e) {
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,
                                          String applyer, String reason) {

        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,
                                        String accepter) {

        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,
                                          String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }
    }

    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("msg", "cmdMessageReceiver");
            // 获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            // 获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;// 获取自定义action
            // 获取扩展属性
            try {
                String attr = message.getStringAttribute("photoUrl");
                ImageLoader.getInstance().getMemoryCache().remove(attr);
                ImageLoader.getInstance().getDiskCache().remove(attr);
                showToast("清除缓存");
                System.out.println("清除缓存");
            } catch (EaseMobException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private void sendLoaction(double latitude, double longitude) {
        User user = User.getInstance();
        DhNet net = new DhNet(API2.sendLocation(user.getUserId(), user.getToken()));
        net.addParam("latitude", latitude);
        net.addParam("longitude", longitude);
        net.doPost(new NetTask(getApplicationContext()) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    Log.d("msg", "成功");
                }
            }
        });
    }


    private void uploadPointRecord() {
        PointRecord record = PointRecord.getInstance();
        String url = API2.CWBaseurl + "record/upload?" + "userId=" + User.getInstance().getUserId();
        DhNet net = new DhNet(url);
        net.addParam("unRegisterNearbyInvited", record.getUnRegisterNearbyInvited());
        net.addParam("unRegisterMatchInvited", record.getUnRegisterMatchInvited());
        net.addParam("userRegister", record.getUserRegister());

        JSONArray activityDynamicCallList = new JSONArray(record.getActivityDynamicCallList());
        net.addParam("activityDynamicCall", activityDynamicCallList);

        JSONArray activityDynamicChatList = new JSONArray(record.getActivityDynamicChatList());
        net.addParam("activityDynamicChat", activityDynamicChatList);

        JSONArray activityMatchInvitedCountList = new JSONArray(record.getActivityMatchInvitedCountList());
        net.addParam("activityMatchInvitedCount", activityMatchInvitedCountList);

        net.addParam("activityMatchCount", record.getActivityMatchCount());

        JSONArray officialActivityBuyTicketList = new JSONArray(record.getOfficialActivityBuyTicketList());
        net.addParam("officialActivityBuyTicket", officialActivityBuyTicketList);

        JSONArray officialActivityChatJoinList = new JSONArray(record.getOfficialActivityChatJoinList());
        net.addParam("officialActivityChatJoin", officialActivityChatJoinList);

        net.addParam("appOpenCount", 1);

        JSONArray dynamicNearbyInvitedList = new JSONArray(record.getDynamicNearbyInvitedList());
        net.addParam("dynamicNearbyInvited", dynamicNearbyInvitedList);

        JSONArray activityTypeClickList = new JSONArray(record.getTypeClick());
        net.addParam("activityTypeClick", activityTypeClickList);


        net.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {

                if (response.isSuccess()) {
                }
            }
        });
    }

}
