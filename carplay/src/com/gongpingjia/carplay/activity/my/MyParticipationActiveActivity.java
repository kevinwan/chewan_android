package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.content.Intent;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

public class MyParticipationActiveActivity extends CarPlayBaseActivity
{
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    String userid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrelated_active);
    }
    
    @Override
    public void initView()
    {
        
        Intent it = getIntent();
        userid = it.getStringExtra("userid");
        User user = User.getInstance();
        listV = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        adapter =
            new ActiveAdapter(API.CWBaseurl + "/user/" + userid + "/join?userId=" + user.getUserId() + "&token="
                + user.getToken(), self, R.layout.item_active_list);
        
        adapter.fromWhat("data");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
    
}
