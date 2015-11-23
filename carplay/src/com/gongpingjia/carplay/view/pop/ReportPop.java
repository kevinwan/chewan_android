package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * 举报
 *
 * Created by Administrator on 2015/11/21.
 */
public class ReportPop implements  View.OnClickListener {

    Activity context;
    static ReportPop instance;
    View contentV;
    String activityid,id;
    String type ;
    PopupWindow pop;
    User user = User.getInstance();
    TextView pornography,advertising,political,bilk,illegal,cancel,submit;
    public ReportPop(final Activity context ,String activityid , String id) {
        this.context = context;
        this.activityid = activityid;
        this.id = id;
        contentV = LayoutInflater.from(context).inflate(R.layout.report, null);
        pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupLookAround);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();

    }
    public static ReportPop getInstance(Activity context,String activityid , String id) {
        instance = new ReportPop(context,activityid,id);
        return instance;
    }
    public void initView(){
        System.out.println("接受举报id"+activityid+"和id"+id);
        contentV.findViewById(R.id.pop_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pornography = (TextView) contentV.findViewById(R.id.pornography);
        advertising = (TextView) contentV.findViewById(R.id.advertising);
        political = (TextView) contentV.findViewById(R.id.political);
        bilk = (TextView) contentV.findViewById(R.id.bilk);
        illegal = (TextView) contentV.findViewById(R.id.illegal);
        cancel = (TextView) contentV.findViewById(R.id.cancel);
        submit = (TextView) contentV.findViewById(R.id.submit);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        illegal.setOnClickListener(this);
        bilk.setOnClickListener(this);
        political.setOnClickListener(this);
        advertising.setOnClickListener(this);
        pornography.setOnClickListener(this);

    }
    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pornography://色情
                type = "色情低俗";
                request();
                break;
//            case R.id.submit://提交
//                request();
//                break;
            case R.id.cancel://取消
                pop.dismiss();
                break;
            case R.id.illegal://违法
                type = "违法";
                request();
                break;
            case R.id.bilk://诈骗
                type = "诈骗";
                request();
                break;
            case R.id.political://政治
                type = "政治敏感";
                request();
                break;
            case R.id.advertising://广告
                type = "广告骚扰";
                request();
                break;
        }

    }
    public void request(){
        DhNet net = new DhNet(API2.CWBaseurl+"user/"+id+"/report?userId="+user.getUserId()+"&token="+user.getToken()+"&activityId="+activityid);
        net.addParam("type",type);
        net.doPostInDialog(new NetTask(context) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()){

                    Toast.makeText(context,"举报成功",Toast.LENGTH_SHORT).show();
                    pop.dismiss();
                    System.out.println(response.isSuccess());
                }

            }
        });
    }


}