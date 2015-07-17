package com.gongpingjia.carplay.activity;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.adapter.BeanAdapter.ViewHolder;
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

public class AttentionPersonActivity extends CarPlayBaseActivity
{
    TextView title;
    
    NetRefreshAndMoreListView listView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_person);
        title = (TextView)findViewById(R.id.title);
        title.setText("我关注的人");
        
        listView = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        
    }
    
    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }
    
}
