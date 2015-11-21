package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

/**
 * Created by Administrator on 2015/11/21.
 */
public class ReportPop implements  View.OnClickListener {

    Activity context;
    static ReportPop instance;
    View contentV;
    PopupWindow pop;
    TextView pornography,advertising,political,bilk,illegal,cancel,submit;
    public ReportPop(final Activity context) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.report, null);

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
    public static ReportPop getInstance(Activity context) {
        instance = new ReportPop(context);
        return instance;
    }
    public void initView(){
        contentV.findViewById(R.id.pop_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pornography = (TextView) contentV.findViewById(R.id.pornography);
        advertising = (TextView) contentV.findViewById(R.id.advertising);
        political = (TextView) contentV.findViewById(R.id.political);
        bilk = (TextView) contentV.findViewById(R.id.bilk);
        illegal = (TextView) contentV.findViewById(R.id.illegal);
        cancel = (TextView) contentV.findViewById(R.id.cancel);
        submit = (TextView) contentV.findViewById(R.id.submit);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        illegal.setOnClickListener(this);
        bilk.setOnClickListener(this);
        political.setOnClickListener(this);
        advertising.setOnClickListener(this);
        pornography.setOnClickListener(this);

    }
    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
    }
    @Override
    public void onClick(View view) {

    }


}
