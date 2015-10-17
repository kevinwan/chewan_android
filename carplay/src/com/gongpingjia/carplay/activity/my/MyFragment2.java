package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;

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
public class MyFragment2 extends Fragment implements OnClickListener {
    View mainV;
    static MyFragment2 instance;
    private RoundImageView headI;
    private ImageView sexI, photo_bgI;
    private TextView attestationT, nameT, ageT, completenessT, txtphotoAuthStatusT, attestation_txtT;
    private Button perfectBtn;
    private RelativeLayout sexbgR;
    private LinearLayout myphotoL, myactiveL, myattentionL, headattestationL, carattestationL;

    public static MyFragment2 getInstance() {
        if (instance == null) {
            instance = new MyFragment2();
        }

        return instance;
    }

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mainV = inflater.inflate(R.layout.fragment_my2, null);
        user = User.getInstance();
        initView();
        return mainV;
    }

    private void initView() {
        headI = (RoundImageView) mainV.findViewById(R.id.head);
        attestationT = (TextView) mainV.findViewById(R.id.attestation);
        nameT = (TextView) mainV.findViewById(R.id.name);
        sexbgR = (RelativeLayout) mainV.findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) mainV.findViewById(R.id.iv_sex);
        ageT = (TextView) mainV.findViewById(R.id.tv_age);
        completenessT = (TextView) mainV.findViewById(R.id.txt_completeness);
        perfectBtn = (Button) mainV.findViewById(R.id.perfect);
        photo_bgI = (ImageView) mainV.findViewById(R.id.photo_bg);
        myphotoL = (LinearLayout) mainV.findViewById(R.id.myphoto);
        myactiveL = (LinearLayout) mainV.findViewById(R.id.myactive);
        myattentionL = (LinearLayout) mainV.findViewById(R.id.myattention);
        headattestationL = (LinearLayout) mainV.findViewById(R.id.headattestation);
        carattestationL = (LinearLayout) mainV.findViewById(R.id.carattestation);
        txtphotoAuthStatusT = (TextView) mainV.findViewById(R.id.txtphotoAuthStatus);
        attestation_txtT = (TextView) mainV.findViewById(R.id.attestation_txt);

        perfectBtn.setOnClickListener(this);
        myactiveL.setOnClickListener(this);
        myattentionL.setOnClickListener(this);
        headattestationL.setOnClickListener(this);
        carattestationL.setOnClickListener(this);
        headI.setOnClickListener(this);

//        if (user.isLogin()) {
        getMyDetails();
//        }
    }

    public void getMyDetails() {

        DhNet verifyNet = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId()
                + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGetInDialog(new NetTask(getActivity()) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println(user.getUserId()+"---------"+user.getToken()); 
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    ViewUtil.bindView(nameT, JSONUtil.getString(jo, "nickname"));
                    String gender = JSONUtil.getString(jo, "gender");
                    if (("男").equals(gender)) {
                        sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    } else {
                        sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }
                    String headimg = JSONUtil.getString(jo, "avatar");

                    ViewUtil.bindNetImage(headI, headimg, "head");
                    ViewUtil.bindNetImage(photo_bgI, headimg, "head");
//                    photo_bgI.setBackgroundResource(R.drawable.vp_third);
                    ViewUtil.bindView(ageT, JSONUtil.getInt(jo, "age"));
//                    Blurry.with(getActivity())
//                            .radius(10)
//                            .sampling(8)
//                            .async()
//                            .capture(photo_bgI)
//                            .into((ImageView) photo_bgI);

                    String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                    String licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");
                    ViewUtil.bindView(txtphotoAuthStatusT, JSONUtil.getString(jo, "photoAuthStatus"));
                    ViewUtil.bindView(attestation_txtT, JSONUtil.getString(jo, "licenseAuthStatus"));
                    //头像认证
                    if (photoAuthStatus.equals("未认证")) {
                        attestationT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                        attestationT.setText("未认证");
                        headattestationL.setEnabled(true);
                    } else if (photoAuthStatus.equals("已认证")) {
                        attestationT.setBackgroundResource(R.drawable.btn_yellow_fillet);
                        attestationT.setText("已认证");
                        headattestationL.setEnabled(false);
                    } else if (photoAuthStatus.equals("认证中")) {
                        attestationT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                        attestationT.setText("未认证");
                        headattestationL.setEnabled(true);
                    }

                    //车主认证
                    if (licenseAuthStatus.equals("未认证")) {
                        carattestationL.setEnabled(true);
                    } else if (licenseAuthStatus.equals("已认证")) {
                        carattestationL.setEnabled(false);
                    } else if (licenseAuthStatus.equals("认证中")) {
                        carattestationL.setEnabled(true);
                    }


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            //编辑资料
            case R.id.head:
                it = new Intent(getActivity(), EditPersonalInfoActivity2.class);
                startActivity(it);
                break;
            //完善信息
            case R.id.perfect:
//                NearbyFilterDialog nearbyFilterDialog = new NearbyFilterDialog(getActivity());
//                nearbyFilterDialog.show();
                getMyDetails();
                break;
            //我的活动
            case R.id.myactive:
                it = new Intent(getActivity(), PersonDetailActivity2.class);
                startActivity(it);
                break;
            //我的关注
            case R.id.myattention:
                it = new Intent(getActivity(), SubscribeActivity2.class);
                startActivity(it);
                break;
            //头像认证
            case R.id.headattestation:
                it = new Intent(getActivity(), HeadAttestationActivity.class);
                startActivity(it);
                break;
            //车主认证
            case R.id.carattestation:
                it = new Intent(getActivity(), AuthenticateOwnersActivity2.class);
                startActivity(it);
                break;

            default:
                break;
        }
    }
}