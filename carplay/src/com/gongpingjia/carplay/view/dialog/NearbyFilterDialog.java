package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/13.
 * 附近筛选弹框
 */
public class NearbyFilterDialog extends BaseAlertDialog {

    Context mContext;


    public NearbyFilterDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_nearby_filter);
        initView();
    }

    private void initView() {

        RadioGroup group = (RadioGroup) this.findViewById(R.id.active_type);

        //响应单选框组内的选中项发生变化时的事件
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

    }

    /** 查找radioButton控件 */
    public RadioButton findRadioButton(ViewGroup group) {
        RadioButton resBtn = null;
        int len = group.getChildCount();
        for (int i = 0; i < len; i++) {
            if (group.getChildAt(i) instanceof RadioButton) {
                resBtn = (RadioButton) group.getChildAt(i);
                group.addView(resBtn);
            } else if (group.getChildAt(i) instanceof ViewGroup) {
                resBtn=findRadioButton((ViewGroup) group.getChildAt(i));
                group.addView(resBtn);
            }
        }
        return resBtn;
    }

}
