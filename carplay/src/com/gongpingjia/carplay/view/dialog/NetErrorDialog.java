package com.gongpingjia.carplay.view.dialog;

import net.duohuo.dhroid.util.ViewUtil;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class NetErrorDialog extends AlertDialog
{
    String title;
    
    String msg;
    
    Activity activity;
    
    public NetErrorDialog(Context context, String title, String msg)
    {
        super(context);
        this.msg = msg;
        this.title = title;
        activity = (Activity)context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_net_error);
        ViewUtil.bindView(findViewById(R.id.title), title);
        ViewUtil.bindView(findViewById(R.id.msg), msg);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                dismiss();
                if (activity.getClass().equals(MainActivity.class))
                {
                    MainActivity mac = (MainActivity)activity;
                    mac.initView();
                }
                else if (activity.getClass().equals(CarPlayBaseActivity.class))
                {
                    CarPlayBaseActivity carmc = (CarPlayBaseActivity)activity;
                    carmc.initView();
                }
            }
        });
    }
}
