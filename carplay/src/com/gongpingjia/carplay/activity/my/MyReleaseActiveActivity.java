package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.MyReleaseActiveAdapter;
import com.gongpingjia.carplay.api.API;

public class MyReleaseActiveActivity extends CarPlayBaseActivity
{
    
    NetRefreshAndMoreListView listV;
    
    MyReleaseActiveAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelated_active);
    }
    
    @Override
    public void initView()
    {
        
        listV = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        adapter = new MyReleaseActiveAdapter(API.allCarData, self, R.layout.item_myrelease_active);
        adapter.fromWhat("car_list");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
}
