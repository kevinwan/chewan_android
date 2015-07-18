package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.util.PicLayoutUtil;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ActiveDetailsActivity extends BaseActivity
{
    
    private NetRefreshAndMoreListView mListView;
    
    private LayoutInflater mInflater;
    
    private NetJSONAdapter mJsonAdapter;
    
    private LinearLayout mPicturesLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details);
        mListView = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        mInflater = LayoutInflater.from(this);
        
        View view = mInflater.inflate(R.layout.active_head_view, null);
        mPicturesLayout = (LinearLayout)view.findViewById(R.id.layout_pictures);
        // PicLayoutUtil photoUtil = new PicLayoutUtil(this, 3, 10, mPicturesLayout);
        // photoUtil.addMoreChild();
        mListView.addHeaderView(view);
        
        mJsonAdapter = new NetJSONAdapter(API.allCarData, this, R.layout.listitem_comment);
        mJsonAdapter.fromWhat("car_list");
        mListView.setAdapter(mJsonAdapter);
        mJsonAdapter.showNextInDialog();
    }
}
