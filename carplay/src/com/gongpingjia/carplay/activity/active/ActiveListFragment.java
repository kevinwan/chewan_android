package com.gongpingjia.carplay.activity.active;

import java.text.SimpleDateFormat;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.INetRefreshAndMorelistView.OnRefreshListener;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.HotAdapter;
import com.gongpingjia.carplay.adapter.SimplePageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.ActiveCreateEB;
import com.gongpingjia.carplay.bean.ActiveEditEB;
import com.gongpingjia.carplay.bean.ActiveParmasEB;
import com.gongpingjia.carplay.bean.JoinEB;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;

import de.greenrobot.event.EventBus;

public class ActiveListFragment extends Fragment {
	View mainV;

	static ActiveListFragment instance;

	User user;

	DhNet net;

	LinearLayout topTab;

	ViewPager page;

	View HotV, NearV, NewV;

	LayoutInflater mLayoutInflater;

	NetRefreshAndMoreListView hotListV, nearListV, newListV;

	NetJSONAdapter newAdapter, nearAdapter;

	HotAdapter hotAdapter;

	View HeadV;

	public static ActiveListFragment getInstance() {
		if (instance == null) {
			instance = new ActiveListFragment();
		}

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		mainV = inflater.inflate(R.layout.activity_active_list_fragment, null);
		initView();
		return mainV;
	}

