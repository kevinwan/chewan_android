package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/8.
 * 匹配意向的fragment
 */
public class MatchingListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerViewPager>, ILoadSuccess {


    static MatchingListFragment instance;
    private RecyclerViewPager mRecyclerView;
    private NearListAdapter adapter;

    View contentView, countdownView;
    //倒计时时间,匹配的数量
    TextView textCountdownTime, textAmount;

    PullToRefreshRecyclerViewVertical listV;

    boolean isfirst;

    User user;

    FilterPreference2 pre;

    View mainV;

    LinearLayout near_layout;
    View currentview;

    private Map<String, Object> mParams;

    public void setParams(Map<String, Object> params) {

        if (mParams == null) {
            mParams = params;
        } else {
            mParams = params;
            //显示倒计时,隐藏内容
            countdownView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            addParams("type", mParams.get("type"));
            addParams("pay", mParams.get("pay"));
            addParams("majorType", mParams.get("majorType"));
            addParams("transfer", mParams.get("transfer"));
            //倒计时60s,等待
            new TimeCount(10 * 1000, 1000).start();
            refresh();
        }
    }

    public static MatchingListFragment getInstance() {
        if (instance == null) {
            instance = new MatchingListFragment();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_matching_list, null);
        EventBus.getDefault().register(this);
        initView();
        return mainV;
    }


    private void initView() {

        contentView = mainV.findViewById(R.id.layout_content);
        countdownView = mainV.findViewById(R.id.layout_countdown);

        textCountdownTime = (TextView) mainV.findViewById(R.id.tv_countdown_time);
        textAmount = (TextView) mainV.findViewById(R.id.tv_matching_amount);

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
                MatchingListFragment.this.currentview = currentview;
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
        if (mParams != null) {
            addParams("type", mParams.get("type"));
            addParams("pay", mParams.get("pay"));
            addParams("majorType", mParams.get("majorType"));
            addParams("transfer", mParams.get("transfer"));
        }
        addParams("token", user.getToken());
        addParams("userId", user.getUserId());
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
        if (mParams != null) {
            addParams("type", mParams.get("type"));
            addParams("pay", mParams.get("pay"));
            addParams("majorType", mParams.get("majorType"));
            addParams("transfer", mParams.get("transfer"));
        }
        if (currentview != null) {
            AnimButtonView animButtonView = (AnimButtonView) currentview.findViewById(R.id.invite);
            animButtonView.clearAnimation();
            animButtonView.startScaleAnimation();
        }
    }


    @Override
    public void loadSuccessOnFirst() {
//            listV.setVisibility(View.GONE);
//        near_layout.setVisibility(View.VISIBLE);

        if (mVaules.size() == 0) {
            near_layout.setVisibility(View.VISIBLE);
        } else {
            near_layout.setVisibility(View.GONE);
        }

    }

    public void onEventMainThread(FilterPreference2 pre) {
//        pre = IocContainer.getShare().get(FilterPreference2.class);
//        pre.load();
//        addParams("type", pre.getType());
//        addParams("pay", pre.getPay());
//        addParams("gender", pre.getGender());
//        addParams("transfer", pre.isTransfer());
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

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textCountdownTime.setText(millisUntilFinished / 1000 + "s");
            textAmount.setText(Html.fromHtml("已匹配到 " + "<font color=black>" + mVaules.size() + "</font> 个活动"));
        }

        @Override
        public void onFinish() {
            countdownView.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
    }
}
