package com.gongpingjia.carplay.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.cache.CachePolicy;
import net.duohuo.dhroid.util.ViewUtil;

public class MessageAdapter extends PSAdapter {
	public MessageAdapter(Context context, int mResource) {
		super(context, mResource);
		mLayoutInflater = LayoutInflater.from(context);
		this.mResource = mResource;
	}

	LayoutInflater mLayoutInflater;

	int mResource;

	/** 是否显示勾选按钮 */
	boolean showcheck = false;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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

		Message msg = (Message) getItem(position);
		holder.checkI.setVisibility(showcheck ? View.VISIBLE : View.GONE);
		Boolean ischeck = msg.getIscheck();
		if (ischeck) {
			holder.checkI.setImageResource(R.drawable.img_check_f);
		} else {
			holder.checkI.setImageResource(R.drawable.img_check_n);
		}

		holder.checkI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = (Message) getItem(position);
				msg.setIscheck(!msg.getIscheck());
				notifyDataSetChanged();
			}
		});
		holder.checkI.setVisibility(showcheck ? View.VISIBLE : View.GONE);

		if (msg.getGender().equals("男")) {
			holder.sexV.setBackgroundResource(R.drawable.man);
		} else {
			holder.sexV.setBackgroundResource(R.drawable.woman);
		}
		ViewUtil.bindNetImage(holder.headI, msg.getPhoto(), "head");
		holder.headI.setTag(msg.getUserId());
		ViewUtil.bindView(holder.ageT, msg.getAge());
		ViewUtil.bindView(holder.nameT, msg.getNickname());
		ViewUtil.bindView(holder.contentT, msg.getContent());

		return convertView;
	}

	public void showCheck(boolean showcheck) {
		this.showcheck = showcheck;
		cleanCheck();
		notifyDataSetChanged();
	}

	public List<Message> getCheckMessage() {

		List<Message> msgList = new ArrayList<Message>();
		for (int i = 0; i < mVaules.size(); i++) {
			Message msg = (Message) mVaules.get(i);
			if (msg.getIscheck()) {
				msgList.add(msg);
			}
		}

		return msgList;
	}

	public void checkAll(boolean check) {
		for (int i = 0; i < mVaules.size(); i++) {
			Message msg = (Message) mVaules.get(i);
			msg.setIscheck(check);
		}
		notifyDataSetChanged();
	}

	/**
	 * 清楚选择的数据
	 */
	public void cleanCheck() {
		for (int i = 0; i < mVaules.size(); i++) {
			Message msg = (Message) mVaules.get(i);
			msg.setIscheck(false);
		}
	}

	class ViewHolder {

		TextView nameT, ageT, contentT;

		ImageView headI;

		ImageView checkI;

		View sexV;
	}
}
