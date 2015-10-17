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

    RadioGroup active_typeG1, active_typeG2, active_typeG3, active_typeG4;


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

        active_typeG1 = (RadioGroup) this.findViewById(R.id.active_type1);
        active_typeG2 = (RadioGroup) this.findViewById(R.id.active_type2);
        active_typeG3 = (RadioGroup) this.findViewById(R.id.active_type3);
        active_typeG4 = (RadioGroup) this.findViewById(R.id.active_type4);


        //响应单选框组内的选中项发生变化时的事件
        active_typeG1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                active_typeG2.clearCheck();
                active_typeG3.clearCheck();
                active_typeG4.clearCheck();
            }
        });


        //响应单选框组内的选中项发生变化时的事件
        active_typeG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                active_typeG1.clearCheck();
                active_typeG3.clearCheck();
                active_typeG4.clearCheck();
            }
        });


        //响应单选框组内的选中项发生变化时的事件
        active_typeG3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                active_typeG2.clearCheck();
                active_typeG1.clearCheck();
                active_typeG4.clearCheck();
            }
        });


        //响应单选框组内的选中项发生变化时的事件
        active_typeG4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                active_typeG2.clearCheck();
                active_typeG3.clearCheck();
                active_typeG1.clearCheck();
            }
        });


    }

    /**
     * 查找radioButton控件
     */
    public RadioButton findRadioButton(ViewGroup group) {
        RadioButton resBtn = null;
        int len = group.getChildCount();
        for (int i = 0; i < len; i++) {
            if (group.getChildAt(i) instanceof RadioButton) {
                resBtn = (RadioButton) group.getChildAt(i);
                group.addView(resBtn);
            } else if (group.getChildAt(i) instanceof ViewGroup) {
                resBtn = findRadioButton((ViewGroup) group.getChildAt(i));
                group.addView(resBtn);
            }
        }
        return resBtn;
    }

}
