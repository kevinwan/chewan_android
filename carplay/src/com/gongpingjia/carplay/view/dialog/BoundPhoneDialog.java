package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/11/5.
 */
public class BoundPhoneDialog extends BaseAlertDialog{
    String type;
    OnPickResultListener onPickResultListener;
    public BoundPhoneDialog(Context context, String type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_dialog);
        TextView type = (TextView) findViewById(R.id.login_type);
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    public interface OnPickResultListener {
        void onResult(int result);
    }

    public OnPickResultListener getOnPickResultListener() {
        return onPickResultListener;
    }

    public void setOnPickResultListener(
            OnPickResultListener onPickResultListener) {
        this.onPickResultListener = onPickResultListener;
    }
}
