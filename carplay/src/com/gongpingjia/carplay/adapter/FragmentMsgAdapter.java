package com.gongpingjia.carplay.adapter;

import java.text.SimpleDateFormat;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.msg.NewMessageActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.BadgeView;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FragmentMsgAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private Context mContext;

	BadgeView normalMsgBadgeT, applicationMsgBadgeT;

	TextView applicationmsg_contentT, normsg_contentT,normal_timeT,application_timeT;

	View aaplication_layoutV, normsg_layoutV;

	JSONObject jo;

	public FragmentMsgAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.information, null);
		RelativeLayout leave_comments_layout = (RelativeLayout) view
				.findViewById(R.id.leave_comments_layout);
		leave_comments_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, NewMessageActivity.class);
				intent.putExtra("type", "comment");
				mContext.startActivity(intent);

			}
		});
		RelativeLayout apply_for_layout = (RelativeLayout) view
				.findViewById(R.id.apply_for_layout);
		apply_for_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, NewMessageActivity.class);
				intent.putExtra("type", "application");
				mContext.startActivity(intent);
			}
		});
		int bgcolor = mContext.getResources().getColor(R.color.text_orange);
		applicationmsg_contentT = (TextView) view
				.findViewById(R.id.applicationmsg_content);
		normsg_contentT = (TextView) view.findViewById(R.id.normsg_content);
		normalMsgBadgeT = (BadgeView) view.findViewById(R.id.normal_msg_cout);
		normalMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		applicationMsgBadgeT = (BadgeView) view
				.findViewById(R.id.application_msg_cout);
		applicationMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		
		normal_timeT=(TextView) view.findViewById(R.id.normal_time);
		application_timeT=(TextView) view.findViewById(R.id.application_time);

		if (jo != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
	        
			JSONObject commentJo = JSONUtil.getJSONObject(jo, "comment");
			normalMsgBadgeT
					.setVisibility(JSONUtil.getInt(commentJo, "count") == 0 ? View.GONE
							: View.VISIBLE);
			if (TextUtils.isEmpty(JSONUtil.getString(commentJo, "content"))) {
				normsg_contentT.setText("暂时还没有新消息");
				normal_timeT.setText("");
			} else {
				normsg_contentT.setText(JSONUtil
						.getString(commentJo, "content"));
				normal_timeT.setText(""+formatter.format(System.currentTimeMillis()));
			}
			normalMsgBadgeT.setText(JSONUtil.getString(commentJo, "count"));

			JSONObject applicationJo = JSONUtil
					.getJSONObject(jo, "application");
			applicationMsgBadgeT.setVisibility(JSONUtil.getInt(applicationJo,
					"count") == 0 ? View.GONE : View.VISIBLE);
			if (TextUtils.isEmpty(JSONUtil.getString(applicationJo, "content"))) {
				applicationmsg_contentT.setText("暂时还没有新消息");
				application_timeT.setText("");
			} else {
				applicationmsg_contentT.setText(JSONUtil.getString(
						applicationJo, "content"));
				application_timeT.setText(""+formatter.format(System.currentTimeMillis()));
			}
			applicationMsgBadgeT.setText(JSONUtil.getString(applicationJo,
					"count"));
		
			
			
		}

		return view;
	}

	public void setData(JSONObject jo) {
		this.jo = jo;
		notifyDataSetChanged();
	}

}
