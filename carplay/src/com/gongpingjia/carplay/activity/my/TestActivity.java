package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.reclcleadapter.NetJSonRecycleAdapter;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.recyclerview.SwipyRefreshLayout;
import net.duohuo.dhroid.view.swipelayout.NetSwipeRefreshAndMoreLayout;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;

public class TestActivity extends CarPlayBaseActivity {
	RecyclerView mRecyclerView;

	NetSwipeRefreshAndMoreLayout mSwipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);

		mSwipeRefreshLayout = (NetSwipeRefreshAndMoreLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
		// mSwipeRefreshLayout.setRefreshing(true);
		User user = User.getInstance();

		String url = API.CWBaseurl + "/user/" + user.getUserId()
				+ "/listen?token=" + user.getToken();
		NetJSonRecycleAdapter adapter = new NetJSonRecycleAdapter(url, self,
				R.layout.itme_attention_person);

		View headView = LayoutInflater.from(self).inflate(R.layout.test_head,
				null);
		adapter.addheadView(headView);
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
		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(self));
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(adapter);
		mSwipeRefreshLayout.bind(adapter, mRecyclerView);
		adapter.showNext();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

}
