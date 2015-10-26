package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.dialog.ActiveDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Administrator on 2015/10/20.
 */
public class MyDyanmicBaseAdapter extends BaseAdapter {
    private final Context mContext;

    private List<JSONObject> data;

    User user = User.getInstance();

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
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
            holder.dyanmic_one.startScaleAnimation();
            holder.dyanmic_two.startScaleAnimation();
            holder.yingyao.startScaleAnimation();
            holder.hulue.startScaleAnimation();




            holder.activity_distance = (TextView) view.findViewById(R.id.active_distance);


//
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();

        }

        JSONObject jo = (JSONObject) getItem(i);
        JSONObject js = JSONUtil.getJSONObject(jo, "applicant");
        JSONObject json = JSONUtil.getJSONObject(jo, "destination");
        JSONObject ob = JSONUtil.getJSONObject(js, "car");
        //活动id
        String activityId = JSONUtil.getString(jo, "activityId");
        int status = JSONUtil.getInt(jo, "status");
        final String appointmentId = JSONUtil.getString(jo, "appointmentId");

//        View[] views = {holder.dyanmic_one, holder.dyanmic_two, holder.yingyao_layout, holder.yingyaohou};
//        View[] viewstwo = {holder.hulue, holder.yingyao, holder.yingyao_layout, holder.yingyaohou};
//
//        CarPlayUtil.bindActiveButton("邀请中", appointmentId, mContext, views);
//        CarPlayUtil.bindActiveButton("应邀", appointmentId, mContext, viewstwo);
//                CarPlayUtil.bindActiveButton("拒绝".views);

//        CarPlayUtil.bindActiveButton2("邀请中", appointmentId, mContext,  holder.yingyao_layout, holder.yingyaohou);
        Boolean isApplicant = JSONUtil.getBoolean(jo,"isApplicant");

        if (status == 2) {
            if (isApplicant == true){
                holder.invitation.setVisibility(View.VISIBLE);
                holder.yingyao_layout.setVisibility(View.GONE);
                holder.yingyaohou.setVisibility(View.GONE);
            }else{
                holder.yingyao_layout.setVisibility(View.GONE);
                holder.yingyaohou.setVisibility(View.VISIBLE);
            }
        } else if (status== 1) {
            holder.yingyao_layout.setVisibility(View.VISIBLE);
            holder.yingyaohou.setVisibility(View.GONE);
        }


        String licenseAuthStatus = JSONUtil.getString(js, "licenseAuthStatus");
        String photoAuthStatus = JSONUtil.getString(js, "photoAuthStatus");
        if (photoAuthStatus.equals("认证通过")) {
            holder.certification_achievement.setImageResource(R.drawable.headaut_dl);
        }else if(photoAuthStatus.equals("未认证")){
            holder.certification_achievement.setImageResource(R.drawable.headaut_no);
        }
        String typeT = JSONUtil.getString(jo, "type");
        String name = JSONUtil.getString(js, "nickname");
        String gender = JSONUtil.getString(js, "gender");

//        String jied = JSONUtil.getString(json, "street");
        if(json ==null){
            holder.activity_place.setVisibility(View.GONE);
        }else{
            holder.activity_place.setVisibility(View.VISIBLE);
            holder.activity_place.setText(JSONUtil.getString(json, "province") + JSONUtil.getString(json, "city") + JSONUtil.getString(json, "district") + JSONUtil.getString(json, "street"));
        }


        int distance = (int)Math.floor(JSONUtil.getDouble(js, "distance"));
//        DecimalFormat df = new DecimalFormat("0.00");
        holder.activity_distance.setText(CarPlayUtil.numberWithDelimiter(distance));
        if (("男").equals(gender)) {
            holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
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
        ViewUtil.bindNetImage(holder.activity_beijing, JSONUtil.getString(js, "avatar"), "back");

        holder.yingyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    JSONObject jo = getItem(i);
                if (TextUtils.isEmpty(user.getPhone())) {
                    System.out.println("获取:" + user.getPhone());
                    ActiveDialog dialog = new ActiveDialog(mContext, appointmentId);
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
                    DhNet net = new DhNet(API2.CWBaseurl+"/application/"+appointmentId+"/process?userId=" + user.getUserId() + "&token=" + user.getToken());
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
        });
        holder.hulue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    JSONObject jo = getItem(position);
                DhNet net = new DhNet(API2.CWBaseurl+"application/"+appointmentId+"/process?userId="+user.getUserId()+"&token="+user.getToken());
//                DhNet net = new DhNet(API2.CWBaseurl + "application/" + appointmentId + "/process?userId=5609eb6d0cf224e7d878f695&token=a767ead8-7c00-4b90-b6de-9dcdb4d5bc41");
                net.addParam("accept", false);
                net.doPostInDialog(new NetTask(mContext) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
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

            }
        });
        holder.dyanmic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    class ViewHolder {
        TextView titleT, dynamic_carname, pay_type, travelmode, activity_place, activity_distance, ageT;
        ImageView dynamic_carlogo, activity_beijing, certification_achievement, sexI;
        AnimButtonView dyanmic_one, dyanmic_two, yingyao, hulue;
        LinearLayout yingyao_layout, yingyaohou,invitation;
        private RelativeLayout sexbgR;
    }
}
