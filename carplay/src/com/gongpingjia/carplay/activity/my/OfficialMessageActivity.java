package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.ListView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.OfficialMessageAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 官方通知
 */
public class OfficialMessageActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess {
    private ListView mRecyclerView;

    PullToRefreshListView listV;

    OfficialMessageAdapter adapter;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_message);
    }

    @Override
    public void initView() {

        user = User.getInstance();

        setTitle("车玩官方");

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);

        mRecyclerView = listV.getRefreshableView();
//        mRecyclerView.setEmptyView(findViewById(R.id.));
        adapter = new OfficialMessageAdapter(self);
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.CWBaseurl+"user/"+user.getUserId()+"/authentication/history?token="+user.getToken());
        showNext();
    }

    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.onRefreshComplete();
    }

    @Override
    public void loadSuccessOnFirst() {

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }
}
