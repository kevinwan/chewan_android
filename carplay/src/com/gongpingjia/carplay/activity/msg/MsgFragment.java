package com.gongpingjia.carplay.activity.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.RefreshAndMoreListView;
import net.duohuo.dhroid.view.RefreshAndMoreListView.OnRefreshListener;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMError;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.adapter.FragmentMsgAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.BadgeView;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Description 消息
 * @author wang
 * @date 2015-7-17 下午2:37:30
 */
public class MsgFragment extends Fragment {

	static MsgFragment instance;

	private RefreshAndMoreListView mRefreshListView;

	private boolean hidden;;

	//
	// BadgeView normalMsgBadgeT, applicationMsgBadgeT;
	//
	// TextView applicationmsg_contentT, normsg_contentT;
	//
	// View aaplication_layoutV, normsg_layoutV;
	private FragmentMsgAdapter mAdapter;

	static JSONObject dataJo;

	BadgeView normalMsgBadgeT, applicationMsgBadgeT;

	TextView applicationmsg_contentT, normsg_contentT, normalTimeT,
			applicationTimeT;

	View aaplication_layoutV, normsg_layoutV;

	View headV;

	List<EMConversation> conversationList;

	public static MsgFragment getInstance(JSONObject data) {
		if (instance == null) {
			instance = new MsgFragment();
		}
		dataJo = data;
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		mAdapter = new FragmentMsgAdapter(getActivity());
		final View view = inflater.inflate(R.layout.fragment_msg, container,
				false);
		// RelativeLayout leave_comments_layout = (RelativeLayout)
		// view.findViewById(R.id.leave_comments_layout);
		headV = inflater.inflate(R.layout.information, null);
		mRefreshListView = (RefreshAndMoreListView) view
				.findViewById(R.id.refreshLv_msg);
		mRefreshListView.addHeaderView(headV);
		mRefreshListView.setAdapter(mAdapter);
		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position > 1) {
					EMConversation conversation = mAdapter.getItem(position
							- mRefreshListView.getHeaderViewsCount());
					String username = conversation.getUserName();
					EMGroup group = EMGroupManager.getInstance().getGroup(
							username);
					Intent it = new Intent(getActivity(), ChatActivity.class);
					it.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
					it.putExtra("groupId", group.getGroupId());
					it.putExtra("activityId",
							group.getDescription().replace("_", "-"));
					startActivity(it);
				}

			}
		});
		mRefreshListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getMsgCount();
				conversationList = loadConversationsWithRecentChat();
				mAdapter.setGroupMessageData(conversationList);
			}
		});

		if (dataJo != null) {
			bindHeadView(dataJo);
			// mAdapter.setData(dataJo);
		}

		return view;
	}

	private void bindHeadView(JSONObject jo) {
		RelativeLayout leave_comments_layout = (RelativeLayout) headV
				.findViewById(R.id.leave_comments_layout);
		leave_comments_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						NewMessageActivity.class);
				intent.putExtra("type", "comment");
				startActivity(intent);

			}
		});
		RelativeLayout apply_for_layout = (RelativeLayout) headV
				.findViewById(R.id.apply_for_layout);
		apply_for_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						NewMessageActivity.class);
				intent.putExtra("type", "application");
				startActivity(intent);
			}
		});
		int bgcolor = getActivity().getResources()
				.getColor(R.color.text_orange);
		applicationmsg_contentT = (TextView) headV
				.findViewById(R.id.applicationmsg_content);
		normsg_contentT = (TextView) headV.findViewById(R.id.normsg_content);
		normalMsgBadgeT = (BadgeView) headV.findViewById(R.id.normal_msg_cout);
		normalMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		applicationMsgBadgeT = (BadgeView) headV
				.findViewById(R.id.application_msg_cout);
		applicationMsgBadgeT.setBadgeBackgroundColor(bgcolor);

		normalTimeT = (TextView) headV.findViewById(R.id.normal_time);
		applicationTimeT = (TextView) headV
				.findViewById(R.id.applicationmsg_time);

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
				ViewUtil.bindView(normalTimeT,
						JSONUtil.getLong(commentJo, "createTime"), "neartime");

				// normalTimeT.setText(formatter.format(new
				// Date(JSONUtil.getLong(commentJo, "createTime"))));
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

				ViewUtil.bindView(applicationTimeT,
						JSONUtil.getLong(applicationJo, "createTime"), "neartime");
				// applicationTimeT.setText(formatter.format(new
				// Date(JSONUtil.getLong(applicationJo, "createTime"))));
			}
			applicationMsgBadgeT.setText(JSONUtil.getString(applicationJo,
					"count"));
		}
	}

	private void getMsgCount() {
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/message/count?token=" + user.getToken());
		net.doGet(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					mRefreshListView.onRefreshComplete();
					mRefreshListView.removeFootView();
					JSONObject jo = response.jSONFromData();
					EventBus.getDefault().post(jo);
				}
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(JSONObject jo) {
		bindHeadView(jo);
		// mAdapter.setData(jo);
	}

	public void onEventMainThread(EMMessage message) {
		System.out.println("2222223");
		conversationList = loadConversationsWithRecentChat();
		mAdapter.setGroupMessageData(conversationList);
		// mAdapter.setData(jo);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			getMsgCount();
			// conversationList = loadConversationsWithRecentChat();
			// mAdapter.setGroupMessageData(conversationList);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden && !((MainActivity) getActivity()).isConflict) {

			getMsgCount();
			conversationList = loadConversationsWithRecentChat();
			mAdapter.setGroupMessageData(conversationList);
		}
	}

	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}

		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}

}
