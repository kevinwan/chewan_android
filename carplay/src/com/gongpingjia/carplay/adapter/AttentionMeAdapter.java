/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttentionMeAdapter extends BaseAdapter{
    private static final int COUNT = 5;

    private final Context mContext;

    private List<JSONObject> data;

    public AttentionMeAdapter(Context context) {
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date sdate = new Date(JSONUtil.getInt(jo, "subscribeTime"));
        ViewUtil.bindView(holder.timeT,format.format(sdate));

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
