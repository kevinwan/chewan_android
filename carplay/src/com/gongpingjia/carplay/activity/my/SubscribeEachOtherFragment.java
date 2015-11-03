package com.gongpingjia.carplay.activity.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.adapter.EachSubscribeAdapter2;
import com.gongpingjia.carplay.adapter.SubscribeListener;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * Created by Administrator on 2015/10/16.
 * 互相关注
 */
public class SubscribeEachOtherFragment extends CarPlayBaseFragment implements ILoadSuccess {
    private PullToRefreshListView mListView;
    private EachSubscribeAdapter2 eachSubscribeAdapter2;
    View view;
    LinearLayout empty;
    TextView msg;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_follow_each_other, container, false);
        mListView = (PullToRefreshListView) view.findViewById(R.id.refresh_list_view);

        empty = (LinearLayout) view.findViewById(R.id.empty);
        msg = (TextView) view.findViewById(R.id.msgT);
        setOnLoadSuccess(this);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        initView();
        return view;
    }

    private void initView() {
        eachSubscribeAdapter2 = new EachSubscribeAdapter2(getActivity());
        eachSubscribeAdapter2.setSubscribeListener(new SubscribeListener() {
            @Override
            public void onSubscribe(String targetId) {
                DhNet dhNet = new DhNet(API2.getUnfollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                dhNet.addParam("targetUserId", targetId);
                dhNet.doPostInDialog(new NetTask(getActivity()) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            //取消关注成功
                            refresh();
                        }
                    }
                });
            }
        });
        mListView.setAdapter(eachSubscribeAdapter2);
        setOnLoadSuccess(this);
        setUrl(API2.getSubscribe(User.getInstance().getUserId(), User.getInstance().getToken()));
        fromWhat("data.eachSubscribe");
        refresh();
    }

//    private void refreshList() {
//        DhNet dhNet = new DhNet(API2.getSubscribe(User.getInstance().getUserId(), User.getInstance().getToken()));
//        dhNet.doGetInDialog(new NetTask(getActivity()) {
//            @Override
//            public void doInUI(Response response, Integer transfer) {
//                mListView.onRefreshComplete();
//                if (response.isSuccess()) {
//                    try {
//                        JSONObject jsonObject = response.jSONFromData();
//                        JSONArray jsonArray = jsonObject.getJSONArray("eachSubscribe");
//                        eachSubscribeAdapter2 = new EachSubscribeAdapter2(getActivity(), jsonArray);
//                        eachSubscribeAdapter2.setSubscribeListener(new SubscribeListener() {
//                            @Override
//                            public void onSubscribe(String targetId) {
//                                DhNet dhNet = new DhNet(API2.getUnfollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
//                                dhNet.addParam("targetUserId", targetId);
//                                dhNet.doPostInDialog(new NetTask(getActivity()) {
//                                    @Override
//                                    public void doInUI(Response response, Integer transfer) {
//                                        if (response.isSuccess()) {
//                                            //取消关注成功
//                                            refreshList();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                        mListView.setAdapter(eachSubscribeAdapter2);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    @Override
    public void loadSuccess() {
        eachSubscribeAdapter2.setData(mVaules);
        mListView.onRefreshComplete();
    }

    @Override
    public void loadSuccessOnFirst() {
        if (mVaules.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            msg.setText("您为和Ta人相互关注");
        } else {
            empty.setVisibility(View.GONE);
        }
    }
}
