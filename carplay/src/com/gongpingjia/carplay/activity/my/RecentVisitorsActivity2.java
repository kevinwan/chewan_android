package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by Administrator on 2015/10/19.
 */
public class RecentVisitorsActivity2 extends CarPlayBaseActivity {

    private PullToRefreshListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_visitors2);
    }

    @Override
    public void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.refresh_list_view);
        
    }
}
