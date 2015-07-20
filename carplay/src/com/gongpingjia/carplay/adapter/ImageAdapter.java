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

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.util.ImageUtil;

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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.griditem_photo, null);
            GridView.LayoutParams params = new GridView.LayoutParams(200, 200);
            convertView.setLayoutParams(params);
        }

        ImageView imgView = ViewHolder.getView(convertView, R.id.imgView_photo);
        ImageView imgVisible = ViewHolder.getView(convertView, R.id.imgView_visible);

        if (photo.isLast()) {
            // 最后一张照片
            imgView.setScaleType(ScaleType.CENTER);
            imgView.setImageResource(R.drawable.icon_add_photo);
            imgView.setBackgroundColor(Color.parseColor("#CCD0D9"));
            imgVisible.setVisibility(View.GONE);
        } else {
            imgView.setScaleType(ScaleType.CENTER_CROP);
            imgView.setImageBitmap(ImageUtil.getBitmap(photo.getPath()));
        }
        return convertView;
    }
}
