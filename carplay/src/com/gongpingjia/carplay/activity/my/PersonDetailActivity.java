package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

public class PersonDetailActivity extends CarPlayBaseActivity
{
    LinearLayout tab;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
    }
    
    @Override
    public void initView()
    {
        tab = (LinearLayout)findViewById(R.id.tab);
        initTopTab();
        setTab(0);
    }
    
    private void initTopTab()
    {
        for (int i = 0; i < tab.getChildCount(); i++)
        {
            final int index = i;
            View childV = tab.getChildAt(i);
            childV.setOnClickListener(new View.OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    setTab(index);
                }
            });
        }
    }
    
    private void setTab(int index)
    {
        for (int i = 0; i < tab.getChildCount(); i++)
        {
            LinearLayout childV = (LinearLayout)tab.getChildAt(i * 2);
            View img = (View)childV.findViewById(R.id.tabline);
            TextView numText = (TextView)childV.getChildAt(0);
            TextView desText = (TextView)childV.getChildAt(1);
            if (index == i)
            {
                img.setVisibility(View.VISIBLE);
                numText.setTextColor(getResources().getColor(R.color.text_orange));
                desText.setTextColor(getResources().getColor(R.color.text_orange));
            }
            else
            {
                img.setVisibility(View.GONE);
                numText.setTextColor(getResources().getColor(R.color.text_grey));
                desText.setTextColor(getResources().getColor(R.color.text_grey));
            }
        }
    }
}
