package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/24.
 * 接受邀请的人的头像
 */
public class AcceptHeadAdapter extends RecyclerView.Adapter<AcceptHeadAdapter.SimpleViewHolder>{
    private final Context mContext;

    private JSONArray data;

    public AcceptHeadAdapter(Context context) {
        mContext = context;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        RoundImageView head;

        public SimpleViewHolder(View view) {
            super(view);
            Log.d("msg", "SimpleViewHolder");
            head = (RoundImageView) view.findViewById(R.id.head);

        }
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_accephead_list, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final JSONObject jo = getItem(position);
        ViewUtil.bindNetImage(holder.head, JSONUtil.getString(jo, "avatar"), "head");
                holder.head.setTag(JSONUtil.getString(jo,"userId"));
    }



    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.length();
    }

    public void setData(JSONArray data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data)
        {
            try {
                item = (JSONObject) data.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

}
