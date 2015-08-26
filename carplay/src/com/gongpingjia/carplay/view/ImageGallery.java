package com.gongpingjia.carplay.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.view.gallery.BasePagerAdapter.OnItemChangeListener;
import com.gongpingjia.carplay.view.gallery.GalleryViewPager;
import com.gongpingjia.carplay.view.gallery.UrlPagerAdapter;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageGallery extends CarPlayBaseActivity {

	private GalleryViewPager mViewPager;

	private TextView mIndicatorText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imagegallery);

		mIndicatorText = (TextView) findViewById(R.id.tv_large_pic_title);
		Bundle bundle = getIntent().getExtras();

		final String[] urls = bundle.getStringArray("imgurls");
		List<String> items = new ArrayList<String>();
		Collections.addAll(items, urls);

		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {
				mIndicatorText.setText(getIndicatorString(currentPosition,
						urls.length));
			}
		});
		Intent it = getIntent();
		mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(it.getIntExtra("currentItem", 0));
		mIndicatorText.setText(getIndicatorString(
				it.getIntExtra("currentItem", 0) + 1, urls.length));
	}

	@Override
	public void initView() {

	}

	private String getIndicatorString(int index, int total) {
		return (index + 1) + "/" + total;
	}

}
