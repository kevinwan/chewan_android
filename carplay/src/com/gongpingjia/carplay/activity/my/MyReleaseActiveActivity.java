package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView.OnEmptyDataListener;
import android.os.Bundle;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.MyReleaseActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

public class MyReleaseActiveActivity extends CarPlayBaseActivity
{
    
    NetRefreshAndMoreListView listV;
    
    MyReleaseActiveAdapter adapter;
    
    String userId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelated_active);
    }
    
    @Override
    public void initView()
    {
        setTitle("我的发布");
        User user = User.getInstance();
        listV = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        listV.setOnEmptyDataListener(new OnEmptyDataListener()
        {
            
            @Override
            public void onEmpty(boolean showeEptyView)
            {
                findViewById(R.id.empty).setVisibility(showeEptyView ? View.VISIBLE : View.GONE);
            }
        });
        adapter =
            new MyReleaseActiveAdapter(API.CWBaseurl + "/user/" + user.getUserId() + "/post?userId=" + user.getUserId()
                + "&token=" + user.getToken(), self, R.layout.item_myrelease_active);
        adapter.fromWhat("data");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
}
