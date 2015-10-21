package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.CarPlayUtil;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20.
 */
public class InterestedPersonAdapter extends BaseAdapter {
    private static final int COUNT = 5;

    private final Context mContext;

    private List<JSONObject> data;

    public InterestedPersonAdapter(Context context) {
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
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data)
        {
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
        ViewHolder holder=null;
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activelist, null);
            holder.titleT = (TextView) convertView.findViewById(R.id.dynamic_title);
            holder.carNameT = (TextView) convertView.findViewById(R.id.dynamic_carname);
            holder.ageT = (TextView) convertView.findViewById(R.id.tv_age);
            holder.payT = (TextView) convertView.findViewById(R.id.pay_type);
            holder.transferT = (TextView) convertView.findViewById(R.id.travelmode);
            holder.locationT = (TextView) convertView.findViewById(R.id.activity_place);
            holder.DistanceT = (TextView) convertView.findViewById(R.id.activity_distance);
            holder.headStateI = (ImageView) convertView.findViewById(R.id.certification_achievement);
            holder.carStateI = (ImageView) convertView.findViewById(R.id.dynamic_carlogo);
            holder.sexI = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.headbgI = (ImageView) convertView.findViewById(R.id.activity_beijing);
            holder.sexLayoutR = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.invitationL = (LinearLayout) convertView.findViewById(R.id.invitation);


            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);

        //用户信息,所在地,car信息,头像信息
        JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
        JSONObject distancejo = JSONUtil.getJSONObject(jo, "destination");
        JSONObject carjo = JSONUtil.getJSONObject(jo, "car");
//        JSONArray albumjsa = JSONUtil.getJSONArray(userjo, "album");

        //题头,性别,年龄,头像
        ViewUtil.bindView(holder.titleT, JSONUtil.getString(userjo, "nickname") + "想邀请你" + JSONUtil.getString(jo, "type"));
        holder.ageT.setText(JSONUtil.getInt(userjo, "age") + "");
        String sex = JSONUtil.getString(userjo, "gender");
        if ("男".equals(sex)) {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setImageResource(R.drawable.icon_man3x);
        } else {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setImageResource(R.drawable.icon_woman3x);
        }
        ViewUtil.bindNetImage(holder.headbgI,JSONUtil.getString(userjo, "avatar"),"default");

        //头像认证状态,车主认证状态
        String photoAuthStatus=JSONUtil.getString(userjo,"photoAuthStatus");
        String licenseAuthStatus=JSONUtil.getString(userjo,"licenseAuthStatus");

        //付费类型,是否包接送
        ViewUtil.bindView(holder.payT, JSONUtil.getString(jo, "pay"));
        boolean transfer =  JSONUtil.getBoolean(jo,"transfer");

        //所在地,距离
        int distance = (int)Math.floor(JSONUtil.getDouble(jo, "distance"));
        holder.DistanceT.setText(CarPlayUtil.numberWithDelimiter(distance));
        holder.locationT.setText(JSONUtil.getString(distancejo, "province") + JSONUtil.getString(distancejo, "city") + JSONUtil.getString(distancejo, "district") + JSONUtil.getString(distancejo, "street"));



        return convertView;
    }

    class ViewHolder{
        TextView titleT,carNameT,ageT,payT,transferT,locationT,DistanceT;

        ImageView headStateI,carStateI,sexI,headbgI;

        RelativeLayout sexLayoutR;

        LinearLayout invitationL;

    }
}
