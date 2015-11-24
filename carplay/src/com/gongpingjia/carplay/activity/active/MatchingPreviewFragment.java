package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.activity.my.CarPlayAlbum;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.MatchingEB;
import com.gongpingjia.carplay.bean.PersonShareActive;
import com.gongpingjia.carplay.bean.TabEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.view.pop.SelectPicturePop;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 匹配结果预览页
 * Created by Administrator on 2015/11/21.
 */
public class MatchingPreviewFragment extends CarPlayBaseFragment implements View.OnClickListener {
    static MatchingPreviewFragment instance;
    public final int CARPLAYALBUM_PHOTOID = 100;
    View mainV;
    TextView nickNameT, ageT, payT, transferT, locationT, distanceT, activeTypeT, promptT;
    ImageView headAttI, carAttI, sexI, active_bgI;
    RelativeLayout sexLayoutR;
    Button changePhotoBtn, nextMatchingBtn;

    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;

    //选择图片id
    String photoId = "";

    User user;

    MatchingEB matchingEB;

    JSONArray albumJsa;

    public MatchingPreviewFragment () {}

    public MatchingPreviewFragment (MatchingEB matchingEB) {
        this.matchingEB = matchingEB;
    }

//    public static MatchingPreviewFragment getInstance() {
////        if (instance == null) {
//            instance = new MatchingPreviewFragment();
////        }
//        return instance;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getMatchingEB();
//    }

//    public void setParams(MatchingEB matchingEB) {
//
//        getMatchingEB();
////        Log.d("msg", "setParams");
//    }

