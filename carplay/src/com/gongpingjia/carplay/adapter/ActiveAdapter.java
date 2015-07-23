package com.gongpingjia.carplay.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.PicLayoutUtil;
import com.gongpingjia.carplay.view.RoundImageView;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

public class ActiveAdapter extends NetJSONAdapter
{
    LayoutInflater mLayoutInflater;
    
    int mResource;
    
    int piclayoutWidth;
    
    int headlayoutWidth;
    
    public ActiveAdapter(String api, Context context, int mResource)
    {
        super(api, context, mResource);
        mLayoutInflater = LayoutInflater.from(context);
        this.mResource = mResource;
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        piclayoutWidth =
            width - DhUtil.dip2px(context, 59) - DhUtil.dip2px(context, 12) * 2 - DhUtil.dip2px(context, 10);
        headlayoutWidth =
            piclayoutWidth - DhUtil.dip2px(context, 75) - DhUtil.dip2px(context, 10) - DhUtil.dip2px(context, 8) * 2;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        // TODO Auto-generated method stub
        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(mResource, null);
            holder = new ViewHolder();
            holder.addressT = (TextView)convertView.findViewById(R.id.address);
            holder.statusT = (TextView)convertView.findViewById(R.id.status);
            holder.seat_countT = (TextView)convertView.findViewById(R.id.seat_count);
            holder.nameT = (TextView)convertView.findViewById(R.id.name);
            holder.tv_publish_timeT = (TextView)convertView.findViewById(R.id.tv_publish_time);
            holder.contentT = (TextView)convertView.findViewById(R.id.content);
            holder.timeT = (TextView)convertView.findViewById(R.id.time);
            holder.joinT = (TextView)convertView.findViewById(R.id.join);
            holder.headI = (RoundImageView)convertView.findViewById(R.id.head);
            holder.car_logoI = (ImageView)convertView.findViewById(R.id.car_logo);
            holder.piclayoutV = (LinearLayout)convertView.findViewById(R.id.piclayout);
            holder.headlayoutV = (LinearLayout)convertView.findViewById(R.id.headlayout);
            holder.layout_sexV = convertView.findViewById(R.id.layout_sex);
            holder.ageT = (TextView)convertView.findViewById(R.id.age);
            holder.drive_ageT = (TextView)convertView.findViewById(R.id.drive_age);
            holder.seat_count_allT = (TextView)convertView.findViewById(R.id.seat_count_all);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        
        JSONObject jo = mVaules.get(position);
        JSONObject creater = JSONUtil.getJSONObject(jo, "organizer");
        ViewUtil.bindView(holder.nameT, JSONUtil.getString(creater, "nickname"));
        
        if (JSONUtil.getString(creater, "gender").equals("男"))
        {
            holder.layout_sexV.setBackgroundResource(R.drawable.man);
        }
        else
        {
            holder.layout_sexV.setBackgroundResource(R.drawable.woman);
        }
        ViewUtil.bindView(holder.ageT, JSONUtil.getString(creater, "age"));
        ViewUtil.bindView(holder.tv_publish_timeT, JSONUtil.getString(jo, "publishTime"), "neartime");
        ViewUtil.bindNetImage(holder.car_logoI, JSONUtil.getString(creater, "carBrandLogo"), "optionsDefault");
        ViewUtil.bindView(holder.drive_ageT,
            JSONUtil.getString(creater, "carModel") + "," + JSONUtil.getString(creater, "drivingExperience") + "年驾龄");
        
        ViewUtil.bindView(holder.contentT, JSONUtil.getString(creater, "introduction"));
        JSONArray picJsa = JSONUtil.getJSONArray(jo, "cover");
        // holder.piclayoutV.removeAllViews();
        PicLayoutUtil util = new PicLayoutUtil(mContext, picJsa, 5, holder.piclayoutV, piclayoutWidth);
        util.addMoreChild();
        // holder.headlayoutV.removeAllViews();
        JSONArray headJsa = JSONUtil.getJSONArray(jo, "members");
        PicLayoutUtil headUtil = new PicLayoutUtil(mContext, headJsa, 5, holder.headlayoutV, headlayoutWidth);
        headUtil.AddChild();
        
        if (JSONUtil.getLong(jo, "start") == 0)
        {
            ViewUtil.bindView(holder.timeT, "不确定");
        }
        else
        {
            ViewUtil.bindView(holder.timeT, JSONUtil.getLong(jo, "start"), "time");
        }
        ViewUtil.bindView(holder.addressT, "地点: " + JSONUtil.getString(jo, "location"));
        ViewUtil.bindNetImage(holder.headI, JSONUtil.getString(creater, "photo"), "optionsDefault");
        
        ViewUtil.bindView(holder.statusT, JSONUtil.getString(jo, "pay"));
        String pay = JSONUtil.getString(jo, "pay");
        if (pay.equals("我请客"))
        {
            holder.statusT.setBackgroundResource(R.drawable.button_radian);
        }
        else if (pay.equals("请我吧"))
        {
            holder.statusT.setBackgroundResource(R.drawable.grey_bg);
        }
        else
        {
            holder.statusT.setBackgroundResource(R.drawable.blue_light_bg);
        }
        
        ViewUtil.bindView(holder.seat_countT, JSONUtil.getString(jo, "holdingSeat"));
        ViewUtil.bindView(holder.seat_count_allT, "/" + JSONUtil.getString(jo, "totalSeat") + "座");
        return convertView;
    }
    
    class ViewHolder
    {
        RoundImageView headI;
        
        ImageView car_logoI;
        
        TextView statusT, seat_countT, nameT, tv_publish_timeT;
        
        TextView contentT, timeT, addressT, joinT;
        
        LinearLayout piclayoutV, headlayoutV;
        
        View layout_sexV;
        
        TextView ageT, drive_ageT, seat_count_allT;
        
    }
}
