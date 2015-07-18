package com.gongpingjia.carplay.adapter;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.PicLayoutUtil;

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
import net.duohuo.dhroid.util.DhUtil;

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
            holder.headI = (ImageView)convertView.findViewById(R.id.head);
            holder.car_logoI = (ImageView)convertView.findViewById(R.id.car_logo);
            holder.piclayoutV = (LinearLayout)convertView.findViewById(R.id.piclayout);
            holder.headlayoutV = (LinearLayout)convertView.findViewById(R.id.headlayout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        
        PicLayoutUtil headUtil = new PicLayoutUtil(mContext, 5, 5, holder.headlayoutV, headlayoutWidth);
        headUtil.AddChild();
        
        if (position % 2 == 0)
        {
            PicLayoutUtil util = new PicLayoutUtil(mContext, 4, 5, holder.piclayoutV, piclayoutWidth);
            util.addMoreChild();
        }
        else
        {
            PicLayoutUtil util = new PicLayoutUtil(mContext, 2, 5, holder.piclayoutV, piclayoutWidth);
            util.addMoreChild();
        }
        
        return convertView;
    }
    
    class ViewHolder
    {
        ImageView headI, car_logoI;
        
        TextView statusT, seat_countT, nameT, tv_publish_timeT;
        
        TextView contentT, timeT, addressT, joinT;
        
        LinearLayout piclayoutV, headlayoutV;
        
    }
}
