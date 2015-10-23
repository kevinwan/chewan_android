package com.gongpingjia.carplay.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.adapter.MyFragmentAlbumAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.util.CarPlayUtil;
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


/**
 * 我的页面
 * +* @author Administrator
 */
public class MyFragment2 extends Fragment implements OnClickListener {
    View mainV;
    static MyFragment2 instance;
    private RoundImageView headI;
    private ImageView sexI, photo_bgI,addPhoto;
    private TextView attestationT, nameT, ageT, completenessT, txtphotoAuthStatusT, attestation_txtT;
    private Button perfectBtn;
    private RelativeLayout sexbgR;
    private LinearLayout myphotoL, myactiveL, myattentionL, headattestationL, carattestationL;
    private RecyclerView recyclerView;

    public static MyFragment2 getInstance() {
        if (instance == null) {
            instance = new MyFragment2();
        }

        return instance;
    }

    Context mContext;

    User user;

    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;

    MyFragmentAlbumAdapter mAdapter;

    private List<PhotoState> mPhotoStates;

    List<JSONObject> album;

    List<JSONObject> newAlbm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mainV = inflater.inflate(R.layout.fragment_my2, null);
        mContext = getActivity();
        user = User.getInstance();

        album = new ArrayList<JSONObject>();
        newAlbm = new ArrayList<JSONObject>();
        mCacheDir = new File(mContext.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        initView();
        return mainV;

    }

    private void initView() {
        headI = (RoundImageView) mainV.findViewById(R.id.head);
        attestationT = (TextView) mainV.findViewById(R.id.attestation);
        nameT = (TextView) mainV.findViewById(R.id.name);
        sexbgR = (RelativeLayout) mainV.findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) mainV.findViewById(R.id.iv_sex);

        ageT = (TextView) mainV.findViewById(R.id.tv_age);
        completenessT = (TextView) mainV.findViewById(R.id.txt_completeness);
        perfectBtn = (Button) mainV.findViewById(R.id.perfect);
        photo_bgI = (ImageView) mainV.findViewById(R.id.photo_bg);
        myphotoL = (LinearLayout) mainV.findViewById(R.id.myphoto);
        myactiveL = (LinearLayout) mainV.findViewById(R.id.myactive);
        myattentionL = (LinearLayout) mainV.findViewById(R.id.myattention);
        headattestationL = (LinearLayout) mainV.findViewById(R.id.headattestation);
        carattestationL = (LinearLayout) mainV.findViewById(R.id.carattestation);
        txtphotoAuthStatusT = (TextView) mainV.findViewById(R.id.txtphotoAuthStatus);
        attestation_txtT = (TextView) mainV.findViewById(R.id.attestation_txt);
        addPhoto = (ImageView) mainV.findViewById(R.id.addphoto);
        recyclerView = (RecyclerView) mainV.findViewById(R.id.recyclerView);

        perfectBtn.setOnClickListener(this);
        myactiveL.setOnClickListener(this);
        myattentionL.setOnClickListener(this);
        headattestationL.setOnClickListener(this);
        carattestationL.setOnClickListener(this);
        headI.setOnClickListener(this);
        addPhoto.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdapter = new MyFragmentAlbumAdapter(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        getMyDetails();

    }

