package com.gongpingjia.carplay.view.dialog;

import net.duohuo.dhroid.util.ViewUtil;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ActiveMsgDialog extends BaseAlertDialog
{
    
    String msg = "";
    
    String msg1 = "";
    
    OnClickResultListener onClickResultListener;
    
    public ActiveMsgDialog(Context context, String msg)
    {
        super(context);
        this.msg = msg;
    }
    
    public ActiveMsgDialog(Context context, String msg, String msg1)
    {
        super(context);
        this.msg = msg;
        this.msg1 = msg1;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_active_msg);
        ViewUtil.bindView(findViewById(R.id.des), msg);
        ViewUtil.bindView(findViewById(R.id.des1), msg1);
        
        Button cancelB = (Button)findViewById(R.id.cancel);
        cancelB.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        
        Button okB = (Button)findViewById(R.id.ok);
        okB.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onClickResultListener != null)
                {
                    onClickResultListener.onclick();
                }
                dismiss();
            }
        });
        
    }
    
    public OnClickResultListener getOnClickResultListener()
    {
        return onClickResultListener;
    }
    
    public void setOnClickResultListener(OnClickResultListener onClickResultListener)
    {
        this.onClickResultListener = onClickResultListener;
    }
    
    public interface OnClickResultListener
    {
        void onclick();
    }
    
}
