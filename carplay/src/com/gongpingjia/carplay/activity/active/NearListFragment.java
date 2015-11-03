package com.gongpingjia.carplay.activity.active;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.adapter.NearListAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.FilterPreference2;
import com.gongpingjia.carplay.bean.LoginEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewVertical;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;

import org.json.JSONObject;

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
    RelativeLayout free_layout;
    CheckBox free_ck;
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
        free_layout = (RelativeLayout)  mainV.findViewById(R.id.free);
        free_layout.getBackground().setAlpha(179);
        free_ck = (CheckBox)  mainV.findViewById(R.id.free_check);
        free_ck.setChecked(true);
        free_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
//                    System.out.println("有空");
                    DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/info?token=" + user.getToken());
                    net.addParam("idle", true);
                    net.doPostInDialog(new NetTask(getActivity()) {
                        @Override
                        public void doInUI(Response response, Integer transfer) {
                            if (response.isSuccess()) {
                                System.out.println(response.isSuccess());
                            }
                        }
                    });
                } else {
//                    System.out.println("没空");
                    DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/info?token=" + user.getToken());
                    net.addParam("idle", false);
                    net.doPostInDialog(new NetTask(getActivity()) {
                        @Override
                        public void doInUI(Response response, Integer transfer) {
                            if (response.isSuccess()) {
                                System.out.println(response.isSuccess());
                            }
                        }
                    });
                }

            }
        });
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
        adapter.setOnItemClick(new NearListAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position, final JSONObject jo) {
                UserInfoManage.getInstance().checkLogin((Activity) getActivity(), new UserInfoManage.LoginCallBack() {
                    @Override
                    public void onisLogin() {
                        Intent it = new Intent(getActivity(), PersonDetailActivity2.class);
                        JSONObject userjo = JSONUtil.getJSONObject(jo, "organizer");
                        String userId = JSONUtil.getString(userjo, "userId");
                        it.putExtra("userId", userId);
                        startActivity(it);
                    }

                    @Override
                    public void onLoginFail() {

                    }
                });

            }
        });
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
        setUrl(API2.CWBaseurl + "activity/list?");
        UserLocation location = UserLocation.getInstance();
//        setUrl("http://cwapi.gongpingjia.com:8080/v2/activity/list?latitude=32&longitude=118&maxDistance=5000000&token="+user.getToken()+"&userId="+user.getUserId());
        addParams("latitude", location.getLatitude());
        addParams("longitude", location.getLongitude());
        addParams("maxDistance", "5000000");
        addParams("majorType", pre.getType());
        addParams("pay", pre.getPay());
        addParams("gender", pre.getGender());
        addParams("transfer", pre.isTransfer());
        addParams("token", user.getToken());
        addParams("userId", user.getUserId());
        showNext();
    }

    @Override
    public void loadSuccess() {
        if (mVaules.size() != 0) {
            listV.isScrollOnFooter = false;

        } else {
            listV.isScrollOnFooter = true;
        }
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
        if (mVaules.size() == 0) {
            near_layout.setVisibility(View.VISIBLE);
        } else {
            near_layout.setVisibility(View.GONE);
        }


    }

    public void onEventMainThread(FilterPreference2 pre) {
        pre = IocContainer.getShare().get(FilterPreference2.class);
        pre.load();
        addParams("majorType", pre.getType());
        addParams("pay", pre.getPay());
        addParams("gender", pre.getGender());
        addParams("transfer", pre.isTransfer());
        refresh();
    }


    public void onEventMainThread(LoginEB login) {
        addParams("token", user.getToken());
        addParams("userId", user.getUserId());
        refresh();
    }

    public void onEventMainThread(String result) {
        if ("刷新附近列表".equals(result)) {
            refresh();
        }
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
