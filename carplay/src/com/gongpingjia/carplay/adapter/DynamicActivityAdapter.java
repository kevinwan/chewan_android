package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.dialog.ActivityDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 */
public class DynamicActivityAdapter extends RecyclerView.Adapter<DynamicActivityAdapter.SimpleViewHolder> {
    private final Context mContext;
    private List<JSONObject> data;
    User user = User.getInstance();

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView titleT, dynamic_carname, pay_type, travelmode, activity_place, activity_distance, ageT;
        ImageView dynamic_carlogo, activity_beijing, certification_achievement, sexI;
        AnimButtonView dyanmic_one, dyanmic_two, yingyao, hulue;
        LinearLayout yingyao_layout, yingyaohou;
        private RelativeLayout sexbgR;

        public SimpleViewHolder(View view) {
            super(view);

            yingyao_layout = (LinearLayout) view.findViewById(R.id.yingyao_layout);
            yingyaohou = (LinearLayout) view.findViewById(R.id.yingyaohou);

            titleT = (TextView) view.findViewById(R.id.dynamic_title);
            dynamic_carname = (TextView) view.findViewById(R.id.dynamic_carname);
            pay_type = (TextView) view.findViewById(R.id.pay_type);
            travelmode = (TextView) view.findViewById(R.id.travelmode);
            activity_place = (TextView) view.findViewById(R.id.activity_place);

            sexbgR = (RelativeLayout) view.findViewById(R.id.layout_sex_and_age);
            sexI = (ImageView) view.findViewById(R.id.iv_sex);
            ageT = (TextView) view.findViewById(R.id.tv_age);


            dynamic_carlogo = (ImageView) view.findViewById(R.id.dynamic_carlogo);
            certification_achievement = (ImageView) view.findViewById(R.id.certification_achievement);
            activity_beijing = (ImageView) view.findViewById(R.id.activity_beijing);


            dyanmic_one = (AnimButtonView) view.findViewById(R.id.dyanmic_one);
            dyanmic_two = (AnimButtonView) view.findViewById(R.id.dyanmic_two);
            yingyao = (AnimButtonView) view.findViewById(R.id.yingyao);
            hulue = (AnimButtonView) view.findViewById(R.id.hulue);

            activity_distance = (TextView) view.findViewById(R.id.active_distance);
        }
    }


    public DynamicActivityAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_activelist, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        final JSONObject jo = getItem(position);


        JSONObject js = JSONUtil.getJSONObject(jo, "applicant");
        JSONObject json = JSONUtil.getJSONObject(jo, "destination");
        JSONObject ob = JSONUtil.getJSONObject(js, "car");
        //活动id
        String activityId = JSONUtil.getString(jo, "activityId");
        String status = JSONUtil.getString(jo,"status");
        if (status.equals("应邀")){
            holder.yingyao_layout.setVisibility(View.GONE);
            holder.yingyaohou.setVisibility(View.VISIBLE);
        }else if (status.equals("邀请中")){
            holder.yingyao_layout.setVisibility(View.VISIBLE);
            holder.yingyaohou.setVisibility(View.GONE);
        }
        final String appointmentId = JSONUtil.getString(jo, "appointmentId");

        String licenseAuthStatus = JSONUtil.getString(js, "licenseAuthStatus");
        String photoAuthStatus = JSONUtil.getString(js, "photoAuthStatus");
        String typeT = JSONUtil.getString(jo, "type");
        String name = JSONUtil.getString(js, "nickname");
        String gender = JSONUtil.getString(js, "gender");
        holder.activity_place.setText(JSONUtil.getString(json, "street"));

        double distance = JSONUtil.getDouble(js, "distance");
        DecimalFormat df = new DecimalFormat("0.00");
        holder.activity_distance.setText(df.format(distance));
        if (("男").equals(gender)) {
            holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
        }
        if (photoAuthStatus.equals("认证通过")) {
            holder.certification_achievement.setVisibility(View.VISIBLE);
        } else {
            holder.certification_achievement.setVisibility(View.GONE);
        }
        if (licenseAuthStatus.equals("认证通过")) {
            ViewUtil.bindNetImage(holder.dynamic_carlogo, JSONUtil.getString(ob, "logo"), "default");
            holder.dynamic_carname.setText(JSONUtil.getString(ob, "model"));
        } else {
            holder.dynamic_carlogo.setVisibility(View.GONE);
            holder.dynamic_carname.setVisibility(View.GONE);
        }
        holder.titleT.setText(name + "想邀请你" + typeT);

        holder.pay_type.setText(JSONUtil.getString(jo, "pay"));

        Boolean transfer = JSONUtil.getBoolean(jo, "transfer");
        if (transfer == true) {
            holder.travelmode.setText("包接送");
        } else {
            holder.travelmode.setText("");
        }
        ViewUtil.bindNetImage(holder.activity_beijing, JSONUtil.getString(js, "avatar"), "default");

        holder.yingyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jo = getItem(position);
                if (TextUtils.isEmpty(user.getPhone())) {
                    System.out.println("获取:"+user.getPhone());
                    ActivityDialog dialog = new ActivityDialog(mContext, appointmentId);
                    dialog.setOnPickResultListener(new ActivityDialog.OnPickResultListener() {

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
//                    DhNet net = new DhNet(API2.CWBaseurl+"/application/"+appointmentId+"/process?userId=" + user.getUserId() + "&token=" + user.getToken());
                    DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
                    net.addParam("accept", "true");
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
        });
        holder.hulue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jo = getItem(position);
//                DhNet net = new DhNet(API2.CWBaseurl+"application/"+appointmentId+"/process?userId="+user.getUserId()+"&token="+user.getToken());
                DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
                net.addParam("accept", "false");
                net.doPostInDialog(new NetTask(mContext) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            System.out.println("忽略：" + response.isSuccess());
                        }
                    }
                });
            }
        });
        holder.dyanmic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.dyanmic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public JSONObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }
}
