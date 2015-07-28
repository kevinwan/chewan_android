package com.gongpingjia.carplay.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
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
public class ImageAdapter extends BaseAdapter {

    private static final String TAG = "ImageAdapter";

    private List<PhotoState> mDatas;

    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<PhotoState> datas) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public PhotoState getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        PhotoState photo = mDatas.get(position);
        ViewHolder holder = new ViewHolder();
        View view = mInflater.inflate(R.layout.griditem_photo, null);
        GridView.LayoutParams params = new GridView.LayoutParams(200, 200);
        view.setLayoutParams(params);
        holder.imgContent = (ImageView) view.findViewById(R.id.imgView_photo);
        holder.imgLabel = (ImageView) view.findViewById(R.id.imgView_visible);
        convertView = view;

        if (photo.isLast()) {
            // 最后一张照片
            holder.imgContent.setScaleType(ScaleType.CENTER);
            holder.imgContent.setImageResource(R.drawable.icon_add_photo);
            holder.imgContent.setBackgroundColor(Color.parseColor("#CCD0D9"));
            holder.imgLabel.setVisibility(View.GONE);
        } else {
            if (photo.isChecked()) {
                holder.imgLabel.setVisibility(View.VISIBLE);
            } else {
                holder.imgLabel.setVisibility(View.GONE);
            }
            holder.imgContent.setScaleType(ScaleType.CENTER_CROP);
            if (photo.getPath().startsWith("http")) {
                ImageLoader.getInstance().displayImage(photo.getPath(), holder.imgContent,
                        CarPlayValueFix.optionsDefault);
            } else {
                ImageLoader.getInstance().displayImage("file://" + photo.getPath(), holder.imgContent,
                        CarPlayValueFix.optionsDefault);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imgContent;

        ImageView imgLabel;
    }
}
