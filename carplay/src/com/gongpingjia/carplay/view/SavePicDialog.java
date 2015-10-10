package com.gongpingjia.carplay.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.gongpingjia.carplay.R;

/**
 * Created by Administrator on 2015/10/9.
 */
public class SavePicDialog extends AlertDialog implements View.OnClickListener {

    private OnSavePicClickListener mListener;

    protected SavePicDialog(Context context) {
        super(context);
    }

    protected SavePicDialog(Context context, int theme) {
        super(context, theme);
    }

    protected SavePicDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnSavePicListener(OnSavePicClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_save_pic);
        findViewById(R.id.tv_save).setOnClickListener(this);

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                if (mListener != null) {
                    mListener.onSaveListener();
                }
                break;
            default:
                dismiss();
        }
    }

    interface OnSavePicClickListener {
        void onSaveListener();
    }
}
