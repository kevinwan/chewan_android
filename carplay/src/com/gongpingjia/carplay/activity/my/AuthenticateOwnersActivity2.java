package com.gongpingjia.carplay.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.ImageGallery;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 车主认证
 */
public class AuthenticateOwnersActivity2 extends CarPlayBaseActivity implements View.OnClickListener {
    public static final int DRIVER_PHOTOGRAPH = 5;
    public static final int DRIVER_GALLERY = 4;
    public static final int DRIVING_PHOTOGRAPH = 3;
    public static final int DRIVING_GALLERY = 2;
    LinearLayout brandchoice, drivinglicense_up, driverlicense_up;
    Button up_button;
    TextView carName;
    public static final int MODEL = 1;
    String carModel = "";
    // 认证类型 (0:未认证 1:认证成功 2:认证中)
    int isAuthenticated = 0;

    String brandName, brandLogo, modelName, modelSlug;
    User user;
    String picUid ;
    String picUids ;
    String mPhotoPath;
    // 图片缓存根目录
    private File mCacheDir;
    ImageView driver_img, driving_img;
    String imgs = "";
    String img = "";
    String[] str;
    String[] strid;
    String[] strs;
    String[] strsid;
    String license = "";

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
        driver_img.setOnClickListener(this);
        driving_img.setOnClickListener(this);

        brandchoice.setOnClickListener(this);
        driverlicense_up.setOnClickListener(this);
        drivinglicense_up.setOnClickListener(this);
        up_button.setOnClickListener(this);
        carName = (TextView) findViewById(R.id.carName);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isAuthenticated = bundle.getInt("isAuthenticated", 0);
            carModel = bundle.getString("carModel");
//            drivingyears = bundle.getInt("drivingyears", 0);
            license = bundle.getString("license");
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
                up_button.setBackgroundResource(R.drawable.btn_grey_fillet);
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
                up_button.setBackgroundResource(R.drawable.btn_grey_fillet);
                drivinglicense_up.setEnabled(false);
                driverlicense_up.setEnabled(false);
                brandchoice.setEnabled(false);
//                drivingExperienceE.setTextColor(self.getResources().getColor(R.color.text_grey));
                carName.setTextColor(self.getResources().getColor(R.color.text_grey));
                carName.setEnabled(false);
//
//                icon_tI.setVisibility(View.INVISIBLE);
//                icon_bI.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }

    }

    private void updrivingPic(String path) {
        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
        driving_img.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+"/drivingLicense/upload?token="+user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    picUid = JSONUtil.getString(jo, "photoId");
                    imgs = JSONUtil.getString(jo, "photoUrl");
//                    System.out.println("行驶证："+JSONUtil.getString(jo, "photoId"));

                    System.out.println("********"+JSONUtil.getString(jo, "photoUrl"));
//                    Toast.makeText(self, "111" + JSONUtil.getString(jo, "photoUrl"), Toast.LENGTH_SHORT).show();
                } else {
                    showToast("上传失败，请重新上传");
                    picUid = "";
                }
            }
        });
    }

    private void updriverPic(String path) {
        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
        driver_img.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+ "/driverLicense/upload?token="+ user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    picUids = JSONUtil.getString(jo, "photoId");
//                    System.out.println("驾驶证："+JSONUtil.getString(jo, "photoId"));
                    img = JSONUtil.getString(jo, "photoUrl");

//                    Toast.makeText(self, "2222" + response.isSuccess(), Toast.LENGTH_SHORT).show();
                } else {
                    showToast("上传失败，请重新上传");
                    picUids = "";
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_button:
                if (TextUtils.isEmpty(brandName)) {
                    showToast("请选择车型品牌!");
                    return;
                }
                if (TextUtils.isEmpty(picUid)) {
                    showToast("请上传行驶证证!");
                    return;
                }
                if (TextUtils.isEmpty(picUids)) {
                    showToast("请上传驾驶证!");
                    return;
                }
                DhNet net = new DhNet(API2.CWBaseurl+"user/"+user.getUserId()+"/license/authentication?token="+ user.getToken());
                net.addParam("brand", brandName);
                net.addParam("model", modelName);
                net.addParam("logo", brandLogo);
                net.addParam("driverLicense", picUids);
                net.addParam("drivingLicense", picUid);
//                System.out.println("1.品牌中文名:" + brandName + "2.车型:" + modelName + "3.驾驶证:" + picUids + "4.行驶证:" + picUid);
                net.doPostInDialog(new NetTask(self) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            showToast("认证车主申请成功,请等待审核!");
                            Intent it = getIntent();
                            it.putExtra("statuss", "认证中");
                            setResult(self.RESULT_OK, it);
                            finish();
//                            showToast(response.isSuccess() + "");
                        }
                    }
                });

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
                PhotoUtil.getPhoto(self, DRIVER_PHOTOGRAPH, DRIVER_GALLERY,
                        new File(mPhotoPath));

                break;
            //行驶证上传
            case R.id.drivinglicense_up:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, DRIVING_PHOTOGRAPH, DRIVING_GALLERY,
                        new File(mPhotoPath));
                break;
            case R.id.driving_img:

                if (TextUtils.isEmpty(picUid)){
                    mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    PhotoUtil.getPhoto(self, DRIVING_PHOTOGRAPH, DRIVING_GALLERY,
                            new File(mPhotoPath));
                }else{
                    Intent it = new Intent(self, ImageGallery.class);
                    str = new String[1];
                    str[0] = imgs;
                    strid = new String[1];
                    strid [0] = picUid;
//                    System.out.println("++++++++++"+str[0]);
                    it.putExtra("imgurls", str);
                    it.putExtra("imgids", strid);
                    startActivity(it);
                }

                break;
            case R.id.driver_img:
                if (TextUtils.isEmpty(picUids)){
                    mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    PhotoUtil.getPhoto(self, DRIVER_PHOTOGRAPH, DRIVER_GALLERY,
                            new File(mPhotoPath));
                }else{
                    Intent ten = new Intent(self, ImageGallery.class);
                    strs = new String[1];
                    strs[0] = img;
                    strsid = new String[1];
                    strsid [0] = picUids;

                    ten.putExtra("imgurls", strs);
                    ten.putExtra("imgids", strsid);
                    startActivity(ten);
                }


                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MODEL:
                    brandName = data.getStringExtra("brandName");
                    brandLogo = data.getStringExtra("brandLogo");
                    modelName = data.getStringExtra("modelName");
                    modelSlug = data.getStringExtra("modelSlug");
                    carName.setText(modelName);
                    showToast(brandLogo);


                    break;
                case DRIVING_GALLERY:
                    Bitmap btp = PhotoUtil.checkImage(self, data);
                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
                    btp.recycle();
                    showProgressDialog("上传图片中...");
                    updrivingPic(mPhotoPath);
                    break;
                case DRIVING_PHOTOGRAPH:
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    showProgressDialog("上传图片中...");
                    updrivingPic(newPath);
                    break;

                case DRIVER_GALLERY:
                    Bitmap btp2 = PhotoUtil.checkImage(self, data);
                    PhotoUtil.saveLocalImage(btp2, new File(mPhotoPath));
                    btp2.recycle();
                    showProgressDialog("上传图片中...");
                    updriverPic(mPhotoPath);
                    break;
                case DRIVER_PHOTOGRAPH:
                    Bitmap btp3 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath2 = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degrees = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp3, new File(newPath2), degrees);
                    btp3.recycle();
                    showProgressDialog("上传图片中...");
                    updriverPic(newPath2);
                    break;

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
