package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
public class HisDyanmicBaseAdapter extends BaseAdapter {
    private final Context mContext;
    String activityId;
    private List<JSONObject> data;
    //    User user = User.getInstance();
    Bundle bundle;
    String cover;
    Double distance;

    public HisDyanmicBaseAdapter(Context context, Bundle bundle, String cover, Double distance) {
        mContext = context;
        this.bundle = bundle;
        this.cover = cover;
        this.distance = distance;
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
        JSONObject jo = (JSONObject) getItem(i);


        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_activelist, viewGroup, false);
            holder.yingyaohou = (LinearLayout) view.findViewById(R.id.yingyaohou);
            holder.invitation = (LinearLayout) view.findViewById(R.id.invitation);
            holder.titleT = (TextView) view.findViewById(R.id.dynamic_title);
            holder.dynamic_carname = (TextView) view.findViewById(R.id.dynamic_carname);
            holder.pay_type = (TextView) view.findViewById(R.id.pay_type);
            holder.travelmode = (TextView) view.findViewById(R.id.travelmode);
            holder.activity_place = (TextView) view.findViewById(R.id.activity_place);
//                holder.inviteT = (TextView) view.findViewById(R.id.inviteT);

            holder.sexbgR = (RelativeLayout) view.findViewById(R.id.layout_sex_and_age);
            holder.sexI = (ImageView) view.findViewById(R.id.iv_sex);
            holder.ageT = (TextView) view.findViewById(R.id.tv_age);


            holder.dynamic_carlogo = (ImageView) view.findViewById(R.id.dynamic_carlogo);
            holder.certification_achievement = (ImageView) view.findViewById(R.id.certification_achievement);
            holder.activity_beijing = (ImageView) view.findViewById(R.id.activity_beijing);
            holder.invitationI = (AnimButtonView) view.findViewById(R.id.invitationI);
            holder.dyanmic_one = (AnimButtonView) view.findViewById(R.id.dyanmic_one);
            holder.dyanmic_two = (AnimButtonView) view.findViewById(R.id.dyanmic_two);
            holder.invitationI.startScaleAnimation();
            holder.dyanmic_one.startScaleAnimation();
            holder.dyanmic_two.startScaleAnimation();

            holder.activity_distance = (TextView) view.findViewById(R.id.active_distance);
            holder.invitationT = (TextView) view.findViewById(R.id.invitationT);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }

        String pay = JSONUtil.getString(jo, "pay");
        activityId = JSONUtil.getString(jo, "activityId");
        String type = JSONUtil.getString(jo, "type");
        Boolean transfer = JSONUtil.getBoolean(jo, "transfer");
        int status = JSONUtil.getInt(jo, "status");
        holder.titleT.setText(bundle.getString("name") + "想约人" + type);
        holder.pay_type.setText(pay);
        ViewUtil.bindNetImage(holder.activity_beijing, cover, "default");
        if (status == 0) {
            holder.invitation.setVisibility(View.VISIBLE);
            holder.yingyaohou.setVisibility(View.GONE);
            holder.invitationT.setText("邀 TA");
            holder.invitationI.setResourseAndBg(R.drawable.red_circle
                    , R.drawable.red_circle);

        } else if (status == 1) {
            holder.invitation.setVisibility(View.VISIBLE);
            holder.yingyaohou.setVisibility(View.GONE);
            holder.invitationT.setText("邀请中");
            holder.invitationI.setResourseAndBg(R.drawable.dynamic_grey
                    , R.drawable.dynamic_grey
            );

        } else if (status == 2) {
            holder.invitation.setVisibility(View.GONE);
            holder.yingyaohou.setVisibility(View.VISIBLE);

        }

        if (transfer == true) {
            holder.travelmode.setText("包接送");
        } else {
            holder.travelmode.setText("");
        }
        holder.ageT.setText(bundle.getString("age"));
        String gender = bundle.getString("gender");
        if (("男").equals(gender)) {
            holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
        }
        String photoAuthStatus = bundle.getString("photoAuthStatus");
        if ("认证通过".equals(photoAuthStatus)) {
            holder.certification_achievement.setImageResource(R.drawable.headaut_dl);
        } else if ("认证中".equals(photoAuthStatus)) {
            holder.certification_achievement.setImageResource(R.drawable.headaut_no);
        } else if ("未认证".equals(photoAuthStatus)) {
            holder.certification_achievement.setImageResource(R.drawable.headaut_no);
        }
        String licenseAuthStatus = bundle.getString("licenseAuthStatus");
        if ("认证通过".equals(licenseAuthStatus)) {
            ViewUtil.bindNetImage(holder.dynamic_carlogo, bundle.getString("logo"), "default");
            holder.dynamic_carname.setText(bundle.getString("brand"));
        } else {
            holder.dynamic_carlogo.setVisibility(View.GONE);
            holder.dynamic_carname.setVisibility(View.GONE);
        }
        int distances = (int) Math.floor(distance);
        holder.activity_distance.setText(distances + "");
        System.out.println(distance);
        JSONObject json = JSONUtil.getJSONObject(jo, "destination");
        if (json == null) {
            holder.activity_place.setVisibility(View.GONE);
        } else {
            holder.activity_place.setVisibility(View.VISIBLE);
            holder.activity_place.setText(JSONUtil.getString(json, "province") + JSONUtil.getString(json, "city") + JSONUtil.getString(json, "district") + JSONUtil.getString(json, "street"));
        }
        holder.invitationI.setOnClickListener(new MyOnClick(holder, i));
        holder.dyanmic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                intent.putExtra("activityId", activityId);
                intent.putExtra("userId", bundle.getString("emchatName"));
                mContext.startActivity(intent);

            }
        });
        holder.dyanmic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, VoiceCallActivity.class);
                it.putExtra("username", bundle.getString("emchatName"));
                it.putExtra("isComingCall", false);
                mContext.startActivity(it);
            }
        });

        return view;
    }

    class MyOnClick implements View.OnClickListener {
        ViewHolder holder;

        int i;

        public MyOnClick(ViewHolder holder, int position) {
            this.holder = holder;
            this.i = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.invitationI:
                    JSONObject jo = (JSONObject) getItem(i);
                    join(activityId, holder, jo);
                    break;
            }
        }
    }

    private void join(String activeId, final ViewHolder holder, final JSONObject jo) {
        User user = User.getInstance();
        String url = API2.CWBaseurl + "activity/" + activeId + "/join?" + "userId=" + user.getUserId() + "&token=" + user.getToken();
        DhNet net = new DhNet(url);
        net.doPostInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    holder.invitationT.setText("邀请中");
                    System.out.println("邀Ta" + response.isSuccess());
                    try {
                        jo.put("status", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    class ViewHolder {
        TextView titleT, dynamic_carname, pay_type, travelmode, activity_place, activity_distance, ageT, invitationT;
        ImageView dynamic_carlogo, activity_beijing, certification_achievement, sexI;
        AnimButtonView dyanmic_one, dyanmic_two, invitationI;
        LinearLayout yingyaohou, invitation;
        RelativeLayout sexbgR;
    }

}
