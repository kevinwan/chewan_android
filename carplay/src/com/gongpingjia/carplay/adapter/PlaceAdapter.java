package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/20.
 */
public class PlaceAdapter extends BaseAdapter implements SectionIndexer {

    private JSONArray mDatum;
    private Context mContext;

    public PlaceAdapter(Context context, JSONArray data) {
        this.mDatum = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatum == null ? 0 : mDatum.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return mDatum.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.textPlace.setText(getItem(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView textPlace;
    }
}
