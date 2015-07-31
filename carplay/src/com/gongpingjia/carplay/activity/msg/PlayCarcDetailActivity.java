package com.gongpingjia.carplay.activity.msg;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.R.menu;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PlayCarcDetailActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_carc_detail);
		Intent intent = getIntent();
		String content = intent.getStringExtra("detail");
		TextView textView = (TextView) findViewById(R.id.detail_txt);

		textView.setText(content);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

}
