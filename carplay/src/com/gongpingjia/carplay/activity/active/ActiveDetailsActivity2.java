package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

/**
 * 活动详情
 */
public class ActiveDetailsActivity2 extends CarPlayBaseActivity {

    private NetRefreshAndMoreListView mListView;

    private LayoutInflater mInflater;

    private View mHeadView;

    private View mFootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details2);
    }

    @Override
    public void initView() {
        mInflater=LayoutInflater.from(this);
        mHeadView=mInflater.inflate(R.layout.item_active_details2_headview, null);
        mFootView=mInflater.inflate(R.layout.item_active_details2_footview, null);
        mListView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
        mListView.addHeaderView(mHeadView);
        mListView.addFooterView(mFootView);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
    }
}
