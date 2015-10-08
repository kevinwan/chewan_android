package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class NearbyActivity extends CarPlayBaseActivity {

    NetRefreshAndMoreListView refreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        refreshListView = (NetRefreshAndMoreListView) findViewById(R.id.refresh_lv);
        NetJSONAdapter adapter = new NetJSONAdapter(API.activeList, this, R.layout.list_item_nearby);
        adapter.addparam("key", "latest");
        refreshListView.setAdapter(adapter);
        adapter.showNext();
    }

    @Override
    public void initView() {

    }

}
