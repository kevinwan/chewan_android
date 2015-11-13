package com.gongpingjia.carplay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.SuggestionPlace;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class SuggestionAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<SuggestionPlace> mDatas;

    public SuggestionAdapter(Context context, List<SuggestionPlace> data) {
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
    public SuggestionPlace getItem(int position) {
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
        SuggestionPlace sug = mDatas.get(position);

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_suggestion, null);
            holder.nameText = (TextView) convertView.findViewById(R.id.tv_suggestion_name);
            holder.detailText = (TextView) convertView.findViewById(R.id.tv_suggestion_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameText.setText(sug.getStyleName());
        holder.detailText.setText(sug.getDetails());
        return convertView;
    }

    class ViewHolder {
        TextView nameText;

        TextView detailText;
    }
}
