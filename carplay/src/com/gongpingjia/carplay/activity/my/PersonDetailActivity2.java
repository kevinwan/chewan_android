package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
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
 * TA 的详情
 */
public class PersonDetailActivity2 extends CarPlayBaseActivity implements View.OnClickListener{

    User user;

    private RoundImageView headI;
    private ImageView sexI, photo_bgI;
    private TextView  nameT, ageT,attentionT;
    private RelativeLayout sexbgR;
    private LinearLayout myactiveL;
    private Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail2);

    }

    @Override
    public void initView() {
        user = User.getInstance();
        setTitle("TA的详情");

        headI = (RoundImageView) findViewById(R.id.head);
        nameT = (TextView) findViewById(R.id.name);
        sexbgR = (RelativeLayout) findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) findViewById(R.id.iv_sex);
        ageT = (TextView)findViewById(R.id.tv_age);
        photo_bgI = (ImageView) findViewById(R.id.photo_bg);
        attentionT = (TextView) findViewById(R.id.attention);
        myactiveL = (LinearLayout) findViewById( R.id.myactive);
        uploadBtn = (Button) findViewById(R.id.upload);

        headI.setOnClickListener(this);
        attentionT.setOnClickListener(this);
        myactiveL.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);


//      if (user.isLogin()) {
        getDetails();
//      }
    }

    public void getDetails() {

        DhNet verifyNet = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId()
                + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGetInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println(user.getUserId()+"---------"+user.getToken());
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    //昵称,性别,年龄
                    ViewUtil.bindView(nameT, JSONUtil.getString(jo, "nickname"));
                    String gender = JSONUtil.getString(jo, "gender");
                    if (gender.equals("男")) {
                        sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
                        sexI.setBackgroundResource(R.drawable.icon_man3x);
                    } else {
                        sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
                        sexI.setBackgroundResource(R.drawable.icon_woman3x);
                    }
                    ViewUtil.bindView(ageT, JSONUtil.getInt(jo, "age"));

                    //头像
                    String headimg = JSONUtil.getString(jo, "avatar");

                    ViewUtil.bindNetImage(headI, headimg, "head");
                    ViewUtil.bindNetImage(photo_bgI, headimg, "head");
//                    photo_bgI.setBackgroundResource(R.drawable.vp_third);
//                    Blurry.with(getActivity())
//                            .radius(10)
//                            .sampling(8)
//                            .async()
//                            .capture(photo_bgI)
//                            .into((ImageView) photo_bgI);

                    //头像认证
                    String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
                    String licenseAuthStatus = JSONUtil.getString(jo, "licenseAuthStatus");

                    ViewUtil.bindView(attentionT, photoAuthStatus);
                    if (photoAuthStatus.equals("未认证")) {
                        attentionT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                    } else if (photoAuthStatus.equals("已认证")) {
                        attentionT.setBackgroundResource(R.drawable.btn_yellow_fillet);
                    } else if (photoAuthStatus.equals("认证中")) {
                        attentionT.setBackgroundResource(R.drawable.radio_sex_man_focused);
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()){
            //编辑资料
            case R.id.head:
                it = new Intent(self, EditPersonalInfoActivity2.class);
                startActivity(it);
                break;
            //关注
            case R.id.attention:

                break;
            //TA的活动
            case R.id.myactive:

                break;
            //上传照片
            case R.id.upload:

                break;
            default:
                break;
        }
    }
}
