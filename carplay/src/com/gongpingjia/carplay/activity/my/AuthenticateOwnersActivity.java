package com.gongpingjia.carplay.activity.my;

import java.io.File;

import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;
import android.app.Activity;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.dialog.PhotoSelectDialog;
import com.gongpingjia.carplay.view.dialog.PhotoSelectDialog.OnStateChangeListener;
import com.gongpingjia.carplay.util.MD5Util;

/**
 * 
 * @Description 认证车主
 * @author wang
 * @date 2015-7-17 上午10:14:30
 */
public class AuthenticateOwnersActivity extends CarPlayBaseActivity implements OnClickListener
{
    
    private TextView modelT = null;
    
    ImageView picI;
    
    User user;
    
    PhotoSelectDialog photoDialog;
    
    String mPhotoPath;
    
    public static final int MODEL = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_owners);
    }
    
    @Override
    public void initView()
    {
        setTitle("车主认证");
        setRightAction("跳过", -1, new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                Intent it = getIntent();
                setResult(Activity.RESULT_OK, it);
                finish();
                
            }
        });
        modelT = (TextView)findViewById(R.id.model);
        modelT.setOnClickListener(this);
        picI = (ImageView)findViewById(R.id.pic);
        picI.setOnClickListener(this);
        user = User.getInstance();
        photoDialog = new PhotoSelectDialog(self);
        photoDialog.setOnStateChangeListener(new OnStateChangeListener()
        {
            
            @Override
            public void close(String photoPath)
            {
                mPhotoPath = photoPath;
            }
        });
    }
    
    private void authtion()
    {
        DhNet net = new DhNet(API.CWBaseurl+"/user/"+user.getUserId()+"/authentication?token="+user.getToken());
//        net.addParam("drivingExperience", value)
//        net.addParam("carBrand", value)
//        net.addParam("carBrandLogo", value)
//        net.addParam("carModel", value)
    }
    
    private void uploadPic(String path)
    {
        DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId() + "/license/upload?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    JSONObject jo = response.jSONFromData();
                }
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.model:
                Intent intent = new Intent(self, CarTypeSelectActivity.class);
                startActivityForResult(intent, MODEL);
                break;
            
            case R.id.pic:
                photoDialog.show();
                break;
            
            default:
                break;
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case Constant.TAKE_PHOTO:
                    PhotoUtil.onPhotoFromCamera(self, Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000);
                    break;
                case Constant.PICK_PHOTO:
                    PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath, data, 1, 1, 1000);
                    break;
                case Constant.ZOOM_PIC:
                    Bitmap bmp = PhotoUtil.getLocalImage(new File(mPhotoPath));
                    picI.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
                    uploadPic(mPhotoPath);
                    break;
                
                case MODEL:
                    
                    break;
            }
        }
    }

	
}
