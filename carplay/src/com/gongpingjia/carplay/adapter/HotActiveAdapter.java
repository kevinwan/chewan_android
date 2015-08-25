package com.gongpingjia.carplay.adapter;

import java.text.SimpleDateFormat;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotActiveAdapter extends BaseAdapter {

	LayoutInflater mLayoutInflater;

	Context context;

	String url;

	JSONArray jsa;

	public HotActiveAdapter(Context context, JSONArray jsa) {
		this.context = context;
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
	public JSONObject getItem(int position) {

		JSONObject o = null;
		if (jsa != null && jsa.length() != 0) {
			try {
				o = (JSONObject) jsa.get(position % jsa.length());
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
			convertView = mLayoutInflater.inflate(
					R.layout.item_hotview_viewpage, null);
		}
		final JSONObject jo = (JSONObject) getItem(position);
		ImageView pghead = (ImageView) convertView.findViewById(R.id.head);
		ImageView pglogo = (ImageView) convertView.findViewById(R.id.logo);
		TextView pgtitle = (TextView) convertView.findViewById(R.id.title);
		TextView pgendtime = (TextView) convertView.findViewById(R.id.endtime);
		TextView pgcontent = (TextView) convertView.findViewById(R.id.content);

		SimpleDateFormat formatdate = new SimpleDateFormat("yyyy.MM.dd");
		pgendtime.setText("截止时间:  "
				+ formatdate.format(JSONUtil.getLong(jo, "end")));
		pgtitle.setText(JSONUtil.getString(jo, "title"));
		pgcontent.setText(JSONUtil.getString(jo, "content"));
		ViewUtil.bindNetImage(pghead, JSONUtil.getString(jo, "cover"), "cover");
		ViewUtil.bindNetImage(pglogo, JSONUtil.getString(jo, "logo"), "logo");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ActiveDetailsActivity.class);
				intent.putExtra("activityId",
						JSONUtil.getString(jo, "activityId"));
				context.startActivity(intent);
			}
		});

		return convertView;
	}

}
