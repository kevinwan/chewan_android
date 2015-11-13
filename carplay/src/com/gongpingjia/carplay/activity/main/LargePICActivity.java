package com.gongpingjia.carplay.activity.main;

import org.json.JSONArray;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.R.menu;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.BigImageAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.util.PicLayoutUtil;
import com.gongpingjia.carplay.view.CarPlayGallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class LargePICActivity extends CarPlayBaseActivity {
	CarPlayGallery gallery;
	TextView pictitle;
	int galleryCount = 0;
	JSONArray jsa;
	View back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_large_pic);
	}

	@Override
	public void initView() {
		galleryCount = getIntent().getExtras().getInt("index");
		pictitle = (TextView) findViewById(R.id.pictitle);
		gallery = (CarPlayGallery) findViewById(R.id.gallery);
		jsa = PicLayoutUtil.picjsa;
		if (jsa != null) {
			BigImageAdapter adapter = new BigImageAdapter(self, jsa);
			gallery.setAdapter(adapter);
			gallery.setSelection(galleryCount);
		}

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				int p = position % jsa.length() + 1;
				pictitle.setText(p + "/" + jsa.length());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				finish();
			}
		});
		// back = findViewById(R.id.back);
		// back.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// finish();
		// }
		// });
	}

}
