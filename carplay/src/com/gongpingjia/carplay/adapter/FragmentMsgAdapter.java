package com.gongpingjia.carplay.adapter;

import java.util.List;

import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.util.SmileUtils;
import com.gongpingjia.carplay.view.BadgeView;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FragmentMsgAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private Context mContext;

	BadgeView normalMsgBadgeT, applicationMsgBadgeT;

	TextView applicationmsg_contentT, normsg_contentT, normalTimeT,
			applicationTimeT;

	View aaplication_layoutV, normsg_layoutV;

	JSONObject jo;

	List<EMConversation> conversationList;

	public FragmentMsgAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (conversationList == null) {
			return 0;
		} else {
			return conversationList.size();
		}
	}

	@Override
	public EMConversation getItem(int position) {
		return conversationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setGroupMessageData(List<EMConversation> conversationList) {
		this.conversationList = conversationList;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View view;
		// if (position == 0) {
		// view = mInflater.inflate(R.layout.information, null);
		// RelativeLayout leave_comments_layout = (RelativeLayout) view
		// .findViewById(R.id.leave_comments_layout);
		// leave_comments_layout
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent intent = new Intent(mContext,
		// NewMessageActivity.class);
		// intent.putExtra("type", "comment");
		// mContext.startActivity(intent);
		//
		// }
		// });
		// RelativeLayout apply_for_layout = (RelativeLayout) view
		// .findViewById(R.id.apply_for_layout);
		// apply_for_layout.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent intent = new Intent(mContext,
		// NewMessageActivity.class);
		// intent.putExtra("type", "application");
		// mContext.startActivity(intent);
		// }
		// });
		// int bgcolor = mContext.getResources().getColor(R.color.text_orange);
		// applicationmsg_contentT = (TextView) view
		// .findViewById(R.id.applicationmsg_content);
		// normsg_contentT = (TextView) view.findViewById(R.id.normsg_content);
		// normalMsgBadgeT = (BadgeView) view
		// .findViewById(R.id.normal_msg_cout);
		// normalMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		// applicationMsgBadgeT = (BadgeView) view
		// .findViewById(R.id.application_msg_cout);
		// applicationMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		//
		// normalTimeT = (TextView) view.findViewById(R.id.normal_time);
		// applicationTimeT = (TextView) view
		// .findViewById(R.id.applicationmsg_time);
		//
		// if (jo != null) {
		// JSONObject commentJo = JSONUtil.getJSONObject(jo, "comment");
		// normalMsgBadgeT.setVisibility(JSONUtil.getInt(commentJo,
		// "count") == 0 ? View.GONE : View.VISIBLE);
		// if (TextUtils.isEmpty(JSONUtil.getString(commentJo, "content"))) {
		// normsg_contentT.setText("暂无留言");
		// normalTimeT.setVisibility(View.GONE);
		// } else {
		// normsg_contentT.setText(JSONUtil.getString(commentJo,
		// "content"));
		// normalTimeT.setVisibility(View.VISIBLE);
		// ViewUtil.bindView(normalTimeT,
		// JSONUtil.getLong(commentJo, "createTime"), "time");
		//
		// // normalTimeT.setText(formatter.format(new
		// // Date(JSONUtil.getLong(commentJo, "createTime"))));
		// }
		// normalMsgBadgeT.setText(JSONUtil.getString(commentJo, "count"));
		//
		// JSONObject applicationJo = JSONUtil.getJSONObject(jo,
		// "application");
		// applicationMsgBadgeT
		// .setVisibility(JSONUtil.getInt(applicationJo, "count") == 0 ?
		// View.GONE
		// : View.VISIBLE);
		// if (TextUtils.isEmpty(JSONUtil.getString(applicationJo,
		// "content"))) {
		// applicationmsg_contentT.setText("暂无消息");
		// applicationTimeT.setVisibility(View.GONE);
		// } else {
		// applicationmsg_contentT.setText(JSONUtil.getString(
		// applicationJo, "content"));
		// applicationTimeT.setVisibility(View.VISIBLE);
		//
		// ViewUtil.bindView(applicationTimeT,
		// JSONUtil.getLong(applicationJo, "createTime"),
		// "time");
		// // applicationTimeT.setText(formatter.format(new
		// // Date(JSONUtil.getLong(applicationJo, "createTime"))));
		// }
		// applicationMsgBadgeT.setText(JSONUtil.getString(applicationJo,
		// "count"));
		//
		// }
		// } else {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_group_message, null);
		}
		EMConversation conversation = conversationList.get(position);
		String username = conversation.getUserName();
		EMGroup group = EMGroupManager.getInstance().getGroup(username);
		if(group!=null) {
			ViewUtil.bindView(convertView.findViewById(R.id.title),
					group.getGroupName());
		}

		convertView.findViewById(R.id.msg_point)
				.setVisibility(
						conversation.getUnreadMsgCount() > 0 ? View.VISIBLE
								: View.GONE);
		TextView contentT = (TextView) convertView.findViewById(R.id.content);
		TextView timeT = (TextView) convertView.findViewById(R.id.time);
		ImageView msg_stateI = (ImageView) convertView
				.findViewById(R.id.msg_state);
		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			contentT.setText(
					SmileUtils.getSmiledText(mContext,
							getMessageDigest(lastMessage, (mContext))),
					BufferType.SPANNABLE);
			timeT.setVisibility(View.VISIBLE);
			ViewUtil.bindView(timeT, lastMessage.getMsgTime(), "time");
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				msg_stateI.setVisibility(View.VISIBLE);
			} else {
				msg_stateI.setVisibility(View.GONE);
			}
		} else {
			contentT.setText("暂无消息");
			timeT.setVisibility(View.GONE);
		}
		// }

		return convertView;
	}

	// public void setData(JSONObject jo) {
	// this.jo = jo;
	// notifyDataSetChanged();
	// }

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
//				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture);
					;
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息

			if (((DemoHXSDKHelper) HXSDKHelper.getInstance())
					.isRobotMenuMessage(message)) {
				digest = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
						.getRobotMenuMessageDigest(message);
			} else if (message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:

			return "";
		}

		return digest;
	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

}
