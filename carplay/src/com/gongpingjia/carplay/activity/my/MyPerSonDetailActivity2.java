package com.gongpingjia.carplay.activity.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.adapter.MyFragmentAlbumAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.view.RoundImageView;

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
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的详情
 */
public class MyPerSonDetailActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private RoundImageView headI;
    private ImageView sexI, photo_bgI, addPhoto;
    private TextView attestationT, nameT, ageT, completenessT, txtphotoAuthStatusT, attestation_txtT;
    private Button perfectBtn;
    private RelativeLayout sexbgR;
    private LinearLayout myphotoL, myactiveL, myattentionL, headattestationL, carattestationL;
    private RecyclerView recyclerView;
    public static final int PERSONAL = 2;

    User user;

    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;

    MyFragmentAlbumAdapter mAdapter;

    private List<PhotoState> mPhotoStates;

    List<JSONObject> album;

    List<JSONObject> newAlbm;

    //上传图片总数
    private int uploadPhotoCount = 0;

    //已上传的图片
    private int uploadedCount = 0;

    String age;
    String name, gender, headimg, photoAuthStatus, licenseAuthStatus, carbradn, carlogo, carmodel, carslug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_per_son_detail2);

        EventBus.getDefault().register(this);

    }

    @Override
    public void initView() {
        setTitle("我的详情");
        user = User.getInstance();

        album = new ArrayList<JSONObject>();
        newAlbm = new ArrayList<JSONObject>();
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        headI = (RoundImageView) findViewById(R.id.head);
        attestationT = (TextView) findViewById(R.id.attestation);
        nameT = (TextView) findViewById(R.id.name);
        sexbgR = (RelativeLayout) findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) findViewById(R.id.iv_sex);

        ageT = (TextView) findViewById(R.id.tv_age);
        completenessT = (TextView) findViewById(R.id.txt_completeness);
        perfectBtn = (Button) findViewById(R.id.perfect);
        photo_bgI = (ImageView) findViewById(R.id.photo_bg);
        myphotoL = (LinearLayout) findViewById(R.id.myphoto);
        myactiveL = (LinearLayout) findViewById(R.id.myactive);
        myattentionL = (LinearLayout) findViewById(R.id.myattention);
        headattestationL = (LinearLayout) findViewById(R.id.headattestation);
        carattestationL = (LinearLayout) findViewById(R.id.carattestation);
        txtphotoAuthStatusT = (TextView) findViewById(R.id.txtphotoAuthStatus);
        attestation_txtT = (TextView) findViewById(R.id.attestation_txt);
        addPhoto = (ImageView) findViewById(R.id.addphoto);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        perfectBtn.setOnClickListener(this);
        myactiveL.setOnClickListener(this);
        myattentionL.setOnClickListener(this);
        headattestationL.setOnClickListener(this);
        carattestationL.setOnClickListener(this);
        headI.setOnClickListener(this);
        addPhoto.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(self);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdapter = new MyFragmentAlbumAdapter(self);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        getMyDetails();
    }

    private void getAlbum(JSONArray albumJsa) {
        album.clear();
        if (albumJsa != null) {
            for (int i = 0; i < albumJsa.length(); i++) {
                try {
                    album.add(albumJsa.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.setData(album);
        }
    }


    public void getMyDetails() {

        DhNet verifyNet = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId()
                + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGetInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println(user.getUserId() + "---------" + user.getToken());
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    JSONObject car = JSONUtil.getJSONObject(jo, "car");
                    carbradn = JSONUtil.getString(car, "brand");
                    carlogo = JSONUtil.getString(car, "logo");
                    carmodel = JSONUtil.getString(car, "model");
                    carslug = JSONUtil.getString(car, "slug");
                    name = JSONUtil.getString(jo, "nickname");
                    ViewUtil.bindView(nameT, JSONUtil.getString(jo, "nickname"));
                    gender = JSONUtil.getString(jo, "gender");
                    if (("男").equals(gender)) {
                        sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    } else {
                        sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }
                    headimg = JSONUtil.getString(jo, "avatar");

                    ViewUtil.bindNetImage(headI, JSONUtil.getString(jo, "avatar"), "head");
//                    ImageLoader.getInstance().displayImage(JSONUtil.getString(jo, "avatar"), headI);
//                    photo_bgI.setBackgroundResource(R.drawable.vp_third);
                    age = String.valueOf(JSONUtil.getInt(jo, "age"));
                    ViewUtil.bindView(ageT, JSONUtil.getInt(jo, "age"));
//                    //设置高斯模糊
//                    Fglass.blur(ageT, mainV.findViewById(R.id.photo_bg_txt), 2, 8);


//                    Blurry.with(getActivity())
//                            .radius(10)
//                            .sampling(8)
//                            .async()
//                            .capture(photo_bgI)
//                            .into((ImageView) photo_bgI);

                    photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                    licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");
                    ViewUtil.bindView(txtphotoAuthStatusT, JSONUtil.getString(jo, "photoAuthStatus"));
                    ViewUtil.bindView(attestation_txtT, JSONUtil.getString(jo, "licenseAuthStatus"));
                    //头像认证
                    if (photoAuthStatus.equals("未认证")) {
                        attestationT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                        attestationT.setText("未认证");
                        headattestationL.setEnabled(true);
                    } else if (photoAuthStatus.equals("认证通过")) {
                        attestationT.setBackgroundResource(R.drawable.btn_yellow_fillet);
                        attestationT.setText("已认证");
                        headattestationL.setEnabled(false);
                    } else if (photoAuthStatus.equals("认证中")) {
                        attestationT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                        attestationT.setText("未认证");
                        headattestationL.setEnabled(true);
                    }

                    //车主认证
                    if (licenseAuthStatus.equals("未认证")) {
                        carattestationL.setEnabled(true);
                    } else if (licenseAuthStatus.equals("认证通过")) {
                        carattestationL.setEnabled(false);
                    } else if (licenseAuthStatus.equals("认证中")) {
                        carattestationL.setEnabled(true);
                    }
                    if (licenseAuthStatus.equals("未认证") && photoAuthStatus.equals("未认证")) {
                        completenessT.setText("资料完成度60%,越高越吸引人");
                    } else if (licenseAuthStatus.equals("认证中") && photoAuthStatus.equals("认证中")) {
                        completenessT.setText("资料完成度60%,越高越吸引人");
                    } else if (licenseAuthStatus.equals("认证通过") || photoAuthStatus.equals("认证通过")) {
                        completenessT.setText("资料完成度80%,越高越吸引人");
                    } else if (licenseAuthStatus.equals("认证通过") && photoAuthStatus.equals("认证通过")) {
                        completenessT.setText("资料完成度100%,越高越吸引人");
                    }

                    JSONArray albumJsa = JSONUtil.getJSONArray(jo, "album");
                    getAlbum(albumJsa);

                    try {
                        if (albumJsa != null) {
                            ViewUtil.bindNetImage(photo_bgI, albumJsa.getJSONObject(0).getString("url"), "default");
                        } else {
                            ViewUtil.bindNetImage(photo_bgI, headimg, "head");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            //编辑资料
            case R.id.head:
                it = new Intent(self, EditPersonalInfoActivity2.class);
                it.putExtra("name", name);
                it.putExtra("gender", gender);
                it.putExtra("headimg", headimg);
                it.putExtra("photoAuthStatus", photoAuthStatus);
                it.putExtra("licenseAuthStatus", licenseAuthStatus);
//                it.putExtra("carbradn",carbradn);
//                it.putExtra("carlogo",carlogo);
//                it.putExtra("carmodel",carmodel);
//                it.putExtra("carslug",carslug);
                it.putExtra("age", age);
                startActivity(it);
                break;
            //完善信息
            case R.id.perfect:
                it = new Intent(self, EditPersonalInfoActivity2.class);
                it.putExtra("name", name);
                it.putExtra("gender", gender);
                it.putExtra("headimg", headimg);
                it.putExtra("photoAuthStatus", photoAuthStatus);
                it.putExtra("licenseAuthStatus", licenseAuthStatus);
                it.putExtra("age", age);
                startActivity(it);
                break;
            //我的活动
            case R.id.myactive:
                it = new Intent(self, MyDynamicActivity.class);
                startActivity(it);
                break;
            //我的关注
            case R.id.myattention:
                it = new Intent(self, SubscribeActivity2.class);
                startActivity(it);
                break;
            //头像认证
            case R.id.headattestation:
                it = new Intent(self, HeadAttestationActivity.class);
                startActivity(it);
                break;
            //车主认证
            case R.id.carattestation:
                it = new Intent(self, AuthenticateOwnersActivity2.class);
                startActivity(it);
                break;
            //上传相册
            case R.id.addphoto:
                newAlbm.clear();
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                final File tempFile = new File(mPhotoPath);
                final CharSequence[] items = {"相册", "拍照"};
                AlertDialog dlg = new AlertDialog.Builder(self).setTitle("选择图片")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 1) {
                                    Intent getImageByCamera = new Intent(
                                            "android.media.action.IMAGE_CAPTURE");
                                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(tempFile));
                                    startActivityForResult(getImageByCamera,
                                            Constant.TAKE_PHOTO);
                                } else {
                                    Intent intent = new Intent(self,
                                            PhotoSelectorActivity.class);
                                    intent.putExtra(PhotoSelectorActivity.KEY_MAX,
                                            9);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivityForResult(intent, Constant.PICK_PHOTO);
                                }
                            }
                        }).create();
                Window window = dlg.getWindow();
                window.setWindowAnimations(R.style.mystyle);
                dlg.show();
                break;


            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PICK_PHOTO:
                    showProgressDialog("图片上传中...");
                    if (data != null && data.getExtras() != null) {
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
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    String newPath = PhotoUtil.saveLocalImage(btp1, degree, self);
                    btp1.recycle();


                    showProgressDialog("上传头像中...");
                    uploadPhotoCount = 1;
                    uploadHead(newPath);
                    break;
                case PERSONAL:
                    txtphotoAuthStatusT.setText(data.getStringExtra("photoAuthStatus"));
                    nameT.setText(data.getStringExtra("nickname"));
                    ageT.setText(data.getStringExtra("age"));
                    attestation_txtT.setText(data.getStringExtra("licenseAuthStatus"));
                    ViewUtil.bindNetImage(headI, data.getStringExtra("head"), "head");
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
                    uploadedCount = uploadedCount + 1;
                    JSONObject jo = response.jSONFromData();
                    String photoUrl = JSONUtil.getString(jo, "photoUrl");
                    String photoId = JSONUtil.getString(jo, "photoId");
                    showToast("上传成功");

                    try {
                        JSONObject json = new JSONObject();
                        json.put("url", photoUrl);
                        json.put("id", photoId);
                        newAlbm.add(json);
                        Log.d("msg", "添加数据");
                        if (uploadPhotoCount == uploadedCount) {
//                            album.add(0, new JSONObject().put("url", photoUrl));
                            Log.d("msg", "相册大小" + newAlbm.size());
                            album.addAll(0, newAlbm);
                            mAdapter.setData(album);
                            uploadedCount = 0;
                            ViewUtil.bindNetImage(photo_bgI, (String) album.get(0).get("url"), "head");
                            DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/photoCount?token=" + user.getToken());
                            net.addParam("count", uploadPhotoCount);
                            net.doPost(new NetTask(self) {
                                @Override
                                public void doInUI(Response response, Integer transfer) {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.d("msg", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(String success) {
        if ("上传成功".equals(success)) {
            getMyDetails();
        }
    }
}
