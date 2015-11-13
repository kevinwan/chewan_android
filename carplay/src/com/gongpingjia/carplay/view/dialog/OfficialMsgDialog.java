package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/12.
 * 官方活动详情弹出框
 */
public class OfficialMsgDialog extends BaseAlertDialog {

    Context mContext;

    private CheckBox isinarchCheck;
    private TextView contentT;
    private Button sendBtn;

    OnOfficialResultListener officialResultListener;

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
        isinarchCheck = (CheckBox) findViewById(R.id.isinarch);
        contentT = (TextView) findViewById(R.id.content);
        sendBtn = (Button) findViewById(R.id.send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (officialResultListener != null) {
                    officialResultListener.onResult(isinarchCheck.isChecked(),contentT.getText().toString());
                }
                dismiss();
            }
        });

    }

    public interface OnOfficialResultListener {
        void onResult(boolean isinarch,String content);
    }

    public OnOfficialResultListener getOnOfficialResultListener() {
        return officialResultListener;
    }

    public void setOnOfficialResultListener(
            OnOfficialResultListener officialResultListener) {
        this.officialResultListener = officialResultListener;
    }


}
