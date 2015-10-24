package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/24.
 */
public class OfficialMembersAdapter extends BaseAdapter{
    private final Context mContext;

    private JSONArray data;

    public OfficialMembersAdapter(Context context) {
        mContext = context;
    }

    public void setData(JSONArray data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data)
        {
            try {
                item = (JSONObject) data.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_attention_me_list2, null);
            holder.headI = (RoundImageView) convertView.findViewById(R.id.head);
            holder.nicknameT = (TextView) convertView.findViewById(R.id.nickname);
            holder.timeT = (TextView) convertView.findViewById(R.id.time);
            holder.distanceT = (TextView) convertView.findViewById(R.id.distance);
            holder.sexLayoutR = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.sexI = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.ageT = (TextView) convertView.findViewById(R.id.tv_age);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);

        ViewUtil.bindNetImage(holder.headI, JSONUtil.getString(jo, "avatar"), "head");
        ViewUtil.bindView(holder.nicknameT, JSONUtil.getString(jo, "nickname"));
        ViewUtil.bindView(holder.distanceT,JSONUtil.getInt(jo, "distance")+"米");
        ViewUtil.bindView(holder.ageT,JSONUtil.getInt(jo, "age"));

        String time= CarPlayValueFix.converTime((long) JSONUtil.getInt(jo, "subscribeTime"));
        ViewUtil.bindView(holder.timeT,time);

        String sex = JSONUtil.getString(jo, "gender");
        if (("男").equals(sex)) {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexLayoutR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
        }

        return convertView;
    }

    class ViewHolder{
        RoundImageView headI;
        ImageView sexI;
        RelativeLayout sexLayoutR;
        TextView nicknameT,timeT,distanceT,ageT;
    }
}
