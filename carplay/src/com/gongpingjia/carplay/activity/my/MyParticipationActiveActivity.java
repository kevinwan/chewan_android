package com.gongpingjia.carplay.activity.my;

import org.json.JSONObject;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

public class MyParticipationActiveActivity extends CarPlayBaseActivity {

	NetRefreshAndMoreListView listV;

	ActiveAdapter adapter;

	String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myrelated_active);
	}

	@Override
	public void initView() {
		setTitle("我的参与");
		User user = User.getInstance();
		listV = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		listV.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				ViewUtil.bindView(findViewById(R.id.icon_msg),
						R.drawable.no_join);
				ViewUtil.bindView(findViewById(R.id.msg), "您还没有参与任何活动,赶紧参与吧!");
				findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});
		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = (JSONObject) adapter.getItem(position - 1);
				Intent it = new Intent(self, ActiveDetailsActivity.class);
				it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
				startActivity(it);
			}
		});
		adapter = new ActiveAdapter(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/join?userId=" + user.getUserId() + "&token="
				+ user.getToken(), self, R.layout.item_active_list);

		adapter.fromWhat("data");
		listV.setAdapter(adapter);
		adapter.showNext();
	}

}
