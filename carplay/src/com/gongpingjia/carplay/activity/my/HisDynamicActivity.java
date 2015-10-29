package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.HisDyanmicBaseAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/19.
 * TA的活动
 */
public class HisDynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess, CarPlayListActivity.onLoadDataSuccess {

    private ListView recyclerView;

    PullToRefreshListView listV;
    LinearLayout empty;
    TextView msg;
    User user = User.getInstance();
    HisDyanmicBaseAdapter adapter;
    String viewUserId;
    String cover, name, photoAuthStatus, licenseAuthStatus, gender, age, brand, logo;
    Double distance;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);

    }


    @Override
    public void initView() {
        setTitle("TA的活动");
        bundle = getIntent().getExtras();
        viewUserId = bundle.getString("userId");
        name = getIntent().getStringExtra("name");
        photoAuthStatus = getIntent().getStringExtra("photoAuthStatus");
        licenseAuthStatus = getIntent().getStringExtra("licenseAuthStatus");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");
        brand = getIntent().getStringExtra("brand");
        logo = getIntent().getStringExtra("logo");

        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        recyclerView = listV.getRefreshableView();
//        adapter = new HisDyanmicBaseAdapter(self);
//        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        setOnLoadDataSuccess(this);
        fromWhat("data.activities");

        setUrl(API2.CWBaseurl + "user/" + viewUserId + "/activity/list?token=" + user.getToken() + "&userId=" + user.getUserId());

        showNext();


    }


    @Override
    public void loadSuccess() {
        adapter.setData(mVaules, bundle, cover, distance);
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

    @Override
    public void load(JSONObject jo) {
        cover = JSONUtil.getString(jo, "cover");
        distance = JSONUtil.getDouble(jo, "distance");

        System.out.println("Ta的活动背景++++++" + cover);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new HisDyanmicBaseAdapter(self);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
