package com.gongpingjia.carplay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Administrator on 2015/10/20.
 */
public class InterestedPersonAdapter extends BaseAdapter {
    private static final int COUNT = 5;
    private final Context mContext;
    String activityId;
    private List<JSONObject> data;

    User user = User.getInstance();
    JSONObject distancejo;
    String name, pay, activityType;
    boolean activityTransfer;

    private boolean uploadFlag = true;

    public String mPhotoPath;

    File mCacheDir;

    public InterestedPersonAdapter(Context context) {
        mContext = context;
        mCacheDir = new File(mContext.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data) {
            item = data.get(position);
        }
        return item;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activelist, null);
            holder.titleT = (TextView) convertView.findViewById(R.id.dynamic_title);
            holder.invitationT = (TextView) convertView.findViewById(R.id.invitationT);
            holder.carNameT = (TextView) convertView.findViewById(R.id.dynamic_carname);
            holder.ageT = (TextView) convertView.findViewById(R.id.tv_age);
            holder.payT = (TextView) convertView.findViewById(R.id.pay_type);
            holder.transferT = (TextView) convertView.findViewById(R.id.travelmode);
            holder.locationT = (TextView) convertView.findViewById(R.id.activity_place);
            holder.activeDistanceT = (TextView) convertView.findViewById(R.id.active_distance);
            holder.headStateI = (ImageView) convertView.findViewById(R.id.certification_achievement);
            holder.carStateI = (ImageView) convertView.findViewById(R.id.dynamic_carlogo);
            holder.sexI = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.headbgI = (ImageView) convertView.findViewById(R.id.activity_beijing);
            holder.sexLayoutR = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.invitationL = (LinearLayout) convertView.findViewById(R.id.invitation);
            holder.photoDistanceT = (TextView) convertView.findViewById(R.id.photo_distance);
            holder.photoDistancelayoutL = (LinearLayout) convertView.findViewById(R.id.photo_distancelayout);
            holder.activeDistancelayoutl = (LinearLayout) convertView.findViewById(R.id.active_distancelayout);
            holder.invitationI = (AnimButtonView) convertView.findViewById(R.id.invitationI);

            holder.upload = (Button) convertView.findViewById(R.id.upload);
            holder.takephotos = (Button) convertView.findViewById(R.id.takephotos);
            holder.album = (Button) convertView.findViewById(R.id.album);
            holder.phtotoV = (LinearLayout)convertView.findViewById(R.id.phtoto);
            holder.promtpT = (TextView) convertView.findViewById(R.id.promtp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);

        //用户信息,所在地,car信息,头像信息
        final JSONObject userjo = JSONUtil.getJSONObject(jo, "user");
        distancejo = JSONUtil.getJSONObject(jo, "activityDestination");
        JSONObject carjo = JSONUtil.getJSONObject(userjo, "car");
//        JSONArray albumjsa = JSONUtil.getJSONArray(userjo, "album");
        name = JSONUtil.getString(userjo, "nickname");
        pay = JSONUtil.getString(jo, "activityPay");
        activityType = JSONUtil.getString(jo, "activityType");
        activityTransfer = JSONUtil.getBoolean(jo, "activityTransfer");

