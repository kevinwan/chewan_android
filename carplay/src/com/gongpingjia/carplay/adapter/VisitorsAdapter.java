package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * 最近访客
 * Created by Administrator on 2015/10/27.
 */
public class VisitorsAdapter extends BaseAdapter {
    private Context mContext;
    private List<JSONObject> data;

    User user = User.getInstance();

    public VisitorsAdapter(Context context) {
        mContext = context;

    }

    public void setData(List<JSONObject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        JSONObject item = null;
        if (null != data) {
            item = data.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_visitors, viewGroup, false);
            holder.nameT = (TextView) view.findViewById(R.id.visitors_name);
            holder.contentT = (TextView) view.findViewById(R.id.visitors_content);
            holder.timeT = (TextView) view.findViewById(R.id.visitors_time);
            holder.distanceT = (TextView) view.findViewById(R.id.visitors_distance);
            holder.tv_ageT = (TextView) view.findViewById(R.id.tv_age);

            holder.sexLayout = (RelativeLayout) view.findViewById(R.id.layout_sex_and_age);
            holder.iv_sexl = (ImageView) view.findViewById(R.id.iv_sex);
            holder.icon = (RoundImageView) view.findViewById(R.id.visitors_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject jo = (JSONObject) getItem(position);
        holder.nameT.setText(JSONUtil.getString(jo, "nickname"));
        holder.contentT.setText(JSONUtil.getString(jo, "nickname")+"访问了你的相册");
//        Log.d("msg", "nameT" + holder.nameT);
//        Log.d("msg", "姓名：" + JSONUtil.getString(jo, "nickname"));

        CarPlayUtil.bindSexView(JSONUtil.getString(jo, "gender"), holder.sexLayout);
//        String gender = JSONUtil.getString(jo,"gender");
//        if ("男".equals(gender)) {
//            holder.sexLayout.setBackgroundResource(R.drawable.radio_sex_man_normal);
//            holder.iv_sexl.setImageResource(R.drawable.icon_man3x);
//        } else {
//            holder.sexLayout.setBackgroundResource(R.drawable.radion_sex_woman_normal);
//            holder.iv_sexl.setImageResource(R.drawable.icon_woman3x);
//        }
        holder.tv_ageT.setText(JSONUtil.getString(jo, "age"));

        ViewUtil.bindNetImage(holder.icon, JSONUtil.getString(jo, "avatar"), "head");
        String id = JSONUtil.getString(jo,"userId");
        holder.icon.setTag(id);
        int distance = JSONUtil.getInt(jo, "distance");
        holder.distanceT.setText(distance + "米");

        long time = JSONUtil.getLong(jo, "viewTime");
        holder.timeT.setText(CarPlayValueFix.converTime(time));
        return view;
    }

    class ViewHolder {
        TextView nameT, contentT, timeT, distanceT, tv_ageT;
        ImageView iv_sexl;
        RelativeLayout sexLayout;
        RoundImageView icon;
    }
}
