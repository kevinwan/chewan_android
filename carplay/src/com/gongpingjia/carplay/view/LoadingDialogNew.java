package com.gongpingjia.carplay.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

/**
 * 加载中Dialog
 * 
 * @author xm
 */
public class LoadingDialogNew extends AlertDialog
{
    
    private TextView tips_loading_msg;
    
    private String message = null;
    
    private Context mContext = null;
    
    static LoadingDialogNew loadingDialog;
    
    public LoadingDialogNew(Context context)
    {
        super(context);
        message = getContext().getResources().getString(R.string.progress_doing);
        this.mContext = context;
    }
    
    public LoadingDialogNew(Context context, String message)
    {
        super(context);
        this.message = message;
        this.mContext = context;
        this.setCancelable(false);
    }
    
    public LoadingDialogNew(Context context, int theme, String message)
    {
        super(context, theme);
        this.message = message;
        this.mContext = context;
        this.setCancelable(false);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        tips_loading_msg = (TextView)findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);
    }
    
    public void setText(String message)
    {
        this.message = message;
        tips_loading_msg.setText(this.message);
    }
    
    public void setText(int resId)
    {
        setText(getContext().getResources().getString(resId));
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        this.dismiss();
        return super.onTouchEvent(event);
    }
}
