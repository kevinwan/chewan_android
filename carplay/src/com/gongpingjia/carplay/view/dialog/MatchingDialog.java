package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.MatchingAdapter;
import com.gongpingjia.carplay.bean.Matching;
import com.gongpingjia.carplay.bean.MatchingEB;
import com.gongpingjia.carplay.bean.PointRecord;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.UserLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MatchingDialog extends BaseAlertDialog {

    private List<Matching> mDatas;
    private CheckBox checkBox;
    private RadioGroup paygroup;
    private TextView textDestination;
    private Context context;

    private OnMatchingDialogResult mResult;

    int dialogType = 0;

    final int[] locations = new int[2];
    CarPlayPerference per;

    int color;

    public void setMatchingResult(OnMatchingDialogResult result) {
        mResult = result;
    }

    public MatchingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public MatchingDialog(Context context, List<Matching> data) {
        super(context, R.style.Dialog_Fullscreen);
        mDatas = data;
        this.context = context;

    }

    public MatchingDialog(Context context, List<Matching> data, int type) {
        super(context, R.style.Dialog_Fullscreen);
        mDatas = data;
        this.context = context;
        dialogType = type;
    }

    public void setCoclor(int color) {
        this.color = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_match_intention);


        findViewById(R.id.bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        checkBox = (CheckBox) findViewById(R.id.chk_pick);
        checkBox.setChecked("女".equals(User.getInstance().getGender()) ? false : true);
        textDestination = (TextView) findViewById(R.id.tv_destination);
        paygroup = (RadioGroup) findViewById(R.id.paygroup);
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

        GridView gridView = (GridView) findViewById(R.id.gv_matching);
        MatchingAdapter mAdapter = new MatchingAdapter(getContext(), mDatas);

        //只传一个参数时，直接表示类型，不需要选择
        if (mDatas.size() == 1) {
            //隐藏GridView
            gridView.setVisibility(View.GONE);
        } else {
            gridView.setAdapter(mAdapter);
        }

        if (dialogType == 0) {
            paygroup.setVisibility(View.GONE);
        } else {
            paygroup.setVisibility(View.VISIBLE);
            paygroup.check(R.id.pay_aa);
        }


        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMatchIntention();
            }
        });

        textDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MateRegionDialog dlg = new MateRegionDialog(context);
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

    private void setMatchIntention() {


        boolean pickOrNot = checkBox.isChecked();
        String type = null;
        for (Matching matching : mDatas) {
            if (matching.isChecked()) {
                type = matching.getName();
                break;
            }
        }
        if (type == null) {
            Toast.makeText(context, "请选择类型", Toast.LENGTH_SHORT).show();
            return;
        }

        MatchingEB matchingEB = new MatchingEB();

//        final DhNet dhNet = new DhNet(API2.getMatchUrl(User.getInstance().getUserId(), User.getInstance().getToken()));
        //类型
        if (mDatas.size() == 1) {
            //覆盖主类型
            matchingEB.setMajorType(CarPlayUtil.getTypeName(type));
//            dhNet.addParam("majorType", CarPlayUtil.getTypeName(type));
        } else {
            if (dialogType == 0) {
                matchingEB.setMajorType("运动");
                matchingEB.setPay("");
//                dhNet.addParam("majorType", "运动");
//                dhNet.addParam("pay","");
                paygroup.setVisibility(View.GONE);
            } else {
//                dhNet.addParam("majorType", "桌游");
                matchingEB.setMajorType("桌游");
                paygroup.setVisibility(View.VISIBLE);
                String pay = ((RadioButton) findViewById(paygroup.getCheckedRadioButtonId())).getText().toString();
//                dhNet.addParam("pay", pay);
                matchingEB.setPay(pay);
            }
        }
//        dhNet.addParam("type", CarPlayUtil.getTypeName(type));
//        dhNet.addParam("transfer", pickOrNot);
        matchingEB.setType(CarPlayUtil.getTypeName(type));
        matchingEB.setTransfer(pickOrNot);

        //目的地信息
        if (textDestination.getText().toString().trim().length() == 0 || textDestination.getText().toString().trim().split(" ").length < 3) {
            //没有填写目的地
        } else {
            //填写目的地
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
//            dhNet.addParam("destination", destination);
            matchingEB.setDestination(destination);
        }

        //发布地经纬度
        Map<String, Double> estabPoint = new HashMap<String, Double>();
        estabPoint.put("longitude", UserLocation.getInstance().getLongitude());
        estabPoint.put("latitude", UserLocation.getInstance().getLatitude());
//        dhNet.addParam("estabPoint", estabPoint);
        matchingEB.setEstabPoint(estabPoint);

        //发布地地理位置信息
        Map<String, String> establish = new HashMap<String, String>();
        establish.put("province", UserLocation.getInstance().getProvice());
        establish.put("city", UserLocation.getInstance().getCity());
        establish.put("district", UserLocation.getInstance().getDistrict());
//        dhNet.addParam("establish", establish);
        matchingEB.setEstablish(establish);
        User user = User.getInstance();
        if (user.isLogin()) {
            EventBus.getDefault().post(matchingEB);
            if (mResult != null) {
                mResult.onResult(null);
            }
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("majorType", matchingEB.getMajorType());
            params.put("type", matchingEB.getType());
            params.put("transfer", matchingEB.isTransfer());
            params.put("pay", matchingEB.getPay());
            params.put("destination", matchingEB.getDestination());
            params.put("estabPoint", matchingEB.getEstabPoint());
            params.put("establish", matchingEB.getEstablish());
            if (mResult != null) {
                mResult.onResult(params);
            }
        }
        dismiss();
        PointRecord record = PointRecord.getInstance();
        record.setActivityMatchCount(record.getActivityMatchCount() + 1);


//        User user = User.getInstance();
//        if (user.isLogin()) {
//            dhNet.doPost(new NetTask(context) {
//                @Override
//                public void doInUI(Response response, Integer transfer) {
//                    if (response.isSuccess()) {
//                        Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
//                        if (mResult != null) {
//                            mResult.onResult(dhNet.getParams());
//                        }
//                    }
//
//                    dismiss();
//                    PointRecord record = PointRecord.getInstance();
//                    record.setActivityMatchCount(record.getActivityMatchCount() + 1);
//                }
//            });
//        } else {
//            if (mResult != null) {
//                mResult.onResult(dhNet.getParams());
//            }
//
//            dismiss();
//            PointRecord record = PointRecord.getInstance();
//            record.setActivityMatchCount(record.getActivityMatchCount() + 1);
//        }
    }

    public interface OnMatchingDialogResult {
        void onResult(Map<String, Object> params);
    }
}
