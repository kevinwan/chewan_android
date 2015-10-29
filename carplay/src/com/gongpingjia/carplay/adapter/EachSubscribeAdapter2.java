package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.chat.VoiceCallActivity;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/17.
 */
public class EachSubscribeAdapter2 extends BaseAdapter {

    private JSONArray mDatum;
    private Context mContext;

    private SubscribeListener mSubscribeListener;

    public void setSubscribeListener(SubscribeListener listener) {
        mSubscribeListener = listener;
    }

    public EachSubscribeAdapter2(Context context, JSONArray data) {
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
            final JSONObject obj = mDatum.getJSONObject(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_each_subscribe2, parent, false);
                holder = new ViewHolder();
                holder.textDistance = (TextView) convertView.findViewById(R.id.tv_distance);
                holder.heartView = (ImageView) convertView.findViewById(R.id.iv_heart);
                holder.roundImageView = (RoundImageView) convertView.findViewById(R.id.iv_avatar);
                holder.textNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.textAge = (TextView) convertView.findViewById(R.id.tv_age);
                holder.imgMessage = (ImageView) convertView.findViewById(R.id.iv_message);
                holder.imgPhone = (ImageView) convertView.findViewById(R.id.iv_phone);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.roundImageView.setTag(obj.getString("userId"));
            holder.textNickname.setText(obj.getString("nickname"));
            holder.textDistance.setText(String.valueOf(obj.getInt("distance")) + "m");
            holder.textAge.setText(String.valueOf(obj.getInt("age")));
            holder.imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                        intent.putExtra("activityId", "");
                        intent.putExtra("userId", obj.getString("emchatName"));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.imgPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(mContext, VoiceCallActivity.class);
                        it.putExtra("username", obj.getString("emchatName"));
                        it.putExtra("isComingCall", false);
                        mContext.startActivity(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
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
        ImageView heartView;
        TextView textAge;
        ImageView imgPhone;
        ImageView imgMessage;
    }
}
