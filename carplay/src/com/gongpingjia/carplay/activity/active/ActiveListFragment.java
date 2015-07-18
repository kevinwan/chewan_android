package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;

public class ActiveListFragment extends Fragment
{
    View mainV;
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    static ActiveListFragment instance;
    
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
        mainV = inflater.inflate(R.layout.include_refresh_listview, null);
        initView();
        return mainV;
    }
    
    private void initView()
    {
        
        listV = (NetRefreshAndMoreListView)mainV.findViewById(R.id.listview);
        adapter = new ActiveAdapter(API.allCarData, getActivity(), R.layout.item_active_list);
        adapter.fromWhat("car_list");
        listV.setAdapter(adapter);
        adapter.showNext();
    }
}
