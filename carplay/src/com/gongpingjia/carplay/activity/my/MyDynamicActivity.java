package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.MyDyanmicBaseAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/19.
 * 我的活动
 */
public class MyDynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess {

    private ListView recyclerView;

    PullToRefreshListView listV;
    LinearLayout empty;
    TextView msg;
    User user = User.getInstance();
    MyDyanmicBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        EventBus.getDefault().register(this);
    }


    @Override
    public void initView() {
        setTitle("我的活动");
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        recyclerView = listV.getRefreshableView();
        adapter = new MyDyanmicBaseAdapter(self);
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");

        setUrl(API2.CWBaseurl + "/user/" + user.getUserId() + "/appointment/list?token=" + user.getToken() + "&status=" + 1 + "&status=" + 2+ "&status=" + 4);
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
        if (mVaules.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            msg.setText("此处暂无活动");
        } else {
            empty.setVisibility(View.GONE);
        }


    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        showNext();
    }
    public void onEventMainThread(String success) {
        if ("刷新我的活动".equals(success)) {
            refresh();
        }
    }
}
