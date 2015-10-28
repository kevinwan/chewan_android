/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gongpingjia.carplay.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.AttentionImageView;
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
import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.blurry.Blurry;

public class NearListAdapter extends RecyclerView.Adapter<NearListAdapter.SimpleViewHolder> {
    private static final int COUNT = 5;

    private final Context mContext;

    private List<JSONObject> data;

    private boolean uploadFlag = true;

    // 图片缓存根目录
    private File mCacheDir;

    OnItemClick onItemClick;


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView nickname, car_name, age, pay, transfer, location, distance, join_desT;
        ImageView headatt, car_logo, sex, active_bg;
        AttentionImageView attention;
        RelativeLayout sexLayout;
        AnimButtonView invite;
        Button upload, takephotos, album;

        public SimpleViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.tv_nickname);
            headatt = (ImageView) view.findViewById(R.id.head_att);
            car_logo = (ImageView) view.findViewById(R.id.iv_car_logo);
            car_name = (TextView) view.findViewById(R.id.tv_car_name);
            attention = (AttentionImageView) view.findViewById(R.id.attention);
            sexLayout = (RelativeLayout) view.findViewById(R.id.layout_sex_and_age);
            sex = (ImageView) view.findViewById(R.id.iv_sex);
            age = (TextView) view.findViewById(R.id.tv_age);
            pay = (TextView) view.findViewById(R.id.pay);
            transfer = (TextView) view.findViewById(R.id.transfer);
            active_bg = (ImageView) view.findViewById(R.id.active_bg);
            invite = (AnimButtonView) view.findViewById(R.id.invite);
            location = (TextView) view.findViewById(R.id.location);
            distance = (TextView) view.findViewById(R.id.tv_distance);
            upload = (Button) view.findViewById(R.id.upload);
            takephotos = (Button) view.findViewById(R.id.takephotos);
            album = (Button) view.findViewById(R.id.album);
            join_desT = (TextView) view.findViewById(R.id.join_des);
        }
    }


    public NearListAdapter(Context context) {
        mContext = context;
        mCacheDir = new File(mContext.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

    }


    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_nearby, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {

//        holder.title.setText(mItems.get(position).toString());
        final JSONObject jo = getItem(position);

        //用户信息,所在地,car信息,头像信息
        JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
        JSONObject distancejo = JSONUtil.getJSONObject(jo, "destination");
        JSONObject carjo = JSONUtil.getJSONObject(userjo, "car");
        JSONArray albumjsa = JSONUtil.getJSONArray(userjo, "album");
        //昵称,活动类型,年龄,性别,头像
        String activetype = JSONUtil.getString(jo, "type");
        holder.nickname.setText(JSONUtil.getString(userjo, "nickname") + "想约人" + activetype);
        holder.age.setText(JSONUtil.getInt(userjo, "age") + "");
        String sex = JSONUtil.getString(userjo, "gender");

        boolean applyFlag = JSONUtil.getBoolean(jo, "applyFlag");
        holder.join_desT.setText(applyFlag ? "邀请中" : "邀 TA");
        if (applyFlag) {
            holder.invite.setResourseAndBg(R.drawable.dynamic_grey
                    , R.drawable.dynamic_grey
            );
        } else {
            holder.invite.setResourseAndBg(R.drawable.red_circle, R.drawable.red_circle);
        }

        if ("男".equals(sex)) {
            holder.sexLayout.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sex.setImageResource(R.drawable.icon_man3x);
        } else {
            holder.sexLayout.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sex.setImageResource(R.drawable.icon_woman3x);
        }
//        ViewUtil.bindNetImage(holder.active_bg, JSONUtil.getString(userjo, "avatar"), "default");

//        Blurry.targetWidth = picWidth;
//        Blurry.targetHeight = picHeight;
        final User user = User.getInstance();
        if (user.isLogin()) {
            holder.upload.setVisibility(user.isHasAlbum() ? View.GONE : View.VISIBLE);
        } else {
            holder.upload.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(JSONUtil.getString(userjo, "avatar"), holder.active_bg, CarPlayValueFix.optionsDefault, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                Log.d("msg", "bitmap" + bitmap + "//////////position" + position);
                if (bitmap != null) {
                    final ImageView img = (ImageView) view;
                    if (!user.isHasAlbum()) {
                        img.setImageBitmap(bitmap);
                        Blurry.with(mContext)
                                .radius(10)
                                .sampling(4)
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
        String headatt = JSONUtil.getString(userjo, "photoAuthStatus");
        holder.headatt.setImageResource("认证通过".equals(headatt)? R.drawable.headaut_dl   :R.drawable.headaut_no );

        if (user.isLogin()) {
            holder.attention.setVisibility(JSONUtil.getString(userjo, "userId").equals(user.getUserId()) ? View.GONE : View.VISIBLE);
        } else {
            holder.attention.setVisibility(View.VISIBLE);
        }
        //关注,是否包接送,付费类型,活动类型
        holder.attention.setImageResource(JSONUtil.getBoolean(userjo, "subscribeFlag") ? R.drawable.icon_hearted : R.drawable.icon_heart);
        holder.attention.setOnClickListener(new MyOnClick(holder, position));
        boolean transfer = JSONUtil.getBoolean(jo, "transfer");
        String pay = JSONUtil.getString(jo, "pay");
        holder.pay.setText(pay);
        if (transfer) {
            holder.transfer.setVisibility(View.VISIBLE);
            holder.transfer.setText("包接送");
        } else {
            holder.transfer.setVisibility(View.GONE);
            holder.transfer.setText("不包接送");
        }

        //所在地,距离
        int distance = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
        holder.distance.setText(CarPlayUtil.numberWithDelimiter(distance));


        if (distancejo == null){
            holder.location.setVisibility(View.GONE);
        }else{
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText(JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street"));
        }

        String licenseAuthStatus = JSONUtil.getString(userjo,"licenseAuthStatus");

        //car logo ,car name
        if ("认证通过".equals(licenseAuthStatus)) {
            holder.car_logo.setVisibility(View.VISIBLE);
            holder.car_name.setVisibility(View.VISIBLE);
            ViewUtil.bindNetImage(holder.car_logo, JSONUtil.getString(carjo, "logo"), "head");
            ViewUtil.bindView(holder.car_name, JSONUtil.getString(carjo, "model"));
        } else {
            holder.car_logo.setVisibility(View.GONE);
            holder.car_name.setVisibility(View.GONE);

        }


        final View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClick != null) {
                    onItemClick.onItemClick(position, jo);
                }

            }
        });


//        holder.invite.startScaleAnimation();

        holder.invite.setOnClickListener(new MyOnClick(holder, position));

        holder.upload.setOnClickListener(new MyOnClick(holder, position));

        holder.takephotos.setOnClickListener(new MyOnClick(holder, position));

        holder.album.setOnClickListener(new MyOnClick(holder, position));
    }

    class MyOnClick implements View.OnClickListener {
        SimpleViewHolder holder;

        int position;

        public MyOnClick(SimpleViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            User user = User.getInstance();
            switch (v.getId()) {
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
                    Integer takephotos = Constant.TAKE_PHOTO;
                    EventBus.getDefault().post(takephotos);
                    break;
                //相册
                case R.id.album:
                    Integer album = Constant.PICK_PHOTO;
                    EventBus.getDefault().post(album);

                    break;

                case R.id.attention:
                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
                        @Override
                        public void onisLogin() {
                            JSONObject jo = getItem(position);
                            JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                            boolean type = JSONUtil.getBoolean(userjo, "subscribeFlag");
                            String userId = JSONUtil.getString(userjo, "userId");
                            attentionorCancle(type, userId, holder, jo);
                        }

                        @Override
                        public void onLoginFail() {

                        }
                    });
                    break;
                case R.id.invite:
                    UserInfoManage.getInstance().checkLogin((Activity) mContext, new UserInfoManage.LoginCallBack() {
                        @Override
                        public void onisLogin() {
                            JSONObject jo = getItem(position);
                            join(JSONUtil.getString(jo, "activityId"), holder, jo);
                        }

                        @Override
                        public void onLoginFail() {

                        }
                    });
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public JSONObject getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }


    private void attentionorCancle(final boolean attention, String userId, final SimpleViewHolder holder, final JSONObject jo) {

        User user = User.getInstance();

        if (userId.equals(user.getUserId())) {

            return;
        }
        String url;
        if (!attention) {
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
                    holder.attention.setImage(attention ? R.drawable.icon_heart : R.drawable.icon_hearted);
                    JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                    try {
                        userjo.put("subscribeFlag", !attention);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        });
    }


    private void join(String activeId, final SimpleViewHolder holder, final JSONObject jo) {
        User user = User.getInstance();
        String url = API2.CWBaseurl + "activity/" + activeId + "/join?" + "userId=" + user.getUserId() + "&token=" + user.getToken();
        DhNet net = new DhNet(url);
        net.doPostInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    holder.join_desT.setText("邀请中");
                    try {
                        jo.put("applyFlag", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public interface OnItemClick {
        void onItemClick(int position, JSONObject jo);
    }


    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
