package com.gongpingjia.carplay.activity;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.PicLayoutUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class MainActivity extends CarPlayBaseActivity
{
    LinearLayout layout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout)findViewById(R.id.head);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        PicLayoutUtil hl = new PicLayoutUtil(MainActivity.this, 5, 20, layout);
        hl.addMoreChild();
    }
    
}
