package com.gongpingjia.carplay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.MatchingPreviewFragment;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 * 车玩已上传过的相册
 */
public class CarPlayAlbumAdapter extends RecyclerView.Adapter<CarPlayAlbumAdapter.SimpleViewHolder> {
    private final Context mContext;

    private List<JSONObject> data;


    public CarPlayAlbumAdapter(Context context) {
        mContext = context;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView photoT,checkI;

        public SimpleViewHolder(View view) {
            super(view);
            Log.d("msg", "SimpleViewHolder");
            photoT = (ImageView) view.findViewById(R.id.photo);
            checkI = (ImageView) view.findViewById(R.id.check);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_carplayalbum, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        final JSONObject jo = getItem(position);
        final int current = position;
        ViewUtil.bindNetImage(holder.photoT, JSONUtil.getString(jo, "url"), "default");
        holder.photoT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MatchingPreviewFragment.class);
                it.putExtra("photosid",JSONUtil.getString(jo, "id"));
                it.putExtra("photourl",JSONUtil.getString(jo, "url"));
                ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,it);
                        ((Activity) mContext).finish();
//                Intent it = new Intent(mContext, ImageGallery.class);
//                String[] photos =new String[data.size()];
//                String[] photosid =new String[data.size()];
//                String[] photoFile = new String[data.size()];
//                for (int i=0;i<data.size();i++){
//                    try {
//                        photos[i]= (String) data.get(i).get("url");
//                        photoFile[i] =  ImageLoader.getInstance().getDiskCache().get( photos[i]).toString();
//                        photosid[i] = (String) data.get(i).get("id");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                it.putExtra("type","myalbum");
//                it.putExtra("imgurls", photos);
//                it.putExtra("imgfile", photoFile);
//                it.putExtra("currentItem", current);
//                it.putExtra("imgids", photosid);
//                mContext.startActivity(it);
            }
        });

    }


    @Override
    public int getItemCount() {
        if (data == null) {
            Log.d("msg", "getItemCount:" + 0);
            return 0;
        }
        Log.d("msg", "getItemCount:" + data.size());
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
