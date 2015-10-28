package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.InterestedPersonAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 感兴趣的
 */
public class InterestedPersonActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess {

    private ListView mRecyclerView;

    PullToRefreshListView listV;

    InterestedPersonAdapter adapter;
    LinearLayout empty;
    TextView msg;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_person);
    }

    @Override
    public void initView() {

        user = User.getInstance();
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);
        setTitle("感兴趣的");
        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);

        mRecyclerView = listV.getRefreshableView();
//        mRecyclerView.setEmptyView(findViewById(R.id.));
        adapter = new InterestedPersonAdapter(self);
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.CWBaseurl + "user/" + user.getUserId() + "/interest/list?token=" + user.getToken());
//        setUrl(API2.CWBaseurl + "user/" + "561f755b0cf24f3b99211d12" + "/interest/list?token=" + "ea9f3e5e-d52c-4be3-9d87-962be9bbb83c");
        showNext();

    }

    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.postDelayed(new Runnable() {
            @Override
            public void run() {
                listV.onRefreshComplete();
            }
        }, 500);


    }


    @Override
    public void loadSuccessOnFirst() {
        empty.setVisibility(View.VISIBLE);
        msg.setText("此处暂无活动");
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        showNext();
    }
}
