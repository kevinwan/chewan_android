package com.gongpingjia.carplay.activity;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class PartyDetailsActivity extends BaseActivity {

    private NetRefreshAndMoreListView mListView;

    private LayoutInflater mInflater;

    private NetJSONAdapter mJsonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);
        mListView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
        mInflater = LayoutInflater.from(this);

        View view = mInflater.inflate(R.layout.party_head_view, null);
        mListView.addHeaderView(view);

        mJsonAdapter = new NetJSONAdapter(API.allCarData, this, R.layout.listitem_comment);
        mJsonAdapter.fromWhat("car_list");
        mListView.setAdapter(mJsonAdapter);
        mJsonAdapter.showNextInDialog();
    }
}
