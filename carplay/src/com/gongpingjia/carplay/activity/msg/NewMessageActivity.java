package com.gongpingjia.carplay.activity.msg;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.MessageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

/***
 * 新的留言页面 hqh
 * 
 * @author Administrator
 * 
 */
public class NewMessageActivity extends CarPlayBaseActivity {

	private NetRefreshAndMoreListView listView = null;

	MessageAdapter mJsonAdapter;

	/** 判断是系统消息还是系统消息 */
	String type;

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
		listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		User user = User.getInstance();
		String url = API.CWBaseurl + "/user/" + user.getUserId()
				+ "/message/list?token=" + user.getToken() + "&type=" + type;
		mJsonAdapter = new MessageAdapter(url, self, R.layout.item_message_list);
		mJsonAdapter.fromWhat("data");
		listView.setAdapter(mJsonAdapter);
		mJsonAdapter.showNextInDialog();
	}
}
