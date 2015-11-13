package com.gongpingjia.carplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/14.
 */
public class MyView extends RecyclerViewPager {

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    float lastDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_MOVE == ev.getAction()) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDownY = event.getY();
//                parentRequestDisallowInterceptTouchEvent(true); // 保证事件可往下传递
            case MotionEvent.ACTION_MOVE:
                Log.d("msg", "执行");
                boolean isTop = event.getY() - lastDownY < 0 && this.getScrollY() == 0;
                if (isTop) { // 允许父控件拦截，即不允许父控件拦截设为false
                    Log.d("msg", "执行parentRequestDisallowInterceptTouchEvent");
                    parentRequestDisallowInterceptTouchEvent(false);
                } else { // 不允许父控件拦截，即不允许父控件拦截设为true
                    parentRequestDisallowInterceptTouchEvent(true);
                    Log.d("msg", "执行true");
                }
            case MotionEvent.ACTION_UP:
                parentRequestDisallowInterceptTouchEvent(false); // 保证事件可往下传递
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void parentRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent vp = this.getParent();
        if (null == vp) {
            return;
        }
        vp.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
