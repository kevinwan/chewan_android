package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.MapActivity;

public class ActiveFilterPop extends PopupWindow implements OnClickListener
{
    Activity context;
    
    public PopupWindow pop;
    
    View contentV;
    
    long animduring = 300;
    
    int direction = 1;
    
    /**
     * 获取数据时提交的参数 "100:运动类型 101:活动综合排序 102:场馆综合排序 103:俱乐部综合排序 104:教练综合排序 105:陪练综合排序"
     */
    
    OnCheckResult onCheckResult;
    
    static ActiveFilterPop activeFilterPop;
    
    RadioGroup genderRG, authenticateRG, carLevelRG;
    
    View locationLayoutV, typeLayoutV;
    
    TextView addresssT, typeT;
    
    Button cancleB, okB;
    
    String province, city, district;
    
    public static final int Location = 1;
    
    public static final int Type = 2;
    
    public ActiveFilterPop(Activity context)
    {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.pop_active_filter, null);
        pop = new PopupWindow(contentV, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupMenu);
        initView();
    }
    
    public static ActiveFilterPop getInstance(Activity context)
    {
        if (activeFilterPop == null)
        {
            activeFilterPop = new ActiveFilterPop(context);
        }
        return activeFilterPop;
        
    }
    
    public void initView()
    {
        View bgV = contentV.findViewById(R.id.bg);
        bgV.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                pop.dismiss();
            }
        });
        
        genderRG = (RadioGroup)contentV.findViewById(R.id.gender_rg);
        authenticateRG = (RadioGroup)contentV.findViewById(R.id.authenticate_rg);
        carLevelRG = (RadioGroup)contentV.findViewById(R.id.carLevel_rg);
        locationLayoutV = contentV.findViewById(R.id.locationLayout);
        typeLayoutV = contentV.findViewById(R.id.typeLayout);
        addresssT = (TextView)contentV.findViewById(R.id.addresss);
        typeT = (TextView)contentV.findViewById(R.id.type);
        
        cancleB = (Button)contentV.findViewById(R.id.cancle);
        
        okB = (Button)contentV.findViewById(R.id.ok);
        cancleB.setOnClickListener(this);
        okB.setOnClickListener(this);
        locationLayoutV.setOnClickListener(this);
        typeLayoutV.setOnClickListener(this);
    }
    
    public void setAddress(String address)
    {
        addresssT.setText(address);
    }
    
    public void show(View v)
    {
        pop.showAsDropDown(v);
    }
    
    public OnCheckResult getOnCheckResult()
    {
        return onCheckResult;
    }
    
    public void setOnCheckResult(OnCheckResult onCheckResult)
    {
        this.onCheckResult = onCheckResult;
    }
    
    public interface OnCheckResult
    {
        void result(String province, String city, String district, String gender, String type, Integer authenticate,
            String carLevel);
    }
    
    private void getSelectResult()
    {
        String gender = "";
        
        Integer authenticate = null;
        
        String carLevel = "";
        if (genderRG.getCheckedRadioButtonId() != 0)
        {
            RadioButton grb = (RadioButton)genderRG.findViewById(genderRG.getCheckedRadioButtonId());
            gender = grb.getText().toString();
        }
        
        if (authenticateRG.getCheckedRadioButtonId() != 0)
        {
            RadioButton grb = (RadioButton)authenticateRG.findViewById(authenticateRG.getCheckedRadioButtonId());
            String authenticateString = grb.getText().toString();
            if (authenticateString.equals("车主"))
            {
                authenticate = 1;
            }
            else if (authenticateString.equals("非车主"))
            {
                authenticate = 2;
            }
            else
            {
                authenticate = 3;
            }
        }
        
        if (carLevelRG.getCheckedRadioButtonId() != 0)
        {
            RadioButton grb = (RadioButton)carLevelRG.findViewById(carLevelRG.getCheckedRadioButtonId());
            carLevel = grb.getText().toString();
        }
        
        if (onCheckResult != null)
        {
            onCheckResult.result(province, city, district, gender, typeT.getText().toString(), authenticate, carLevel);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cancle:
                pop.dismiss();
                break;
            
            case R.id.ok:
                getSelectResult();
                dismiss();
                break;
            
            case R.id.locationLayout:
                Intent it = new Intent(context, MapActivity.class);
                context.startActivityForResult(it, Location);
                break;
            
            case R.id.typeLayout:
                
                break;
            
            default:
                break;
        }
    }
    
}
