package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.gongpingjia.carplay.view.dialog.DateTimerDialog2;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    private RoundImageView headI;

    /**
     * 昵称
     */
    private EditText nicknameT;

    private TextView edit_ageT;
    private String nickname;
    String age;
    /**
     * 性别
     */
    private TextView sexT;
    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;
    String photoUid;
    ImageView carlogo;
    public static final int APPROVE_HEAD = 2;
    public static final int APPROVE_CAR = 3;
    String years;
    User user;
    TextView head_approve, car_approve;
    String photo = "";
    //    private String drivingExperience;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();
    LinearLayout approve_layout_head, approve_layout_car, name_layout, personal_age_layout;
    TextView right_txt;
    private long mBirthday;
    String head_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("msg", "oncreate");
        setContentView(R.layout.activity_editpersonal);
    }

    @Override
    public void initView() {
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        setTitle("编辑资料");
        user = User.getInstance();

        View backV = findViewById(R.id.backLayout);
        right_txt = (TextView) findViewById(R.id.right_text);
        right_txt.setText("保存");
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
        if (getIntent().getStringExtra("photoId") != null) {
            ImageLoader.getInstance().displayImage(
                    getIntent().getStringExtra("photoUrl"), headI);

            photoUid = getIntent().getStringExtra("photoId");
        }
         carlogo = (ImageView) findViewById(R.id.person_carlogo);
        headI = (RoundImageView) findViewById(R.id.head);
        sexT = (TextView) findViewById(R.id.sex);
        head_approve = (TextView) findViewById(R.id.head_approve);
        car_approve = (TextView) findViewById(R.id.car_approve);
        nicknameT = (EditText) findViewById(R.id.nickname);
        edit_ageT = (TextView) findViewById(R.id.edit_age);
        approve_layout_head = (LinearLayout) findViewById(R.id.approve_layout_head);
        approve_layout_car = (LinearLayout) findViewById(R.id.approve_layout_car);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        personal_age_layout = (LinearLayout) findViewById(R.id.personal_age_layout);
        personal_age_layout.setOnClickListener(this);
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
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        DhNet dhNet = net.doGetInDialog(new NetTask(this) {

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
                age = JSONUtil.getString(jo, "age");
                edit_ageT.setText(age);
                head_approve.setText(photoAuthStatus);
                car_approve.setText(licenseAuthStatus);
                JSONObject ob =  JSONUtil.getJSONObject(jo, "car");
                String logo = JSONUtil.getString(ob,"logo");
                if (licenseAuthStatus.equals("认证通过")){

                    ViewUtil.bindNetImage(carlogo, logo,"carlogo");
                }

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
        if (!edit_ageT.getText().toString().equals(age) || !name.equals(nickname)
            // || !carage.equals(drivingExperience)
                ) {
            return true;
        }
        return false;
    }
    private void uploadHead(String path) {
        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
        headI.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl +"/user/"+user.getUserId()+ "/avatar?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    photoUid = JSONUtil.getString(jo, "photoId");
                    head_url = JSONUtil.getString(jo, "photoUrl");
                    System.out.println("更改头像返回："+JSONUtil.getString(jo, "photoUrl"));
                    showToast("上传成功");
//
                } else {
                    headI.setImageResource(R.drawable.head_icon);
                    photoUid = "";
                    showToast("上传失败,重新上传");
                }
            }
        });
    }

