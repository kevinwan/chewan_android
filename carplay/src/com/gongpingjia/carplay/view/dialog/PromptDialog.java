package com.gongpingjia.carplay.view.dialog;

import net.duohuo.dhroid.util.ViewUtil;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

public class PromptDialog extends BaseAlertDialog {

    private String msg;

    public PromptDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    public PromptDialog(Context context, String msg, String msg1) {
        super(context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_active_prompt);
        ViewUtil.bindView(findViewById(R.id.des), msg);

        Button okB = (Button) findViewById(R.id.ok);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
