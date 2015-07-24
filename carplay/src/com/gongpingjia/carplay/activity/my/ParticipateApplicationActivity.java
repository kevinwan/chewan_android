package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.adapter.BeanAdapter;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.bean.User;

/**
 * 活动参与申请
 * 
 * @author wang
 * 
 */
public class ParticipateApplicationActivity extends CarPlayBaseActivity {
	NetJSONAdapter adapter;

	NetRefreshAndMoreListView listView;
	RelativeLayout participate_sex;
	private String strApplicationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participate_application);
	}

	@Override
	public void initView() {
		setTitle("活动参与申请");

		User user = User.getInstance();
		listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		adapter = new NetJSONAdapter(
				"http://cwapi.gongpingjia.com/v1/user/846de312-306c-4916-91c1-a5e69b158014/application/list?token=750dd49c-6129-4a9a-9558-27fa74fc4ce7",
				self, R.layout.itme_participate_application);
		adapter.fromWhat("data");
		adapter.addField("nickname", R.id.participate_name);
		adapter.addField("age", R.id.participate_age);
		adapter.addField("photo", R.id.head_img);
		adapter.addField("carBrandLogo", R.id.car_brand);

		adapter.addField(new FieldMap("gender", R.id.gender_txt) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				participate_sex = (RelativeLayout) itemV
						.findViewById(R.id.participate_sex);
				if (o.equals("男")) {
					participate_sex.setBackgroundResource(R.drawable.man);
				} else {
					participate_sex.setBackgroundResource(R.drawable.woman);
				}
				return null;
			}
		});
		adapter.addField(new FieldMap("applicationId", R.id.applicationId_txt) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				strApplicationId = o.toString();

				return null;
			}
		});
		adapter.addField(new FieldMap("title", R.id.participate_txt) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return "想加入" + o + "活动";
			}
		});

		adapter.addField(new FieldMap("seat", R.id.zuoweishu) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return "提供" + o + "座位";
			}
		});

		listView.setAdapter(adapter);
//		adapter.setOnInViewClickListener(R.id.button_agree,
//				new BeanAdapter.InViewClickListener() {
//
//					@Override
//					public void OnClickListener(View parentV, View v,
//							Integer position, Object values) {
//						final TextView textView = (TextView) v
//								.findViewById(R.id.button_agree);
//						DhNet net = new DhNet(
//								"http://cwapi.gongpingjia.com/v1/application/"
//										+ strApplicationId
//										+ "/process?userId=846de312-306c-4916-91c1-a5e69b158014&token=750dd49c-6129-4a9a-9558-27fa74fc4ce7");
//						net.addParam("action", 1);
//						net.doPost(new NetTask(self) {
//
//							@Override
//							public void doInUI(Response response,
//									Integer transfer) {
//								if (response.isSuccess()) {
//									textView.setText("已同意");
//									adapter.refresh();
//								} else {
//									showToast(response.result);
//								}
//
//							}
//						});
//					}
//				});
		adapter.showNextInDialog();
	}
}
