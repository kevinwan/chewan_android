package com.gongpingjia.carplay.activity.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.my.MyFragment;
import com.gongpingjia.carplay.activity.my.ParticipateApplicationActivity;

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
        RelativeLayout leave_comments_layout = (RelativeLayout)view.findViewById(R.id.leave_comments_layout);
        leave_comments_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(getActivity(),NewMessageActivity.class);
//				startActivity(intent);
				
			}
		});
        RelativeLayout apply_for_layout = (RelativeLayout) view.findViewById(R.id.apply_for_layout);
        apply_for_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),ParticipateApplicationActivity.class);
				startActivity(intent);
			}
		});
        return view;
    }
}
