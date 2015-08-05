package com.gongpingjia.carplay.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.Message;
import com.gongpingjia.carplay.bean.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.CachePolicy;
import net.duohuo.dhroid.util.ViewUtil;

public class MessageAdapter extends NetJSONAdapter {

	LayoutInflater mLayoutInflater;

	int mResource;

	/** 是否显示勾选按钮 */
	boolean showcheck = false;

	String type;

	public MessageAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mLayoutInflater = LayoutInflater.from(context);
		this.mResource = mResource;
	}

	public MessageAdapter(String api, Context context, int mResource,
			String type) {
		super(api, context, mResource);
		mLayoutInflater = LayoutInflater.from(context);
		this.mResource = mResource;
		this.type = type;
	}

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
			holder.agreeT = (TextView) convertView.findViewById(R.id.agree);
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

		holder.checkI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject jo = (JSONObject) getItem(position);
				try {
					jo.put("ischeck", !jo.getBoolean("ischeck"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				notifyDataSetChanged();
			}
		});
		holder.checkI.setVisibility(showcheck ? View.VISIBLE : View.GONE);

		if (type.equals("comment")) {
			holder.agreeT.setVisibility(View.GONE);
		} else {
			String remarks = JSONUtil.getString(jo, "remarks");
			String type = JSONUtil.getString(jo, "type");
			if (remarks.equals("") && type.equals("活动申请处理")) {
				holder.agreeT.setVisibility(View.VISIBLE);
				holder.agreeT.setText("同意");
				holder.agreeT
						.setBackgroundResource(R.drawable.button_yanzheng_bg);
				holder.agreeT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						JSONObject jo = (JSONObject) getItem(position);
						agree(JSONUtil.getString(jo, "applicationId"));
					}
				});
			} else if(remarks.equals("已同意") && type.equals("活动申请处理")){
				holder.agreeT.setVisibility(View.VISIBLE);
				holder.agreeT.setText(remarks);
				holder.agreeT.setBackgroundResource(R.drawable.button_grey_bg);
			} else {
				holder.agreeT.setVisibility(View.GONE);
			}
		}

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

	public List<String> getCheckMessage() {
		List<String> msgList = new ArrayList<String>();
		for (int i = 0; i < mVaules.size(); i++) {
			JSONObject msg = (JSONObject) getItem(i);
			if (JSONUtil.getBoolean(msg, "ischeck")) {
				msgList.add(JSONUtil.getString(msg, "messageId"));
			}
		}

		return msgList;
	}

	/**
	 * 清楚选择的数据
	 */
	public void checkAll(boolean check) {
		for (int i = 0; i < mVaules.size(); i++) {
			JSONObject jo = mVaules.get(i);
			try {
				jo.put("ischeck", check);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	private void agree(String messageId) {
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/application/" + messageId
				+ "/process?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.addParam("action", 1);
		net.doPostInDialog("发布评论中...", new NetTask(mContext) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					refreshDialog();
				}
			}
		});
	}

	class ViewHolder {

		TextView nameT, ageT, contentT;

		ImageView headI;

		ImageView checkI;

		View sexV;

		TextView agreeT;
	}
}
