package com.gongpingjia.carplay.view.dialog;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class CommonDialog {

    private Context mContext;

    private List<String> mDatas;

    private String mTitle;

    private AlertDialog mDialog;

    private OnCommonDialogItemClickListener mListener;

    public CommonDialog(Context context, List<String> data, String title) {
        this.mContext = context;
        this.mTitle = title;
        mDatas = data;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View content = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
        ListView listView = (ListView) content.findViewById(R.id.lv_dlg);
        TextView mTitleText = (TextView) content.findViewById(R.id.tv_dlg_title);
        mTitleText.setText(mTitle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.item_dlg, R.id.tv_dlg_item, mDatas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.onDialogItemClick(position);
                }
                dismiss();
            }
        });
        builder.setView(content);
        mDialog = builder.create();
        Window window = mDialog.getWindow();
        window.setWindowAnimations(R.style.mystyle);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.show();
    }

    private void dismiss() {
        mDialog.dismiss();
    }

    public void setOnDialogItemClickListener(OnCommonDialogItemClickListener listener) {
        mListener = listener;
    }

    public interface OnCommonDialogItemClickListener {
        void onDialogItemClick(int position);
    }

}
