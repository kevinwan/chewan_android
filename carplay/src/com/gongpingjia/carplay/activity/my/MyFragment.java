package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ActiveAdapter;

public class MyFragment extends Fragment
{
    View mainV;
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    static MyFragment instance;
    
    public static MyFragment getInstance()
    {
        if (instance == null)
        {
            instance = new MyFragment();
        }
        
        return instance;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        mainV = inflater.inflate(R.layout.fragment_my, null);
        initView();
        return mainV;
    }
    
    private void initView()
    {
        
    }
}