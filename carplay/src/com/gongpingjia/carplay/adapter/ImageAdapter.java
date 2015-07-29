package com.gongpingjia.carplay.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.duohuo.dhroid.util.DhUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.PhotoState;
import com.nostra13.universalimageloader.core.ImageLoader;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageAdapter extends BaseAdapter
{
    
    private static final String TAG = "ImageAdapter";
    
    private List<PhotoState> mDatas;
    
    private LayoutInflater mInflater;
    
    int picHeight;
    
    public ImageAdapter(Context context, List<PhotoState> datas)
    {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        picHeight = (width - DhUtil.dip2px(context, 40)) / 3;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        if (mDatas == null)
        {
            return 0;
        }
        return mDatas.size();
    }
    
    @Override
    public PhotoState getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        PhotoState photo = mDatas.get(position);
        ViewHolder holder = new ViewHolder();
        View view = mInflater.inflate(R.layout.griditem_photo, null);
        GridView.LayoutParams params = new GridView.LayoutParams(picHeight, picHeight);
        view.setLayoutParams(params);
        holder.imgContent = (ImageView)view.findViewById(R.id.imgView_photo);
        holder.imgLabel = (ImageView)view.findViewById(R.id.imgView_visible);
        convertView = view;
        
        if (photo.isLast())
        {
            // 最后一张照片
            holder.imgContent.setScaleType(ScaleType.CENTER);
            holder.imgContent.setImageResource(R.drawable.icon_add_photo);
            holder.imgContent.setBackgroundColor(Color.parseColor("#CCD0D9"));
            holder.imgLabel.setVisibility(View.GONE);
        }
        else
        {
            if (photo.isChecked())
            {
                holder.imgLabel.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.imgLabel.setVisibility(View.GONE);
            }
            holder.imgContent.setScaleType(ScaleType.CENTER_CROP);
            if (photo.getPath().startsWith("http"))
            {
                ImageLoader.getInstance().displayImage(photo.getPath(),
                    holder.imgContent,
                    CarPlayValueFix.optionsDefault);
            }
            else
            {
                ImageLoader.getInstance().displayImage("file://" + photo.getPath(),
                    holder.imgContent,
                    CarPlayValueFix.optionsDefault);
            }
        }
        return convertView;
    }
    
    public static Bitmap getLocalImage(File f)
    {
        File file = f;
        if (file.exists())
        {
            try
            {
                file.setLastModified(System.currentTimeMillis());
                FileInputStream in = new FileInputStream(file);
                
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, null, options);
                int sWidth = 200;
                int sHeight = 200;
                int mWidth = options.outWidth;
                int mHeight = options.outHeight;
                int s = 1;
                while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2))
                {
                    s *= 2;
                }
                options = new BitmapFactory.Options();
                options.inSampleSize = s;
                in.close();
                // 再次获取
                in = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                in.close();
                return bitmap;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    class ViewHolder
    {
        ImageView imgContent;
        
        ImageView imgLabel;
    }
}
