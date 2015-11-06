package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.FilterPreference2;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.ioc.IocContainer;

/**
 * Created by Administrator on 2015/10/13.
 * 附近筛选弹框
 */
public class NearbyFilterDialog extends BaseAlertDialog implements
        RadioGroup.OnCheckedChangeListener {

    Context mContext;

    private RadioGroup active_typeG1, active_typeG2, active_typeG3, active_typeG4,active_pay,active_gender;

    private Button submit ;

    private ImageView closeI;

    private CheckBox active_transfer;

    String type="不限";
    String pay="不限";
    String gender="不限";
    boolean transfer=true;

    int type_id;
    int pay_id;
    int gender_id;

    FilterPreference2 pre;

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
        pre = IocContainer.getShare().get(FilterPreference2.class);

        active_typeG1 = (RadioGroup) this.findViewById(R.id.active_type1);
        active_typeG2 = (RadioGroup) this.findViewById(R.id.active_type2);
        active_typeG3 = (RadioGroup) this.findViewById(R.id.active_type3);
        active_typeG4 = (RadioGroup) this.findViewById(R.id.active_type4);
        active_pay = (RadioGroup) this.findViewById(R.id.active_pay);
        active_gender = (RadioGroup) this.findViewById(R.id.active_gender);

        active_transfer = (CheckBox) this.findViewById(R.id.active_transfer);

        submit = (Button) this.findViewById(R.id.submit);

        setDefault();
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
                transfer = active_transfer.isChecked();
                if (nearbyFilterResultListener != null) {
//                    System.out.println("type:" + type + "pay:" + pay + "gender:" + gender + "transfer" + transfer);
                    nearbyFilterResultListener.onResult(type.replace("不限",""), pay.replace("不限",""), gender.replace("不限",""), transfer);
                    pre.setType_id(type_id);
                    pre.setPay_id(pay_id);
                    pre.setGender_id(gender_id);
                    pre.commit();
                }
            }
        });

        closeI = (ImageView) findViewById(R.id.close);
        closeI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
    private void setCheackedPay(){
        active_pay.setOnCheckedChangeListener(null);
        active_pay.clearCheck();
        active_pay.setOnCheckedChangeListener(this);
    }
    private void setCheackedGender(){
        active_gender.setOnCheckedChangeListener(null);
        active_gender.clearCheck();
        active_gender.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton;
        switch (group.getId()) {
            case R.id.active_type1:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type_id = group.getCheckedRadioButtonId();
                type=radioButton.getText().toString();
                setCheackedG2();
                setCheackedG3();
                setCheackedG4();
                break;
            case R.id.active_type2:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type_id = group.getCheckedRadioButtonId();
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG3();
                setCheackedG4();
                break;
            case R.id.active_type3:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type_id = group.getCheckedRadioButtonId();
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG2();
                setCheackedG4();
                break;
            case R.id.active_type4:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                type_id = group.getCheckedRadioButtonId();
                type=radioButton.getText().toString();
                setCheackedG1();
                setCheackedG2();
                setCheackedG3();
                break;
            case R.id.active_pay:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                pay_id = group.getCheckedRadioButtonId();
                pay=radioButton.getText().toString();
                break;
            case R.id.active_gender:
                radioButton =(RadioButton)findViewById(group.getCheckedRadioButtonId());
                gender_id = group.getCheckedRadioButtonId();
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

    /**
     * 设置默认值
     */
    private void setDefault(){
        //读取记录id
        pre.load();

        type_id = pre.getType_id();
        pay_id = pre.getPay_id();
        gender_id =  pre.getGender_id();

        type = pre.getType();
        pay = pre.getPay();
        gender  = pre.getGender();

        //设置邀请类型
        active_pay.check(pre.getPay_id() == -1 ? R.id.pay_3 : pre.getPay_id());
        //设置性别
        active_gender.check(pre.getGender_id()==-1 ? R.id.gender_3 : pre.getGender_id());
        //是否包接送
        active_transfer.setChecked(pre.isTransfer());
        //设置活动类型
        for (int i = 0;i<active_typeG1.getChildCount();i++){
            if (active_typeG1.getChildAt(i).getId()==pre.getType_id()){
                active_typeG1.check(pre.getType_id());
                return;
            }
        }
        for (int i = 0;i<active_typeG2.getChildCount();i++){
            if (active_typeG2.getChildAt(i).getId()==pre.getType_id()){
                active_typeG2.check(pre.getType_id());
                return;
            }
        }
        for (int i = 0;i<active_typeG3.getChildCount();i++){
            if (active_typeG3.getChildAt(i).getId()==pre.getType_id()){
                active_typeG3.check(pre.getType_id());
                return;
            }
        }
        for (int i = 0;i<active_typeG4.getChildCount();i++){
            if (active_typeG4.getChildAt(i).getId()==pre.getType_id()){
                active_typeG4.check(pre.getType_id());
                return;
            }
        }
        active_typeG4.check(R.id.rb11);
    }

    //隐藏付费类型 并设置默认值
    private void gonePay(){

    }
}
