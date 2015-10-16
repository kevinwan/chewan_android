package com.gongpingjia.carplay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gongpingjia.carplay.R;

import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;

public class BigImageAdapter extends BaseAdapter {

	LayoutInflater mLayoutInflater;

	Activity mcontext;

	String url;

	JSONArray jsa;

	ImageView img;

	public BigImageAdapter(Context context, JSONArray jsa) {
		this.mcontext = (Activity) context;
		mLayoutInflater = LayoutInflater.from(context);
		this.jsa = jsa;
	}

	@Override
	public int getCount() {

		if (jsa == null || jsa.length() == 0) {
			return 0;
		}
		if (jsa.length() == 1) {
			return 1;
		}
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
		if (jsa != null && jsa.length() != 0) {
			try {
				o = jsa.get(position % jsa.length());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			convertView = mLayoutInflater
					.inflate(R.layout.item_big_image, null);
		}
		img = (ImageView) convertView.findViewById(R.id.pic);
		try {
			ViewUtil.bindNetImage((ImageView) convertView.findViewById(R.id.pic),
					(String) jsa.get(position), "big_pic");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;
	}

}
