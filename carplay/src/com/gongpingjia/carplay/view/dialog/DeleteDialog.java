package com.gongpingjia.carplay.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class DeleteDialog extends BaseAlertDialog {

    private OnDeleteClickListener mListener;

    public DeleteDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setOnDeleteListener(OnDeleteClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_delete);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete();
                    dismiss();
                }
            }
        });
    }

    public interface OnDeleteClickListener {
        void onDelete();
    }
}
