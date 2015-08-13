package com.gongpingjia.carplay.activity.msg;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.view.RefreshAndMoreListView;
import net.duohuo.dhroid.view.RefreshAndMoreListView.OnRefreshListener;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.FragmentMsgAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

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

	//
	// BadgeView normalMsgBadgeT, applicationMsgBadgeT;
	//
	// TextView applicationmsg_contentT, normsg_contentT;
	//
	// View aaplication_layoutV, normsg_layoutV;
	private FragmentMsgAdapter mAdapter;

	static JSONObject dataJo;

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

		mRefreshListView = (RefreshAndMoreListView) view
				.findViewById(R.id.refreshLv_msg);
		mRefreshListView.setAdapter(mAdapter);
		mRefreshListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getMsgCount();
			}
		});

		if (dataJo != null) {
			mAdapter.setData(dataJo);
		}
		return view;
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
		mAdapter.setData(jo);
	}

	@Override
	public void onResume() {
		super.onResume();
		// getMsgCount();
	}

}