    private void getMatchingEB(){
        if (matchingEB!=null){
            activeTypeT.setText("想找人" + matchingEB.getType());
            payT.setText(matchingEB.getPay());
            transferT.setText(matchingEB.isTransfer() ? "包接送" : "");
            Map<String,String> map=matchingEB.getDestination();
            if (map!=null){
                locationT.setText(map.get("province")+map.get("city")+map.get("district")+map.get("street"));
            }else {
                locationT.setText("地点待定");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_matching_preview, null);
//        EventBus.getDefault().register(this);
        initView();
        return mainV;
    }

    private void initView() {
        user = User.getInstance();
        mCacheDir = new File(getActivity().getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        nickNameT = (TextView) mainV.findViewById(R.id.tv_nickname);
        activeTypeT = (TextView) mainV.findViewById(R.id.activetype);
        sexLayoutR = (RelativeLayout) mainV.findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) mainV.findViewById(R.id.iv_sex);
        ageT = (TextView) mainV.findViewById(R.id.tv_age);
        payT = (TextView) mainV.findViewById(R.id.pay);
        transferT = (TextView) mainV.findViewById(R.id.transfer);
        locationT = (TextView) mainV.findViewById(R.id.location);
        distanceT = (TextView) mainV.findViewById(R.id.tv_distance);
        headAttI = (ImageView) mainV.findViewById(R.id.head_att);
        carAttI = (ImageView) mainV.findViewById(R.id.iv_car_logo);
        active_bgI = (ImageView) mainV.findViewById(R.id.active_bg);
        changePhotoBtn = (Button) mainV.findViewById(R.id.changephoto);
        nextMatchingBtn = (Button) mainV.findViewById(R.id.nextmatching);
        promptT = (TextView) mainV.findViewById(R.id.prompt);

        changePhotoBtn.setOnClickListener(this);
        nextMatchingBtn.setOnClickListener(this);

        getMatchingEB();

        promptT.setText("你的 '匹配活动意向意向' 将以下面的形式展示给他人" + "\n" +
                "不上传真人头像被邀请率会很低哟~");
        Log.d("msg", "initView");
        getMyDetails();
    }

    public void getMyDetails() {

        DhNet verifyNet = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId()
                + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGetInDialog(new NetTask(getActivity()) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                System.out.println(user.getUserId() + "---------" + user.getToken());
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    JSONObject car = JSONUtil.getJSONObject(jo, "car");
                    nickNameT.setText(JSONUtil.getString(jo, "nickname"));
                    String gender = JSONUtil.getString(jo, "gender");
                    if (("男").equals(gender)) {
                        sexLayoutR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    } else {
                        sexLayoutR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }
                    ViewUtil.bindView(ageT, JSONUtil.getInt(jo, "age"));

                    String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                    String licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");
                    headAttI.setImageResource("认证通过".equals(photoAuthStatus) ? R.drawable.headaut_dl : R.drawable.headaut_no);
                    if ("认证通过".equals(licenseAuthStatus)) {
                        String carlogo = JSONUtil.getString(car, "logo");
                        ViewUtil.bindNetImage(carAttI, carlogo, "head");
                    }
                    albumJsa = JSONUtil.getJSONArray(jo, "album");
                    if (albumJsa != null) {
                        try {
                            JSONObject json = (JSONObject) albumJsa.get(0);
                            ViewUtil.bindNetImage(active_bgI, (String) json.get("url"), "default");
                            PersonShareActive.image=(String) json.get("url");
                        } catch (JSONException e) {
                            ViewUtil.bindNetImage(active_bgI, JSONUtil.getString(jo,"avatar"), "default");
                            e.printStackTrace();
                        }
                    } else {
                        ViewUtil.bindNetImage(active_bgI, JSONUtil.getString(jo,"avatar"), "default");
                        PersonShareActive.image=JSONUtil.getString(jo,"avatar");
                    }
                    PersonShareActive.matchingEB = matchingEB;
                    PersonShareActive.shareTitle = "我想找人一起"+matchingEB.getType();
                    Map<String,String> map=matchingEB.getDestination();
                    if (map!=null){
                        PersonShareActive.shareContent = map.get("province")+map.get("city")+map.get("district")+map.get("street")+"\n"+matchingEB.getPay()+"\t"+(matchingEB.isTransfer() ? "包接送" : "");
                    }else {
                        PersonShareActive.shareContent = "地点待定"+"\n"+matchingEB.getPay()+"\t"+(matchingEB.isTransfer() ? "包接送" : "");
                    }


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //更换照片
            case R.id.changephoto:
                final SelectPicturePop pop = SelectPicturePop.getInstance(getActivity(), 0);
                //拍照
                pop.setPhotoGraphListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                        final File tempFile = new File(mPhotoPath);
                        Intent getImageByCamera = new Intent(
                                "android.media.action.IMAGE_CAPTURE");
                        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(tempFile));
                        startActivityForResult(getImageByCamera,
                                Constant.TAKE_PHOTO);
                        pop.dismiss();
                    }
                });
                //相册
                pop.setAlbumListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                PhotoSelectorActivity.class);
                        intent.putExtra(PhotoSelectorActivity.KEY_MAX,
                                1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, Constant.PICK_PHOTO);
                        pop.dismiss();
                    }
                });
                //上传过的图片
                pop.setExistingListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getActivity(), CarPlayAlbum.class);
                        List<JSONObject> album = new ArrayList<JSONObject>();
                        String [] photoUrl = null;
                        String [] photoId = null;
                        if (albumJsa!=null){
                            photoUrl = new String[albumJsa.length()];
                            photoId = new String[albumJsa.length()];
                            for (int i=0;i<albumJsa.length();i++){
                                try {
                                    photoUrl[i] = albumJsa.getJSONObject(i).get("url").toString();
                                    photoId[i] =  albumJsa.getJSONObject(i).get("id").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        it.putExtra("photoUrl",photoUrl);
                        it.putExtra("photoId",photoId);
                        startActivityForResult(it, CARPLAYALBUM_PHOTOID);
                        pop.dismiss();
                    }
                });
                pop.show();
                break;
            //继续匹配
            case R.id.nextmatching:
                nextMatching();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constant.PICK_PHOTO:
                    ((MainActivity2) getActivity()).showProgressDialog("");
                    if (data != null && data.getExtras() != null) {
                        List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
                        if (photos == null || photos.isEmpty()) {
                            ((MainActivity2) getActivity()).showToast("没有选择图片!");
                        } else {
                            for (int i = 0; i < photos.size(); i++) {
                                String newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
                                        .getAbsolutePath();
                                Bitmap btp = PhotoUtil.getLocalImage(new File(photos.get(i).getOriginalPath()));
                                PhotoUtil.saveLocalImageSquare(btp, new File(newPhotoPath));
                                uploadHead(newPhotoPath);
                            }
                        }
                    }
                    break;
                case Constant.TAKE_PHOTO:
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    String newPath = PhotoUtil.saveLocalImage(btp1, degree, getActivity());
                    btp1.recycle();
                    ((MainActivity2) getActivity()).showProgressDialog("图片上传中...");
                    uploadHead(newPath);
                    break;

                case CARPLAYALBUM_PHOTOID:
                    photoId = data.getStringExtra("photosid");
                    ViewUtil.bindNetImage(active_bgI, data.getStringExtra("photourl"),"default");
                    PersonShareActive.photoId = photoId;
                    PersonShareActive.image = data.getStringExtra("photourl");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path) {
        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/album/upload?token=" + user.getToken()+"&type=1");
        net.upload(new FileInfo("attach", new File(path)), new NetTask(getActivity()) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                ((MainActivity2) getActivity()).hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    String photoUrl = JSONUtil.getString(jo, "photoUrl");
                    photoId = JSONUtil.getString(jo, "photoId");
                    ViewUtil.bindNetImage(active_bgI, photoUrl, "default");
                    //控制附近列表刷新
                    EventBus.getDefault().post(new String("刷新附近列表"));
                    //控制我的页面刷新
                    EventBus.getDefault().post(new String("上传成功"));
                    PersonShareActive.photoId = photoId;
                    PersonShareActive.image = photoUrl;
                }
            }
        });
    }

    private void nextMatching(){
        final DhNet dhNet = new DhNet(API2.getMatchUrl(user.getUserId(), user.getToken()));
        dhNet.addParam("majorType",matchingEB.getMajorType() );
        dhNet.addParam("type",matchingEB.getType());
        dhNet.addParam("pay",matchingEB.getPay());
        dhNet.addParam("transfer",matchingEB.isTransfer());
        dhNet.addParam("destination", matchingEB.getDestination());
        dhNet.addParam("estabPoint", matchingEB.getEstabPoint());
        dhNet.addParam("establish", matchingEB.getEstablish());
        if (!TextUtils.isEmpty(photoId)){
            dhNet.addParam("cover",photoId);
        }

        dhNet.doPost(new NetTask(getActivity()) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("type", matchingEB.getType());
                    map.put("pay", matchingEB.getPay());
                    map.put("majorType", matchingEB.getMajorType());
                    map.put("transfer", matchingEB.isTransfer());
                    TabEB tab = new TabEB(2, map);
                    //控制主页跳往匹配意向结果页
                    EventBus.getDefault().post(tab);
                }
            }
        });

    }


}
