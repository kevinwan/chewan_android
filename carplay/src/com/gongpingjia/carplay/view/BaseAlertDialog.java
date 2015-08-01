package com.gongpingjia.carplay.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;

import com.gongpingjia.carplay.R;

public class BaseAlertDialog extends AlertDialog
{
    long animduring = 250;
    
    int direction = 1;
    
    public BaseAlertDialog(Context context)
    {
        super(context);
        Window window = getWindow();
        window.setWindowAnimations(R.style.mystyle);
    }
    
}
