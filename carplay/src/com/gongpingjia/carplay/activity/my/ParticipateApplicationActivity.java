package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
				String strApplicationId = o.toString();

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
		adapter.showNextInDialog();
	}
}
