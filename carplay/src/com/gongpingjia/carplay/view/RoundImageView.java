package com.gongpingjia.carplay.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.gongpingjia.carplay.activity.my.PersonDetailActivity;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;

/**
 * 圆形的imageView
 * 
 * @author Zr
 * 
 */
public class RoundImageView extends ImageView
{
    
    private Context mContext;
    
    public RoundImageView(Context context)
    {
        super(context);
        mContext = context;
        
        // this.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(final View v)
        // {
        // UserInfoManage manager = UserInfoManage.getInstance();
        // manager.checkLogin((Activity)mContext, new LoginCallBack()
        // {
        //
        // @Override
        // public void onisLogin()
        // {
        // User user = User.getInstance();
        // if (user.getUserId().equals(v.getTag().toString()))
        // {
        //
        // }
        // else
        // {
        // Intent it = new Intent(mContext, PersonDetailActivity.class);
        // it.putExtra("userId", v.getTag().toString());
        // mContext.startActivity(it);
        // }
        // }
        //
        // @Override
        // public void onLoginFail()
        // {
        //
        // }
        // });
        // }
        // });
    }
    
    public RoundImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        this.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(final View v)
            {
                if (v.getTag() == null)
                {
                    return;
                }
                
                UserInfoManage manager = UserInfoManage.getInstance();
                manager.checkLogin((Activity)mContext, new LoginCallBack()
                {
                    
                    @Override
                    public void onisLogin()
                    {
                        User user = User.getInstance();
                        if (user.getUserId().equals(v.getTag().toString()))
                        {
                            
                        }
                        else
                        {
                            Intent it = new Intent(mContext, PersonDetailActivity.class);
                            it.putExtra("userId", v.getTag().toString());
                            mContext.startActivity(it);
                        }
                    }
                    
                    @Override
                    public void onLoginFail()
                    {
                        
                    }
                });
            }
        });
    }
    
    public RoundImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
    }
    
    @Override
    public void setImageResource(int resId)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        setImageBitmap(bitmap);
    }
    
    @Override
    public void setImageDrawable(Drawable drawable)
    {
        if (drawable == null)
            return;
        BitmapDrawable bd = (BitmapDrawable)drawable;
        Drawable _drawable = new BitmapDrawable(toRoundBitmap(bd.getBitmap()));
        super.setImageDrawable(_drawable);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm)
    {
        if (bm == null)
            return;
        try
        {
            Bitmap bitmap = toRoundBitmap(bm);
            super.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            
        }
    }
    
    public static Bitmap toRoundBitmap(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height)
        {
            roundPx = width;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        }
        else
        {
            roundPx = height;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);
        
        paint.setAntiAlias(true);
        
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
    
}
