package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.AnimButtonView2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class MatePop implements Runnable {
    Activity context;

    View contentV;

    PopupWindow pop;

    static MatePop instance;

    AnimButtonView2 eatR;

    RelativeLayout vesselR;

    List<AnimButtonView2> list;


    public MatePop(Activity context) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.activity_mate, null);
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

    public static MatePop getInstance(Activity context) {
        instance = new MatePop(context);
        return instance;

    }

    private void initView() {
        list = new ArrayList<AnimButtonView2>();

        vesselR = (RelativeLayout) contentV.findViewById(R.id.content_layout);

        for (int i = 1; i < vesselR.getChildCount(); i++) {
            AnimButtonView2 view = (AnimButtonView2) vesselR.getChildAt(i);
            list.add(view);
        }

        contentV.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LookAroundPop pop = LookAroundPop.getInstance(context);
                pop.show();

//                Intent it = new Intent(getActivity(), LookAroundActivity.class);
//                startActivity(it);
//                getActivity().overridePendingTransition(R.anim.zoom_in,
//                        R.anim.zoom_out);
            }
        });
        new Thread(this).start();
    }

    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
        // if (addresssT != null) {
        // addresssT.setText("不限");
        // }
    }

    @Override
    public void run() {
        for (int i = 0; true; i++) {
            Message msg = new Message();
            msg.what = 1;
            Bundle bd = new Bundle();
            bd.putInt("index", i % list.size());
            msg.setData(bd);
            mHandler.sendMessage(msg);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int index = msg.getData().getInt("index");
                    list.get(index).startAnimation();

                    break;
            }
        }
    };


}