        //,性别,年龄,头像
        holder.ageT.setText(JSONUtil.getInt(userjo, "age") + "");
        final String sex = JSONUtil.getString(userjo, "gender");
        if ("男".equals(sex)) {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setImageResource(R.drawable.icon_man3x);
        } else {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setImageResource(R.drawable.icon_woman3x);
        }
        holder.invitationI.clearAnimation();
        holder.invitationI.startScaleAnimation();
        ImageLoader.getInstance().displayImage(JSONUtil.getString(userjo, "cover"), holder.headbgI, CarPlayValueFix.optionsDefault, new ImageLoadingListener() {
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
                    if (!user.isHasAlbum() && !user.getUserId().equals(JSONUtil.getString(userjo, "userId"))) {
                        img.setImageBitmap(bitmap);
                        Blurry.with(mContext)
                                .radius(10)
                                .sampling(Constant.BLUR_VALUE)
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
        //头像认证状态,车主认证状态
        String photoAuthStatus = JSONUtil.getString(userjo, "photoAuthStatus");
//        holder.headStateI.setImageResource("未认证".equals(photoAuthStatus) ? R.drawable.headaut_no : R.drawable.headaut_dl);
        holder.headStateI.setImageResource("认证通过".equals(photoAuthStatus) ? R.drawable.headaut_dl : R.drawable.headaut_no);
        String licenseAuthStatus = JSONUtil.getString(userjo, "licenseAuthStatus");
        if ("认证通过".equals(licenseAuthStatus)) {
            ViewUtil.bindNetImage(holder.carStateI, JSONUtil.getString(carjo, "logo"), "default");
        } else {
            holder.carStateI.setImageResource(R.drawable.no_car);
        }

        final User user = User.getInstance();
        if (user.isLogin()) {
            holder.phtotoV.setVisibility(user.isHasAlbum() ? View.GONE : View.VISIBLE);
            holder.promtpT.setVisibility(user.isHasAlbum() ? View.GONE : View.VISIBLE);
        }

        holder.upload.setOnClickListener(new MyOnClick(holder, position));

        holder.takephotos.setOnClickListener(new MyOnClick(holder, position));

        holder.album.setOnClickListener(new MyOnClick(holder, position));

        // 0为活动信息   1位上传相册信息
        int type = JSONUtil.getInt(jo, "type");
        if (type == 0) {
            holder.payT.setVisibility(View.VISIBLE);
            holder.transferT.setVisibility(View.VISIBLE);
            holder.activeDistancelayoutl.setVisibility(View.VISIBLE);
            holder.photoDistancelayoutL.setVisibility(View.GONE);
            holder.invitationL.setVisibility(View.VISIBLE);
            activityId = JSONUtil.getString(jo, "relatedId");
            int activityStatus = JSONUtil.getInt(jo, "activityStatus");
            //题头
            ViewUtil.bindView(holder.titleT, JSONUtil.getString(userjo, "nickname") + "想找人" + JSONUtil.getString(jo, "activityType"));
            //所在地,距离
            int distance = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
            holder.activeDistanceT.setText(CarPlayUtil.numberWithDelimiter(distance));
            String locationS = JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street") + JSONUtil.getString(distancejo, "detail");
            locationS=locationS.replace("null","");
            String district = JSONUtil.getString(distancejo,"district");
            String street = JSONUtil.getString(distancejo,"street");
            if (TextUtils.isEmpty(locationS)) {
                holder.locationT.setText("地点待定");
            } else {
                if (district.equals(street)){
                    holder.locationT.setText(JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district"));
                }else{
                    holder.locationT.setText(JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district")+ JSONUtil.getString(distancejo, "street"));
                }
            }

            //付费类型,是否包接送
            ViewUtil.bindView(holder.payT, JSONUtil.getString(jo, "activityPay"));
            boolean transfer = JSONUtil.getBoolean(jo, "transfer");
            if (transfer) {
                holder.transferT.setVisibility(View.VISIBLE);
                holder.transferT.setText("包接送");
            } else {
                holder.transferT.setVisibility(View.GONE);
                holder.transferT.setText("不包接送");
            }
            holder.invitationL.setVisibility(View.VISIBLE);
            holder.invitationI.setOnClickListener(new MyOnClick(holder, position));
            if (activityStatus == 0) {
                holder.invitationT.setText("邀 TA");
                holder.invitationI.setEnabled(true);
                holder.invitationI.setResourseAndBg(R.drawable.red_circle, R.drawable.red_circle);
            } else {
                holder.invitationT.setText("邀请中");
                holder.invitationI.setResourseAndBg(R.drawable.dynamic_grey
                        , R.drawable.dynamic_grey);
                holder.invitationI.setEnabled(false);
            }

        } else {
            holder.payT.setVisibility(View.GONE);
            holder.transferT.setVisibility(View.GONE);
            holder.activeDistancelayoutl.setVisibility(View.GONE);
            holder.photoDistancelayoutL.setVisibility(View.VISIBLE);
            holder.invitationL.setVisibility(View.GONE);

            //题头
            ViewUtil.bindView(holder.titleT, JSONUtil.getString(userjo, "nickname") + "上传了" + JSONUtil.getInt(jo, "photoCount") + "张照片");
            //距离
            int distance = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
            holder.photoDistanceT.setText(CarPlayUtil.numberWithDelimiter(distance));
        }

        return convertView;
    }

    class MyOnClick implements View.OnClickListener {
        ViewHolder holder;

        int position;

        public MyOnClick(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.invitationI:
                    JSONObject jo = getItem(position);
                    join(JSONUtil.getString(jo, "relatedId"), holder, jo);
                    break;
                //上传
                case R.id.upload:
                            if (uploadFlag) {
                                uploadFlag = !uploadFlag;
                                holder.takephotos.setVisibility(View.VISIBLE);
                                holder.album.setVisibility(View.VISIBLE);
                            } else {
                                uploadFlag = !uploadFlag;
                                holder.takephotos.setVisibility(View.GONE);
                                holder.album.setVisibility(View.GONE);
                            }

                    break;
                //拍照
                case R.id.takephotos:
                    mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    Intent getImageByCamera = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(mPhotoPath)));
                    getImageByCamera.putExtra("mPhotoPath",mPhotoPath);
                    ((Activity)mContext).startActivityForResult(getImageByCamera,
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
                    ((Activity)mContext).startActivityForResult(intent, Constant.PICK_PHOTO);
                    break;

            }
        }
    }

    private void join(String activeId, final ViewHolder holder, final JSONObject jo) {
        User user = User.getInstance();
        String url = API2.CWBaseurl + "activity/" + activeId + "/join?" + "userId=" + user.getUserId() + "&token=" + user.getToken();
        DhNet net = new DhNet(url);
        net.addParam("type", JSONUtil.getString(jo,"activityType"));
        net.addParam("pay", JSONUtil.getString(jo,"activityPay"));
        net.addParam("transfer", JSONUtil.getString(jo,"activityTransfer"));
//        net.addParam("destPoint",destPoint);
        net.addParam("destination", JSONUtil.getJSONObject(jo, "activityDestination"));
        net.doPostInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    holder.invitationT.setText("邀请中");
                    System.out.println("邀Ta" + response.isSuccess());
                    holder.invitationI.setResourseAndBg(R.drawable.dynamic_grey, R.drawable.dynamic_grey);
                    try {
                        jo.put("activityStatus", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    class ViewHolder {
        TextView titleT, carNameT, ageT, payT, transferT, locationT, activeDistanceT, photoDistanceT, invitationT,promtpT;

        ImageView headStateI, carStateI, sexI, headbgI;

        RelativeLayout sexLayoutR;
        Button upload,takephotos,album;

        AnimButtonView invitationI;
        LinearLayout invitationL, activeDistancelayoutl, photoDistancelayoutL,phtotoV;

    }


}
