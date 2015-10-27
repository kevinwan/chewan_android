package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
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
 * Created by Administrator on 2015/10/16.
 * 关注我的人
 */
public class BeSubscribedFragment extends Fragment {
    private PullToRefreshListView mListView;
    private BeSubscribedAdapter2 beSubscribeAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_follow_each_other, container, false);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshList();
            }
        });
        return mListView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshList();
    }

    private void refreshList() {
        DhNet dhNet = new DhNet(API2.getSubscribe(User.getInstance().getUserId(), User.getInstance().getToken()));
        dhNet.doGetInDialog(new NetTask(getActivity()) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                mListView.onRefreshComplete();
                if (response.isSuccess()) {
                    try {
                        JSONObject jsonObject = response.jSONFromData();
                        JSONArray jsonArray = jsonObject.getJSONArray("beSubscribed");
                        beSubscribeAdapter = new BeSubscribedAdapter2(getActivity(), jsonArray);
                        beSubscribeAdapter.setSubscribeListener(new SubscribeListener() {
                            @Override
                            public void onSubscribe(String targetId) {
                                DhNet dhNet = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                                dhNet.addParam("targetUserId", targetId);
                                dhNet.doPostInDialog(new NetTask(getActivity()) {
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
