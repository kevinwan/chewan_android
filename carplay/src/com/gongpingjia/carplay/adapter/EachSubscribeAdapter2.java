package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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

import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/17.
 */
public class EachSubscribeAdapter2 extends BaseAdapter {

//    private JSONArray mDatum;
    private Context mContext;
    List<JSONObject> mDatum;
    private SubscribeListener mSubscribeListener;

    public EachSubscribeAdapter2(Context context) {
        mContext = context;
    }

    public void setSubscribeListener(SubscribeListener listener) {
        mSubscribeListener = listener;
    }

//    public EachSubscribeAdapter2(Context context, JSONArray data) {
//        this.mDatum = data;
//        mContext = context;
//    }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
         final JSONObject obj = (JSONObject) getItem(position);
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
            holder.roundImageView.setTag(JSONUtil.getString(obj, "userId"));
            holder.textNickname.setText(JSONUtil.getString(obj,"nickname"));
            holder.textDistance.setText(JSONUtil.getString(obj, "distance") + "m");
            holder.textAge.setText(JSONUtil.getString(obj, "age"));
            holder.imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                        intent.putExtra("activityId", "");
                        intent.putExtra("userId", JSONUtil.getString(obj,"emchatName"));
                        mContext.startActivity(intent);
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
            ImageLoader.getInstance().displayImage(JSONUtil.getString(obj,"avatar"), holder.roundImageView);
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
