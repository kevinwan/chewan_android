package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.adapter.NearListAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.FilterPreference2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewVertical;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.UserLocation;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NearListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerViewPager>, ILoadSuccess {


    static NearListFragment instance;
    private RecyclerViewPager mRecyclerView;
    private NearListAdapter adapter;

    PullToRefreshRecyclerViewVertical listV;

    boolean isfirst;

    User user;

    FilterPreference2 pre;

    View mainV;

    LinearLayout near_layout;
    View currentview;

    public static NearListFragment getInstance() {
        if (instance == null) {
            instance = new NearListFragment();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_near_list, null);
        EventBus.getDefault().register(this);
        initView();


        return mainV;
    }


    private void initView() {

        user = User.getInstance();
        pre = IocContainer.getShare().get(FilterPreference2.class);
        pre.load();
        near_layout = (LinearLayout) mainV.findViewById(R.id.near_empty);
        listV = (PullToRefreshRecyclerViewVertical) mainV.findViewById(R.id.list);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        listV.setOnPageChange(new PullToRefreshRecyclerViewVertical.OnPageChange() {
            @Override
            public void change(View currentview) {
                NearListFragment.this.currentview = currentview;
                AnimButtonView animButtonView = (AnimButtonView) currentview.findViewById(R.id.invite);
                animButtonView.clearAnimation();
                animButtonView.startScaleAnimation();
            }
        });
        mRecyclerView = listV.getRefreshableView();
        adapter = new NearListAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.CWBaseurl + "activity/list?");

        UserLocation location = UserLocation.getInstance();
//        setUrl("http://cwapi.gongpingjia.com:8080/v2/activity/list?latitude=32&longitude=118&maxDistance=5000000&token="+user.getToken()+"&userId="+user.getUserId());
        addParams("latitude", location.getLatitude());
        addParams("longitude", location.getLongitude());
        addParams("maxDistance", "5000000");
        addParams("type", pre.getType());
        addParams("pay", pre.getPay());
        addParams("gender", pre.getGender());
        addParams("transfer", pre.isTransfer());
        addParams("token", user.getToken());
        addParams("userId", user.getUserId());
//        Toast.makeText(getActivity(), pre.getType() + "--" + pre.getPay() + "---" + pre.getGender() + "---" + pre.isTransfer(), Toast.LENGTH_LONG).show();
        showNext();
    }

    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentview != null) {
            AnimButtonView animButtonView = (AnimButtonView) currentview.findViewById(R.id.invite);
            animButtonView.clearAnimation();
            animButtonView.startScaleAnimation();
        }
    }


    @Override
    public void loadSuccessOnFirst() {
//            listV.setVisibility(View.GONE);
        near_layout.setVisibility(View.VISIBLE);

    }

    public void onEventMainThread(FilterPreference2 pre) {
        pre = IocContainer.getShare().get(FilterPreference2.class);
        pre.load();
        addParams("type", pre.getType());
        addParams("pay", pre.getPay());
        addParams("gender", pre.getGender());
        addParams("transfer", pre.isTransfer());
        refresh();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {
        showNext();
    }
}
