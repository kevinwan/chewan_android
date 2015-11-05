package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/11/5.
 */
public class BoundPhoneDialog extends BaseAlertDialog{
    public BoundPhoneDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_dialog);
        TextView type = (TextView) findViewById(R.id.login_type);
        Button ok = (Button) findViewById(R.id.ok);


    }
}
