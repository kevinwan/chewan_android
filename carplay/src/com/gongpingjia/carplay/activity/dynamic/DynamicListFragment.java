package com.gongpingjia.carplay.activity.dynamic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/10/13.
 */
public class DynamicListFragment extends Fragment {

    static DynamicListFragment instance;

    public static DynamicListFragment getInstance() {
        if (instance == null) {
            instance = new DynamicListFragment();
        }

        return instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
