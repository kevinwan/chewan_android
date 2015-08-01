package com.gongpingjia.carplay.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.cache.CachePolicy;
import net.duohuo.dhroid.util.ViewUtil;

public class MessageAdapter extends NetJSONAdapter {
	LayoutInflater mLayoutInflater;

	int mResource;

	/** 是否显示勾选按钮 */
	boolean showcheck = false;

	public MessageAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mLayoutInflater = LayoutInflater.from(context);
		this.mResource = mResource;
		useCache(CachePolicy.POLICY_NOCACHE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.nameT = (TextView) convertView.findViewById(R.id.name);
			holder.contentT = (TextView) convertView.findViewById(R.id.content);
			holder.ageT = (TextView) convertView.findViewById(R.id.age);
			holder.checkI = (ImageView) convertView.findViewById(R.id.check);
			holder.headI = (ImageView) convertView.findViewById(R.id.head);
			holder.sexV = convertView.findViewById(R.id.sex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		JSONObject jo = (JSONObject) getItem(position);
		holder.checkI.setVisibility(showcheck ? View.VISIBLE : View.GONE);
		Boolean ischeck = JSONUtil.getBoolean(jo, "ischeck");
		if (ischeck) {
			holder.checkI.setImageResource(R.drawable.img_check_f);
		} else {
			holder.checkI.setImageResource(R.drawable.img_check_n);
		}

		holder.checkI.setVisibility(showcheck ? View.VISIBLE : View.GONE);

		if (JSONUtil.getString(jo, "gender").equals("男")) {
			holder.sexV.setBackgroundResource(R.drawable.man);
		} else {
			holder.sexV.setBackgroundResource(R.drawable.woman);
		}
		ViewUtil.bindNetImage(holder.headI, JSONUtil.getString(jo, "photo"),
				"head");
		holder.headI.setTag(JSONUtil.getString(jo, "userId"));
		ViewUtil.bindView(holder.ageT, JSONUtil.getString(jo, "age"));
		ViewUtil.bindView(holder.nameT, JSONUtil.getString(jo, "nickname"));
		ViewUtil.bindView(holder.contentT, JSONUtil.getString(jo, "content"));

		return convertView;
	}

	public void showCheck(boolean showcheck) {
		this.showcheck = showcheck;
		cleanCheck();
		notifyDataSetChanged();
	}

	/**
	 * 清楚选择的数据
	 */
	public void cleanCheck() {
		for (int i = 0; i < mVaules.size(); i++) {
			JSONObject jo = mVaules.get(i);
			try {
				jo.put("ischeck", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ViewHolder {

		TextView nameT, ageT, contentT;

		ImageView headI;

		ImageView checkI;

		View sexV;
	}
}
