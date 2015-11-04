package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.MatchingAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.Matching;
import com.gongpingjia.carplay.bean.PointRecord;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MatchingDialog extends BaseAlertDialog {

    private List<Matching> mDatas;
    private CheckBox checkBox;
    private TextView textDestination;
    private Context context;

    private OnMatchingDialogResult mResult;
    CarPlayPerference per;

    int dialogType = 0;

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
        textDestination = (TextView) findViewById(R.id.tv_destination);
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

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                final DhNet dhNet = new DhNet(API2.getMatchUrl(User.getInstance().getUserId(), User.getInstance().getToken()));
                //类型
                if (mDatas.size() == 1) {
                    //覆盖主类型
                    dhNet.addParam("majorType", type);
                } else {
                    if (dialogType == 0) {
                        dhNet.addParam("majorType", "运动");
                    } else {
                        dhNet.addParam("majorType", "桌游");
                    }
                }
                dhNet.addParam("type", type);
                dhNet.addParam("transfer", pickOrNot);

                //暂时写死
                dhNet.addParam("pay", "我请客");


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
                User user = User.getInstance();
                if (user.isLogin()) {
                    dhNet.doPost(new NetTask(context) {
                        @Override
                        public void doInUI(Response response, Integer transfer) {
                            if (response.isSuccess()) {
                                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
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
                dlg.show();
            }
        });
    }

    public interface OnMatchingDialogResult {
        void onResult(Map<String, Object> params);
    }
}
