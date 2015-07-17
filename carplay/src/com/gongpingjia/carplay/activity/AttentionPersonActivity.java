package com.gongpingjia.carplay.activity;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.adapter.BeanAdapter.ViewHolder;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
/**
 * 
 * @Description 我关注的人
 * @author wang
 * @date 2015-7-17 上午11:09:27
 */

public class AttentionPersonActivity extends CarPlayBaseActivity
{
    NetJSONAdapter adapter;
    NetRefreshAndMoreListView listView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_person);
        
       
        
    }
    
    @Override
    public void initView()
    {
        setTitle("我关注的人");
        listView = (NetRefreshAndMoreListView)findViewById(R.id.listview);
         adapter = new NetJSONAdapter(API.Baseurl, self, R.layout.itme_attention_person);
    }
    
}
