package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
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

import org.json.JSONObject;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

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
    private long mBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data2);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

    }

    @Override
    public void initView() {
        setTitle("注册");
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
        Button mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(this);

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
                        GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        mBirthday = calendar.getTimeInMillis();
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
                    showToast("上传失败,重新上传");
                }
            }
        });
    }


    private void register() {

        if (TextUtils.isEmpty(mEditNickname.getText().toString())) {
            showToast("请输入昵称");
            return;
        }

        if (mBirthday <= 0) {
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
            net.addParam("password", getIntent().getStringExtra("password"));
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
        landmark.put("longitude", 180.5);
        landmark.put("latitude", 36.5);
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
                        loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
                                        "userId")), getIntent().getStringExtra("password"),
                                jo);
                        per.phone = getIntent().getStringExtra("phone");
                        per.password = getIntent().getStringExtra("password");

                    } else {
                        loginHX(MD5Util.string2MD5(JSONUtil.getString(jo,
                                "userId")), MD5Util.string2MD5(getIntent()
                                .getStringExtra("uid")
                                + getIntent().getStringExtra("channel")
                                + "com.gongpingjia.carplay"), jo);
                        per.thirdId = getIntent().getStringExtra("uid");
                        per.channel = getIntent().getStringExtra("channel");
                    }
                    per.commit();
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                } else {
                    showToast("注册失败");
                }
            }

        });
    }

    private void loginHX(String userId, String s, JSONObject jo) {
    }
}
