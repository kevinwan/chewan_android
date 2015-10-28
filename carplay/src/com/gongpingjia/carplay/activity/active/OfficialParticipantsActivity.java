package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.activity.my.MyPerSonDetailActivity2;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.adapter.OfficialParticipantsAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

public class OfficialParticipantsActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess, CarPlayListActivity.onLoadDataSuccess {

    private ListView mRecyclerView;
    LinearLayout empty;
    TextView msg;
    PullToRefreshListView listV;

    OfficialParticipantsAdapter adapter;

    User user;

    String activeid;

    boolean isMember=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_participants);
    }

    @Override
    public void initView() {
        setTitle("参与成员");
        user = User.getInstance();
        activeid = getIntent().getStringExtra("activityId");

        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);

        mRecyclerView = listV.getRefreshableView();
//        mRecyclerView.setEmptyView(findViewById(R.id.));
        adapter = new OfficialParticipantsAdapter(self);
        mRecyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        setOnLoadDataSuccess(this);
        fromWhat("data.members");
        setUrl(API2.CWBaseurl + "/official/activity/" + activeid + "/members?userId=" + user.getUserId() + "&token=" + user.getToken());
        showNext();

        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it;
                    String userId = JSONUtil.getString(adapter.getItem(position), "userId");
                    if (userId.equals(user.getUserId())) {
                        it = new Intent(self, MyPerSonDetailActivity2.class);
                        startActivity(it);
                    } else {
                        it = new Intent(self, PersonDetailActivity2.class);
                        it.putExtra("activeid", userId);
                        startActivity(it);
                    }
            }
        });

    }

    @Override
    public void loadSuccess() {
        adapter.setData(mVaules,isMember,activeid);
        listV.onRefreshComplete();

    }

    @Override
    public void loadSuccessOnFirst() {
        empty.setVisibility(View.VISIBLE);
        msg.setText("暂无参与人员");
    }


    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void load(JSONObject jo) {
        isMember = JSONUtil.getBoolean(jo, "isMember");
    }
}
