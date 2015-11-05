package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.PointRecord;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.ioc.IocContainer;
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

    private OnMatchingDialogResult mResult;

    public void setMatchingResult(OnMatchingDialogResult result) {
        mResult = result;
    }

    private int selectIndex = -1;

    private String type;

    User user;
    CarPlayPerference per;
    final int[] locations = new int[2];

    int color;


    public MateLayerDialog(Context context, String type) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext = context;
        this.type = type;
    }

    public  void  setCoclor (int color) {
        this.color = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mate_layer);
        initView();
    }

    private void initView() {
        user = User.getInstance();

        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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


        per = IocContainer.getShare().get(CarPlayPerference.class);
        per.load();
        if (per.isShowDialogGuilde == 0) {
            findViewById(R.id.guide_bg).setVisibility(View.VISIBLE);
            ViewTreeObserver
                    vto = textDestination.getViewTreeObserver();

            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {

                    textDestination.getViewTreeObserver().removeOnPreDrawListener(this);
                    textDestination.getLocationOnScreen(locations);

                    ImageView gideText = (ImageView) findViewById(R.id.guide_icon);
                    LinearLayout.LayoutParams pams = (LinearLayout.LayoutParams) gideText.getLayoutParams();
                    pams.topMargin = locations[1] - 288;
                    gideText.setLayoutParams(pams);

                    return true;
                }


            });
        }

        findViewById(R.id.know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                per.isShowDialogGuilde = 1;
                per.commit();
                findViewById(R.id.guide_bg).setVisibility(View.GONE);

            }
        });
        Button btnMatch = (Button) findViewById(R.id.btn_match);

        //设置默认选中值
        setDefault();

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = User.getInstance();
                if (user.isLogin()) {

                }

                final boolean pickOrNot = checkBox.isChecked();
                if (selectIndex < 0) {
                    Toast.makeText(mContext, "请选择类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                final DhNet dhNet = new DhNet(API2.getMatchUrl(User.getInstance().getUserId(), User.getInstance().getToken()));
                //类型
                dhNet.addParam("majorType", CarPlayUtil.getTypeName(type));
                dhNet.addParam("type", CarPlayUtil.getTypeName(type));
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

                if (textDestination.getText().toString().trim().length() == 0 || textDestination.getText().toString().trim().split(" ").length < 3) {
                    //没有选择地点
                } else {
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
                }

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
                if (user.isLogin()) {
                    dhNet.doPost(new NetTask(mContext) {
                        @Override
                        public void doInUI(Response response, Integer transfer) {
                            if (response.isSuccess()) {
                                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                                if (mResult != null) {
                                    mResult.onResult(dhNet.getParams());
                                }
                            }
                        }
                    });
                } else {
                    if (mResult != null) {
                        mResult.onResult(dhNet.getParams());
                    }
                }
                dismiss();
                PointRecord record = PointRecord.getInstance();
                record.setActivityMatchCount(record.getActivityMatchCount() + 1);
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
                dlg.getWindow().setBackgroundDrawableResource(color);
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

    private void setDefault() {
        if (!user.isLogin()) {
            selectIndex = 2;
            imgMyTreat.setBackgroundResource(R.drawable.mate_manner_n);
            imgAa.setBackgroundResource(R.drawable.mate_manner_y);
            imgYourTurn.setBackgroundResource(R.drawable.mate_manner_n);
        } else if ("男".equals(user.getGender())) {
            selectIndex = 1;
            imgMyTreat.setBackgroundResource(R.drawable.mate_manner_y);
            imgAa.setBackgroundResource(R.drawable.mate_manner_n);
            imgYourTurn.setBackgroundResource(R.drawable.mate_manner_n);
        }
        if ("女".equals(user.getGender())) {
            selectIndex = 3;
            imgMyTreat.setBackgroundResource(R.drawable.mate_manner_n);
            imgAa.setBackgroundResource(R.drawable.mate_manner_n);
            imgYourTurn.setBackgroundResource(R.drawable.mate_manner_y);
        }

    }

    public interface OnMatchingDialogResult {
        void onResult(Map<String, Object> params);
    }
}
