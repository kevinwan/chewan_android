package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.DateTimerDialog2;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
    private RoundImageView headI;

    /**
     * 昵称
     */
    private TextView nicknameT;

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
    public static final int APPROVE_HEAD = 2;
    public static final int APPROVE_CAR = 3;
    public static final int NICKNAME = 4;
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
    ImageView car_img,head_img;
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
        Intent myIntent = getIntent();
        View backV = findViewById(R.id.backLayout);
//        right_txt = (TextView) findViewById(R.id.right_text);
//        right_txt.setVisibility(View.VISIBLE);
//        right_txt.setText("保存");
//        right_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 如果没有改动 直接关闭本页
//                if (isModify()) {
//                    modification();
//                } else {
//                    finish();
//                }
//            }
//        });

        if (backV != null) {
            backV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 如果没有改动 直接关闭本页
                    if (isModify()) {
                        Intent intent = getIntent();
                        intent.putExtra("nickname",nicknameT.getText().toString());
                        intent.putExtra("head",head_url);
                        intent.putExtra("age",edit_ageT.getText().toString());
                        intent.putExtra("photoAuthStatus",car_approve.getText().toString());
                        intent.putExtra("licenseAuthStatus",head_approve.getText().toString());
                        setResult(self.RESULT_OK, intent);
                        EventBus.getDefault().post("上传成功");
                        finish();
                    } else {
                    setValue();
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
        headI = (RoundImageView) findViewById(R.id.head);
        sexT = (TextView) findViewById(R.id.sex);
        head_approve = (TextView) findViewById(R.id.head_approve);
        car_approve = (TextView) findViewById(R.id.car_approve);
        nicknameT = (TextView) findViewById(R.id.nickname);
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
         car_img = (ImageView) findViewById(R.id.car_img);
         head_img = (ImageView) findViewById(R.id.head_img);
//        getMyDetails();
        ViewUtil.bindNetImage(headI, myIntent.getStringExtra("headimg"), "head");
        sexT.setText(myIntent.getStringExtra("gender"));
        nicknameT.setText(myIntent.getStringExtra("name"));
        edit_ageT.setText(myIntent.getStringExtra("age"));
//        String carbradn = myIntent.getStringExtra("carbradn");
//        String carlogo = myIntent.getStringExtra("carlogo");
//        String carmodel = myIntent.getStringExtra("carmodel");
//        String carslug = myIntent.getStringExtra("carslug");
        if (myIntent.getStringExtra("licenseAuthStatus").equals("认证中")){
            car_approve.setText(myIntent.getStringExtra("licenseAuthStatus"));
            car_approve.setTextColor(getResources().getColor(R.color.text_black));
            approve_layout_car.setEnabled(true);
            head_img.setVisibility(View.VISIBLE);
        }else if(myIntent.getStringExtra("licenseAuthStatus").equals("未认证")){
            car_approve.setText(myIntent.getStringExtra("licenseAuthStatus"));
            car_approve.setTextColor(getResources().getColor(R.color.text_black));
            head_img.setVisibility(View.VISIBLE);
            approve_layout_car.setEnabled(true);
        }else if(myIntent.getStringExtra("licenseAuthStatus").equals("认证通过")){
            car_approve.setText(myIntent.getStringExtra("licenseAuthStatus"));
            car_approve.setTextColor(getResources().getColor(R.color.text_grey));
            head_img.setVisibility(View.GONE);
            approve_layout_car.setEnabled(false);
        }else if(myIntent.getStringExtra("licenseAuthStatus").equals("认证未通过")){
            car_approve.setText(myIntent.getStringExtra("licenseAuthStatus"));
            car_approve.setTextColor(getResources().getColor(R.color.text_black));
            head_img.setVisibility(View.VISIBLE);
            approve_layout_car.setEnabled(true);
        }
        if (myIntent.getStringExtra("photoAuthStatus").equals("认证中")){
            head_approve.setText(myIntent.getStringExtra("photoAuthStatus"));
            head_approve.setTextColor(getResources().getColor(R.color.text_black));
            car_img.setVisibility(View.VISIBLE);
            approve_layout_head.setEnabled(true);
        }else if(myIntent.getStringExtra("photoAuthStatus").equals("未认证")){
            head_approve.setText(myIntent.getStringExtra("photoAuthStatus"));
            head_approve.setTextColor(getResources().getColor(R.color.text_black));
            car_img.setVisibility(View.VISIBLE);
            approve_layout_head.setEnabled(true);
        }else if(myIntent.getStringExtra("photoAuthStatus").equals("认证通过")){
            head_approve.setText(myIntent.getStringExtra("photoAuthStatus"));
            head_approve.setTextColor(getResources().getColor(R.color.text_grey));
            car_img.setVisibility(View.GONE);
            approve_layout_head.setEnabled(false);
        }else if(myIntent.getStringExtra("photoAuthStatus").equals("认证未通过")){
            head_approve.setText(myIntent.getStringExtra("photoAuthStatus"));
            head_approve.setTextColor(getResources().getColor(R.color.text_black));
            car_img.setVisibility(View.VISIBLE);
            approve_layout_head.setEnabled(true);
        }



    }

//
    public void setValue(){

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
        DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+"/avatar?token="+user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println("更改头像返回："+user.getUserId() + "---------" + user.getToken());
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    photoUid = JSONUtil.getString(jo, "photoId");
                    head_url = JSONUtil.getString(jo, "photoUrl");
                    boolean a = ImageLoader.getInstance().getDiskCache()
                            .remove(head_url);
                    Bitmap b = ImageLoader.getInstance().getMemoryCache()
                            .remove(head_url);
//                    System.out.println("第一个：+++++++++++" + a);
//                    System.out.println("第二个：***************" + b);
//                    System.out.println("更改头像返回：" + JSONUtil.getString(jo, "photoUrl"));
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
//        DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+"/avatar?token="+user.getToken());
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

        if (TextUtils.isEmpty(nickname)) {
            showToast("请输入昵称");
            return;
        }

        if (nickname.length()>7){
            showToast("昵称不能大于7个字符");
            return;
        }
//        if (mBirthday <= 0) {
//            showToast("请选择生日");
//            return;
//        }
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
//                    finish();
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
               Intent inte = new Intent(self,ModifyName.class);
                inte.putExtra("name",nicknameT.getText().toString());
                startActivityForResult(inte, NICKNAME);
                break;
            case R.id.approve_layout_head:
//                showToast("头像认证");
                Intent intent = new Intent(self, HeadAttestationActivity.class);
                startActivityForResult(intent, APPROVE_HEAD);

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
                            modification();
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
                case NICKNAME:

                    nicknameT.setText(data.getStringExtra("nickname"));
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isModify()) {
                Intent intent = getIntent();
                intent.putExtra("nickname",nicknameT.getText().toString());
                intent.putExtra("head",head_url);
                intent.putExtra("age",edit_ageT.getText().toString());
                intent.putExtra("photoAuthStatus",car_approve.getText().toString());
                intent.putExtra("licenseAuthStatus",head_approve.getText().toString());
                setResult(self.RESULT_OK, intent);
                EventBus.getDefault().post("上传成功");
                finish();
            } else {
            finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
