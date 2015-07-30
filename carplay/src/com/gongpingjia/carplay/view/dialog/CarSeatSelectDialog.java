package com.gongpingjia.carplay.view.dialog;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.dialog.CommonDialog.OnCommonDialogItemClickListener;

public class CarSeatSelectDialog extends AlertDialog
{
    
    RadioGroup radiogroup;
    
    OnSelectResultListener onSelectResultListener;
    
    TextView countT;
    
    Context mContext;
    
    private List<String> mSeatOptions;
    
    View seatLayoutV;
    
    TextView desT;
    
    public CarSeatSelectDialog(Context context)
    {
        super(context);
        this.mContext = context;
        mSeatOptions = new ArrayList<String>();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_carseat_select);
        radiogroup = (RadioGroup)findViewById(R.id.radiogroup);
        countT = (TextView)findViewById(R.id.count);
        desT = (TextView)findViewById(R.id.des);
        seatLayoutV = findViewById(R.id.seatLayout);
        seatLayoutV.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (mSeatOptions.size() == 0)
                {
                    IocContainer.getShare().get(IDialog.class).showToastShort(mContext, "正在加载可提供座位的数量,请稍等!");
                    return;
                }
                CommonDialog dlg = new CommonDialog(mContext, mSeatOptions, "请选择提供座位数");
                dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener()
                {
                    
                    @Override
                    public void onDialogItemClick(int position)
                    {
                        // TODO Auto-generated method stub
                        countT.setText(mSeatOptions.get(position));
                    }
                });
                dlg.show();
            }
        });
        Button btn_submitB = (Button)findViewById(R.id.btn_submit);
        btn_submitB.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onSelectResultListener != null)
                {
                    onSelectResultListener.click(Integer.parseInt(countT.getText().toString()));
                }
                dismiss();
            }
        });
        getSeatCount();
    }
    
    private void getSeatCount()
    {
        User user = User.getInstance();
        DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId() + "/seats?token=" + user.getToken());
        net.doGet(new NetTask(mContext)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    JSONObject json = response.jSONFrom("data");
                    try
                    {
                        if (json.getInt("isAuthenticated") == 1)
                        {
                            // 认证车主
                            int minSeat = json.getInt("minValue");
                            int maxSeat = json.getInt("maxValue");
                            for (int i = minSeat; i <= maxSeat; i++)
                            {
                                mSeatOptions.add(String.valueOf(i));
                            }
                        }
                        else
                        {
                            // 未认证
                            desT.setText("邀请人数");
                            mSeatOptions.add("1");
                            mSeatOptions.add("2");
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    public OnSelectResultListener getOnSelectResultListener()
    {
        return onSelectResultListener;
    }
    
    public void setOnSelectResultListener(OnSelectResultListener onSelectResultListener)
    {
        this.onSelectResultListener = onSelectResultListener;
    }
    
    public interface OnSelectResultListener
    {
        void click(int seatCount);
    }
    
}
