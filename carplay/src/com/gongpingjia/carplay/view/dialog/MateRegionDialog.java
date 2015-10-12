package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

/**
 * Created by Administrator on 2015/10/12.
 * 匹配意向弹框(区域选择)
 */
public class MateRegionDialog extends BaseAlertDialog {

    Context mContext;

    OnMateRegionResultListener mateRegionResultListener;

    public MateRegionDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mate_region);
        initView();
    }

    private void initView() {


    }

    public interface OnMateRegionResultListener {
        void onResult(String province, String city, String district);
    }

    public OnMateRegionResultListener getOnMateRegionResultListener() {
        return mateRegionResultListener;
    }

    public void setOnMateRegionResultListener(
            OnMateRegionResultListener mateRegionResultListener) {
        this.mateRegionResultListener = mateRegionResultListener;
    }

}
