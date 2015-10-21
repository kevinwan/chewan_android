package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gongpingjia.carplay.R;

/**
 * Created by Administrator on 2015/10/20.
 */
public class LookAroundPop {

    Activity context;

    View contentV;

    PopupWindow pop;

    static LookAroundPop instance;

    public LookAroundPop(Activity context) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.activity_lookaround, null);
        pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupLookAround);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();

    }

    public static LookAroundPop getInstance(Activity context) {
        instance = new LookAroundPop(context);
        return instance;

    }

    private void initView() {

    }

    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
        // if (addresssT != null) {
        // addresssT.setText("不限");
        // }
    }


}
