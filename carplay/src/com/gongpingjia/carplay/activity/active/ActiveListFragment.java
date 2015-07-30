package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.ActiveParmasEB;
import com.gongpingjia.carplay.bean.User;

import de.greenrobot.event.EventBus;

public class ActiveListFragment extends Fragment
{
    View mainV;
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    static ActiveListFragment instance;
    
    User user;
    
    DhNet net;
    
    LinearLayout topTab;
    
    public static ActiveListFragment getInstance()
    {
        if (instance == null)
        {
            instance = new ActiveListFragment();
        }
        
        return instance;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        EventBus.getDefault().register(this);
        mainV = inflater.inflate(R.layout.activity_active_list_fragment, null);
        initView();
        return mainV;
    }
    
    private void initView()
    {
        topTab = (LinearLayout)mainV.findViewById(R.id.top_tab);
        initTopTab();
        user = User.getInstance();
        listV = (NetRefreshAndMoreListView)mainV.findViewById(R.id.listview);
        listV.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                JSONObject jo = (JSONObject)adapter.getItem(position - 1);
                Intent it = new Intent(getActivity(), ActiveDetailsActivity.class);
                it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
                startActivity(it);
            }
        });
        adapter = new ActiveAdapter(API.activeList, getActivity(), R.layout.item_active_list);
        
        adapter.addparam("key", "hot");
        adapter.addparam("userId", user.getUserId());
        adapter.addparam("token", user.getToken());
        adapter.addparam("city", "南京");
        adapter.addparam("district", "");
        adapter.addparam("type", "");
        adapter.addparam("gender", "");
        adapter.addparam("authenticate", "");
        adapter.addparam("carLevel", "");
        
        adapter.fromWhat("data");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
    
    public void changeData(String key)
    {
        adapter.addparam("key", key);
        adapter.refreshDialog();
    }
    
    private void initTopTab()
    {
        for (int i = 0; i < topTab.getChildCount(); i++)
        {
            final int index = i;
            View childV = topTab.getChildAt(i);
            childV.setOnClickListener(new View.OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    setTopTab(index);
                }
            });
        }
    }
    
    private void setTopTab(int index)
    {
        for (int i = 0; i < topTab.getChildCount(); i++)
        {
            View childV = topTab.getChildAt(i);
            ImageView img = (ImageView)childV.findViewById(R.id.tabline);
            TextView text = (TextView)childV.findViewById(R.id.tab_text);
            if (index == i)
            {
                text.setTextColor(getResources().getColor(R.color.text_orange));
                img.setVisibility(View.VISIBLE);
                switch (index)
                {
                    case 0:
                        changeData("hot");
                        break;
                    
                    case 1:
                        changeData("nearby");
                        break;
                    
                    case 2:
                        changeData("latest");
                        break;
                    
                    default:
                        break;
                }
            }
            else
            {
                text.setTextColor(getResources().getColor(R.color.text_black));
                img.setVisibility(View.GONE);
            }
        }
    }
    
    /** 接受ActiveFilterPop的选择事件 */
    public void onEventMainThread(ActiveParmasEB pa)
    {
        adapter.addparam("city", pa.getCity());
        adapter.addparam("district", pa.getDistrict());
        adapter.addparam("type", pa.getActiveType());
        adapter.addparam("gender", pa.getGender());
        adapter.addparam("authenticate", pa.getAuthenticate());
        adapter.addparam("carLevel", pa.getCarLevel());
        adapter.refreshDialog();
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
}
