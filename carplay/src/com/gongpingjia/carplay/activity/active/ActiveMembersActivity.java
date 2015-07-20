package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.util.CarSeatUtil;

public class ActiveMembersActivity extends CarPlayBaseActivity
{
    View headV;
    
    ListView listV;
    
    NetJSONAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_members);
    }
    
    @Override
    public void initView()
    {
        headV = LayoutInflater.from(self).inflate(R.layout.head_active_members, null);
        TextView cardesT = (TextView)headV.findViewById(R.id.seat_des);
        cardesT.setVisibility(View.VISIBLE);
        LinearLayout carLayout = (LinearLayout)headV.findViewById(R.id.car_layout);
        CarSeatUtil util = new CarSeatUtil(self, null, carLayout);
        util.addCar();
        
        listV = (ListView)findViewById(R.id.listview);
        adapter = new NetJSONAdapter(API.allCarData, self, R.layout.item_newmessage_list);
        adapter.fromWhat("car_list");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
    
}
