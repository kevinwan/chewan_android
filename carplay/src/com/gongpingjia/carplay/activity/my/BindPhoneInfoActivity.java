package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/6.
 */
public class BindPhoneInfoActivity extends CarPlayBaseActivity implements View.OnClickListener {

    private RadioGroup mGroupSex;
    private EditText mEditNickname;
    private TextView mTextBirthday;
    private ImageView mImgAvatar;

    private String mPhotoPath;
    private File mCacheDir;
    private String photoUid;
    private long mBirthday = 0;


    CarPlayPerference per;

    User user;

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
        user = User.getInstance();
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
                edit();
            }
        });

        mGroupSex = (RadioGroup) findViewById(R.id.rg_sex);
        mEditNickname = (EditText) findViewById(R.id.et_nickname);
        mTextBirthday = (TextView) findViewById(R.id.tv_birthday);
        mImgAvatar = (ImageView) findViewById(R.id.iv_avatar);


        mImgAvatar.setOnClickListener(this);
        mTextBirthday.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(user.getHeadUrl(), mImgAvatar);
        mEditNickname.setText(user.getNickName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd");
        long birthday = getIntent().getLongExtra("birthday", 0);
        mBirthday = birthday;
        mTextBirthday.setText(df.format(birthday));
        if (user.getGender().equals("男")) {
            mGroupSex.check(R.id.rb_male);
        } else {
            mGroupSex.check(R.id.rb_female);
        }

        RadioButton rb_maleB = (RadioButton) findViewById(R.id.rb_male);
        RadioButton rb_femaleB = (RadioButton) findViewById(R.id.rb_female);
        rb_maleB.setClickable(false);
        rb_femaleB.setClickable(false);
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


                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        //现在时间
                        int currentYear = Integer.parseInt(formatter.format(curDate));

                        //出生年
                        int yearL = Integer.parseInt(year);
                        user.setAge(currentYear - yearL);
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

    private void edit() {
        final String nickname = mEditNickname.getText().toString().trim();
        // String carage = carageT.getText().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            showToast("请输入昵称");
            return;
        }

        if (nickname.length() > 7) {
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
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("修改信息成功");
                    user.setNickName(nickname);
                    Intent it = new Intent(self, MainActivity2.class);
                    startActivity(it);
                    finish();
                }
            }
        });
    }

    private void uploadHead(String path) {
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/avatar?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println("更改头像返回："+user.getUserId() + "---------" + user.getToken());
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    String head_url = JSONUtil.getString(jo, "photoUrl");
                    user.setHeadUrl(head_url);
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
                    mImgAvatar.setImageResource(R.drawable.head_icon);
                    photoUid = "";
                    showToast("上传失败,重新上传");
                }
            }
        });
    }

}