	private void initView() {
		mLayoutInflater = LayoutInflater.from(getActivity());
		topTab = (LinearLayout) mainV.findViewById(R.id.top_tab);
		initTopTab();
		setTopTab(0);
		user = User.getInstance();
		page = (ViewPager) mainV.findViewById(R.id.page);
		page.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				setTopTab(index);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		HeadV = mLayoutInflater.inflate(R.layout.item_hotlistview, null);

		HotV = mLayoutInflater.inflate(R.layout.include_refresh_listview, null);
		NearV = mLayoutInflater
				.inflate(R.layout.include_refresh_listview, null);
		NewV = mLayoutInflater.inflate(R.layout.include_refresh_listview, null);
		hotListV = (NetRefreshAndMoreListView) HotV.findViewById(R.id.listview);
		newListV = (NetRefreshAndMoreListView) NewV.findViewById(R.id.listview);
		nearListV = (NetRefreshAndMoreListView) NearV
				.findViewById(R.id.listview);
		hotListV.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				HotV.findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});

		hotListV.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getOfficalData();
			}
		});

		nearListV.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				NearV.findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});

		newListV.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				NewV.findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});

		hotListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = (JSONObject) hotAdapter.getItem(position - 1);
				Intent it = new Intent(getActivity(),
						ActiveDetailsActivity.class);
				it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
				startActivity(it);
			}
		});

		newListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = (JSONObject) newAdapter.getItem(position - 1);
				Intent it = new Intent(getActivity(),
						ActiveDetailsActivity.class);
				it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
				startActivity(it);
			}
		});

		nearListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = (JSONObject) nearAdapter.getItem(position - 1);
				Intent it = new Intent(getActivity(),
						ActiveDetailsActivity.class);
				it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
				startActivity(it);
			}
		});
		SimplePageAdapter adapter = new SimplePageAdapter(HotV, NearV, NewV);
		page.setAdapter(adapter);
		buildAdapter();
	}

	private void buildAdapter() {
		UserLocation location = UserLocation.getInstance();
		hotAdapter = new HotAdapter(API.activeList, getActivity(),
				R.layout.item_active_list);
		hotAdapter.addparam("key", "hot");
		hotAdapter.addparam("userId", user.getUserId());
		hotAdapter.addparam("token", user.getToken());
		hotAdapter.addparam("city", location.getCity());
		hotAdapter.addparam("district", "");
		hotAdapter.addparam("type", "");
		hotAdapter.addparam("gender", "");
		hotAdapter.addparam("authenticate", "");
		hotAdapter.addparam("carLevel", "");
		hotAdapter.fromWhat("data");

		hotListV.setAdapter(hotAdapter);
		hotAdapter.showNext();

		nearAdapter = new ActiveAdapter(API.activeList, getActivity(),
				R.layout.item_active_list);
		nearAdapter.addparam("key", "nearby");
		nearAdapter.addparam("userId", user.getUserId());
		nearAdapter.addparam("token", user.getToken());
		nearAdapter.addparam("city", location.getCity());
		nearAdapter.addparam("district", "");
		nearAdapter.addparam("type", "");
		nearAdapter.addparam("gender", "");
		nearAdapter.addparam("authenticate", "");
		nearAdapter.addparam("carLevel", "");
		nearAdapter.addparam("longitude", location.getLongitude());
		nearAdapter.addparam("latitude", location.getLatitude());
		nearAdapter.fromWhat("data");
		nearListV.setAdapter(nearAdapter);
		nearAdapter.showNext();

		newAdapter = new ActiveAdapter(API.activeList, getActivity(),
				R.layout.item_active_list);
		newAdapter.addparam("key", "latest");
		newAdapter.addparam("userId", user.getUserId());
		newAdapter.addparam("token", user.getToken());
		newAdapter.addparam("city", location.getCity());
		newAdapter.addparam("district", "");
		newAdapter.addparam("type", "");
		newAdapter.addparam("gender", "");
		newAdapter.addparam("authenticate", "");
		newAdapter.addparam("carLevel", "");
		newAdapter.fromWhat("data");
		newListV.setAdapter(newAdapter);
		newAdapter.showNext();

		getOfficalData();
	}

	private void initTopTab() {
		for (int i = 0; i < topTab.getChildCount(); i++) {
			final int index = i;
			View childV = topTab.getChildAt(i);
			childV.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setTopTab(index);
					page.setCurrentItem(index);
				}
			});
		}
	}

	private void setTopTab(int index) {
		for (int i = 0; i < topTab.getChildCount(); i++) {
			View childV = topTab.getChildAt(i);
			ImageView img = (ImageView) childV.findViewById(R.id.tabline);
			TextView text = (TextView) childV.findViewById(R.id.tab_text);
			if (index == i) {
				text.setTextColor(getResources().getColor(R.color.text_orange));
				img.setVisibility(View.VISIBLE);
				// switch (index)
				// {
				// case 0:
				// changeData("hot");
				// break;
				//
				// case 1:
				// changeData("nearby");
				// break;
				//
				// case 2:
				// changeData("latest");
				// break;
				//
				// default:
				// break;
				// }
			} else {
				text.setTextColor(getResources().getColor(R.color.text_black));
				img.setVisibility(View.GONE);
			}
		}
	}

	/** 接受ActiveFilterPop的选择事件 */
	public void onEventMainThread(ActiveParmasEB pa) {
		hotAdapter.addparam("city", pa.getCity());
		hotAdapter.addparam("district", pa.getDistrict());
		hotAdapter.addparam("type", pa.getActiveType());
		hotAdapter.addparam("gender", pa.getGender());
		if (pa.getAuthenticate() == 3) {
			hotAdapter.addparam("authenticate", "");
		} else {
			hotAdapter.addparam("authenticate", pa.getAuthenticate());
		}
		hotAdapter.addparam("carLevel", pa.getCarLevel());
		hotAdapter.refreshDialog();

		nearAdapter.addparam("city", pa.getCity());
		nearAdapter.addparam("district", pa.getDistrict());
		nearAdapter.addparam("type", pa.getActiveType());
		nearAdapter.addparam("gender", pa.getGender());
		if (pa.getAuthenticate() == 3) {
			nearAdapter.addparam("authenticate", "");
		} else {
			nearAdapter.addparam("authenticate", pa.getAuthenticate());
		}
		nearAdapter.addparam("carLevel", pa.getCarLevel());
		nearAdapter.refreshDialog();

		newAdapter.addparam("city", pa.getCity());
		newAdapter.addparam("district", pa.getDistrict());
		newAdapter.addparam("type", pa.getActiveType());
		newAdapter.addparam("gender", pa.getGender());
		if (pa.getAuthenticate() == 3) {
			newAdapter.addparam("authenticate", "");
		} else {
			newAdapter.addparam("authenticate", pa.getAuthenticate());
		}
		newAdapter.addparam("carLevel", pa.getCarLevel());
		newAdapter.refreshDialog();

	}

	public void onEventMainThread(LoginEB login) {
		if (login.islogin) {
			hotAdapter.addparam("userId", user.getUserId());
			hotAdapter.addparam("token", user.getToken());
			hotAdapter.refresh();

			nearAdapter.addparam("userId", user.getUserId());
			nearAdapter.addparam("token", user.getToken());
			nearAdapter.refresh();

			newAdapter.addparam("userId", user.getUserId());
			newAdapter.addparam("token", user.getToken());
			newAdapter.refresh();

		}
	}

	public void onEventMainThread(ActiveEditEB activeEditEB) {
		hotAdapter.refresh();
		nearAdapter.refresh();
		newAdapter.refresh();
	}

	public void onEventMainThread(ActiveCreateEB activeCreateEB) {
		nearAdapter.refresh();
		newAdapter.refresh();
	}

	private void getOfficalData() {
		DhNet net = new DhNet(API.official);
		net.doGetInDialog(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				JSONArray jo = response.jSONArrayFromData();
				if (jo != null && jo.length() > 0) {
					// bindViewPageAdapter(jo);
					hotAdapter.setJsa(jo);
				} else {

				}

			}
		});

	}

	private void bindViewPageAdapter(JSONArray jsa) {
		if (jsa != null && jsa.length() > 0) {
			View[] views = new View[jsa.length()];
			for (int i = 0; i < jsa.length(); i++) {
				final JSONObject jot;
				try {
					jot = jsa.getJSONObject(i);
					View leftV = mLayoutInflater.inflate(
							R.layout.item_hotview_viewpage, null);

					ImageView pghead = (ImageView) leftV
							.findViewById(R.id.head);
					ImageView pglogo = (ImageView) leftV
							.findViewById(R.id.logo);
					TextView pgtitle = (TextView) leftV
							.findViewById(R.id.title);
					TextView pgendtime = (TextView) leftV
							.findViewById(R.id.endtime);
					TextView pgcontent = (TextView) leftV
							.findViewById(R.id.content);

					SimpleDateFormat formatdate = new SimpleDateFormat(
							"yyyy.MM.dd");
					pgendtime.setText("截止时间:  "
							+ formatdate.format(JSONUtil.getLong(jot, "end")));
					pgtitle.setText(JSONUtil.getString(jot, "title"));
					pgcontent.setText(JSONUtil.getString(jot, "content"));
					ViewUtil.bindNetImage(pghead,
							JSONUtil.getString(jot, "cover"), "cover");
					ViewUtil.bindNetImage(pglogo,
							JSONUtil.getString(jot, "logo"), "logo");

					leftV.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(getActivity(),
									ActiveDetailsActivity.class);
							intent.putExtra("activityId",
									JSONUtil.getString(jot, "activityId"));
							startActivity(intent);
						}
					});
					views[i] = leftV;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			ViewPager page = (ViewPager) HeadV.findViewById(R.id.page);
			SimplePageAdapter middleAdapter = new SimplePageAdapter(views);
			page.setAdapter(middleAdapter);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		hotAdapter.stopTimer();
	}

}
