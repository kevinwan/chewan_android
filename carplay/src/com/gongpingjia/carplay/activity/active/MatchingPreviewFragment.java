package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.view.pop.SelectPicturePop;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 匹配结果预览页
 * Created by Administrator on 2015/11/21.
 */
public class MatchingPreviewFragment extends CarPlayBaseFragment implements View.OnClickListener{
    static MatchingPreviewFragment instance;
    View mainV;
    TextView nickNameT,ageT,payT,transferT,locationT,distanceT;
    ImageView headAttI,carAttI,sexI,active_bgI;
    RelativeLayout sexLayoutR;
    Button changePhotoBtn,nextMatchingBtn;

    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;

    //选择图片id
    String photoId = "";

    User user;

    public static MatchingPreviewFragment getInstance() {
        if (instance == null) {
            instance = new MatchingPreviewFragment();
        }

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_matching_preview, null);
        EventBus.getDefault().register(this);
        initView();
        return mainV;
    }

    private void initView() {
        user = User.getInstance();
        mCacheDir = new File(getActivity().getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        nickNameT = (TextView) mainV.findViewById(R.id.tv_nickname);
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

        changePhotoBtn.setOnClickListener(this);
        nextMatchingBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //更换照片
            case R.id.changephoto:
                final SelectPicturePop pop = SelectPicturePop.getInstance(getActivity(),0);
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
                        pop.dismiss();
                    }
                });
                pop.show();
                break;
            //继续匹配
            case R.id.nextmatching:

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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path){
        DhNet net = new DhNet(API2.CWBaseurl + "/activity/cover/upload?userId=" + user.getUserId() + "&token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(getActivity()) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                ((MainActivity2) getActivity()).hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    String photoUrl = JSONUtil.getString(jo, "photoUrl");
                    photoId = JSONUtil.getString(jo, "photoId");
                }
            }
        });
    }
}
