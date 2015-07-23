package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

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
    
    public PicLayoutUtil(Context context, JSONArray jsa, int padding, LinearLayout layout, int width)
    {
        this.data = jsa;
        this.count = jsa.length();
        this.padding = DhUtil.dip2px(context, padding);
        this.layout = layout;
        this.mContext = context;
        this.width = width;
    }
    
    /**
     * 添加多个用户头像
     * 
     */
    public void AddChild()
    {
        if (layout.getChildCount() == 0)
        {
            int childWidth = (width - (headMax - 1) * padding) / headMax;
            params = new LayoutParams(childWidth, childWidth);
            params.rightMargin = padding;
            for (int i = 0; i < count; i++)
            {
                try
                {
                    layout.addView(createHeadImageView(data.getJSONObject(i)), params);
                }
                catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 添加活动的多个图片
     * 
     * 
     */
    public void addMoreChild()
    {
        if (layout.getChildCount() == 0)
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
                    try
                    {
                        addMorePic(data.getJSONObject(i));
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
    
    private void AddPicThreeChild()
    {
        LinearLayout child = addChild(layout);
        for (int i = 0; i < count; i++)
        {
            try
            {
                child.addView(createPicImageView(data.getJSONObject(i)), params);
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private ImageView createHeadImageView(JSONObject jo)
    {
        RoundImageView img = new RoundImageView(mContext);
        img.setLayoutParams(params);
        ViewUtil.bindNetImage(img, JSONUtil.getString(jo, "photo"), "optionsDefault");
        img.setTag(JSONUtil.getString(jo, "userId"));
        img.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onChildClickListener != null)
                {
                    ImageView i = (ImageView)v;
                    onChildClickListener.onclick(i.getTag().toString());
                }
            }
        });
        
        return img;
    }
    
    private ImageView createPicImageView(JSONObject jo)
    {
        ImageView img = new ImageView(mContext);
        img.setLayoutParams(params);
        ViewUtil.bindNetImage(img, JSONUtil.getString(jo, "thumbnail_pic"), "optionsDefault");
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
        try
        {
            JSONObject jo = data.getJSONObject(0);
            params = new LayoutParams(200, 100);
            ImageView img = new ImageView(mContext);
            ViewUtil.bindNetImage(img, JSONUtil.getString(jo, "thumbnail_pic"), "optionsDefault");
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
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void addMorePic(JSONObject jo)
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
        
        child.addView(createPicImageView(jo), params);
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
