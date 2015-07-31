package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.RoundImageView;

public class CarSeatUtil
{
    int totalSeatcount, usedSeatCount;
    
    LayoutInflater mLayoutInflater;
    
    Context mContext;
    
    JSONArray jsa;
    
    LinearLayout parentV;
    
    OnSeatClickListener onSeatClickListener;
    
    public CarSeatUtil(Context context, LinearLayout layout)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.parentV = layout;
    }
    
    public void addCar(JSONArray jsa)
    {
        totalSeatcount = 0;
        usedSeatCount = 0;
        parentV.removeAllViews();
        this.jsa = jsa;
        if (jsa == null)
        {
            return;
        }
        for (int i = 0; i < jsa.length(); i++)
        {
            try
            {
                JSONObject carJo = jsa.getJSONObject(i);
                View childV = mLayoutInflater.inflate(R.layout.item_car_seat, null);
                parentV.addView(childV);
                
                ImageView carLogo = (ImageView)childV.findViewById(R.id.logo);
                ViewUtil.bindNetImage(carLogo, JSONUtil.getString(carJo, "carBrandLogo"), "carlogo");
                ViewUtil.bindView(childV.findViewById(R.id.name), JSONUtil.getString(carJo, "carModel"));
                
                final String carId = JSONUtil.getString(carJo, "carId");
                
                int seatCount = JSONUtil.getInt(carJo, "totalSeat");
                totalSeatcount += seatCount;
                LinearLayout seatLayout = (LinearLayout)childV.findViewById(R.id.seat_layout);
                
                for (int j = 0; j < seatCount; j++)
                {
                    final int childPosition = j;
                    View seatChild = seatLayout.getChildAt(j);
                    ImageView seatI = (ImageView)seatChild.findViewById(R.id.seat);
                    
                    seatI.setVisibility(View.VISIBLE);
                    seatChild.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            if (onSeatClickListener != null)
                            {
                                onSeatClickListener.onSeatClick(carId, childPosition);
                            }
                        }
                    });
                }
                
                JSONArray userJsa = JSONUtil.getJSONArray(carJo, "users");
                usedSeatCount += userJsa.length();
                for (int k = 0; k < userJsa.length(); k++)
                {
                    final JSONObject userJo = JSONUtil.getJSONObjectAt(userJsa, k);
                    int seatIndex = JSONUtil.getInt(userJo, "seatIndex");
                    View seatChild = seatLayout.getChildAt(seatIndex);
                    RoundImageView headI = (RoundImageView)seatChild.findViewById(R.id.head);
                    ImageView seatI = (ImageView)seatChild.findViewById(R.id.seat);
                    seatI.setImageResource(R.drawable.icon_seat_white);
                    ViewUtil.bindNetImage(headI, JSONUtil.getString(userJo, "photo"), "head");
                    // 不可以去掉
                    headI.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            if (onSeatClickListener != null)
                            {
                                onSeatClickListener.onHeadClick(userJo);
                            }
                        }
                    });
                    headI.setVisibility(View.VISIBLE);
                    seatChild.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            if (onSeatClickListener != null)
                            {
                                onSeatClickListener.onHeadClick(userJo);
                            }
                        }
                    });
                }
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        if (onSeatClickListener != null)
        {
            onSeatClickListener.seatCount(totalSeatcount, totalSeatcount - usedSeatCount);
        }
    }
    
    // public void changeSeatStatus(int groupPosition, int childPosition, String headUrl, int type)
    // {
    // for (int i = 0; i < jsa.length(); i++)
    // {
    // View childV = mLayoutInflater.inflate(R.layout.item_car_seat, null);
    // parentV.addView(childV);
    // LinearLayout seatLayout = (LinearLayout)childV.findViewById(R.id.seat_layout);
    // for (int j = 0; j < seatcount; j++)
    // {
    // View seatChild = seatLayout.getChildAt(j);
    // ImageView seatI = (ImageView)seatChild.findViewById(R.id.seat);
    // RoundImageView headI = (RoundImageView)seatChild.findViewById(R.id.head);
    // if (type == Possession)
    // {
    // headI.setVisibility(View.VISIBLE);
    // seatI.setImageResource(R.drawable.icon_seat_white);
    // ImageLoader.getInstance().displayImage(headUrl, headI, CarPlayValueFix.headOptions);
    // }
    // else
    // {
    // seatI.setImageResource(R.drawable.icon_seat_blue);
    // headI.setVisibility(View.GONE);
    // }
    // }
    // }
    // }
    
    public OnSeatClickListener getOnSeatClickListener()
    {
        return onSeatClickListener;
    }
    
    public void setOnSeatClickListener(OnSeatClickListener onSeatClickListener)
    {
        this.onSeatClickListener = onSeatClickListener;
    }
    
    public interface OnSeatClickListener
    {
        void onSeatClick(String carId, int childPosition);
        
        void onHeadClick(JSONObject headJo);
        
        void seatCount(int totalCount, int emptyCount);
    }
}
