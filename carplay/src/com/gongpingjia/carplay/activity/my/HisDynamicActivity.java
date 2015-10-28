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
import com.gongpingjia.carplay.adapter.MyDyanmicBaseAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/19.
 * TA的活动
 */
public class HisDynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess {

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
        setOnLoadSuccess(this);
        fromWhat("data.activities");

        setUrl(API2.CWBaseurl + "user/" + viewUserId + "/activity/list?token=" + user.getToken() + "&userId=" + user.getUserId() + "&limit=" + 10 + "&ignore=" + 0);

        DhNet net = new DhNet(API2.CWBaseurl + "user/" + viewUserId + "/activity/list?token=" + user.getToken() + "&userId=" + user.getUserId() + "&limit=" + 10 + "&ignore=" + 0);
        net.doGet(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    cover = JSONUtil.getString(jo, "cover");
                    distance = JSONUtil.getDouble(jo, "distance");
                    adapter = new HisDyanmicBaseAdapter(self, bundle, cover, distance);
                    recyclerView.setAdapter(adapter);
                    showNext();
                }
            }
        });




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
        empty.setVisibility(View.VISIBLE);
        msg.setText("此处暂无活动");

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        showNext();
    }
}
