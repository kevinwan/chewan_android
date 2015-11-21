package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.pop.SharePop;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActiveShareActivity extends CarPlayBaseActivity {

    private ImageView activeImgI;
    private EditText remarksEt;
    private TextView titleT, startTimeT, priceT, placeT;
    private Button shareBtn;

    private String activeid="";

    String sTitle="";
    String starttime="";
    String price="";
    String place="";
    String active_img;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_share);
    }

    @Override
    public void initView() {
        setTitle("分享");
        user = User.getInstance();
        activeid = getIntent().getStringExtra("activityId");

        activeImgI = (ImageView) findViewById(R.id.active_img);
        remarksEt = (EditText) findViewById(R.id.remarks);
        titleT = (TextView) findViewById(R.id.activetitle);
        startTimeT = (TextView) findViewById(R.id.starttime);
        priceT = (TextView) findViewById(R.id.price);
        placeT = (TextView) findViewById(R.id.place);
        shareBtn = (Button) findViewById(R.id.share);

        getActiveDetailsData();

        //分享按钮
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shareUrl","http://www.baidu.com");
                bundle.putString("shareTitle",sTitle);
                bundle.putString("shareContent",starttime+price+place);
                bundle.putString("image",active_img);
                SharePop pop = new SharePop(self, bundle,0);
                pop.show();
            }
        });
    }

    private void getActiveDetailsData() {
        DhNet verifyNet = new DhNet(API2.ActiveDetails + activeid + "/info?userId=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGetInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    JSONArray jsc = JSONUtil.getJSONArray(jo, "covers");

                    sTitle = "我正在参加 " + JSONUtil.getString(jo, "title") + "活动 ,来跟我一起参加吧~";
                    SimpleDateFormat format = new SimpleDateFormat("MM年dd月 HH:ss");
                    Date sdate = new Date(JSONUtil.getLong(jo, "start"));
                    starttime = format.format(sdate);

                    double dPrice = JSONUtil.getDouble(jo, "price");
                    if (dPrice == 0) {
                        price = "价格 : 免费";
                    } else {
                        price = "价格 : " + dPrice + "元/人";
                    }
                    //目的地
                    JSONObject js = JSONUtil.getJSONObject(jo, "destination");
                    place = JSONUtil.getString(js, "province") + "省"+ JSONUtil.getString(js, "detail");
                    try {
                        active_img = jsc.get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ViewUtil.bindView(titleT,sTitle);
                    ViewUtil.bindView(startTimeT,starttime);
                    ViewUtil.bindView(priceT,price);
                    ViewUtil.bindView(placeT,place);
                    ViewUtil.bindView(activeImgI,active_img,"default");


//        bundle.putString("shareUrl","http://www.baidu.com");
//        bundle.putString("shareTitle",title);
//        bundle.putString("shareContent",starttime+price+place);
//        try {
//            bundle.putString("image",jsc.get(0).toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
                }
            }
        });
    }

}
