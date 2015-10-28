package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.BeSubscribedAdapter2;
import com.gongpingjia.carplay.adapter.SubscribeListener;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/27.
 * 谁关注我
 */
public class MySubscriberActivity2 extends CarPlayBaseActivity {

    private PullToRefreshListView mListView;
    private BeSubscribedAdapter2 beSubscribeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriber);
        refreshList();
    }

    @Override
    public void initView() {
        setTitle("谁关注我");
        mListView = (PullToRefreshListView) findViewById(R.id.refresh_list_view);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshList();
            }
        });
    }

    private void refreshList() {
        DhNet dhNet = new DhNet(API2.getSubscribe(User.getInstance().getUserId(), User.getInstance().getToken()));
        dhNet.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                mListView.onRefreshComplete();
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObject = response.jSONFromData();
                        JSONArray jsonArray = jsonObject.getJSONArray("beSubscribed");
                        beSubscribeAdapter = new BeSubscribedAdapter2(self, jsonArray);
                        beSubscribeAdapter.setSubscribeListener(new SubscribeListener() {
                            @Override
                            public void onSubscribe(String targetId) {
                                DhNet dhNet = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                                dhNet.addParam("targetUserId", targetId);
                                dhNet.doPostInDialog(new NetTask(self) {
                                    @Override
                                    public void doInUI(Response response, Integer transfer) {
                                        if (response.isSuccess()) {
                                            //取消关注成功
                                            refreshList();
                                        }
                                    }
                                });
                            }
                        });
                        mListView.setAdapter(beSubscribeAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
