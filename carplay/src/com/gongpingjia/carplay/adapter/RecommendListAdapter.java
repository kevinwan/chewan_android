package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity2;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;

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
        TextView titleT, locationT, priceT,  priceDescT, cityT,participate_womanT, participate_manT,unparticipateT,pricerightT;
        LinearLayout limitedlayoutL,unlimitedlayoutL;

        ImageView picI;
        RoundImageView headI;

        public SimpleViewHolder(View view) {
            super(view);
            titleT = (TextView) view.findViewById(R.id.title);
            locationT = (TextView) view.findViewById(R.id.location);
            priceT = (TextView) view.findViewById(R.id.price);
            pricerightT = (TextView) view.findViewById(R.id.priceright);
            priceDescT = (TextView) view.findViewById(R.id.priceDesc);
            picI = (ImageView) view.findViewById(R.id.pic);
            headI = (RoundImageView) view.findViewById(R.id.head);

            limitedlayoutL = (LinearLayout) view.findViewById(R.id.limitedlayout);
            unlimitedlayoutL = (LinearLayout) view.findViewById(R.id.unlimitedlayout);
            participate_womanT = (TextView) view.findViewById(R.id.participate_woman);
            participate_manT = (TextView) view.findViewById(R.id.participate_man);
            unparticipateT = (TextView) view.findViewById(R.id.unparticipate);

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
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {

        final JSONObject jo = getItem(position);
        double price=JSONUtil.getDouble(jo, "price");
        if (((int)price)==0){
            holder.priceT.setText("免费");
            holder.pricerightT.setVisibility(View.GONE);
        }else {
            holder.priceT.setText(price+"");
            holder.pricerightT.setVisibility(View.VISIBLE);
        }
//        holder.priceDescT.setVisibility(TextUtils.isEmpty(JSONUtil.getString(jo, "subsidyPrice")) ? View.GONE : View.VISIBLE);
//        holder.priceDescT.setText("官方补贴" + JSONUtil.getString(jo, "subsidyPrice") + "元/人");

//        holder.infoT.setText(JSONUtil.getString(jo, "title"));


        //0:无限制 1：限制总人数 2：限制男女人数
        int limitType = JSONUtil.getInt(jo, "limitType");
        //男生,女生数量,总量
        if (limitType == 1) {
            holder.limitedlayoutL.setVisibility(View.GONE);
            holder.unlimitedlayoutL.setVisibility(View.VISIBLE);
            ViewUtil.bindView(holder.unparticipateT, CarPlayUtil.setTextColor(mContext, JSONUtil.getInt(jo, "nowJoinNum")+" / ", JSONUtil.getInt(jo, "nowJoinNum") + " / " + JSONUtil.getInt(jo, "totalLimit"), R.color.text_grey));
        } else if (limitType == 2) {
            holder.limitedlayoutL.setVisibility(View.VISIBLE);
            holder.unlimitedlayoutL.setVisibility(View.GONE);
            ViewUtil.bindView(holder.participate_womanT, CarPlayUtil.setTextColor(mContext,  JSONUtil.getInt(jo, "femaleNum")+" / ", JSONUtil.getInt(jo, "femaleNum") + " / " + JSONUtil.getInt(jo, "femaleLimit"), R.color.text_grey));
            ViewUtil.bindView(holder.participate_manT, CarPlayUtil.setTextColor(mContext,  JSONUtil.getInt(jo, "maleNum")+" / ", JSONUtil.getInt(jo, "maleNum") + " / " + JSONUtil.getInt(jo, "maleLimit"), R.color.text_grey));
        } else {
            holder.limitedlayoutL.setVisibility(View.GONE);
            holder.unlimitedlayoutL.setVisibility(View.VISIBLE);
            ViewUtil.bindView(holder.unparticipateT, CarPlayUtil.setTextColor(mContext, JSONUtil.getInt(jo, "nowJoinNum")+" / ", JSONUtil.getInt(jo, "nowJoinNum") + " / " + "人数不限", R.color.text_grey)) ;
        }


        JSONObject locationJo = JSONUtil.getJSONObject(jo, "destination");
        String detail = JSONUtil.getString(locationJo, "detail");
        holder.locationT.setText(TextUtils.isEmpty(detail) ? "地点待定" : detail);

        String citystr="["+JSONUtil.getString(locationJo, "city")+"]  ";
        String title = citystr+JSONUtil.getString(jo, "title");
        ViewUtil.bindView(holder.cityT, CarPlayUtil.setTextColor(mContext, citystr, title, R.color.text_orange));
//        holder.cityT.setText("[" + JSONUtil.getString(locationJo, "city") + "]");

        JSONObject organizerJo = JSONUtil.getJSONObject(jo, "organizer");
        holder.titleT.setText(JSONUtil.getString(organizerJo, "nickname"));
        ViewUtil.bindNetImage(holder.headI, JSONUtil.getString(organizerJo, "avatar"), "head");
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
                JSONObject jo = getItem(position);
                Intent it = new Intent(mContext, ActiveDetailsActivity2.class);
                it.putExtra("activityId", JSONUtil.getString(jo, "officialActivityId"));
                mContext.startActivity(it);
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

