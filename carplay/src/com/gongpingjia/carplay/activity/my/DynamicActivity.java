package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.DyanmicBaseAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by Administrator on 2015/10/19.
 * 活动动态
 */
public class DynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess {

    private ListView recyclerView;

    PullToRefreshListView listV;
    LinearLayout empty;
    TextView msg;
    User user = User.getInstance();
    //    DynamicActivityAdapter adapter;
    DyanmicBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicactivity);

    }


    @Override
    public void initView() {
        setTitle("活动动态");
//        String [] str = {"邀请中","应邀"};
//        JSONArray jsonarray = new JSONArray();
//        jsonarray.put("邀请中");
//        jsonarray.put("应邀");
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        recyclerView = listV.getRefreshableView();
        adapter = new DyanmicBaseAdapter(self);
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");

        setUrl(API2.CWBaseurl + "/user/" + user.getUserId() + "/appointment/list?token=" + user.getToken() + "&status=" + 1 + "&status=" + 2);
//        setUrl(API2.CWBaseurl + "user/5609eb2c0cf224e7d878f693/appointment/list?token=67666666-f2ff-456d-a9cc-e83761749a6a&status=邀请中&status=应邀");

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
