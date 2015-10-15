package com.gongpingjia.carplay.activity.active;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.RecommendListAdapter;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewHorizontal;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/12.
 */
public class RecommendListFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager> {


    static RecommendListFragment instance;
    private RecyclerViewPager recyclerView;

    PullToRefreshRecyclerViewHorizontal listV;

    LinearLayoutManager layout;
    View mainV;


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
        recyclerView = listV.getRefreshableView();
        recyclerView.setAdapter(new RecommendListAdapter(getActivity(), recyclerView));

    }

    @Override
    public void onRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {
        new GetDataTask().execute();
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listV.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

}
