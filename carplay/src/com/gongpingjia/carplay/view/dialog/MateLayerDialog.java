package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/12.
 * 匹配意向弹框(请我,AA,我请)
 */
public class MateLayerDialog extends BaseAlertDialog {

    Context mContext;


    public MateLayerDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mate_layer);
        initView();
    }

    private void initView() {


    }


}
