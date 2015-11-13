package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/26.
 */
public class NojoinOfficialDialog extends BaseAlertDialog{

    Context mContext;


    public NojoinOfficialDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_nojoin_official_msg);
        initView();
    }

    private void initView() {

        ((TextView)findViewById(R.id.join)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("报名参加");
                dismiss();
            }
        });
        ((TextView)findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


}
