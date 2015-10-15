package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.view.AnimButtonView2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/15.
 */
public class MateActivity extends CarPlayBaseActivity implements Runnable{

    AnimButtonView2 eatR;

    RelativeLayout vesselR;

    List<AnimButtonView2> list;

    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate);
    }

    @Override
    public void initView() {
        list = new ArrayList<AnimButtonView2>();

        vesselR = (RelativeLayout) findViewById(R.id.vessel);

        for (int i=1;i<vesselR.getChildCount();i++){
            AnimButtonView2 view = (AnimButtonView2) vesselR.getChildAt(i);
            list.add(view);
        }

        new Thread(this).start();

//        eatR = (AnimButtonView2) findViewById(R.id.eat);
//        eatR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                eatR.startAnimation();
//            }
//        });
    }

    @Override
    public void run() {
        for (int i=0;true;i++){
            Message msg=new Message();
            msg.what=1;
            Bundle bd=new Bundle();
            bd.putInt("index",i%list.size());
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
                   int index= msg.getData().getInt("index");
                        list.get(index).startAnimation();

                    break;
            }
        }
    };
}
