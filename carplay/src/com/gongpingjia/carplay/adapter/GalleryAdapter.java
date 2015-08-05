package com.gongpingjia.carplay.adapter;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;

public class GalleryAdapter extends BaseAdapter {

	LayoutInflater mLayoutInflater;

	Context context;

	String url;

	JSONArray jsa;

	public GalleryAdapter(Context context, JSONArray jsa) {
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		this.jsa = jsa;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	// @Override
	// public int getCount() {
	// if (jsa == null) {
	// return 0;
	// } else {
	// return jsa.length();
	//
	// }
	// }

	@Override
	public Object getItem(int position) {
		Object o = null;
		try {
			o = jsa.get(position % jsa.length());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.gallery_item_view,
					null);
		}
		JSONObject jo = (JSONObject) getItem(position);
		ViewUtil.bindNetImage((ImageView) convertView.findViewById(R.id.pic),
				JSONUtil.getString(jo, "thumbnail_pic"), "default");
		return convertView;
	}

}