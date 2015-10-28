package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity2;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.chat.VoiceCallActivity;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.dialog.ActiveDialog;
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

import java.util.List;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by Administrator on 2015/10/20.
 */
public class MyDyanmicBaseAdapter extends BaseAdapter {
    private final Context mContext;
    String activityId;
    private List<JSONObject> data;

    User user = User.getInstance();

    final int TYPE_1 = 0;       //官方活动
    final int TYPE_2 = 1;

    public MyDyanmicBaseAdapter(Context context) {
        mContext = context;

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
    public int getItemViewType(int position) {
        String type = JSONUtil.getString((JSONObject) getItem(position), "activityCategory");
        if ("官方活动".equals(type)) {
            return TYPE_1;
        }
        return TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        JSONObject item = null;
        if (null != data) {
            item = data.get(i);
        }
        return item;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        ViewHolders holders = null;
        final JSONObject jo = (JSONObject) getItem(i);
        final JSONObject js = JSONUtil.getJSONObject(jo, "applicant");
        JSONObject json = JSONUtil.getJSONObject(jo, "destination");
        JSONObject ob = JSONUtil.getJSONObject(js, "car");
//        String type = JSONUtil.getString(jo, "activityCategory");

        int type = getItemViewType(i);

        if (view == null) {
//            if ("官方活动".equals(type)) {
            if (TYPE_1 == type) {
                holders = new ViewHolders();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mydyanmic_recommend, viewGroup, false);
                holders.people_num = (TextView) view.findViewById(R.id.people_num);
                holders.info = (TextView) view.findViewById(R.id.info);
                holders.price = (TextView) view.findViewById(R.id.price);
                holders.priceDesc = (TextView) view.findViewById(R.id.priceDesc);
                holders.location = (TextView) view.findViewById(R.id.location);
                holders.city = (TextView) view.findViewById(R.id.city);
                holders.pic = (ImageView) view.findViewById(R.id.pic);
                view.setTag(holders);
            } else {
                holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_activelist, viewGroup, false);

                holder.yingyaohou = (LinearLayout) view.findViewById(R.id.yingyaohou);
                holder.yingyao_layout = (LinearLayout) view.findViewById(R.id.yingyao_layout);
                holder.invitation = (LinearLayout) view.findViewById(R.id.invitation);
                holder.titleT = (TextView) view.findViewById(R.id.dynamic_title);
                holder.dynamic_carname = (TextView) view.findViewById(R.id.dynamic_carname);
                holder.pay_type = (TextView) view.findViewById(R.id.pay_type);
                holder.travelmode = (TextView) view.findViewById(R.id.travelmode);
                holder.activity_place = (TextView) view.findViewById(R.id.activity_place);
                holder.inviteT = (TextView) view.findViewById(R.id.inviteT);

                holder.sexbgR = (RelativeLayout) view.findViewById(R.id.layout_sex_and_age);
                holder.sexI = (ImageView) view.findViewById(R.id.iv_sex);
                holder.ageT = (TextView) view.findViewById(R.id.tv_age);


                holder.dynamic_carlogo = (ImageView) view.findViewById(R.id.dynamic_carlogo);
                holder.certification_achievement = (ImageView) view.findViewById(R.id.certification_achievement);
                holder.activity_beijing = (ImageView) view.findViewById(R.id.activity_beijing);


                holder.dyanmic_one = (AnimButtonView) view.findViewById(R.id.dyanmic_one);
                holder.dyanmic_two = (AnimButtonView) view.findViewById(R.id.dyanmic_two);
                holder.yingyao = (AnimButtonView) view.findViewById(R.id.yingyao);
                holder.hulue = (AnimButtonView) view.findViewById(R.id.hulue);
                holder.invitationI = (AnimButtonView) view.findViewById(R.id.invitationI);
                holder.dyanmic_one.startScaleAnimation();
                holder.dyanmic_two.startScaleAnimation();
                holder.yingyao.startScaleAnimation();
                holder.hulue.startScaleAnimation();
                holder.invitationI.startScaleAnimation();


                holder.activity_distance = (TextView) view.findViewById(R.id.active_distance);
                holder.invitationT = (TextView) view.findViewById(R.id.invitationT);

                view.setTag(holder);
            }


        } else {
//            if ("官方活动".equals(type)) {
            if (TYPE_1 == type) {
                holders = (ViewHolders) view.getTag();
            } else {
                holder = (ViewHolder) view.getTag();
            }


        }
//        if ("官方活动".equals(type)) {
        if (TYPE_1 == type) {
            String officialactivityId = JSONUtil.getString(jo, "officialActivityId");
            if (json == null) {
                holders.location.setVisibility(View.GONE);
                holders.city.setVisibility(View.GONE);
            } else {
                holders.location.setVisibility(View.VISIBLE);
                holders.city.setVisibility(View.VISIBLE);
                holders.city.setText(JSONUtil.getString(json, "[" + "city" + "]"));
                holders.location.setText(JSONUtil.getString(json, "detail"));

            }
            int people = JSONUtil.getInt(jo, "nowJoinNum");
            holders.people_num.setText("参与" + people + "人");
            holders.price.setText(JSONUtil.getString(jo, "price"));
            holders.priceDesc.setText(JSONUtil.getString(jo, "priceDesc"));
            holders.info.setText(JSONUtil.getString(jo, "title"));
            try {
                JSONArray coversJSa = jo.getJSONArray("covers");
                String picUrl = coversJSa.getString(0);

                ImageLoader.getInstance().displayImage(picUrl, holders.pic, CarPlayValueFix.optionsDefault, new ImageLoadingListener() {
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
                            if (!user.isHasAlbum() && !user.getUserId().equals(JSONUtil.getString(js, "userId"))) {
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jo = (JSONObject) getItem(i);
                    Intent it = new Intent(mContext, ActiveDetailsActivity2.class);
                    it.putExtra("activityId", JSONUtil.getString(jo, "officialActivityId"));
                    mContext.startActivity(it);
                }
            });

        } else {
            //活动id
            activityId = JSONUtil.getString(jo, "activityId");
            int status = JSONUtil.getInt(jo, "status");
            final String appointmentId = JSONUtil.getString(jo, "appointmentId");

            Boolean isApplicant = JSONUtil.getBoolean(jo, "isApplicant");

            String licenseAuthStatus = JSONUtil.getString(js, "licenseAuthStatus");
            String photoAuthStatus = JSONUtil.getString(js, "photoAuthStatus");
            if ("认证通过".equals(photoAuthStatus)) {
                holder.certification_achievement.setImageResource(R.drawable.headaut_dl);
            } else if ("认证中".equals(photoAuthStatus)) {
                holder.certification_achievement.setImageResource(R.drawable.headaut_no);
            } else if ("未认证".equals(photoAuthStatus)) {
                holder.certification_achievement.setImageResource(R.drawable.headaut_no);
            }
            String typeT = JSONUtil.getString(jo, "type");
            String name = JSONUtil.getString(js, "nickname");
            String gender = JSONUtil.getString(js, "gender");

//        String jied = JSONUtil.getString(json, "street");
            if (json == null) {
                holder.activity_place.setVisibility(View.GONE);
            } else {
                holder.activity_place.setVisibility(View.VISIBLE);
                holder.activity_place.setText(JSONUtil.getString(json, "province") + JSONUtil.getString(json, "city") + JSONUtil.getString(json, "district") + JSONUtil.getString(json, "street"));
            }
            String message = JSONUtil.getString(jo,"message");
            if (!message.isEmpty()){
                holder.inviteT.setVisibility(View.VISIBLE);
                holder.inviteT.setText(message);
            }else{
                holder.inviteT.setVisibility(View.GONE);
            }

            int distance = (int) Math.floor(JSONUtil.getDouble(js, "distance"));
//        DecimalFormat df = new DecimalFormat("0.00");
            holder.activity_distance.setText(CarPlayUtil.numberWithDelimiter(distance));
            if (("男").equals(gender)) {
                holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
            } else {
                holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
            }

            if ("认证通过".equals(licenseAuthStatus)) {
                ViewUtil.bindNetImage(holder.dynamic_carlogo, JSONUtil.getString(ob, "logo"), "default");
                holder.dynamic_carname.setText(JSONUtil.getString(ob, "model"));
            } else {
                holder.dynamic_carlogo.setVisibility(View.GONE);
                holder.dynamic_carname.setVisibility(View.GONE);
            }


            holder.pay_type.setText(JSONUtil.getString(jo, "pay"));

            Boolean transfer = JSONUtil.getBoolean(jo, "transfer");
            if (transfer == true) {
                holder.travelmode.setText("包接送");
            } else {
                holder.travelmode.setText("");
            }

            ImageLoader.getInstance().displayImage(JSONUtil.getString(js, "avatar"), holder.activity_beijing, CarPlayValueFix.optionsDefault, new ImageLoadingListener() {
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
                        if (!user.isHasAlbum() && !user.getUserId().equals(JSONUtil.getString(js, "userId"))) {
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

            if (status == 1) {
                if (isApplicant == true) {
//                System.out.println("我应邀别人。。。。。。。。。。。。");
                    holder.invitation.setVisibility(View.VISIBLE);
                    holder.yingyao_layout.setVisibility(View.GONE);
                    holder.yingyaohou.setVisibility(View.GONE);
                    holder.titleT.setText("你邀请" + name + "去" + typeT);
                    holder.invitationT.setText("邀请中");


                } else {
//                System.out.println("别人应邀我。。。。。。。。。。。。");
                    holder.yingyao_layout.setVisibility(View.VISIBLE);
                    holder.invitation.setVisibility(View.GONE);
                    holder.yingyaohou.setVisibility(View.GONE);
                    holder.titleT.setText(name + "想邀请你" + typeT);
                }

            } else if (status == 2) {
                if (isApplicant == true) {
                    holder.yingyao_layout.setVisibility(View.GONE);
                    holder.yingyaohou.setVisibility(View.VISIBLE);
                    holder.invitation.setVisibility(View.GONE);
                    holder.titleT.setText("你邀请" + name + "去" + typeT);
                    holder.invitationT.setText("邀请中");
                } else {
                    holder.yingyao_layout.setVisibility(View.GONE);
                    holder.invitation.setVisibility(View.GONE);
                    holder.yingyaohou.setVisibility(View.VISIBLE);
                    holder.titleT.setText(name + "想邀请你" + typeT);
                }

            }
            holder.yingyao.setOnClickListener(new MyOnClick(holder, jo));
            holder.hulue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=" + user.getUserId() + "&token=" + user.getToken());
//                DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb6d0cf224e7d878f695&token=a767ead8-7c00-4b90-b6de-9dcdb4d5bc41");
                    net.addParam("accept", false);
                    net.doPostInDialog(new NetTask(mContext) {
                        @Override
                        public void doInUI(Response response, Integer transfer) {
                            if (response.isSuccess()) {
                                data.remove(i);
                                notifyDataSetChanged();
                                System.out.println("忽略：" + response.isSuccess());
                            }
                        }
                    });
                }
            });
            holder.dyanmic_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("userId", JSONUtil.getString(js, "emchatName"));
                    mContext.startActivity(intent);

                }
            });
            holder.dyanmic_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent it = new Intent(mContext, VoiceCallActivity.class);
                        it.putExtra("username", js.getString("emchatName"));
                        it.putExtra("isComingCall", false);
                        mContext.startActivity(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PersonDetailActivity2.class);
                    String userId = JSONUtil.getString(js, "userId");
                    it.putExtra("userId", userId);
                    mContext.startActivity(it);
                }
            });

        }


        return view;
    }

    class MyOnClick implements View.OnClickListener {
        ViewHolder holder;
        JSONObject jo;

        public MyOnClick(ViewHolder holder, JSONObject jo) {
            this.holder = holder;
            this.jo = jo;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.yingyao: {
                    String appID = JSONUtil.getString(jo, "appointmentId");
                    if (TextUtils.isEmpty(user.getPhone())) {
//                    System.out.println("获取:" + user.getPhone());
                        ActiveDialog dialog = new ActiveDialog(mContext, appID);
                        dialog.setOnPickResultListener(new ActiveDialog.OnPickResultListener() {

                            @Override
                            public void onResult(int result) {
                                if (result == 1) {
                                    holder.yingyao_layout.setVisibility(View.GONE);
                                    holder.yingyaohou.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        dialog.show();
                    } else {
                        DhNet net = new DhNet(API2.CWBaseurl + "/application/" + appID + "/process?userId=" + user.getUserId() + "&token=" + user.getToken());
//                    DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb6d0cf224e7d878f695&token=a767ead8-7c00-4b90-b6de-9dcdb4d5bc41");
                        net.addParam("accept", true);
                        net.doPostInDialog(new NetTask(mContext) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {
                                if (response.isSuccess()) {
                                    holder.yingyao_layout.setVisibility(View.GONE);
                                    holder.yingyaohou.setVisibility(View.VISIBLE);
                                    System.out.println("应邀：" + response.isSuccess());
                                }
                            }
                        });
                    }
                }
                break;
            }
        }
    }


    class ViewHolder {
        TextView titleT, dynamic_carname, pay_type, travelmode, activity_place, activity_distance, ageT, invitationT,inviteT;
        ImageView dynamic_carlogo, activity_beijing, certification_achievement, sexI;
        AnimButtonView dyanmic_one, dyanmic_two, yingyao, hulue, invitationI;
        LinearLayout yingyao_layout, yingyaohou, invitation;
        private RelativeLayout sexbgR;
    }

    class ViewHolders {
        TextView people_num, info, price, priceDesc, location, city;
        ImageView pic;
    }
}
