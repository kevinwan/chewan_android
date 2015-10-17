package com.gongpingjia.carplay.activity.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.duohuo.dhroid.adapter.PSAdapter;

/**
 * Created by Administrator on 2015/10/13.
 */
public class DynamicListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager>, ILoadSuccess {

    static DynamicListFragment instance;

    View mainV;

    ListView listV;

    PSAdapter adapter;

    View headV;

    public static DynamicListFragment getInstance() {
        if (instance == null) {
            instance = new DynamicListFragment();
        }

        return instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.fragment_dynamiclist, null);
        initView();
        return mainV;
    }

    private void initView() {
        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) mainV.findViewById(R.id.listview);
        headV = LayoutInflater.from(getActivity()).inflate(R.layout.head_dynamic, null);
        listV = pullToRefreshListView.getRefreshableView();
        listV.addHeaderView(headV);
        adapter = new PSAdapter(getActivity(), R.layout.item_group_message2);
        listV.setAdapter(adapter);
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadSuccessOnFirst() {

    }

    @Override
    public void onRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {

    }
}
