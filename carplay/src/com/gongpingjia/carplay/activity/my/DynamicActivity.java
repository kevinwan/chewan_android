package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.DynamicActivityAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewHorizontal;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/19.
 * 活动动态
 */
public class DynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager>, ILoadSuccess {

    private RecyclerViewPager recyclerView;

    PullToRefreshRecyclerViewHorizontal listV;

    User user  = User.getInstance();
    DynamicActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicactivity);

    }


    @Override
    public void initView() {
        listV = (PullToRefreshRecyclerViewHorizontal)findViewById(R.id.list);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        recyclerView = listV.getRefreshableView();
         adapter = new DynamicActivityAdapter(self);
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
//        setUrl(API2.CWBaseurl+"/user/"+ user.getUserId()+"/appointment/list?token="+ user.getToken());
        setUrl(API2.CWBaseurl+"user/5609eb2c0cf224e7d878f693/appointment/list?token=67666666-f2ff-456d-a9cc-e83761749a6a");
        getData();
    }

    private void getData(){
        showNext();
    }
    @Override
    public void onRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {
        refresh();
    }
    @Override
    public void loadSuccess(){
        adapter.setData(mVaules);
        listV.onRefreshComplete();
    }
    @Override
    public void loadSuccessOnFirst(){

    }
}
