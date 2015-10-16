package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.SimpleViewHolder> {
    private final Context mContext;
    private List<JSONObject> data;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView titleT, locationT, priceT, infoT, priceDescT, cityT;
        TextView maleLimitT, maleNumT, femaleLimitT, femaleNumT;
        ImageView picI, headI;

        public SimpleViewHolder(View view) {
            super(view);
            titleT = (TextView) view.findViewById(R.id.title);
            locationT = (TextView) view.findViewById(R.id.location);
            priceT = (TextView) view.findViewById(R.id.price);
            infoT = (TextView) view.findViewById(R.id.info);
            priceDescT = (TextView) view.findViewById(R.id.priceDesc);
            picI = (ImageView) view.findViewById(R.id.pic);
            headI = (ImageView) view.findViewById(R.id.head);

            maleLimitT = (TextView) view.findViewById(R.id.maleLimit);
            maleNumT = (TextView) view.findViewById(R.id.maleNum);
            femaleLimitT = (TextView) view.findViewById(R.id.femaleLimit);
            femaleNumT = (TextView) view.findViewById(R.id.femaleNum);
            cityT = (TextView) view.findViewById(R.id.city);
        }
    }

    public RecommendListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_recommend, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        JSONObject jo = getItem(position);
        holder.priceT.setText(JSONUtil.getString(jo, "price"));
        holder.priceDescT.setText(JSONUtil.getString(jo, "priceDesc"));
        holder.infoT.setText(JSONUtil.getString(jo, "title"));
        holder.maleLimitT.setText(JSONUtil.getString(jo, "maleLimit"));
        holder.maleNumT.setText(JSONUtil.getString(jo, "maleNum"));
        holder.femaleLimitT.setText(JSONUtil.getString(jo, "femaleLimit"));
        holder.femaleNumT.setText(JSONUtil.getString(jo, "femaleNum"));

        JSONObject locationJo = JSONUtil.getJSONObject(jo, "destination");
        holder.locationT.setText(JSONUtil.getString(locationJo, "detail"));
        holder.cityT.setText("[" + JSONUtil.getString(locationJo, "city") + "]");

        JSONObject organizerJo = JSONUtil.getJSONObject(jo, "organizer");
        holder.titleT.setText(JSONUtil.getString(organizerJo, "nickname"));
        ViewUtil.bindNetImage(holder.picI, JSONUtil.getString(organizerJo, "avatar"), "head");
        try {
            JSONArray coversJSa = jo.getJSONArray("covers");
            String picUrl = coversJSa.getString(0);
            ViewUtil.bindNetImage(holder.picI, picUrl, "default");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg", "22222222");
                Toast.makeText(mContext, "哈哈", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public JSONObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }
}

