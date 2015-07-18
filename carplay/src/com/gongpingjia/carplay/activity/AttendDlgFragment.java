package com.gongpingjia.carplay.activity;

import com.gongpingjia.carplay.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class AttendDlgFragment extends DialogFragment {

    private OnDialogCallbacks mCallbacks;

    static AttendDlgFragment newInstance(OnDialogCallbacks callbacks) {
        AttendDlgFragment f = new AttendDlgFragment();
        f.mCallbacks = callbacks;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_dlg_attend, container);
        Button submitBtn = (Button) view.findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCallbacks.onSubmit();
            }
        });
        return view;
    }

    public interface OnDialogCallbacks {
        void onSubmit();
    }

}
