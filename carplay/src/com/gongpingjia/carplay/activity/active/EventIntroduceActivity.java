package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
/***
 * 活动介绍
 * @author Administrator
 *
 */
public class EventIntroduceActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_introduce);
	}


	@Override
	public void initView() {
		setTitle("活动介绍");
	}
}
