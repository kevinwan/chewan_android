package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * 头像认证
 */
public class HeadAttestationActivity extends CarPlayBaseActivity implements View.OnClickListener{
    Button head_authenticate;
    ImageView up_head;
    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;
    String photoUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_attestation);
    }

    @Override
    public void initView() {
         head_authenticate = (Button) findViewById(R.id.head_authenticate);
         up_head = (ImageView) findViewById(R.id.up_head);
         up_head.setOnClickListener(this);


        if (getIntent().getStringExtra("photoId") != null) {
            ImageLoader.getInstance().displayImage(
                    getIntent().getStringExtra("photoUrl"), up_head);

            photoUid = getIntent().getStringExtra("photoId");
        }


    }
    private void uploadHead(String path) {
        DhNet net = new DhNet(API.uploadHead);
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    photoUid = JSONUtil.getString(jo, "photoId");
                } else {
                    up_head.setImageResource(R.drawable.head_icon);
                    photoUid = "";
                    showToast("上传失败,重新上传");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.up_head:
                mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
                        + ".jpg").getAbsolutePath();
                PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
                        new File(mPhotoPath));
                break;
            case R.id.head_authenticate:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            switch (requestCode){
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
                    up_head.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
                    showProgressDialog("上传头像中...");
                    uploadHead(mPhotoPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
