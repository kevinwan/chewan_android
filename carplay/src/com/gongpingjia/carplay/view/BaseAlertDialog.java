package com.gongpingjia.carplay.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;

import com.gongpingjia.carplay.R;

public class BaseAlertDialog extends AlertDialog {
    public BaseAlertDialog(Context context, int theme) {
        super(context, theme);
        Window window = getWindow();
        window.setWindowAnimations(R.style.mystyle);
    }

    long animduring = 250;

    int direction = 1;

    public BaseAlertDialog(Context context) {
        super(context);
        Window window = getWindow();
        window.setWindowAnimations(R.style.mystyle);
    }

}
