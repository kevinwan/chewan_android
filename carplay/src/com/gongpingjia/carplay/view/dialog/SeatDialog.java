package com.gongpingjia.carplay.view.dialog;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.util.CarPlayUtil;

/**
 * 成员管理 弹出框
 * 
 * @author wang
 * 
 */
public class SeatDialog extends AlertDialog
{
    
    Context context;
    
    JSONObject memberJo;
    
    OnGradResultListener onGradResultListener;
    
    OnPullDownResultListener onPullDownResultListener;
    
    String carId;
    
    int seatIndex;
    
    public SeatDialog(Context context)
    {
        super(context);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_down_seat);
    }
    
    private void initView()
    {
        Button cancleB = (Button)findViewById(R.id.cancel);
        cancleB.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        
        Button okB = (Button)findViewById(R.id.ok);
        okB.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onGradResultListener != null)
                {
                    onGradResultListener.click(carId, seatIndex);
                }
                
                if (onPullDownResultListener != null)
                {
                    onPullDownResultListener.click(JSONUtil.getString(memberJo, "userId"));
                }
                dismiss();
            }
        });
        
        if (memberJo != null)
        {
            okB.setText("拉下座位");
            findViewById(R.id.age_layout).setVisibility(View.VISIBLE);
            ViewUtil.bindView(findViewById(R.id.des), "占座中...");
            ViewUtil.bindView(findViewById(R.id.name), JSONUtil.getString(memberJo, "nickname"));
            ViewUtil.bindView(findViewById(R.id.age), JSONUtil.getString(memberJo, "age"));
            ViewUtil.bindNetImage((ImageView)findViewById(R.id.head), JSONUtil.getString(memberJo, "photo"), "default");
            CarPlayUtil.bindSexView(JSONUtil.getString(memberJo, "gender"), findViewById(R.id.person_sex));
        }
        else
        {
            okB.setText("立即抢座");
            ViewUtil.bindView(findViewById(R.id.des), "虚位以待...");
            findViewById(R.id.age_layout).setVisibility(View.GONE);
        }
        
    }
    
    public void setData(JSONObject memberJo)
    {
        this.memberJo = memberJo;
        initView();
    }
    
    public void setSeatData(String carId, int seatIndex)
    {
        this.carId = carId;
        this.seatIndex = seatIndex;
        initView();
    }
    
    public OnGradResultListener getOnGradResultListener()
    {
        return onGradResultListener;
    }
    
    public void setOnGradResultListener(OnGradResultListener onGradResultListener)
    {
        this.onGradResultListener = onGradResultListener;
    }
    
    public OnPullDownResultListener getOnPullDownResultListener()
    {
        return onPullDownResultListener;
    }
    
    public void setOnPullDownResultListener(OnPullDownResultListener onPullDownResultListener)
    {
        this.onPullDownResultListener = onPullDownResultListener;
    }
    
    /*** 拉下座位 */
    public interface OnPullDownResultListener
    {
        void click(String userid);
    }
    
    /*** 立即抢座 */
    public interface OnGradResultListener
    {
        void click(String carId, int seatIndex);
    }
}
