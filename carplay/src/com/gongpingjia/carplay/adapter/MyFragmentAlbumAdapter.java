package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 * 我的页面相册
 */
public class MyFragmentAlbumAdapter extends RecyclerView.Adapter<MyFragmentAlbumAdapter.SimpleViewHolder> {
    private final Context mContext;

    private List<JSONObject> data;

    public MyFragmentAlbumAdapter(Context context) {
        mContext = context;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView photoT;

        public SimpleViewHolder(View view) {
            super(view);
            Log.d("msg", "SimpleViewHolder");
            photoT = (ImageView) view.findViewById(R.id.photo);

        }
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
//        holder.photoT

    }



    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public JSONObject getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }



}
