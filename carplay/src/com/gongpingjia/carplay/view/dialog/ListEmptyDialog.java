package com.gongpingjia.carplay.view.dialog;

import net.duohuo.dhroid.util.ViewUtil;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.active.CreateActiveActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListEmptyDialog extends AlertDialog
{
    int type;
    
    String title;
    
    String msg;
    
    public static final int RELEASE = 1;
    
    public static final int JOIN = 2;
    
    public static final int ATTENTION = 3;
    
    Context context;
    
    public ListEmptyDialog(Context context, int type, String title, String msg)
    {
        super(context);
        this.type = type;
        this.title = title;
        this.msg = msg;
        this.context = context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_net_error);
        ViewUtil.bindView(findViewById(R.id.title), title);
        ViewUtil.bindView(findViewById(R.id.msg), msg);
        
        switch (type)
        {
            case RELEASE:
                ViewUtil.bindView(findViewById(R.id.btn_submit), "发布活动");
                break;
            
            case JOIN:
                ViewUtil.bindView(findViewById(R.id.btn_submit), "添加关注");
                break;
            
            case ATTENTION:
                ViewUtil.bindView(findViewById(R.id.btn_submit), "参与活动");
                break;
            
            default:
                break;
        }
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                dismiss();
                Intent it;
                switch (type)
                {
                    case RELEASE:
                        it = new Intent(context, CreateActiveActivity.class);
                        break;
                    
                    case JOIN:
                        
                        break;
                    
                    case ATTENTION:
                        
                        break;
                    
                    default:
                        break;
                }
            }
        });
    }
    
}
