package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.BeSubscribedAdapter2;
import com.gongpingjia.carplay.adapter.BeSubscribedAdaptertwo;
import com.gongpingjia.carplay.adapter.SubscribeListener;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * Created by Administrator on 2015/10/27.
 * 谁关注我
 */
public class MySubscriberActivity2 extends CarPlayListActivity implements ILoadSuccess {

    private PullToRefreshListView mListView;
    private BeSubscribedAdaptertwo beSubscribeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriber);
    }

    @Override
    public void initView() {
        setTitle("谁关注我");
        mListView = (PullToRefreshListView) findViewById(R.id.refresh_list_view);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        beSubscribeAdapter = new BeSubscribedAdaptertwo(self, 1);
        beSubscribeAdapter.setSubscribeListener(new SubscribeListener() {
            @Override
            public void onSubscribe(String targetId) {
                DhNet dhNet = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                dhNet.addParam("targetUserId", targetId);
                dhNet.doPostInDialog(new NetTask(self) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            refresh();
                        }
                    }
                });
            }
        });
        ListView listV = mListView.getRefreshableView();
        listV.setAdapter(beSubscribeAdapter);
        setUrl(API2.getSubscribeMy(User.getInstance().getUserId(), User.getInstance().getToken()));
        setOnLoadSuccess(this);
        fromWhat("data");
        refresh();
    }


    @Override
    public void loadSuccess() {
        Log.d("msg", mVaules.toString());
        beSubscribeAdapter.setData(mVaules);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.onRefreshComplete();
            }
        }, 500);
    }

    @Override
    public void loadSuccessOnFirst() {

    }
}
