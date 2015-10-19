package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.HeartView;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/17.
 */
public class BeSubscribedAdapter2 extends BaseAdapter {

    private JSONArray mDatum;
    private Context mContext;

    public BeSubscribedAdapter2(Context context, JSONArray data) {
        this.mDatum = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatum == null ? 0 : mDatum.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mDatum.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            JSONObject obj = mDatum.getJSONObject(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_be_subscribed2, parent, false);
                holder = new ViewHolder();
                holder.textDistance = (TextView) convertView.findViewById(R.id.tv_distance);
                holder.heartView = (HeartView) convertView.findViewById(R.id.iv_heart);
                holder.roundImageView = (RoundImageView) convertView.findViewById(R.id.iv_avatar);
                holder.textNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.textAge = (TextView) convertView.findViewById(R.id.tv_age);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textNickname.setText(obj.getString("nickname"));
            holder.textDistance.setText(String.valueOf(obj.getInt("distance")) + "m");
            holder.textAge.setText(String.valueOf(obj.getInt("age")));
            holder.heartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImageLoader.getInstance().displayImage(obj.getString("avatar"), holder.roundImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        RoundImageView roundImageView;
        TextView textNickname;
        TextView textDistance;
        HeartView heartView;
        TextView textAge;
    }

}
