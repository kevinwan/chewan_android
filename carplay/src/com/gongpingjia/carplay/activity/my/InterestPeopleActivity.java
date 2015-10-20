package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.ListView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * 感兴趣的人
 * Created by Administrator on 2015/10/20.
 */
public class InterestPeopleActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicactivity);


    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadSuccessOnFirst() {

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}