    private void  getAlbum(JSONArray albumJsa){
        album.clear();
        if(albumJsa!=null) {
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
        verifyNet.doGetInDialog(new NetTask(mContext) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                System.out.println(user.getUserId() + "---------" + user.getToken());
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    ViewUtil.bindView(nameT, JSONUtil.getString(jo, "nickname"));
                    String gender = JSONUtil.getString(jo, "gender");
                    if (("男").equals(gender)) {
                        sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    } else {
                        sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }
                    String headimg = JSONUtil.getString(jo, "avatar");

                    ViewUtil.bindNetImage(headI, headimg, "head");
                    ViewUtil.bindNetImage(photo_bgI, headimg, "default");
//                    photo_bgI.setBackgroundResource(R.drawable.vp_third);
                    ViewUtil.bindView(ageT, JSONUtil.getInt(jo, "age"));
//                    //设置高斯模糊
//                    Fglass.blur(ageT, mainV.findViewById(R.id.photo_bg_txt), 2, 8);


//                    Blurry.with(getActivity())
//                            .radius(10)
//                            .sampling(8)
//                            .async()
//                            .capture(photo_bgI)
//                            .into((ImageView) photo_bgI);

                    String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                    String licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");
                    ViewUtil.bindView(txtphotoAuthStatusT, JSONUtil.getString(jo, "photoAuthStatus"));
                    ViewUtil.bindView(attestation_txtT, JSONUtil.getString(jo, "licenseAuthStatus"));
                    //头像认证
                    if (photoAuthStatus.equals("未认证")) {
                        attestationT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                        attestationT.setText("未认证");
                        headattestationL.setEnabled(true);
                    } else if (photoAuthStatus.equals("已认证")) {
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
                    } else if (licenseAuthStatus.equals("已认证")) {
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

                    JSONArray albumJsa = JSONUtil.getJSONArray(jo,"album");
                    getAlbum(albumJsa);

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
                it = new Intent(mContext, EditPersonalInfoActivity2.class);
                startActivity(it);
                break;
            //完善信息
            case R.id.perfect:
                it = new Intent(getActivity(), EditPersonalInfoActivity2.class);
                startActivity(it);
//                NearbyFilterDialog nearbyFilterDialog = new NearbyFilterDialog(getActivity());
//                nearbyFilterDialog.show();
//                getMyDetails();
                break;
            //我的活动
            case R.id.myactive:
                it = new Intent(mContext, AttentionMeActivity.class);
                startActivity(it);
                break;
            //我的关注
            case R.id.myattention:
                it = new Intent(mContext, SubscribeActivity2.class);
                startActivity(it);
                break;
            //头像认证
            case R.id.headattestation:
                it = new Intent(mContext, HeadAttestationActivity.class);
                startActivity(it);
                break;
            //车主认证
            case R.id.carattestation:
                it = new Intent(mContext, AuthenticateOwnersActivity2.class);
                startActivity(it);
                break;
            //上传相册
            case R.id.addphoto:
//                it = new Intent(mContext, CreateActiveActivity.class);
//                startActivity(it);
//                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
//                        + ".jpg").getAbsolutePath();
//                PhotoUtil.getPhoto(getActivity(), Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
//                        new File(mPhotoPath));
                newAlbm.clear();
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                // CarPlayUtil.getPhoto(self, Constant.TAKE_PHOTO,
                // Constant.PICK_PHOTO, new File(mCurPath));

                CarPlayUtil.getPhoto(getActivity(), Constant.TAKE_PHOTO, Constant.PICK_PHOTO, new File(mPhotoPath),
                        10);
                break;


            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        ((MainActivity2)getActivity()).showToast("11111111111111");
        if (resultCode == getActivity().RESULT_OK){
//            ((MainActivity2)getActivity()).showToast(requestCode+"++++++++++++");
            switch (requestCode){
                case Constant.PICK_PHOTO:
                    ((MainActivity2)getActivity()).showToast(Constant.PICK_PHOTO+"");
//                    Bitmap btp = PhotoUtil.checkImage(getActivity(), data);
//                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
//                    btp.recycle();
//                    ((MainActivity2)getActivity()).showProgressDialog("上传头像中...");
//                    System.out.println("上传成功+++++++++++++");
//                    uploadHead(mPhotoPath);

                    ((MainActivity2)getActivity()).showProgressDialog("图片上传中...");
                    if (data != null && data.getExtras() != null) {
                        @SuppressWarnings("unchecked")
                        List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
                        if (photos == null || photos.isEmpty()) {
                            ((MainActivity2)getActivity()).showToast("没有选择图片!");
                        } else {
//                            mSize = photos.size();
                            for (int i = 0; i < photos.size(); i++) {
                                String newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
                                        .getAbsolutePath();
                                Bitmap btp = PhotoUtil.getLocalImage(new File(photos.get(i).getOriginalPath()));
                                PhotoUtil.saveLocalImage(btp, new File(newPhotoPath));
                                uploadHead(newPhotoPath);
//                                if (i==photos.size()-1){
//                                    album.addAll(0,newAlbm);
//                                    mAdapter.setData(album);
//                                }
                            }
//                            if (newAlbm!=null){
//                                for (int i=0;i<newAlbm.size();i++){
//                                    album.add(0,newAlbm.get(i));
//                                }
//                                mAdapter.setData(album);
//                            }
                        }
                    }
                    break;
                case Constant.TAKE_PHOTO:
//                    ((MainActivity2)getActivity()).showToast(Constant.TAKE_PHOTO+"");
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    ((MainActivity2)getActivity()).showProgressDialog("上传头像中...");
                    System.out.println("上传成功+++++++++++++");
                    uploadHead(newPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path) {

        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
//        addPhoto.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl+"user/"+user.getUserId()+"/album/upload?token="+user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(mContext) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                ((MainActivity2)getActivity()).hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    String photoUrl = JSONUtil.getString(jo, "photoUrl");
                    ((MainActivity2) getActivity()).showToast("上传成功");

                    try {
//                        newAlbm.add(new JSONObject().put("url",photoUrl));
                        album.add(0, new JSONObject().put("url",photoUrl));
                        mAdapter.setData(album);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((MainActivity2)getActivity()).showToast("上传失败,重新上传");
                    System.out.println("上传失败----------------");
                }
            }
        });
    }
}