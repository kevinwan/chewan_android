package com.gongpingjia.carplay.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPage extends ViewPager {

	public MyViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (MotionEvent.ACTION_UP == event.getAction()) {
			getParent().requestDisallowInterceptTouchEvent(false);
		} else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		return super.onTouchEvent(event);
	}

}
