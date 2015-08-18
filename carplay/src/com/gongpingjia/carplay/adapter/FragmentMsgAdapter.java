package com.gongpingjia.carplay.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.duohuo.dhroid.net.JSONUtil;

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
import com.gongpingjia.carplay.view.BadgeView;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FragmentMsgAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private Context mContext;

	BadgeView normalMsgBadgeT, applicationMsgBadgeT;

	TextView applicationmsg_contentT, normsg_contentT,normalTimeT,applicationTimeT;

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
		
		normalTimeT=(TextView) view.findViewById(R.id.normal_time);
		applicationTimeT=(TextView) view.findViewById(R.id.applicationmsg_time);

		if (jo != null) {
			JSONObject commentJo = JSONUtil.getJSONObject(jo, "comment");
			normalMsgBadgeT
					.setVisibility(JSONUtil.getInt(commentJo, "count") == 0 ? View.GONE
							: View.VISIBLE);
			if (TextUtils.isEmpty(JSONUtil.getString(commentJo, "content"))) {
				normsg_contentT.setText("暂无留言");
				normalTimeT.setVisibility(View.GONE);
			} else {
				normsg_contentT.setText(JSONUtil
						.getString(commentJo, "content"));
				normalTimeT.setVisibility(View.VISIBLE);
				normalTimeT.setText(setTime(JSONUtil.getLong(commentJo, "createTime")));
//				normalTimeT.setText(formatter.format(new Date(JSONUtil.getLong(commentJo, "createTime"))));
			}
			normalMsgBadgeT.setText(JSONUtil.getString(commentJo, "count"));

			JSONObject applicationJo = JSONUtil
					.getJSONObject(jo, "application");
			applicationMsgBadgeT.setVisibility(JSONUtil.getInt(applicationJo,
					"count") == 0 ? View.GONE : View.VISIBLE);
			if (TextUtils.isEmpty(JSONUtil.getString(applicationJo, "content"))) {
				applicationmsg_contentT.setText("暂无消息");
				applicationTimeT.setVisibility(View.GONE);
			} else {
				applicationmsg_contentT.setText(JSONUtil.getString(
						applicationJo, "content"));
				applicationTimeT.setVisibility(View.VISIBLE);
				applicationTimeT.setText(setTime(JSONUtil.getLong(applicationJo, "createTime")));
//				applicationTimeT.setText(formatter.format(new Date(JSONUtil.getLong(applicationJo, "createTime"))));
			}
			applicationMsgBadgeT.setText(JSONUtil.getString(applicationJo,
					"count"));

		}

		return view;
	}
	
	private String setTime(long time){
		SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
		long now=System.currentTimeMillis();
		String strtime="";
		if(now-time<=60*1000)
		{
			return "刚刚";
		}
		if(now-time>60*1000&&now-time<60*60*1000)
		{
			strtime=(int)Math.floor((now-time)/60000)+"分前";
			return strtime;
		}
		if(now-time>=60*60*1000&&now-time<24*60*60*1000){
			strtime=formattime.format(new Date(time));
			return strtime;
		}
		if (now-time>=24*60*60*1000&&now-time<2*24*60*60*1000) {
			return "昨天";
		}
		if (now-time>=2*24*60*60*1000) {
			strtime=formatdate.format(new Date(time));
		}
		
		return strtime;
	}

	public void setData(JSONObject jo) {
		this.jo = jo;
		notifyDataSetChanged();
	}

}
