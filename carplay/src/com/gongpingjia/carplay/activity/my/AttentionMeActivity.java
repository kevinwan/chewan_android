package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.AttentionMeAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 谁关注我
 */
public class AttentionMeActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess {

    private ListView mRecyclerView;
    LinearLayout empty;
    TextView msg;
    PullToRefreshListView listV;

    AttentionMeAdapter adapter;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_me);
    }

    @Override
    public void initView() {
        setTitle("谁关注我");
        user = User.getInstance();

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);

        mRecyclerView = listV.getRefreshableView();
//        mRecyclerView.setEmptyView(findViewById(R.id.));
        adapter = new AttentionMeAdapter(self);
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.CWBaseurl+"user/"+user.getUserId()+"/subscribe/history?token="+user.getToken());
//        addParams("token", user.getToken());
//        addParams("type", "吃饭");
//        addParams("pay", "AA");
//        addParams("province", "");
//        addParams("city", "");
//        addParams("district", "");
//        addParams("street", "");
//        addParams("longitude","118");
//        addParams("latitude", "32 ");
//        addParams("maxDistance", "5609");
        showNext();

    }

    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.onRefreshComplete();

    }

    @Override
    public void loadSuccessOnFirst() {
        empty.setVisibility(View.VISIBLE);
        msg.setText("暂未被Ta人关注");
    }


    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }
}