//    private void uploadHead(String path) {
//        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
//        headI.setImageBitmap(bmp);
//        DhNet net = new DhNet(API2.CWBaseurl +"/user/"+user.getUserId()+ "avatar?token=" + user.getToken());
//        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {
//
//            @Override
//            public void doInUI(Response response, Integer transfer) {
//                if (response.isSuccess()) {
//                    JSONObject jo = response.jSONFromData();
//                    User.getInstance().setHeadUrl(
//                            JSONUtil.getString(jo, "photoUrl"));
//                    head_url = JSONUtil.getString(jo, "photoUrl");
//                    photoUid = JSONUtil.getString(jo, "photoId");
//                    boolean a = ImageLoader.getInstance().getDiskCache()
//                            .remove(photo);
//                    Bitmap b = ImageLoader.getInstance().getMemoryCache()
//                            .remove(photo);
//                    System.out.println("a" + a);
//                    System.out.println("b" + b);
//                    EditHeadPhotoEB eb = new EditHeadPhotoEB();
//                    eb.setHeadUrl(photo);
//                    EventBus.getDefault().post(eb);
//
//                    List<EMGroup> list = EMGroupManager.getInstance()
//                            .getAllGroups();
//
//                    for (int i = 0; i < list.size(); i++) {
//                        EMGroup group = list.get(i);
//                        System.out.println("group:" + group.getGroupId());
//                        EMMessage cmdMsg = EMMessage
//                                .createSendMessage(EMMessage.Type.CMD);
//
//                        // 支持单聊和群聊，默认单聊，如果是群聊添加下面这行
//                        cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
//
//                        String action = "updateAvatar";// action可以自定义，在广播接收时可以收到
//                        CmdMessageBody cmdBody = new CmdMessageBody(action);
//                        String toUsername = group.getGroupId();// 发送给某个人
//                        cmdMsg.setReceipt(toUsername);
//                        cmdMsg.setAttribute("photoUrl", photo);// 支持自定义扩展
//                        cmdMsg.addBody(cmdBody);
//                        EMChatManager.getInstance().sendMessage(cmdMsg,
//                                new EMCallBack() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        System.out.println("发布成功!");
//                                    }
//
//                                    @Override
//                                    public void onProgress(int arg0, String arg1) {
//                                        // TODO Auto-generated method stub
//
//                                    }
//
//                                    @Override
//                                    public void onError(int arg0, String arg1) {
//                                        // TODO Auto-generated method stub
//
//                                    }
//                                });
//                    }
//                } else {
//                    headI.setImageResource(R.drawable.head_icon);
//                    photoUid = "";
//                    showToast("上传失败,重新上传");
//                }
//            }
//        });
//    }

    private void modification() {
        final String nickname = nicknameT.getText().toString().trim();
        // String carage = carageT.getText().toString().trim();
        if (nickname.length() > 7 || nickname.length() == 0) {
            showToast("昵称不能大于8个字符或者不能为空");
            return;
        }
        if (mBirthday <= 0) {
            showToast("请选择生日");
            return;
        }
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/info?token=" + user.getToken());
        net.addParam("nickname", nickname);
        net.addParam("birthday", mBirthday);
        System.out.println(nickname + mBirthday);
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("修改信息成功");
                    Intent it = getIntent();
                    it.putExtra("name", nickname);
                    it.putExtra("age", years);
                    it.putExtra("head", head_url);
                    setResult(self.RESULT_OK, it);
                    finish();
                } else {
                    showToast("修改信息失败");
                }
            }
        });
    }

    public void save() {
        right_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modification();
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
//            case R.id.name_layout:
//               Intent intent = new Intent(self,ModifyName.class);
//                intent.putExtra("name",nicknameT.getText().toString());
//                startActivity(intent);
//                break;
            case R.id.approve_layout_head:
//                showToast("头像认证");
                Intent intent = new Intent(self, HeadAttestationActivity.class);
                startActivityForResult(intent,APPROVE_HEAD);

                break;
            case R.id.approve_layout_car:
//                showToast("车主认证");
                Intent it = new Intent(self, AuthenticateOwnersActivity2.class);
                startActivityForResult(it, APPROVE_CAR);

                break;
            case R.id.personal_age_layout:
                DateTimerDialog2 dateTimerDialog2 = new DateTimerDialog2(self);
                dateTimerDialog2.setOnDateTimerResultListener(new DateTimerDialog2.OnDateTimerResultListener() {
                    @Override
                    public void onResult(String year, String month, String day) {
                        GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        mBirthday = calendar.getTimeInMillis();

//                        edit_ageT.setText(year + "年" + month + "月" + day + "日");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        //现在时间
                        long str = Long.parseLong(formatter.format(curDate));

                        //出生年
                        long yearL = Long.parseLong(year);
                         years = str - yearL + "";
                        edit_ageT.setText(years);
//                        System.out.println(years);
                    }
                });
                dateTimerDialog2.show();

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PICK_PHOTO:
                    Bitmap btp = PhotoUtil.checkImage(self, data);
                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
                    btp.recycle();
                    showProgressDialog("上传头像中...");
                    uploadHead(mPhotoPath);
                    break;
                case Constant.TAKE_PHOTO:
                    File file = new File(mPhotoPath);
                    Bitmap btp1 = PhotoUtil.getLocalImage(file);
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    showProgressDialog("上传头像中...");
                    uploadHead(newPath);
                    break;

                case APPROVE_HEAD:

                    head_approve.setText(data.getStringExtra("status"));


                    break;
                case APPROVE_CAR:

                    car_approve.setText(data.getStringExtra("statuss"));

                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            if (isModify()) {
//                modification();
//            } else {
                finish();
//            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
