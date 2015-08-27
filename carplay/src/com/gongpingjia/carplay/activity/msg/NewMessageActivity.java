package com.gongpingjia.carplay.activity.msg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;
import net.duohuo.dhroid.view.RefreshAndMoreListView;
import net.duohuo.dhroid.view.RefreshAndMoreListView.OnRefreshListener;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity;
import com.gongpingjia.carplay.activity.my.AttestationNotifyActivity;
import com.gongpingjia.carplay.adapter.MessageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.Message;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.db.DaoHelper;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

/***
 * 新的留言页面 hqh
 * 
 * @author Administrator
 * 
 */
public class NewMessageActivity extends CarPlayBaseActivity implements
		OnClickListener {

	NetRefreshAndMoreListView listView;

	MessageAdapter mJsonAdapter;

	/** 判断是系统消息还是系统消息 */
	String type;

	TextView leftTitleT, rightTitleT;

	Button delB;

	Dao<Message, Integer> msgDao;

	DaoHelper daoHelper;

	List<Message> dataList;

	CarPlayPerference per;

	ImageView backI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
	}

	@Override
	public void initView() {
		type = getIntent().getStringExtra("type");
		per = IocContainer.getShare().get(CarPlayPerference.class);
		per.load();
		if (per.isShowMessageGuilde == 0) {
			findViewById(R.id.guide).setVisibility(View.VISIBLE);
		}

		findViewById(R.id.know).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				per.load();
				per.isShowMessageGuilde = 1;
				per.commit();
				findViewById(R.id.guide).setVisibility(View.GONE);
			}
		});
		backI = (ImageView) findViewById(R.id.back);
		if (type.equals("comment")) {
			setTitle("新的留言");
		} else {
			setTitle("活动消息");
		}
		leftTitleT = (TextView) findViewById(R.id.left_text);
		leftTitleT.setText("全选");
		leftTitleT.setPadding(DhUtil.dip2px(self, 12), 0, 0, 0);
		leftTitleT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (leftTitleT.getText().toString().equals("全选")) {
					mJsonAdapter.checkAll(true);
					leftTitleT.setText("取消全选");
				} else {
					leftTitleT.setText("全选");
					mJsonAdapter.checkAll(false);
				}
				mJsonAdapter.notifyDataSetChanged();
			}
		});
		rightTitleT = (TextView) findViewById(R.id.right_text);
		rightTitleT.setText("取消");
		rightTitleT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mJsonAdapter.showCheck(false);
				leftTitleT.setText("全选");
				rightTitleT.setVisibility(View.GONE);
				delB.setVisibility(View.GONE);
				mJsonAdapter.cleanCheck();
				backI.setVisibility(View.VISIBLE);
				mJsonAdapter.showCheck(false);
				leftTitleT.setVisibility(View.GONE);
			}
		});

		delB = (Button) findViewById(R.id.del);
		delB.setOnClickListener(this);
		listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				mJsonAdapter.showCheck(true);
				rightTitleT.setVisibility(View.VISIBLE);
				leftTitleT.setVisibility(View.VISIBLE);
				delB.setVisibility(View.VISIBLE);
				backI.setVisibility(View.GONE);
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = null;
				JSONObject jo = (JSONObject) mJsonAdapter.getItem(position - 1);
				if (type.equals("comment")) {

					it = new Intent(self, ActiveDetailsActivity.class);
					it.putExtra("activityId",
							JSONUtil.getString(jo, "activityId"));
					startActivity(it);
				} else {
					String activityId = JSONUtil.getString(jo, "activityId");
					String type = JSONUtil.getString(jo, "type").trim();
					if (!TextUtils.isEmpty(activityId)) {

						it = new Intent(self, ActiveDetailsActivity.class);
						it.putExtra("activityId", activityId);
						startActivity(it);

					}
					if (type.equals("车主认证")) {
						String carModel = JSONUtil.getString(jo, "carModel");
						String content = JSONUtil.getString(jo, "content");
						String rs = "";
						if (JSONUtil.getString(jo, "remarks").equals("")) {
							rs = "0";
						} else {
							rs = "1";
						}
						it = new Intent(self, AttestationNotifyActivity.class);
						it.putExtra("carModel", carModel);
						it.putExtra("result", rs);
						it.putExtra("content", content);
						startActivity(it);
					}
				}
			}
		});

		listView.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});
		User user = User.getInstance();
		String url = API.CWBaseurl + "/user/" + user.getUserId()
				+ "/message/list?token=" + user.getToken() + "&type=" + type;
		mJsonAdapter = new MessageAdapter(url, self,
				R.layout.item_message_list, type);
		mJsonAdapter.fromWhat("data");
		listView.setAdapter(mJsonAdapter);
		// getData();
		mJsonAdapter.showNextInDialog();
	}

	// private void getData() {
	// User user = User.getInstance();
	// String url = API.CWBaseurl + "/user/" + user.getUserId()
	// + "/message/list?token=" + user.getToken() + "&type=" + type;
	// DhNet net = new DhNet(url);
	// net.doGetInDialog(new NetTask(self) {
	//
	// @Override
	// public void doInUI(Response response, Integer transfer) {
	// if (response.isSuccess()) {
	// listView.onRefreshComplete();
	// listView.removeFootView();
	// List<Message> MessageList = response.listFrom(
	// Message.class, "data");
	//
	// try {
	// if (MessageList == null || MessageList.size() == 0) {
	// emptyV.setVisibility(View.VISIBLE);
	// } else {
	// mJsonAdapter.clear();
	// mJsonAdapter.addAll(MessageList);
	// emptyV.setVisibility(View.GONE);
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// }
	// });
	//
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.del:
			deleteMessage();
			break;

		default:
			break;
		}
	}

	private void deleteMessage() {
		List<String> delList = mJsonAdapter.getCheckMessage();
		if (delList.size() == 0) {
			showToast("请选择需要删除的消息!");
			return;
		}
		JSONArray jsa = new JSONArray(delList);
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/message/remove?userId="
				+ user.getUserId() + "&token=" + user.getToken());
		net.addParam("messages", jsa);
		net.doPostInDialog("删除中...", new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("删除成功!");
					leftTitleT.setVisibility(View.GONE);
					leftTitleT.setText("全选");
					backI.setVisibility(View.VISIBLE);
					delB.setVisibility(View.GONE);
					mJsonAdapter.showCheck(false);
					rightTitleT.setVisibility(View.GONE);
					mJsonAdapter.refresh();
				}
			}
		});
	}

	// private void del() {
	// List<Message> list = mJsonAdapter.getCheckMessage();
	// if (list.size() == 0) {
	// showToast("请选择需要删除的消息!");
	// return;
	// }
	// try {
	// msgDao.delete(list);
	// dataList.removeAll(list);
	// mJsonAdapter.clear();
	// mJsonAdapter.addAll(dataList);
	// mJsonAdapter.showCheck(false);
	// leftTitleT.setVisibility(View.GONE);
	// leftTitleT.setText("全选");
	// rightTitleT.setText("编辑");
	// delB.setVisibility(View.GONE);
	// showToast("删除成功!");
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

}
