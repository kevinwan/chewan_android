package com.gongpingjia.carplay.activity.my;

import android.view.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;


/**
 * 我的页面
 * +* @author Administrator
 */
public class MyFragment2 extends Fragment {
    View mainV;

    static MyFragment2 instance;

    public static MyFragment2 getInstance() {
        if (instance == null) {
            instance = new MyFragment2();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mainV = inflater.inflate(R.layout.fragment_my2, null);
        initView();
        return mainV;
    }

    private void initView() {


    }
}