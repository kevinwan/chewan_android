package com.gongpingjia.carplay.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ActiveAdapter.ViewHolder;
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
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

public class MyReleaseActiveAdapter extends NetJSONAdapter
{
    
    LayoutInflater mLayoutInflater;
    
    int mResource;
    
    int piclayoutWidth;
    
    int headlayoutWidth;
    
    public MyReleaseActiveAdapter(String api, Context context, int mResource)
    {
        super(api, context, mResource);
        
        mLayoutInflater = LayoutInflater.from(context);
        this.mResource = mResource;
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        piclayoutWidth = width - DhUtil.dip2px(context, 46 + 3 + 3 + 6 + 10 + 12);
        headlayoutWidth =
            piclayoutWidth - DhUtil.dip2px(context, 75) - DhUtil.dip2px(context, 10) - DhUtil.dip2px(context, 8) * 2;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        JSONObject jo = mVaules.get(position);
        int piccount = JSONUtil.getJSONArray(jo, "cover").length();
        return piccount;
    }
    
    @Override
    public int getViewTypeCount()
    {
        // TODO Auto-generated method stub
        return 10;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(mResource, null);
            holder = new ViewHolder();
            holder.addressT = (TextView)convertView.findViewById(R.id.address);
            holder.contentT = (TextView)convertView.findViewById(R.id.content);
            holder.date_leftT = (TextView)convertView.findViewById(R.id.date_left);
            holder.dateT = (TextView)convertView.findViewById(R.id.date);
            holder.chatT = (TextView)convertView.findViewById(R.id.chat);
            holder.payTypeT = (TextView)convertView.findViewById(R.id.pay_type);
            holder.piclayoutV = (LinearLayout)convertView.findViewById(R.id.piclayout);
            holder.headlayoutV = (LinearLayout)convertView.findViewById(R.id.headlayout);
            holder.lineTopI = (ImageView)convertView.findViewById(R.id.line_top);
            convertView.setTag(holder);
            
            PicLayoutUtil picUtil = new PicLayoutUtil(mContext, type, 5, holder.piclayoutV, piclayoutWidth);
            picUtil.addMoreChild();
            
            PicLayoutUtil headUtil = new PicLayoutUtil(mContext, 5, 5, holder.headlayoutV, headlayoutWidth);
            headUtil.AddAllHead();
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            
        }
        holder.lineTopI.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        
        JSONObject jo = mVaules.get(position);
        JSONObject creater = JSONUtil.getJSONObject(jo, "organizer");
        
        ViewUtil.bindView(holder.contentT, JSONUtil.getString(creater, "introduction"));
        JSONArray picJsa = JSONUtil.getJSONArray(jo, "cover");
        // holder.piclayoutV.removeAllViews();
        PicLayoutUtil util = new PicLayoutUtil();
        util.BindImageView(holder.piclayoutV, picJsa);
        // util.addMoreChild();
        // holder.headlayoutV.removeAllViews();
        JSONArray headJsa = JSONUtil.getJSONArray(jo, "members");
        PicLayoutUtil headUtil = new PicLayoutUtil();
        headUtil.BindHeadImage(holder.headlayoutV, headJsa);
        
        if (JSONUtil.getLong(jo, "start") == 0)
        {
            ViewUtil.bindView(holder.dateT, "不确定");
        }
        else
        {
            ViewUtil.bindView(holder.dateT, JSONUtil.getLong(jo, "start"), "time");
        }
        ViewUtil.bindView(holder.addressT, "地点: " + JSONUtil.getString(jo, "location"));
        
        ViewUtil.bindView(holder.payTypeT, JSONUtil.getString(jo, "pay"));
        ViewUtil.bindView(holder.date_leftT, JSONUtil.getString(jo, "publishDate"));
        
        return convertView;
    }
    
    class ViewHolder
    {
        
        TextView contentT, addressT, payTypeT;
        
        LinearLayout piclayoutV, headlayoutV;
        
        TextView chatT, dateT, date_leftT;
        
        ImageView lineTopI;
        
    }
}
