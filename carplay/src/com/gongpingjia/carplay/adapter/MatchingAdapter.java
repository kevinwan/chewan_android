package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.Matching;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MatchingAdapter extends BaseAdapter {

    private List<Matching> mDatas;
    private Context mContext;

    public MatchingAdapter(Context context, List<Matching> data) {
        mDatas = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_matching, parent, false);
        final CheckBox radioButton = (CheckBox) view.findViewById(R.id.radio_matching);
        TextView textView = (TextView) view.findViewById(R.id.tv_matching_content);
        textView.setText(mDatas.get(position).getName());

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatas.get(position).setIsChecked(isChecked);
                radioButton.setBackgroundResource(isChecked ? R.drawable.radio_bubble_checked : R.drawable.radio_bubble_normal);
            }
        });
        return view;
    }
}
