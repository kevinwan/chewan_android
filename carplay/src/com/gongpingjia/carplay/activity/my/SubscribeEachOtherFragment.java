package com.gongpingjia.carplay.activity.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.EachSubscribeAdapter2;
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
 * 互相关注
 */
public class SubscribeEachOtherFragment extends Fragment {

    private PullToRefreshListView mListView;
    private EachSubscribeAdapter2 mySubscriberAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_follow_each_other, container, false);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                DhNet dhNet = new DhNet(API2.getSubscribe(User.getInstance().getUserId(), User.getInstance().getToken()));
                dhNet.doGetInDialog(new NetTask(getActivity()) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        mListView.onRefreshComplete();
                        if (response.isSuccess()) {
                            try {
                                JSONObject jsonObject = response.jSONFromData();
                                JSONArray jsonArray = jsonObject.getJSONArray("eachSubscribe");
                                mySubscriberAdapter = new EachSubscribeAdapter2(getActivity(), jsonArray);
                                mListView.setAdapter(mySubscriberAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        return mListView;
    }
}