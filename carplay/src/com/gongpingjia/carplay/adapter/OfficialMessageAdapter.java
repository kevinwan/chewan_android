package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20.
 */
public class OfficialMessageAdapter extends BaseAdapter {

    private static final int COUNT = 5;

    private final Context mContext;

    private List<JSONObject> data;

    public OfficialMessageAdapter(Context context) {
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
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data)
        {
            item = data.get(position);
        }
        return item;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_official_message_list2, null);
            holder.headI = (RoundImageView) convertView.findViewById(R.id.head);
            holder.nicknameT = (TextView) convertView.findViewById(R.id.nickname);
            holder.contentT = (TextView) convertView.findViewById(R.id.content);
            holder.timeT = (TextView) convertView.findViewById(R.id.time);
            holder.messageT = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);
        ViewUtil.bindNetImage(holder.headI, JSONUtil.getString(jo, "avatar"), "head");
        ViewUtil.bindView(holder.nicknameT, JSONUtil.getString(jo, "nickname"));

        String status = JSONUtil.getString(jo, "status");
        if ("认证通过".equals(status)){
            holder.contentT.setVisibility(View.GONE);
            ViewUtil.bindView(holder.messageT, JSONUtil.getString(jo, "type")+"审核通过");
        }else {
            holder.contentT.setVisibility(View.VISIBLE);
            ViewUtil.bindView(holder.contentT,"原因:  " + JSONUtil.getString(jo, "content"));
            ViewUtil.bindView(holder.messageT, JSONUtil.getString(jo, "type")+"审核未通过");
        }

        String time=CarPlayValueFix.converTime((long)JSONUtil.getInt(jo, "authTime"));
        ViewUtil.bindView(holder.timeT,time);

        return convertView;
    }

    class ViewHolder{
        RoundImageView headI;
        TextView nicknameT,messageT,contentT,timeT;
    }
}
