package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * 车主认证
 */
public class AuthenticateOwnersActivity2 extends CarPlayBaseActivity implements View.OnClickListener{
    LinearLayout brandchoice,drivinglicense_up,driverlicense_up;
    Button up_button;
    TextView carName;
    public static final int MODEL = 1;
    String carModel = "";
    // 认证类型 (0:未认证 1:认证成功 2:认证中)
    int isAuthenticated = 0;

    String brandName, brandLogo, modelName, modelSlug;
    User user;
    String picUid;
    String mPhotoPath;
    // 图片缓存根目录
    private File mCacheDir;
    ImageView driver_img,driving_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_owners2);
    }

    @Override
    public void initView() {
        setTitle("车主认证");
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        user = User.getInstance();
         brandchoice = (LinearLayout) findViewById(R.id.brandchoice);
         drivinglicense_up = (LinearLayout) findViewById(R.id.drivinglicense_up);
         driverlicense_up = (LinearLayout) findViewById(R.id.driverlicense_up);
         up_button = (Button) findViewById(R.id.up_button);
         driver_img = (ImageView) findViewById(R.id.driver_img);
         driving_img = (ImageView) findViewById(R.id.driving_img);
        brandchoice.setOnClickListener(this);
        driverlicense_up.setOnClickListener(this);
        drivinglicense_up.setOnClickListener(this);
        up_button.setOnClickListener(this);
         carName = (TextView) findViewById(R.id.carName);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isAuthenticated = bundle.getInt("isAuthenticated", 0);
            carModel = bundle.getString("carModel");

        }
        switch (isAuthenticated) {
            // 未认证
            case 0:

                carName.setText("");
                up_button.setText("认证车主");
                up_button.setEnabled(true);
                break;
            // 已认证
            case 1:

                carName.setText(carModel);
//                if (!TextUtils.isEmpty(license)) {
//                    ViewUtil.bindNetImage(picI, license,
//                            CarPlayValueFix.optionsDefault.toString());
//                }
                up_button.setText("已认证");
                up_button.setEnabled(false);
                up_button.setBackgroundResource(R.drawable.btn_grey_bg);
                break;
            // 认证中
            case 2:

                carName.setText(carModel);
//                if (!TextUtils.isEmpty(license)) {
//                    ViewUtil.bindNetImage(picI, license,
//                            CarPlayValueFix.optionsDefault.toString());
//                }
                up_button.setText("认证中");
                up_button.setEnabled(false);
                up_button.setBackgroundResource(R.drawable.btn_grey_bg);
//                picI.setEnabled(false);
//                drivingExperienceE.setEnabled(false);
//                drivingExperienceE.setTextColor(self.getResources().getColor(R.color.text_grey));
//                carName.setTextColor(self.getResources().getColor(R.color.text_grey));
//                carName.setEnabled(false);
//
//                icon_tI.setVisibility(View.INVISIBLE);
//                icon_bI.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }

    }
    private void uploadPic(String path) {
        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
        driver_img.setImageBitmap(bmp);
        DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
                + "/drivingLicense/upload?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    picUid = JSONUtil.getString(jo, "photoId");
                } else {
                    showToast("上传失败，请重新上传");
                    picUid = "";
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.up_button:
                if (TextUtils.isEmpty(brandName)) {
                    showToast("请选择车型品牌!");
                    return;
                }
//                DhNet net = new DhNet("http://cwapi.gongpingjia.com/v1/user/$userId/license/authentication?token=$token");
//                net.addParam("carBrand", brandName);
//
//                net.addParam("carModel", modelName);

            break;
            //品牌选择
            case R.id.brandchoice:
                Intent intent = new Intent(self, CarTypeSelectActivity.class);
                startActivityForResult(intent, MODEL);

                break;
            //驾驶证上传
            case R.id.driverlicense_up:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
                        new File(mPhotoPath));

                break;
            //行驶证上传
            case R.id.drivinglicense_up:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
                        new File(mPhotoPath));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case MODEL:
                    brandName = data.getStringExtra("brandName");
                    brandLogo = data.getStringExtra("brandLogo");
                    modelName = data.getStringExtra("modelName");
                    modelSlug = data.getStringExtra("modelSlug");
                    carName.setText(modelName);


                    break;
                case Constant.PICK_PHOTO:
                    Bitmap btp = PhotoUtil.checkImage(self, data);
                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
                    btp.recycle();
                    showProgressDialog("上传图片中...");
                    uploadPic(mPhotoPath);
                    break;
                case Constant.TAKE_PHOTO:
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    showProgressDialog("上传图片中...");
                    uploadPic(newPath);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
