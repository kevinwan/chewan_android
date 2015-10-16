package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
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
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_attestation);
    }

    @Override
    public void initView() {
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
         head_authenticate = (Button) findViewById(R.id.head_authenticate);
         up_head = (ImageView) findViewById(R.id.up_head);
         up_head.setOnClickListener(this);
        head_authenticate.setOnClickListener(this);
        user = User.getInstance();

        if (getIntent().getStringExtra("photoId") != null) {
            ImageLoader.getInstance().displayImage(
                    getIntent().getStringExtra("photoUrl"), up_head);

            photoUid = getIntent().getStringExtra("photoId");
        }


    }
    private void uploadHead(String path) {
        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
        up_head.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+"/photo/upload?token="+user.getToken());
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
                if (TextUtils.isEmpty(photoUid)) {
                    showToast("请上传头像");
                    return;
                }
                DhNet net = new DhNet(API2.CWBaseurl+"/user/"+user.getUserId()+"/photo/authentication?token="+user.getToken());
                net.addParam("photoId",photoUid);
                net.doPostInDialog(new NetTask(self) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if(response.isSuccess()){
                            Intent intent = getIntent();
                            self.setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case Constant.PICK_PHOTO:
                    Bitmap btp = PhotoUtil.checkImage(self, data);
                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
                    btp.recycle();
                    showProgressDialog("上传头像中...");
                    uploadHead(mPhotoPath);
                    break;
                case Constant.TAKE_PHOTO:
                    Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    String newPath = new File(mCacheDir, System.currentTimeMillis()
                            + ".jpg").getAbsolutePath();
                    int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                    PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                    btp1.recycle();
                    showProgressDialog("上传头像中...");
                    uploadHead(newPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
