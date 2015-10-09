package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import net.duohuo.dhroid.view.RefreshAndMoreListView;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NearListActivity extends CarPlayBaseActivity {


    RefreshAndMoreListView listV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_list);
    }

    @Override
    public void initView() {
        listV = (RefreshAndMoreListView) findViewById(R.id.listview);


    }
}
