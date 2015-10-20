package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.view.AnimButtonView2;
import com.gongpingjia.carplay.view.pop.LookAroundPop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/15.
 */
public class MateFragment extends CarPlayBaseFragment implements Runnable {

    AnimButtonView2 eatR;

    RelativeLayout vesselR;

    List<AnimButtonView2> list;

    View mainV;

    static MateFragment instance;


    public static MateFragment getInstance() {
        if (instance == null) {
            instance = new MateFragment();
        }

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_mate, null);
        initView();
        return mainV;
    }

    public void initView() {
        list = new ArrayList<AnimButtonView2>();

        vesselR = (RelativeLayout) mainV.findViewById(R.id.content_layout);

        for (int i = 1; i < vesselR.getChildCount(); i++) {
            AnimButtonView2 view = (AnimButtonView2) vesselR.getChildAt(i);
            list.add(view);
        }

        mainV.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LookAroundPop pop = LookAroundPop.getInstance(getActivity());
                pop.show();

//                Intent it = new Intent(getActivity(), LookAroundActivity.class);
//                startActivity(it);
//                getActivity().overridePendingTransition(R.anim.zoom_in,
//                        R.anim.zoom_out);
            }
        });
        new Thread(this).start();

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
