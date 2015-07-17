package com.gongpingjia.carplay.util;

import org.json.JSONArray;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gongpingjia.carplay.R;

public class PicLayoutUtil
{
    LayoutParams params;
    
    JSONArray data;
    
    Context mContext;
    
    int count;
    
    int padding;
    
    LinearLayout layout;
    
    OnChildClickListener onChildClickListener;
    
    int width;
    
    int horizontalMax = 3;
    
    public PicLayoutUtil(Context context, int count, int padding, LinearLayout layout)
    {
        this.count = count;
        this.padding = padding;
        this.layout = layout;
        this.mContext = context;
        width = 1080;
        layout.removeAllViews();
    }
    
    /**
     * 添加多个用户头像
     * 
     */
    public void AddChild()
    {
        int childWidth = (width - (count - 1) * padding) / count;
        params = new LayoutParams(childWidth, childWidth);
        for (int i = 0; i < count; i++)
        {
            layout.addView(createImageView(), params);
        }
    }
    
    /**
     * 添加活动的多个图片
     * 
     * 
     */
    public void addMoreChild()
    {
        if (count == 1)
        {
            addPicOneChild();
        }
        else if (count == 2)
        {
            int childWidth = (width - (horizontalMax - 1) * padding) / horizontalMax;
            params = new LayoutParams(childWidth, childWidth);
            AddPicTwoChild();
        }
        else
        {
            int childWidth = (width - (horizontalMax - 1) * padding) / horizontalMax;
            params = new LayoutParams(childWidth, childWidth);
            for (int i = 0; i < count; i++)
            {
                addMorePic();
            }
        }
        
    }
    
    private void AddPicTwoChild()
    {
        for (int i = 0; i < 2; i++)
        {
            layout.addView(createImageView(), params);
        }
    }
    
    private ImageView createImageView()
    {
        ImageView img = new ImageView(mContext);
        img.setImageResource(R.drawable.ic_launcher);
        img.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onChildClickListener != null)
                {
                    onChildClickListener.onclick("userid");
                }
            }
        });
        
        return img;
    }
    
    private void addPicOneChild()
    {
        params = new LayoutParams(200, 100);
        ImageView img = new ImageView(mContext);
        img.setImageResource(R.drawable.ic_launcher);
        layout.addView(img, params);
        img.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onChildClickListener != null)
                {
                    onChildClickListener.onclick("userid");
                }
            }
        });
    }
    
    private void addMorePic()
    {
        LinearLayout child;
        if (layout.getChildCount() == 0)
        {
            child = addChild(layout);
        }
        else
        {
            child = (LinearLayout)layout.getChildAt(layout.getChildCount() - 1);
            if (child.getChildCount() > 2)
            {
                child = addChild(layout);
            }
        }
        
        child.addView(createImageView(), params);
    }
    
    private LinearLayout addChild(LinearLayout piclayout)
    {
        LinearLayout child = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        piclayout.addView(child, params);
        return child;
    }
    
    public interface OnChildClickListener
    {
        void onclick(String userid);
    }
}
