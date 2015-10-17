package com.gongpingjia.carplay.activity.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.MySubscriberAdapter;
import com.gongpingjia.carplay.api.API2;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;

/**
 * Created by Administrator on 2015/10/16.
 * 我关注的人
 */
public class MySubscriberFragment extends Fragment {

    private PullToRefreshListView mListView;
    private MySubscriberAdapter mySubscriberAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_follow_each_other, container, false);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                DhNet dhNet = new DhNet(API2.getSubscribe())
            }
        });
        return mListView;
    }
}
