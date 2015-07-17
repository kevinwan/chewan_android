package com.gongpingjia.carplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

public class MainActivity extends CarPlayBaseActivity
{
    LinearLayout layout;
    
    LinearLayout tabV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        setTab(0);
    }
    
    @Override
    public void initView()
    {
        tabV = (LinearLayout)findViewById(R.id.tab);
    }
    
    private void initTab()
    {
        for (int i = 0; i < tabV.getChildCount(); i++)
        {
            final int index = i;
            View childV = tabV.getChildAt(i);
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
        for (int i = 0; i < tabV.getChildCount(); i++)
        {
            View childV = tabV.getChildAt(i);
            ImageView img = (ImageView)childV.findViewById(R.id.img);
            TextView text = (TextView)childV.findViewById(R.id.text);
            if (index == i)
            {
                text.setTextColor(getResources().getColor(R.color.text_blue));
                switch (index)
                {
                    case 0:
                        setTitle("同城");
                        setRightAction("创建活动", -1, new OnClickListener()
                        {
                            
                            @Override
                            public void onClick(View arg0)
                            {
                                // Intent it = new Intent(MainActivity.this, CreateAcActivity.class);
                                // startActivity(it);
                            }
                        });
                        setLeftAction(R.drawable.filtrate, "筛选", new OnClickListener()
                        {
                            
                            @Override
                            public void onClick(View arg0)
                            {
                                
                            }
                        });
                        break;
                    
                    case 1:
                        setTitle("消息");
                        setLeftAction(-2, null, new OnClickListener()
                        {
                            
                            @Override
                            public void onClick(View arg0)
                            {
                            }
                        });
                        break;
                    
                    case 2:
                        setTitle("我的");
                        setLeftAction(R.drawable.icon_setting, "设置", new OnClickListener()
                        {
                            
                            @Override
                            public void onClick(View arg0)
                            {
                                Intent it = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(it);
                            }
                        });
                        setRightAction(null, R.drawable.icon_camera, new OnClickListener()
                        {
                            
                            @Override
                            public void onClick(View arg0)
                            {
                                // Intent it = new Intent(MainActivity.this, EditCarmeraActivity.class);
                                // startActivity(it);
                            }
                        });
                        break;
                    
                    default:
                        break;
                }
            }
            else
            {
                text.setTextColor(getResources().getColor(R.color.text_grey));
                switch (i)
                {
                    case 0:
                        img.setImageResource(R.drawable.city_n);
                        break;
                    
                    case 1:
                        img.setImageResource(R.drawable.msg_n);
                        break;
                    
                    case 2:
                        img.setImageResource(R.drawable.my_n);
                        break;
                    
                    default:
                        break;
                }
            }
        }
    }
    
}
