package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/11/9.
 */
public class DynamicDelDialog extends BaseAlertDialog {

    OnCLickResult onCLickResult;
    String des="";

    public DynamicDelDialog(Context context, int theme) {
        super(context, theme);
    }

    public DynamicDelDialog(Context context) {
        super(context);
    }

    public DynamicDelDialog(Context context,String des) {
        super(context);
        this.des = des;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dynamic_del
        );

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

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(des)) {
            ((TextView) findViewById(R.id.des)).setText(des);
        }

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

