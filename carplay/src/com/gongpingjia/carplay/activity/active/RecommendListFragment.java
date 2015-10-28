package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.adapter.RecommendListAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewHorizontal;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/12.
 */
public class RecommendListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager>, ILoadSuccess {


    static RecommendListFragment instance;
    private RecyclerViewPager recyclerView;
    TextView msg;
    PullToRefreshRecyclerViewHorizontal listV;

    LinearLayoutManager layout;
    View mainV;

    RecommendListAdapter adapter;

    LinearLayout empty;
    public static RecommendListFragment getInstance() {
        if (instance == null) {
            instance = new RecommendListFragment();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.fragment_recommend, null);
        initView();
        return mainV;
    }


    private void initView() {
        listV = (PullToRefreshRecyclerViewHorizontal) mainV.findViewById(R.id.list);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
         empty = (LinearLayout) mainV.findViewById(R.id.empty);
        msg = (TextView) mainV. findViewById(R.id.msg);
        recyclerView = listV.getRefreshableView();
        adapter = new RecommendListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.recommendList);
        addParams("province", "");
        addParams("city", "北京");
        getData();

    }

    private void getData() {
        showNext();
    }

    @Override
    public void onRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {
        refresh();
    }


    @Override
    public void loadSuccess() {

        adapter.setData(mVaules);
        listV.onRefreshComplete();

    }

    @Override
    public void loadSuccessOnFirst() {
        if (mVaules.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            msg.setText("此处暂无消息");
        } else {
            empty.setVisibility(View.GONE);
        }
    }
}
