package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;

public class MyFragment extends Fragment implements OnClickListener
{
    View mainV;
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    static MyFragment instance;
    
    /** 我的关注,我的发布,我的参与 三个点击区域的View */
    View my_attentionV, my_releaseV, my_participationV;
    
    LinearLayout carchat,owners_certification,people_concerned;
    
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
    	
        my_attentionV = mainV.findViewById(R.id.my_attention);
        my_releaseV = mainV.findViewById(R.id.my_release);
        my_participationV = mainV.findViewById(R.id.my_participation);
        carchat=(LinearLayout) mainV.findViewById(R.id.carchat);
        people_concerned=(LinearLayout) mainV.findViewById(R.id.people_concerned);
        owners_certification=(LinearLayout) mainV.findViewById(R.id.owners_certification);
        
        my_attentionV.setOnClickListener(this);
        my_releaseV.setOnClickListener(this);
        my_participationV.setOnClickListener(this);
        carchat.setOnClickListener(this);
        people_concerned.setOnClickListener(this);
        owners_certification.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        Intent it;
        
        switch (v.getId())
        {
            case R.id.my_attention:
                it = new Intent(getActivity(), MyAttentionActiveActivity.class);
                startActivity(it);
                break;
            
            case R.id.my_release:
                it = new Intent(getActivity(), MyReleaseActiveActivity.class);
                startActivity(it);
                break;
            
            case R.id.my_participation:
                it = new Intent(getActivity(), MyParticipationActiveActivity.class);
                startActivity(it);
                break;
            case R.id.carchat:
                it = new Intent(getActivity(), PlayCarChatActivity.class);
                startActivity(it);
                break;
            case R.id.people_concerned:
                it = new Intent(getActivity(), AttentionPersonActivity.class);
                startActivity(it);
                break;
            case R.id.owners_certification:
                it = new Intent(getActivity(), AuthenticateOwnersActivity.class);
                startActivity(it);
            default:
                break;
        }
    }
}