package com.gongpingjia.carplay.activity.msg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.RefreshAndMoreListView;
import net.duohuo.dhroid.view.RefreshAndMoreListView.OnRefreshListener;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.MessageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.Message;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.db.DaoHelper;
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

	RefreshAndMoreListView listView;

	MessageAdapter mJsonAdapter;

	/** 判断是系统消息还是系统消息 */
	String type;

	TextView leftTitleT, rightTitleT;

	Button delB;

	Dao<Message, Integer> msgDao;

	DaoHelper daoHelper;

	View emptyV;

	List<Message> dataList;
	ImageView leftTitler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
	}

	@Override
	public void initView() {
		type = getIntent().getStringExtra("type");
		if (type.equals("comment")) {
			setTitle("新的留言");
		} else {
			setTitle("系统消息");
		}
		setLeftAction(-2, null, null);
		leftTitleT = (TextView) findViewById(R.id.left_text);
		leftTitler = (ImageView) findViewById(R.id.back);
		leftTitler.setVisibility(View.VISIBLE);
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
			}
		});
		rightTitleT = (TextView) findViewById(R.id.right_text);
		rightTitleT.setVisibility(View.VISIBLE);
		rightTitleT.setText("编辑");
		rightTitleT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rightTitleT.getText().toString().equals("编辑")) {
					leftTitleT.setVisibility(View.VISIBLE);
					leftTitler.setVisibility(View.GONE);
					leftTitleT.setText("全选");
					rightTitleT.setText("取消");
					mJsonAdapter.showCheck(true);
					if (dataList != null && dataList.size() != 0) {
						delB.setVisibility(View.VISIBLE);
					}
				} else {
					mJsonAdapter.showCheck(false);
					leftTitleT.setVisibility(View.GONE);
					leftTitleT.setText("全选");
					rightTitleT.setText("编辑");
					leftTitler.setVisibility(View.VISIBLE);
					delB.setVisibility(View.GONE);
					mJsonAdapter.cleanCheck();
				}
			}
		});

		delB = (Button) findViewById(R.id.del);
		delB.setOnClickListener(this);
		emptyV = findViewById(R.id.empty);
		listView = (RefreshAndMoreListView) findViewById(R.id.listview);
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getData();
			}
		});
		// String url = API.CWBaseurl + "/user/" + user.getUserId()
		// + "/message/list?token=" + user.getToken() + "&type=" + type;
		mJsonAdapter = new MessageAdapter(self, R.layout.item_message_list);
		// mJsonAdapter.fromWhat("data");
		listView.setAdapter(mJsonAdapter);
		getData();
		daoHelper = IocContainer.getShare().get(DaoHelper.class);
		OrmLiteSqliteOpenHelper ormLitedaoHelper = IocContainer.getShare().get(
				OrmLiteSqliteOpenHelper.class);
		try {
			msgDao = ormLitedaoHelper.getDao(Message.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// mJsonAdapter.showNextInDialog();
	}

	private void getData() {
		User user = User.getInstance();
		String url = API.CWBaseurl + "/user/" + user.getUserId()
				+ "/message/list?token=" + user.getToken() + "&type=" + type;
		DhNet net = new DhNet(url);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					listView.onRefreshComplete();
					listView.removeFootView();
					List<Message> MessageList = response.listFrom(
							Message.class, "data");
					ArrayList<ContentValues> list = new ArrayList<ContentValues>();
					for (Message msg : MessageList) {
						ContentValues content = new ContentValues();
						content.put("userId", msg.getUserId());
						content.put("photo", msg.getPhoto());
						content.put("nickname", msg.getNickname());
						content.put("content", msg.getContent());
						content.put("age", msg.getAge());
						content.put("gender", msg.getGender());
						list.add(content);
					}

					if (MessageList != null && MessageList.size() > 0) {
						daoHelper.insertAll("message", list);
					}

					try {
						dataList = msgDao.queryForAll();
						if (dataList == null || dataList.size() == 0) {
							emptyV.setVisibility(View.VISIBLE);
						} else {
							mJsonAdapter.clear();
							mJsonAdapter.addAll(dataList);
							emptyV.setVisibility(View.GONE);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.del:
			del();
			break;

		default:
			break;
		}
	}

	private void del() {
		List<Message> list = mJsonAdapter.getCheckMessage();
		if (list.size() == 0) {
			showToast("请选择需要删除的消息!");
			return;
		}
		try {
			msgDao.delete(list);
			dataList.removeAll(list);
			mJsonAdapter.clear();
			mJsonAdapter.addAll(dataList);
			mJsonAdapter.showCheck(false);
			leftTitleT.setVisibility(View.GONE);
			leftTitleT.setText("全选");
			rightTitleT.setText("编辑");
			delB.setVisibility(View.GONE);
			showToast("删除成功!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
