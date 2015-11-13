package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.Place;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20.
 */
public class PlaceAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<Place> mPlaces;

    public PlaceAdapter(Context context, List<Place> data) {
        this.mPlaces = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPlaces == null ? 0 : mPlaces.size();
    }

    @Override
    public Place getItem(int position) {
        return mPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dlg_place, parent, false);
            holder = new ViewHolder();
            holder.textPlace = (TextView) convertView.findViewById(R.id.tv_place);
            holder.textLetter = (TextView) convertView.findViewById(R.id.tv_letter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textPlace.setText(getItem(position).getName());
        //第一个或者是下一个和上一个不一致的情况下显示字母
        if (position == 0 || !getItem(position).getFirstLetter().equals(getItem(position - 1).getFirstLetter())) {
            holder.textLetter.setVisibility(View.VISIBLE);
            holder.textLetter.setText(getItem(position).getFirstLetter());
        } else {
            holder.textLetter.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (Place place : mPlaces) {
            if (place.getFirstLetter().charAt(0) == sectionIndex) {
                return mPlaces.indexOf(place);
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    class ViewHolder {
        TextView textPlace;
        TextView textLetter;
    }
}
