package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.EditHeadPhotoEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 编辑资料（新）
 * <p/>
 * Created by Administrator on 2015/10/16.
 */
public class EditPersonalInfoActivity2 extends CarPlayBaseActivity implements View.OnClickListener {
    /**
     * 头像
     */
    private RoundImageView headI = null;

    /**
     * 昵称
     */
    private TextView nicknameT ;

    private EditText edit_ageT = null;
    private String nickname;
    /**
     * 性别
     */
    private TextView sexT = null;
    /**
     * 图片缓存根目录
     */
    private File mCacheDir;

    private String mPhotoPath;

    private String photoUid;

    private User mUser = User.getInstance();
    TextView head_approve, car_approve;
    String photo = "";
    //    private String drivingExperience;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();
    LinearLayout approve_layout_head, approve_layout_car,name_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpersonal);
    }

    @Override
    public void initView() {
        setTitle("编辑资料");
        setRightAction("保存", R.id.right_text, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View backV = findViewById(R.id.backLayout);
        if (backV != null) {
            backV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 如果没有改动 直接关闭本页
                    if (isModify()) {
                        modification();
                    } else {
                        finish();
                    }

                }
            });
        }
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        headI = (RoundImageView) findViewById(R.id.head);
        sexT = (TextView) findViewById(R.id.sex);
        head_approve = (TextView) findViewById(R.id.head_approve);
        car_approve = (TextView) findViewById(R.id.car_approve);
        nicknameT = (TextView) findViewById(R.id.nickname);
        edit_ageT = (EditText) findViewById(R.id.edit_age);
        approve_layout_head = (LinearLayout) findViewById(R.id.approve_layout_head);
        approve_layout_car = (LinearLayout) findViewById(R.id.approve_layout_car);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        headI.setOnClickListener(this);
        name_layout.setOnClickListener(this);
        approve_layout_head.setOnClickListener(this);
        approve_layout_car.setOnClickListener(this);
        getMyDetails();
    }

    /**
     * 获取个人资料
     */
    private void getMyDetails() {
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + mUser.getUserId()
                + "/info? viewUser=" + mUser.getUserId() + "&token="
                + mUser.getToken());
        System.out.println( "/user/" + mUser.getUserId()
                + "/info? viewUser=" + mUser.getUserId() + "&token="
                + mUser.getToken());
        net.doGetInDialog(new NetTask(this) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                JSONObject jo = response.jSONFromData();

                nickname = JSONUtil.getString(jo, "nickname");
//                drivingExperience = JSONUtil.getInt(jo, "drivingExperience")
//                        + "";
                photo = JSONUtil.getString(jo, "avatar");
                String gender = JSONUtil.getString(jo, "gender");
                String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                String licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");
                String age = JSONUtil.getString(jo, "age");
                edit_ageT.setText(age);
                head_approve.setText(photoAuthStatus);
                car_approve.setText(licenseAuthStatus);
                nicknameT.setText(nickname);
                sexT.setText(gender);
//                edit_ageT.setText(drivingExperience);
                ViewUtil.bindNetImage(headI, photo, "head");
            }
        });

    }

    /**
     * 判断资料是否有改动
     */
    private boolean isModify() {
        String name = nicknameT.getText().toString();
//         String carage = edit_ageT.getText().toString();
//         if (carage.contains("年")) {
//         carage = carage.replace("年", "");
//         }
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (map.get("flag") || !name.equals(nickname)
            // || !carage.equals(drivingExperience)
                ) {
            return true;
        }
        return false;
    }

    private void uploadHead(String path) {
        DhNet net = new DhNet(API2.CWBaseurl + mUser.getUserId()
                + "avatar?token=" + mUser.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    User.getInstance().setHeadUrl(
                            JSONUtil.getString(jo, "photoUrl"));
                    photoUid = JSONUtil.getString(jo, "photoId");
                    boolean a = ImageLoader.getInstance().getDiskCache()
                            .remove(photo);
                    Bitmap b = ImageLoader.getInstance().getMemoryCache()
                            .remove(photo);
                    System.out.println("a" + a);
                    System.out.println("b" + b);
                    EditHeadPhotoEB eb = new EditHeadPhotoEB();
                    eb.setHeadUrl(photo);
                    EventBus.getDefault().post(eb);

                    List<EMGroup> list = EMGroupManager.getInstance()
                            .getAllGroups();

                    for (int i = 0; i < list.size(); i++) {
                        EMGroup group = list.get(i);
                        System.out.println("group:" + group.getGroupId());
                        EMMessage cmdMsg = EMMessage
                                .createSendMessage(EMMessage.Type.CMD);

                        // 支持单聊和群聊，默认单聊，如果是群聊添加下面这行
                        cmdMsg.setChatType(EMMessage.ChatType.GroupChat);

                        String action = "updateAvatar";// action可以自定义，在广播接收时可以收到
                        CmdMessageBody cmdBody = new CmdMessageBody(action);
                        String toUsername = group.getGroupId();// 发送给某个人
                        cmdMsg.setReceipt(toUsername);
                        cmdMsg.setAttribute("photoUrl", photo);// 支持自定义扩展
                        cmdMsg.addBody(cmdBody);
                        EMChatManager.getInstance().sendMessage(cmdMsg,
                                new EMCallBack() {

                                    @Override
                                    public void onSuccess() {
                                        System.out.println("发布成功!");
                                    }

                                    @Override
                                    public void onProgress(int arg0, String arg1) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onError(int arg0, String arg1) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                    }
                }
            }
        });
    }

    private void modification() {
        String nickname = nicknameT.getText().toString().trim();
        // String carage = carageT.getText().toString().trim();
        if (nickname.length() > 7 || nickname.length() == 0) {
            showToast("昵称不能大于8个字符或者不能为空");
            return;
        }
        DhNet net = new DhNet(API2.CWBaseurl + mUser.getUserId()
                + "/info?token=" + mUser.getToken());
        net.addParam("nickname", nickname);
//        net.addParam("birthday", mCity);
//        net.addParam("district", mDistrict);
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("修改信息成功");
                    finish();
                } else {
                    showToast("修改信息失败");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
                        new File(mPhotoPath));
                break;
            case R.id.name_layout:
                showToast("修改昵称");
                break;
            case R.id.approve_layout_head:

                showToast("头像认证");

                break;
            case R.id.approve_layout_car:

                showToast("车主认证");

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    headI.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
                    uploadHead(mPhotoPath);
                    break;

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isModify()) {
                modification();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
