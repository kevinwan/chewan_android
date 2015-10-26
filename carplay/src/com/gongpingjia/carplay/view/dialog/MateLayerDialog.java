package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/12.
 * 匹配意向弹框(请我,AA,我请)
 */
public class MateLayerDialog extends BaseAlertDialog implements View.OnClickListener {

    Context mContext;
    private CheckBox checkBox;
    private TextView textDestination;
    private ImageView imgMyTreat, imgAa, imgYourTurn;

    private int selectIndex = -1;

    private String type;

    public MateLayerDialog(Context context, String type) {
        super(context);
        this.mContext = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mate_layer);
        initView();
    }

    private void initView() {

        View viewMyTreat = findViewById(R.id.layout_my_treat);
        View viewAa = findViewById(R.id.layout_aa);
        View viewYourTurn = findViewById(R.id.layout_your_turn);

        viewMyTreat.setOnClickListener(this);
        viewAa.setOnClickListener(this);
        viewYourTurn.setOnClickListener(this);

        imgAa = (ImageView) findViewById(R.id.iv_pay_aa);
        imgMyTreat = (ImageView) findViewById(R.id.iv_pay_my_treat);
        imgYourTurn = (ImageView) findViewById(R.id.iv_pay_your_turn);

        checkBox = (CheckBox) findViewById(R.id.chk_pick);
        textDestination = (TextView) findViewById(R.id.tv_destination);
        Button btnMatch = (Button) findViewById(R.id.btn_match);

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pickOrNot = checkBox.isChecked();
//                if (textDestination.getText().toString().trim().length() == 0 || textDestination.getText().toString().trim().split(" ").length < 3) {
//                    //请选择地点
//                    Toast.makeText(mContext, "请选择地点", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (selectIndex < 0) {
                    Toast.makeText(mContext, "请选择类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                DhNet dhNet = new DhNet(API2.getMatchUrl(User.getInstance().getUserId(), User.getInstance().getToken()));
                //类型
                dhNet.addParam("majorType", type);
                dhNet.addParam("type", type);
                dhNet.addParam("transfer", pickOrNot);

                switch (selectIndex) {
                    case 1:
                        dhNet.addParam("pay", "我请客");
                        break;
                    case 2:
                        dhNet.addParam("pay", "AA制");
                        break;
                    case 3:
                        dhNet.addParam("pay", "请我吧");
                        break;
                }

                //目的地信息
                String[] destinations = textDestination.getText().toString().trim().split(" ");
                Map<String, String> destination = new HashMap<String, String>();
                destination.put("province", destinations[0]);
                destination.put("city", destinations[1]);
                destination.put("district", destinations[2]);
                if (destinations.length == 3) {
                    //直辖市的情况
                    destination.put("street", destinations[2]);
                } else {
                    //普通地区
                    destination.put("street", destinations[3]);
                }
                dhNet.addParam("destination", destination);

                //发布地经纬度
                Map<String, Double> estabPoint = new HashMap<String, Double>();
                estabPoint.put("longitude", UserLocation.getInstance().getLongitude());
                estabPoint.put("latitude", UserLocation.getInstance().getLatitude());
                dhNet.addParam("estabPoint", estabPoint);

                //发布地地理位置信息
                Map<String, String> establish = new HashMap<String, String>();
                establish.put("province", UserLocation.getInstance().getProvice());
                establish.put("city", UserLocation.getInstance().getCity());
                establish.put("district", UserLocation.getInstance().getDistrict());
                dhNet.addParam("establish", establish);
                dhNet.doPost(new NetTask(mContext) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        dismiss();
                        if (response.isSuccess()) {
                            Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //区域选择
        textDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MateRegionDialog dlg = new MateRegionDialog(mContext);
                dlg.setOnMateRegionResultListener(new MateRegionDialog.OnMateRegionResultListener() {
                    @Override
                    public void onResult(String place) {
                        textDestination.setText(place);
                    }
                });
                dlg.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_my_treat:
                selectIndex = 1;
                imgMyTreat.setBackgroundResource(R.drawable.mate_manner_y);
                imgAa.setBackgroundResource(R.drawable.mate_manner_n);
                imgYourTurn.setBackgroundResource(R.drawable.mate_manner_n);
                break;
            case R.id.layout_aa:
                selectIndex = 2;
                imgMyTreat.setBackgroundResource(R.drawable.mate_manner_n);
                imgAa.setBackgroundResource(R.drawable.mate_manner_y);
                imgYourTurn.setBackgroundResource(R.drawable.mate_manner_n);
                break;
            case R.id.layout_your_turn:
                selectIndex = 3;
                imgMyTreat.setBackgroundResource(R.drawable.mate_manner_n);
                imgAa.setBackgroundResource(R.drawable.mate_manner_n);
                imgYourTurn.setBackgroundResource(R.drawable.mate_manner_y);
                break;
        }
    }
}
