package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.ListView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.DyanmicBaseAdapter;
import com.gongpingjia.carplay.adapter.MySubscriberAdapter2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.PullToRefreshRecyclerViewVertical;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;


import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/19.
 * 活动动态
 */
public class DynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess{

    private ListView recyclerView;

    PullToRefreshListView listV;

    User user = User.getInstance();
    //    DynamicActivityAdapter adapter;
    DyanmicBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicactivity);

    }


    @Override
    public void initView() {
        setTitle("活动动态");
        String [] str = {"邀请中","应邀"};
        JSONArray jsonarray = new JSONArray();
//        JSONObject SO1=new JSONObject();
//        SO1.
        jsonarray.put("邀请中");
        jsonarray.put("应邀");

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);

        recyclerView = listV.getRefreshableView();
        adapter = new DyanmicBaseAdapter(self);
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");
//        setUrl(API2.CWBaseurl+"/user/"+ user.getUserId()+"/appointment/list?token="+ user.getToken());
        setUrl(API2.CWBaseurl + "user/5609eb2c0cf224e7d878f693/appointment/list?token=67666666-f2ff-456d-a9cc-e83761749a6a&status=邀请中&status=应邀");

        showNext();
    }


    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.onRefreshComplete();
    }

    @Override
    public void loadSuccessOnFirst() {

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}
