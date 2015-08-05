package com.gongpingjia.carplay.activity.msg;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.my.ParticipateApplicationActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.BadgeView;

/**
 * 
 * @Description 消息
 * @author wang
 * @date 2015-7-17 下午2:37:30
 */
public class MsgFragment extends Fragment {

	static MsgFragment instance;

	BadgeView normalMsgBadgeT, applicationMsgBadgeT;

	TextView applicationmsg_contentT, normsg_contentT;

	View aaplication_layoutV, normsg_layoutV;

	Timer mTimer;


	public static MsgFragment getInstance() {
		if (instance == null) {
			instance = new MsgFragment();
		}

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.information, container,
				false);
		RelativeLayout leave_comments_layout = (RelativeLayout) view
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
		RelativeLayout apply_for_layout = (RelativeLayout) view
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
		applicationmsg_contentT = (TextView) view
				.findViewById(R.id.applicationmsg_content);
		normsg_contentT = (TextView) view.findViewById(R.id.normsg_content);
		normalMsgBadgeT = (BadgeView) view.findViewById(R.id.normal_msg_cout);
		normalMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		applicationMsgBadgeT = (BadgeView) view
				.findViewById(R.id.application_msg_cout);
		applicationMsgBadgeT.setBadgeBackgroundColor(bgcolor);
		// aaplication_layoutV = view.findViewById(R.id.applicationmsg_layout);
		// normsg_layoutV = view.findViewById(R.id.normsg_layout);

		// normalMsgBadgeT = new BadgeView(getActivity(), normsg_contentT);//
		// 创建一个BadgeView对象，view为你需要显示提醒信息的控件
		// normalMsgBadgeT.setBadgePosition(BadgeView.POSITION_RIGHT_CENTER);//
		// 显示的位置.中间，还有其他位置属性
		// normalMsgBadgeT.setTextColor(Color.WHITE); // 文本颜色
		// normalMsgBadgeT.setBadgeBackgroundColor(getResources().getColor(R.color.text_red));
		// // 背景颜色
		// normalMsgBadgeT.setTextSize(getResources().getDimension(R.dimen.text_small));
		//
		// applicationMsgBadgeT = new BadgeView(getActivity(),
		// applicationmsg_contentT);//
		// 创建一个BadgeView对象，view为你需要显示提醒信息的控件
		// applicationMsgBadgeT.setBadgePosition(BadgeView.POSITION_RIGHT_CENTER);//
		// 显示的位置.中间，还有其他位置属性
		// applicationMsgBadgeT.setTextColor(Color.WHITE); // 文本颜色
		// applicationMsgBadgeT.setBadgeBackgroundColor(getResources().getColor(R.color.text_red));
		// // 背景颜色
		// applicationMsgBadgeT.setTextSize(getResources().getDimension(R.dimen.text_small));
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				getMsgCount();

			}
		}, 0, 30 * 60 * 1000);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mTimer != null) {
			mTimer.cancel();
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
					JSONObject jo = response.jSONFromData();
					JSONObject commentJo = JSONUtil
							.getJSONObject(jo, "comment");
					normalMsgBadgeT.setVisibility(JSONUtil.getInt(commentJo,
							"count") == 0 ? View.GONE : View.VISIBLE);
					normsg_contentT.setText(JSONUtil.getString(commentJo,
							"content"));
					normalMsgBadgeT.setText(JSONUtil.getString(commentJo,
							"count"));

					JSONObject applicationJo = JSONUtil.getJSONObject(jo,
							"application");
					applicationMsgBadgeT.setVisibility(JSONUtil.getInt(
							applicationJo, "count") == 0 ? View.GONE
							: View.VISIBLE);
					applicationmsg_contentT.setText(JSONUtil.getString(
							applicationJo, "content"));
					applicationMsgBadgeT.setText(JSONUtil.getString(
							applicationJo, "count"));
				}
			}
		});
	}
}
