package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.util.DhUtil;

import org.json.JSONArray;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
    
    int headMax = 5;
    
    public PicLayoutUtil(Context context, int count, int padding, LinearLayout layout, int width)
    {
        this.count = count;
        this.padding = DhUtil.dip2px(context, padding);
        this.layout = layout;
        this.mContext = context;
        this.width = width;
        layout.removeAllViews();
    }
    
    /**
     * 添加多个用户头像
     * 
     */
    public void AddChild()
    {
        int childWidth = (width - (headMax - 1) * padding) / headMax;
        params = new LayoutParams(childWidth, childWidth);
        params.rightMargin = padding;
        for (int i = 0; i < count; i++)
        {
            layout.addView(createHeadImageView(), params);
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
        else if (count < 4)
        {
            int childWidth = (width - (horizontalMax - 1) * padding) / horizontalMax;
            params = new LayoutParams(childWidth, childWidth);
            params.rightMargin = padding;
            AddPicThreeChild();
        }
        else
        {
            int childWidth = (width - (horizontalMax - 1) * padding) / horizontalMax;
            params = new LayoutParams(childWidth, childWidth);
            params.rightMargin = padding;
            for (int i = 0; i < count; i++)
            {
                addMorePic();
            }
        }
        
    }
    
    private void AddPicThreeChild()
    {
        LinearLayout child = addChild(layout);
        for (int i = 0; i < count; i++)
        {
            child.addView(createPicImageView(), params);
        }
    }
    
    private ImageView createHeadImageView()
    {
        ImageView img = new ImageView(mContext);
        img.setImageResource(R.drawable.head3);
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
    
    private ImageView createPicImageView()
    {
        ImageView img = new ImageView(mContext);
        img.setImageResource(R.drawable.model_pic);
        img.setScaleType(ScaleType.FIT_XY);
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
            if (child.getChildCount() > 1)
            {
                child = addChild(layout);
            }
        }
        
        child.addView(createPicImageView(), params);
    }
    
    private LinearLayout addChild(LinearLayout piclayout)
    {
        LinearLayout child = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.bottomMargin = padding;
        piclayout.addView(child, params);
        return child;
    }
    
    public interface OnChildClickListener
    {
        void onclick(String userid);
    }
}
