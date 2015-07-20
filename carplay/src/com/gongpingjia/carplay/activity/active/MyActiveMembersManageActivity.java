package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.util.DhUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.util.CarSeatUtil;
import com.gongpingjia.carplay.view.swipeMenuListView.SwipeMenu;
import com.gongpingjia.carplay.view.swipeMenuListView.SwipeMenuCreator;
import com.gongpingjia.carplay.view.swipeMenuListView.SwipeMenuItem;
import com.gongpingjia.carplay.view.swipeMenuListView.SwipeMenuListView;
import com.gongpingjia.carplay.view.swipeMenuListView.SwipeMenuListView.OnMenuItemClickListener;

public class MyActiveMembersManageActivity extends CarPlayBaseActivity
{
    SwipeMenuListView listV;
    
    PSAdapter adapter;
    
    View headV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_active_members);
    }
    
    @Override
    public void initView()
    {
        headV = LayoutInflater.from(self).inflate(R.layout.head_active_members, null);
        LinearLayout carLayout = (LinearLayout)headV.findViewById(R.id.car_layout);
        CarSeatUtil util = new CarSeatUtil(self, null, carLayout);
        util.addCar();
        
        listV = (SwipeMenuListView)findViewById(R.id.listView);
        listV.addHeaderView(headV);
        adapter = new PSAdapter(self, R.layout.item_newmessage_list);
        JSONArray jsa = new JSONArray();
        JSONObject jo1 = new JSONObject();
        JSONObject jo2 = new JSONObject();
        JSONObject jo3 = new JSONObject();
        JSONObject jo4 = new JSONObject();
        jsa.put(jo1);
        jsa.put(jo2);
        jsa.put(jo3);
        jsa.put(jo4);
        adapter.addAll(jsa);
        listV.setAdapter(adapter);
        
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(getResources().getColor(R.color.text_orange)));
                openItem.setWidth(DhUtil.dip2px(self, 90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
                
            }
        };
        listV.setMenuCreator(creator);
        
        listV.setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        // open
                        showToast("删除");
                        break;
                }
            }
        });
    }
}
