package com.gongpingjia.carplay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gongpingjia.carplay.bean.ImageState;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageAdapter1 extends BaseAdapter {

    private List<ImageState> mDatas;

    private LayoutInflater mInflater;

    public ImageAdapter1(Context context, List<ImageState> data) {
        mDatas = data;
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
    public ImageState getItem(int position) {
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

        return null;
    }

}
