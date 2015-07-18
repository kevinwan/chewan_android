package com.gongpingjia.carplay.activity.msg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.my.MyFragment;

/**
 * 
 * @Description 消息
 * @author wang
 * @date 2015-7-17 下午2:37:30
 */
public class MsgFragment extends Fragment
{
    
    static MsgFragment instance;
    
    public static MsgFragment getInstance()
    {
        if (instance == null)
        {
            instance = new MsgFragment();
        }
        
        return instance;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.information, container, false);
        return view;
    }
}
