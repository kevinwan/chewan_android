package com.gongpingjia.carplay.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SimplePageAdapter extends PagerAdapter {
	View[] views;

	public SimplePageAdapter(View... views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		View listV = null;
		listV = views[position];
		((ViewPager) collection).addView(listV);
		return listV;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}
}
