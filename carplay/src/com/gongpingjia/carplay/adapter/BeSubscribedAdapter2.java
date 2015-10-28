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

import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/17.
 */
public class BeSubscribedAdapter2 extends BaseAdapter {

    private Context mContext;

    private SubscribeListener mSubscribeListener;

    List<JSONObject> mDatum;

    int type = 0;

    public void setSubscribeListener(SubscribeListener listener) {
        mSubscribeListener = listener;
    }

    public BeSubscribedAdapter2(Context context) {
        mContext = context;
    }

    public BeSubscribedAdapter2(Context context, int type) {
        mContext = context;
        this.type = type;
    }


    @Override
    public int getCount() {

        return mDatum == null ? 0 : mDatum.size();
    }

    public void setData(List<JSONObject> mDatum) {
        this.mDatum = mDatum;
        notifyDataSetChanged();

    }

    @Override
    public JSONObject getItem(int position) {
        return mDatum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final JSONObject obj = getItem(position);
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
        holder.textNickname.setText(JSONUtil.getString(obj, "nickname"));
        holder.textDistance.setText(JSONUtil.getString(obj, "distance") + "m");
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
//        boolean subscribeFlag = JSONUtil.getBoolean(obj, "subscribeFlag");
        holder.heartView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        ImageLoader.getInstance().displayImage(JSONUtil.getString(obj, "avatar"), holder.roundImageView);
        return convertView;
    }

    class ViewHolder {
        RoundImageView roundImageView;
        TextView textNickname;
        TextView textDistance;
        HeartView heartView;
        TextView textAge;
    }

    private void attention() {
//        DhNet dhNet = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
//        dhNet.addParam("targetUserId", targetId);
//        dhNet.doPostInDialog(new NetTask(self) {
//            @Override
//            public void doInUI(Response response, Integer transfer) {
//                if (response.isSuccess()) {
//                    //取消关注成功
//                    refresh();
//                }
//            }
//        });
    }


}
