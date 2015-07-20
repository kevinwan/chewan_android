package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.image.ImageLoad;

import org.json.JSONArray;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CarSeatUtil
{
    int jsatotalCount = 2;
    
    int seatcount = 5;
    
    LayoutInflater mLayoutInflater;
    
    Context mContext;
    
    JSONArray jsa;
    
    LinearLayout parentV;
    
    OnSeatClickListener onSeatClickListener;
    
    /** 占座 */
    public static final int Possession = 1;
    
    /** 拉下 */
    public static final int Pulldown = 2;
    
    public CarSeatUtil(Context context, JSONArray jsa, LinearLayout layout)
    {
        this.mContext = context;
        this.jsa = jsa;
        mLayoutInflater = LayoutInflater.from(context);
        this.parentV = layout;
    }
    
    public void addCar()
    {
        for (int i = 0; i < jsatotalCount; i++)
        {
            final int groupPostion = i;
            View childV = mLayoutInflater.inflate(R.layout.item_car_seat, null);
            parentV.addView(childV);
            LinearLayout seatLayout = (LinearLayout)childV.findViewById(R.id.seat_layout);
            for (int j = 0; j < seatcount; j++)
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
                            onSeatClickListener.onClick(groupPostion, childPosition);
                        }
                    }
                });
            }
        }
    }
    
    public void changeSeatStatus(int groupPosition, int childPosition, String headUrl, int type)
    {
        for (int i = 0; i < jsatotalCount; i++)
        {
            View childV = mLayoutInflater.inflate(R.layout.item_car_seat, null);
            parentV.addView(childV);
            LinearLayout seatLayout = (LinearLayout)childV.findViewById(R.id.seat_layout);
            for (int j = 0; j < seatcount; j++)
            {
                View seatChild = seatLayout.getChildAt(j);
                ImageView seatI = (ImageView)seatChild.findViewById(R.id.seat);
                ImageView headI = (ImageView)seatChild.findViewById(R.id.head);
                if (type == Possession)
                {
                    headI.setVisibility(View.VISIBLE);
                    seatI.setImageResource(R.drawable.icon_seat_white);
                    ImageLoader.getInstance().displayImage(headUrl, headI, CarPlayValueFix.headOptions);
                }
                else
                {
                    seatI.setImageResource(R.drawable.icon_seat_blue);
                    headI.setVisibility(View.GONE);
                }
            }
        }
    }
    
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
        void onClick(int groupPosition, int childPosition);
    }
}
