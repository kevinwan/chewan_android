package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;


/**
 * 我的页面
 * +* @author Administrator
 */
public class MyFragment2 extends Fragment {
    View mainV;
    static MyFragment2 instance;

    private ImageView headI,sexI;
    private TextView attestationT,nameT,ageT,completenessT;
    private Button perfectBtn;
    private RelativeLayout sexbgR,photo_bgR;
    private LinearLayout myphotoL,myactiveL,myattentionL,headattestationL,carattestationL;

    public static MyFragment2 getInstance() {
        if (instance == null) {
            instance = new MyFragment2();
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mainV = inflater.inflate(R.layout.fragment_my2, null);
        initView();
        return mainV;
    }

    private void initView() {
        headI= (ImageView) mainV.findViewById(R.id.head);
        attestationT = (TextView) mainV.findViewById(R.id.attestation);
        nameT = (TextView) mainV.findViewById(R.id.name);
        sexbgR= (RelativeLayout) mainV.findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) mainV.findViewById(R.id.iv_sex);
        ageT = (TextView) mainV.findViewById(R.id.tv_age);
        completenessT = (TextView) mainV.findViewById(R.id.txt_completeness);
        perfectBtn = (Button) mainV.findViewById(R.id.perfect);
        photo_bgR = (RelativeLayout) mainV.findViewById(R.id.photo_bg);
        myphotoL = (LinearLayout) mainV.findViewById(R.id.myphoto);
        myactiveL = (LinearLayout) mainV.findViewById(R.id.myactive);
        myattentionL = (LinearLayout) mainV.findViewById(R.id.myattention);
        headattestationL = (LinearLayout) mainV.findViewById(R.id.headattestation);
        carattestationL = (LinearLayout) mainV.findViewById(R.id.carattestation);

        getMyDetails();

    }

    public void getMyDetails() {

        DhNet verifyNet = new DhNet(API2.CWBaseurl + "/user/" + "561ba2d60cf2429fb48e86bd"
                + "/info?viewUser=" + "561ba2d60cf2429fb48e86bd" + "&token=" + "9927f747-c615-4362-bd43-a2ec31362205");
        verifyNet.doGet(new NetTask(getActivity()) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    ViewUtil.bindView(nameT,JSONUtil.getString(jo, "nickname"));
                    String gender = JSONUtil.getString(jo, "gender");
                    if (gender.equals("男")){
                        sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    }else {
                        sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }

                }
            }
        });

    }
}