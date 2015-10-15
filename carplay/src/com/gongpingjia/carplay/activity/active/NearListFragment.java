package com.gongpingjia.carplay.activity.active;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.adapter.NearListAdapter;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewVertical;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NearListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager> {


    static NearListFragment instance;
    private RecyclerViewPager mRecyclerView;


    PullToRefreshRecyclerViewVertical listV;

    boolean isfirst;


    View mainV;

    public static NearListFragment getInstance() {
        if (instance == null) {
            instance = new NearListFragment();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_near_list, null);
        initView();
        return mainV;
    }


    private void initView() {

        listV = (PullToRefreshRecyclerViewVertical) mainV.findViewById(R.id.list);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        mRecyclerView
                = listV.getRefreshableView();
        mRecyclerView.setAdapter(new NearListAdapter(getActivity(), mRecyclerView));

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
