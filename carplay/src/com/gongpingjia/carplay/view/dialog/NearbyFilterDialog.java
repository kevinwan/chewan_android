package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/13.
 * 附近筛选弹框
 */
public class NearbyFilterDialog extends BaseAlertDialog implements
        RadioGroup.OnCheckedChangeListener {

    Context mContext;

    private RadioGroup active_typeG1, active_typeG2, active_typeG3, active_typeG4,active_pay,active_gender;

    private Button submit ;


    private CheckBox active_transfer;

    String type="";
    String pay="";
    String gender="";
    boolean transfer=true;

    OnNearbyFilterResultListener nearbyFilterResultListener;

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
        active_pay = (RadioGroup) this.findViewById(R.id.active_pay);
        active_gender = (RadioGroup) this.findViewById(R.id.active_gender);

        active_transfer = (CheckBox) this.findViewById(R.id.active_transfer);

        submit = (Button) this.findViewById(R.id.submit);

        //响应单选框组内的选中项发生变化时的事件
        active_typeG1.setOnCheckedChangeListener(this);
        active_typeG2.setOnCheckedChangeListener(this);
        active_typeG3.setOnCheckedChangeListener(this);
        active_typeG4.setOnCheckedChangeListener(this);
        active_pay.setOnCheckedChangeListener(this);
        active_gender.setOnCheckedChangeListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                transfer=active_transfer.isChecked();
                if (nearbyFilterResultListener != null) {
                    nearbyFilterResultListener.onResult(type,pay,gender,transfer);
                }

            }
        });


    }

    private void setCheackedG1(){
        active_typeG1.setOnCheckedChangeListener(null);
        active_typeG1.clearCheck();
        active_typeG1.setOnCheckedChangeListener(this);
    }
    private void setCheackedG2(){
        active_typeG2.setOnCheckedChangeListener(null);
        active_typeG2.clearCheck();
        active_typeG2.setOnCheckedChangeListener(this);
    }
    private void setCheackedG3(){
        active_typeG3.setOnCheckedChangeListener(null);
        active_typeG3.clearCheck();
        active_typeG3.setOnCheckedChangeListener(this);
    }
    private void setCheackedG4(){
        active_typeG4.setOnCheckedChangeListener(null);
        active_typeG4.clearCheck();
        active_typeG4.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton;
        switch (group.getId()) {
            case R.id.active_type1:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type=radioButton.getText().toString();
                setCheackedG2();
                setCheackedG3();
                setCheackedG4();
                break;
            case R.id.active_type2:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG3();
                setCheackedG4();
                break;
            case R.id.active_type3:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG2();
                setCheackedG4();
                break;
            case R.id.active_type4:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG2();
                setCheackedG3();
                break;
            case R.id.active_pay:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                pay=radioButton.getText().toString();
                break;
            case R.id.active_gender:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                gender=radioButton.getText().toString();
                break;

            default:
                break;
        }
    }

    public interface OnNearbyFilterResultListener {
        void onResult(String type, String pay, String gender, boolean transfer);
    }

    public OnNearbyFilterResultListener getOnNearbyFilterResultListener() {
        return nearbyFilterResultListener;
    }

    public void setOnNearbyFilterResultListener(
            OnNearbyFilterResultListener nearbyFilterResultListener) {
        this.nearbyFilterResultListener = nearbyFilterResultListener;
    }
}
