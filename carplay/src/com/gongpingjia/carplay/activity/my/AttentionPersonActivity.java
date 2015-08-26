package com.gongpingjia.carplay.activity.my;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.BeanAdapter.InViewClickListener;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;

/**
 * 
 * @Description 我关注的人
 * @author wang
 * @date 2015-7-17 上午11:09:27
 */

public class AttentionPersonActivity extends CarPlayBaseActivity {
	NetJSONAdapter adapter;

	NetRefreshAndMoreListView listView;

	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attention_person);

	}

	@Override
	public void initView() {
		setTitle("我关注的人");
		user = User.getInstance();
		listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		String url = API.CWBaseurl + "/user/" + user.getUserId()
				+ "/listen?token=" + user.getToken();
		adapter = new NetJSONAdapter(url, self, R.layout.itme_attention_person);
		adapter.fromWhat("data");
		adapter.addField("age", R.id.age);
		adapter.addField(new FieldMap("nickname", R.id.name) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				JSONObject data = (JSONObject) jo;
				RoundImageView headI = (RoundImageView) itemV
						.findViewById(R.id.head);
				ViewUtil.bindNetImage(headI, JSONUtil.getString(data, "photo"),
						"head");
				headI.setTag(JSONUtil.getString(data, "userId"));
				View sexBg = itemV.findViewById(R.id.layout_sex);
				CarPlayUtil.bindSexView(JSONUtil.getString(data, "gender"),
						sexBg);

				ImageView carimg = (ImageView) itemV.findViewById(R.id.car_img);
				TextView model = (TextView) itemV.findViewById(R.id.car_model);
				TextView age = (TextView) itemV.findViewById(R.id.car_age);

				if (TextUtils.isEmpty(JSONUtil.getString(data, "carModel"))) {
					carimg.setVisibility(View.GONE);
					ViewUtil.bindView(model, "带我飞~");
				} else {
					carimg.setVisibility(View.VISIBLE);
					model.setText(JSONUtil.getString(data, "carModel"));
					age.setText(","
							+ JSONUtil.getString(data, "drivingExperience")
							+ "年驾龄");
					ViewUtil.bindNetImage((ImageView) carimg,
							JSONUtil.getString(data, "carBrandLogo"), "carlogo");
				}

				// CarPlayUtil.bindDriveAge(data,
				// (ImageView) itemV.findViewById(R.id.car_img),
				// (TextView) itemV.findViewById(R.id.car_age));
				return o;
			}

		});

		adapter.setOnInViewClickListener(R.id.person_cancel,
				new InViewClickListener() {

					@Override
					public void OnClickListener(View parentV, View v,
							Integer position, Object values) {
						JSONObject jo = (JSONObject) adapter.getItem(position);
						cancleAttention(JSONUtil.getString(jo, "userId"));
					}
				});

		listView.setAdapter(adapter);
		listView.setOnEmptyDataListener(new OnEmptyDataListener() {

			@Override
			public void onEmpty(boolean showeEptyView) {
				ViewUtil.bindView(findViewById(R.id.icon_msg),
						R.drawable.icon_no_attention);
				ViewUtil.bindView(findViewById(R.id.msg), "您还没有关注任何人!");
				findViewById(R.id.empty).setVisibility(
						showeEptyView ? View.VISIBLE : View.GONE);
			}
		});

		adapter.showNextInDialog();
	}

	/** 取消关注人 */
	private void cancleAttention(String userid) {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/unlisten?&token=" + user.getToken());
		net.addParam("targetUserId", userid);
		net.doPost(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("取消关注成功!");
					adapter.refreshDialog();
				}
			}
		});
	}
}
