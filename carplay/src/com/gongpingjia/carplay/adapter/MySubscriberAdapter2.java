package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.HeartView;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 * 我关注的人 list适配器
 */
public class MySubscriberAdapter2 extends BaseAdapter {

    //    private JSONArray mDatum;
    private Context mContext;
    List<JSONObject> mDatum;

    //    public MySubscriberAdapter2(Context context, JSONArray data) {
//        this.mDatum = data;
//        mContext = context;
//    }
    public MySubscriberAdapter2(Context context) {
        mContext = context;
    }

    private SubscribeListener mSubscribeListener;

    public void setSubscribeListener(SubscribeListener listener) {
        mSubscribeListener = listener;
    }

    public void setData(List<JSONObject> mDatum) {
        this.mDatum = mDatum;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mDatum == null ? 0 : mDatum.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final JSONObject obj = (JSONObject) getItem(position);
        JSONObject car = JSONUtil.getJSONObject(obj, "car");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_subscriber, parent, false);
            holder = new ViewHolder();
            holder.textDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.heartView = (HeartView) convertView.findViewById(R.id.iv_heart);
            holder.roundImageView = (RoundImageView) convertView.findViewById(R.id.iv_avatar);
            holder.textNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.textAge = (TextView) convertView.findViewById(R.id.tv_age);
            holder.sexbgR = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.sexI = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.icon = (ImageView) convertView.findViewById(R.id.dynamic_carlogo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.roundImageView.setTag(JSONUtil.getString(obj, "userId"));
        holder.textNickname.setText(JSONUtil.getString(obj, "nickname"));
//            holder.textDistance.setText(JSONUtil.getString(obj, "distance") + "m");
        int distance = (int) Math.floor(JSONUtil.getDouble(obj, "distance"));
        holder.textDistance.setText(CarPlayUtil.numberWithDelimiter(distance));
        String sex = JSONUtil.getString(obj, "gender");
        if (("男").equals(sex)) {
            holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
        }
        String licenseAuthStatus  = JSONUtil.getString(obj,"licenseAuthStatus");
        //车主认证
        if ("认证通过".equals(licenseAuthStatus)) {
            holder.icon.setVisibility(View.VISIBLE);
            ViewUtil.bindNetImage(holder.icon, JSONUtil.getString(car, "logo"), "default");
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        holder.textAge.setText(JSONUtil.getString(obj, "age"));
        holder.heartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscribeListener != null) {
                    try {
                        mSubscribeListener.onSubscribe(obj.getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ImageLoader.getInstance().displayImage(JSONUtil.getString(obj, "avatar"), holder.roundImageView);
        return convertView;
    }

    class ViewHolder {
        RoundImageView roundImageView;
        TextView textNickname;
        TextView textDistance;
        HeartView heartView;
        TextView textAge;
        private RelativeLayout sexbgR;
        ImageView sexI;
        ImageView icon;
    }
}
