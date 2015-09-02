package com.gongpingjia.carplay.adapter;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

public class MessageAdapter extends NetJSONAdapter {

	LayoutInflater mLayoutInflater;

	int mResource;

	/** 是否显示勾选按钮 */
	boolean showcheck = false;

	String type;

	public MessageAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mLayoutInflater = LayoutInflater.from(mContext);
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
		final ViewHolder holder;
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
			holder.countT = (TextView) convertView.findViewById(R.id.count);
			holder.carLogoI = (ImageView) convertView
					.findViewById(R.id.carlogo);
			holder.timeT = (TextView) convertView.findViewById(R.id.time);
			holder.content_lastT=(TextView) convertView.findViewById(R.id.content_last);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		JSONObject jo = (JSONObject) getItem(position);

		if (!TextUtils.isEmpty(JSONUtil.getString(jo, "carBrandLogo"))) {
			ViewUtil.bindNetImage(holder.carLogoI,
					JSONUtil.getString(jo, "carBrandLogo"), "carlogo");
			holder.carLogoI.setVisibility(View.VISIBLE);
		} else {
			holder.carLogoI.setVisibility(View.GONE);
		}

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
		String msgtype = JSONUtil.getString(jo, "type");
		if (type.equals("comment")) {
			holder.countT.setVisibility(View.GONE);
			holder.agreeT.setVisibility(View.GONE);
			holder.timeT.setVisibility(View.VISIBLE);
			ViewUtil.bindView(holder.timeT,
					JSONUtil.getString(jo, "createTime"), "neartime");
		} else {
			holder.timeT.setVisibility(View.GONE);
			String remarks = JSONUtil.getString(jo, "remarks");
			String seatcontent = "提供" + JSONUtil.getInt(jo, "seat") + "个空座";
			if (remarks.equals("") && msgtype.equals("活动申请处理")) {
				holder.agreeT.setVisibility(View.VISIBLE);
				holder.countT
						.setVisibility(JSONUtil.getInt(jo, "seat") > 0 ? View.VISIBLE
								: View.GONE);
				holder.agreeT.setText("同意");
				holder.agreeT.setTextColor(mContext.getResources().getColor(R.color.white));
				holder.agreeT
						.setBackgroundResource(R.drawable.button_yanzheng_bg);
				holder.agreeT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						JSONObject jo = (JSONObject) getItem(position);
						agree(JSONUtil.getString(jo, "applicationId"), jo,holder.agreeT);
					}
				});

				SpannableStringBuilder style = new SpannableStringBuilder(
						seatcontent);
				style.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.text_orange)), 2, seatcontent
						.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.countT.setText(style);

			} else if (remarks.equals("已同意") && msgtype.equals("活动申请处理")) {
				holder.agreeT.setVisibility(View.VISIBLE);
				holder.agreeT.setText(remarks);
				holder.agreeT.setTextColor((mContext.getResources().getColor(R.color.text_grey)));
				holder.agreeT.setBackgroundResource(R.drawable.button_grey_bg);
				
				holder.countT
						.setVisibility(JSONUtil.getInt(jo, "seat") > 0 ? View.VISIBLE
								: View.GONE);
				SpannableStringBuilder style = new SpannableStringBuilder(
						seatcontent);
				style.setSpan(new ForegroundColorSpan(mContext.getResources()
						.getColor(R.color.text_orange)), 2, seatcontent
						.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.countT.setText(style);
				
				holder.agreeT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});
			} else {
				holder.agreeT.setVisibility(View.GONE);
				holder.countT.setVisibility(View.GONE);
			}
		}

		String newcontent = null;
		String content = JSONUtil.getString(jo, "content");
		if (!msgtype.equals("留言")) {
			holder.content_lastT.setVisibility(View.VISIBLE);
			if (msgtype.equals("车主认证")) {
				newcontent = "您提交的" + content;
				holder.headI.setEnabled(false);
			} else if (msgtype.equals("活动邀请")) {
				newcontent = "邀请您加入" + content ;
				holder.content_lastT.setText("活动");
			} else if (msgtype.equals("活动申请结果")) {
				newcontent = "活动申请: " + content;
				holder.content_lastT.setText("已同意");
			} else if (msgtype.equals("活动申请处理")) {
				newcontent = "想加入" + content;
				holder.content_lastT.setText("活动");
			}
			int start = newcontent.indexOf(content);
			SpannableStringBuilder style = new SpannableStringBuilder(
					newcontent);
			style.setSpan(new ForegroundColorSpan(mContext.getResources()
					.getColor(R.color.text_blue_light)), start,
					start + content.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// style.setSpan(new ForegroundColorSpan(Color.RED), 0, start,
			// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.contentT.setText(style);
		} else {
			holder.content_lastT.setVisibility(View.GONE);
			ViewUtil.bindView(holder.contentT,
					JSONUtil.getString(jo, "content"));
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

	private void agree(String messageId, final JSONObject jo,final TextView v) {
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/application/" + messageId
				+ "/process?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.addParam("action", 1);
		net.doPostInDialog("加载中...", new NetTask(mContext) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					try {
						jo.put("remarks", "已同意");
						jo.put("type", "活动申请处理");
						notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// refreshDialog();
				}
			}
		});
	}

	class ViewHolder {

		TextView nameT, ageT, contentT;

		ImageView headI, carLogoI;

		ImageView checkI;

		View sexV;

		TextView agreeT;

		TextView countT, timeT,content_lastT;

	}
}
