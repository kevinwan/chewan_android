package com.gongpingjia.carplay.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.greenrobot.event.EventBus;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by Administrator on 2015/10/22.
 * 随便看看dialog
 */
public class LookAroundDialog extends BaseAlertDialog {

    Context mContext;

    TextView nickname, car_name, age, pay, transfer, location, distance, join_desT;
    ImageView headatt, car_logo, sex, active_bg;
//    AttentionImageView attention;
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
        LinearLayout beijing  = (LinearLayout) findViewById(R.id.kankan_dismiss);
        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCacheDir = new File(mContext.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        initView();
    }

    private void initView() {

        nickname = (TextView) findViewById(R.id.tv_nickname);
        headatt = (ImageView) findViewById(R.id.head_att);
        car_logo = (ImageView) findViewById(R.id.iv_car_logo);
        car_name = (TextView) findViewById(R.id.tv_car_name);
//        attention = (AttentionImageView) findViewById(R.id.attention);
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
        join_desT = (TextView) findViewById(R.id.join_des);

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

        boolean applyFlag = JSONUtil.getBoolean(jo, "applyFlag");
        join_desT.setText(applyFlag ? "邀请中" : "邀 TA");
        if (applyFlag) {
            invite.setResourseAndBg(R.drawable.dynamic_grey
                    , R.drawable.dynamic_grey
            );
        } else {
            invite.setResourseAndBg(R.drawable.red_circle, R.drawable.red_circle);
        }

        if ("男".equals(sexstr)) {
            sexLayout.setBackgroundResource(R.drawable.radio_sex_man_normal);
            sex.setImageResource(R.drawable.icon_man3x);
        } else {
            sexLayout.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            sex.setImageResource(R.drawable.icon_woman3x);
        }
//        ViewUtil.bindNetImage(active_bg, JSONUtil.getString(userjo, "avatar"), "default");

//        Blurry.targetWidth = picWidth;
//        Blurry.targetHeight = picHeight;
        final User user = User.getInstance();
        if (user.isLogin()) {
            upload.setVisibility(user.isHasAlbum() ? View.GONE : View.VISIBLE);
        } else {
            upload.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(JSONUtil.getString(userjo, "avatar"), active_bg, CarPlayValueFix.optionsDefault, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                if (bitmap != null) {
                    final ImageView img = (ImageView) view;
                    if (!user.isHasAlbum()) {
                        img.setImageBitmap(bitmap);
                        Blurry.with(mContext)
                                .radius(10)
                                .sampling(8)
                                .async()
                                .capture(img)
                                .into(img);
                    } else {
                        img.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        //头像认证,车主认证
        String headattstr = JSONUtil.getString(userjo, "photoAuthStatus");
        headatt.setImageResource("认证通过".equals(headattstr) ? R.drawable.headaut_no : R.drawable.headaut_dl);

//        if (user.isLogin()) {
//            attention.setVisibility(JSONUtil.getString(userjo, "userId").equals(user.getUserId()) ? View.GONE : View.VISIBLE);
//        } else {
//            attention.setVisibility(View.VISIBLE);
//        }
//        //关注,是否包接送,付费类型,活动类型
//        attention.setImageResource(JSONUtil.getBoolean(userjo, "subscribeFlag") ? R.drawable.icon_hearted : R.drawable.icon_heart);
//        attention.setOnClickListener(new MyOnClick());
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
//        location.setText(JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street"));
        String locationS = JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street") + JSONUtil.getString(distancejo, "detail");
        locationS=locationS.replace("null","");
        String district = JSONUtil.getString(distancejo,"district");
        String street = JSONUtil.getString(distancejo,"street");
        if (TextUtils.isEmpty(locationS)) {
            location.setText("地点待定");
        } else {
            if (district.equals(street)){
                location.setText(JSONUtil.getString(distancejo, "city")+"市" + JSONUtil.getString(distancejo, "district"));
            }else{
                location.setText(JSONUtil.getString(distancejo, "city")+"市" + JSONUtil.getString(distancejo, "district")+ JSONUtil.getString(distancejo, "street"));
            }
        }
        //car logo ,car name
        if (carjo == null) {
            car_logo.setImageResource(R.drawable.no_car);
//            car_name.setVisibility(View.GONE);
        } else {
            car_logo.setVisibility(View.VISIBLE);
//            car_name.setVisibility(View.VISIBLE);
            ViewUtil.bindNetImage(car_logo, JSONUtil.getString(carjo, "logo"), "head");
//            ViewUtil.bindView(car_name, JSONUtil.getString(carjo, "model"));
        }


//        final View itemView = itemView;
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (onItemClick != null) {
//                    onItemClick.onItemClick(position, jo);
//                }
//
//            }
//        });

        active_bg.setOnClickListener(new MyOnClick());


        invite.startScaleAnimation();

        invite.setOnClickListener(new MyOnClick());

        upload.setOnClickListener(new MyOnClick());

        takephotos.setOnClickListener(new MyOnClick());

        album.setOnClickListener(new MyOnClick());
    }



    class MyOnClick implements View.OnClickListener {


        public MyOnClick() {
        }

        @Override
        public void onClick(View v) {
            User user = User.getInstance();
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
                    Integer takephotos = Constant.TAKE_PHOTO;
                    EventBus.getDefault().post(takephotos);
                    break;
                //相册
                case R.id.album:
                    Integer album = Constant.PICK_PHOTO;
                    EventBus.getDefault().post(album);

                    break;

//                case R.id.attention:
//                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
//                        @Override
//                        public void onisLogin() {
//                            JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
//                            boolean type = JSONUtil.getBoolean(userjo, "subscribeFlag");
//                            String userId = JSONUtil.getString(userjo, "userId");
//                            attentionorCancle(type, userId);
//                        }
//
//                        @Override
//                        public void onLoginFail() {
//
//                        }
//                    });
//                    break;
                case R.id.invite:
                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
                        @Override
                        public void onisLogin() {
                            join(JSONUtil.getString(jo, "activityId"));
                        }

                        @Override
                        public void onLoginFail() {

                        }
                    });
                    break;
                case R.id.active_bg:
                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
                        @Override
                        public void onisLogin() {
                            Intent it = new Intent(mContext, PersonDetailActivity2.class);
                            JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                            String userId = JSONUtil.getString(userjo, "userId");
                            it.putExtra("userId", userId);
                            mContext.startActivity(it);
                        }

                        @Override
                        public void onLoginFail() {

                        }
                    });
                    break;
            }
        }
    }




//    private void attentionorCancle(final boolean attentionB, String userId) {
//
//        User user = User.getInstance();
//
//        if (userId.equals(user.getUserId())) {
//
//            return;
//        }
//        String url;
//        if (!attentionB) {
//            url = API2.CWBaseurl + "/user/" + user.getUserId() + "/listen?token=" + user.getToken();
//        } else {
//            url = API2.CWBaseurl + "/user/" + user.getUserId() + "/unlisten?token=" + user.getToken();
//        }
//        DhNet net = new DhNet(url);
//        net.addParam("targetUserId", userId);
//        net.doPostInDialog(new NetTask(mContext) {
//            @Override
//            public void doInUI(Response response, Integer transfer) {
//                if (response.isSuccess()) {
//                    attention.setImage(attentionB ? R.drawable.icon_heart : R.drawable.icon_hearted);
//                    JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
//                    try {
//                        userjo.put("subscribeFlag", !attentionB);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    }
//                }
//
//            }
//        });
//    }


    private void join(String activeId) {
        User user = User.getInstance();
        String url = API2.CWBaseurl + "activity/" + activeId + "/join?" + "userId=" + user.getUserId() + "&token=" + user.getToken();
        DhNet net = new DhNet(url);
        net.doPostInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    join_desT.setText("邀请中");
                    invite.setResourseAndBg(R.drawable.dynamic_grey
                            , R.drawable.dynamic_grey
                    );
                    try {
                        jo.put("applyFlag", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
