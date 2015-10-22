package com.gongpingjia.carplay.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.AttentionImageView;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2015/10/22.
 * 随便看看dialog
 */
public class LookAroundDialog extends BaseAlertDialog {

    Context mContext;

    TextView nickname, car_name, age, pay, transfer, location, distance;
    ImageView headatt, car_logo, sex, active_bg;
    AttentionImageView attention;
    RelativeLayout sexLayout;
    AnimButtonView invite;
    Button upload, takephotos, album;

    JSONObject jo;

    private boolean uploadFlag = true;

    // 图片缓存根目录
    private File mCacheDir;
    private String mPhotoPath;

    public LookAroundDialog(Context context, JSONObject jo) {
        super(context);
        this.mContext = context;
        this.jo = jo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lookaround);

        mCacheDir = new File(mContext.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        initView();
    }

    private void initView() {

        nickname = (TextView) findViewById(R.id.tv_nickname);
        headatt = (ImageView) findViewById(R.id.head_att);
        car_logo = (ImageView) findViewById(R.id.iv_car_logo);
        car_name = (TextView) findViewById(R.id.tv_car_name);
        attention = (AttentionImageView) findViewById(R.id.attention);
        sexLayout = (RelativeLayout) findViewById(R.id.layout_sex_and_age);
        sex = (ImageView) findViewById(R.id.iv_sex);
        age = (TextView) findViewById(R.id.tv_age);
        pay = (TextView) findViewById(R.id.pay);
        transfer = (TextView) findViewById(R.id.transfer);
        active_bg = (ImageView) findViewById(R.id.active_bg);
        invite = (AnimButtonView) findViewById(R.id.invite);
        location = (TextView) findViewById(R.id.location);
        distance = (TextView) findViewById(R.id.tv_distance);
        upload = (Button) findViewById(R.id.upload);
        takephotos = (Button) findViewById(R.id.takephotos);
        album = (Button) findViewById(R.id.album);

        if (jo != null) {
            bindData();
        }

    }

    private void bindData() {
//用户信息,所在地,car信息,头像信息
        JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
        JSONObject distancejo = JSONUtil.getJSONObject(jo, "destination");
        JSONObject carjo = JSONUtil.getJSONObject(userjo, "car");
        JSONArray albumjsa = JSONUtil.getJSONArray(userjo, "album");
        //昵称,活动类型,年龄,性别,头像
        String activetype = JSONUtil.getString(jo, "type");
        nickname.setText(JSONUtil.getString(userjo, "nickname") + "想约人" + activetype);
        age.setText(JSONUtil.getInt(userjo, "age") + "");
        String sexstr = JSONUtil.getString(userjo, "gender");
        if ("男".equals(sexstr)) {
            sexLayout.setBackgroundResource(R.drawable.radio_sex_man_normal);
            sex.setImageResource(R.drawable.icon_man3x);
        } else {
            sexLayout.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            sex.setImageResource(R.drawable.icon_woman3x);
        }
        ViewUtil.bindNetImage(active_bg, JSONUtil.getString(userjo, "avatar"), "default");

        //头像认证,车主认证
        String headattstr = JSONUtil.getString(userjo, "photoAuthStatus");
        headatt.setImageResource("未认证".equals(headattstr) ? R.drawable.headaut_dl : R.drawable.headaut_no);

        User user = User.getInstance();
        if (user.isLogin()) {
            attention.setVisibility(JSONUtil.getString(userjo, "userId").equals(user.getUserId()) ? View.GONE : View.VISIBLE);
        } else {
            attention.setVisibility(View.VISIBLE);
        }
        //关注,是否包接送,付费类型,活动类型
        attention.setImageResource(JSONUtil.getBoolean(userjo, "subscribeFlag") ? R.drawable.icon_hearted : R.drawable.icon_heart);
        attention.setOnClickListener(new MyOnClick());
        boolean transferB = JSONUtil.getBoolean(jo, "transfer");
        String paystr = JSONUtil.getString(jo, "pay");
        pay.setText(paystr);
        if (transferB) {
            transfer.setVisibility(View.VISIBLE);
            transfer.setText("包接送");
        } else {
            transfer.setVisibility(View.GONE);
            transfer.setText("不包接送");
        }

        //所在地,距离
        int distanceI = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
        distance.setText(CarPlayUtil.numberWithDelimiter(distanceI));
        location.setText(JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street"));

        //car logo ,car name
        if (carjo == null) {
            car_logo.setVisibility(View.GONE);
            car_name.setVisibility(View.GONE);
        } else {
            car_logo.setVisibility(View.VISIBLE);
            car_name.setVisibility(View.VISIBLE);
            ViewUtil.bindNetImage(car_logo, JSONUtil.getString(carjo, "logo"), "head");
            ViewUtil.bindView(car_name, JSONUtil.getString(carjo, "model"));
        }

        //相册为空模糊效果
        if (albumjsa == null) {

        }

//        final View itemView = itemView;
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
//            }
//        });


//        holder.invite.startScaleAnimation();

        upload.setOnClickListener(new MyOnClick());

        takephotos.setOnClickListener(new MyOnClick());

        album.setOnClickListener(new MyOnClick());
    }

    class MyOnClick implements View.OnClickListener {


        public MyOnClick() {
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //上传
                case R.id.upload:
                    if (uploadFlag) {
                        uploadFlag = !uploadFlag;
                        takephotos.setVisibility(View.VISIBLE);
                        album.setVisibility(View.VISIBLE);
                    } else {
                        uploadFlag = !uploadFlag;
                        takephotos.setVisibility(View.GONE);
                        album.setVisibility(View.GONE);
                    }
                    break;
                //拍照
                case R.id.takephotos:
                    mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    Intent getImageByCamera = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(mPhotoPath)));
                    ((MainActivity2) mContext).startActivityForResult(getImageByCamera,
                            Constant.TAKE_PHOTO);
                    break;
                //相册
                case R.id.album:
                    mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    Intent intent = new Intent(mContext,
                            PhotoSelectorActivity.class);
                    intent.putExtra(PhotoSelectorActivity.KEY_MAX,
                            10);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ((MainActivity2) mContext).startActivityForResult(intent, Constant.PICK_PHOTO);

                    break;

                case R.id.attention:
                    User user = User.getInstance();
                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
                        @Override
                        public void onisLogin() {
                            JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                            boolean type = JSONUtil.getBoolean(userjo, "subscribeFlag");
                            String userId = JSONUtil.getString(userjo, "userId");
                            attentionorCancle(type, userId);
                        }

                        @Override
                        public void onLoginFail() {

                        }
                    });
                    break;
            }
        }
    }

    private void attentionorCancle(final boolean attentionB, String userId) {

        User user = User.getInstance();

        if (userId.equals(user.getUserId())) {

            return;
        }
        String url;
        if (!attentionB) {
            url = API2.CWBaseurl + "/user/" + user.getUserId() + "/listen?token=" + user.getToken();
        } else {
            url = API2.CWBaseurl + "/user/" + user.getUserId() + "/unlisten?token=" + user.getToken();
        }
        DhNet net = new DhNet(url);
        net.addParam("targetUserId", userId);
        net.doPostInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    attention.setImage(attentionB ? R.drawable.icon_heart : R.drawable.icon_hearted);
                    JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                    try {
                        userjo.put("subscribeFlag", !attentionB);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        });
    }
}
