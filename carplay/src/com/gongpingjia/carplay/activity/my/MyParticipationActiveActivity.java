package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;

public class MyParticipationActiveActivity extends CarPlayBaseActivity
{
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
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
        adapter = new ActiveAdapter(API.allCarData, self, R.layout.item_active_list);
        adapter.fromWhat("car_list");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
    
}
