package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/11/7.
 */
public class BindPhoneDialog extends BaseAlertDialog {

    String des;

    OnCLickResult onCLickResult;


    public BindPhoneDialog(Context context, int theme) {
        super(context, theme);
    }

    public BindPhoneDialog(Context context, String des) {
        super(context);
        this.des = des;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bind_phone);
        TextView desT = (TextView) findViewById(R.id.des);
        desT.setText(des);

        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onCLickResult != null) {
                    onCLickResult.clickResult();
                }
            }
        });


    }

    public OnCLickResult getOnCLickResult() {
        return onCLickResult;
    }

    public void setOnCLickResult(OnCLickResult onCLickResult) {
        this.onCLickResult = onCLickResult;
    }

    public interface OnCLickResult {
        void clickResult();
    }
}
