package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/12.
 * 官方活动详情弹出框
 */
public class OfficialMsgDialog extends BaseAlertDialog {

    Context mContext;


    public OfficialMsgDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_official_msg);
        initView();
    }

    private void initView() {


    }


}
