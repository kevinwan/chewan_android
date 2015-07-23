package com.gongpingjia.carplay.view.dialog;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class CommonDialog {

    private Context mContext;

    private String[] mDatas;

    private String mTitle;

    private OnItemClickListener mListener;

    public CommonDialog(Context context, List<String> data, String title) {
        this.mContext = context;
        this.mTitle = title;
        int size = data.size();
        mDatas = new String[size];
        for (int i = 0; i < size; i++) {
            mDatas[i] = data.get(i);
        }
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setItems(mDatas, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.onItemClickListener(which);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int which);
    }

}
