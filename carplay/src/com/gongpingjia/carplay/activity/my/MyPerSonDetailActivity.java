package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.msg.PlayCarChatActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;

/**
 * 我的详情
 * 
 * @author Administrator
 * 
 */
public class MyPerSonDetailActivity extends CarPlayBaseActivity implements OnClickListener
{
    public static final int LOGINCODE = 1;
    
    NetRefreshAndMoreListView listV;
    
    ActiveAdapter adapter;
    
    /** 我的关注,我的发布,我的参与 三个点击区域的View */
    View my_attentionV, my_releaseV, my_participationV;
    
    LinearLayout carchat, owners_certification, people_concerned;
    
    User user;
    
    /** 已登录,未登录 */
    LinearLayout loginedLl, notloginLl;
    
    Button loginBtn;
    
    /** 头像,车logo */
    RoundImageView headI, carBrandLogoI;
    
    /** 昵称,年龄,车型+车龄 */
    TextView nicknameT, ageT, carModelT;
    
    /** 性别 */
    RelativeLayout genderR;
    
    /** 发布数,关注数,参与数 */
    TextView postNumberT, subscribeNumberT, joinNumberT;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my);
    }
    
    @Override
    public void initView()
    {
        
        loginedLl = (LinearLayout)findViewById(R.id.logined);
        notloginLl = (LinearLayout)findViewById(R.id.notlogin);
        loginBtn = (Button)findViewById(R.id.login);
        headI = (RoundImageView)findViewById(R.id.head);
        carBrandLogoI = (RoundImageView)findViewById(R.id.carBrandLogo);
        nicknameT = (TextView)findViewById(R.id.nickname);
        ageT = (TextView)findViewById(R.id.age);
        carModelT = (TextView)findViewById(R.id.carModel);
        genderR = (RelativeLayout)findViewById(R.id.gender);
        
        postNumberT = (TextView)findViewById(R.id.postNumber);
        subscribeNumberT = (TextView)findViewById(R.id.subscribeNumber);
        joinNumberT = (TextView)findViewById(R.id.joinNumber);
        
        my_attentionV = findViewById(R.id.my_attention);
        my_releaseV = findViewById(R.id.my_release);
        my_participationV = findViewById(R.id.my_participation);
        carchat = (LinearLayout)findViewById(R.id.carchat);
        people_concerned = (LinearLayout)findViewById(R.id.people_concerned);
        owners_certification = (LinearLayout)findViewById(R.id.owners_certification);
        
        loginBtn.setOnClickListener(this);
        my_attentionV.setOnClickListener(this);
        my_releaseV.setOnClickListener(this);
        my_participationV.setOnClickListener(this);
        carchat.setOnClickListener(this);
        people_concerned.setOnClickListener(this);
        owners_certification.setOnClickListener(this);
        user = User.getInstance();
        loginedLl.setVisibility(View.VISIBLE);
        notloginLl.setVisibility(View.GONE);
        getMyDetails();
        // 未登录
    }
    
    /** 获取个人资料 */
    private void getMyDetails()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/user/" + user.getUserId() + "/info?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doGetInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                JSONObject jo = response.jSONFromData();
                
                String nickname = JSONUtil.getString(jo, "nickname");
                String age = JSONUtil.getString(jo, "age");
                String carModel = JSONUtil.getString(jo, "carModel");
                String drivingExperience = JSONUtil.getString(jo, "drivingExperience");
                String photo = JSONUtil.getString(jo, "photo");
                String carBrandLogo = JSONUtil.getString(jo, "carBrandLogo");
                String gender = JSONUtil.getString(jo, "gender");
                String postNumber = JSONUtil.getString(jo, "postNumber");
                String subscribeNumber = JSONUtil.getString(jo, "subscribeNumber");
                String joinNumber = JSONUtil.getString(jo, "joinNumber");
                
                nicknameT.setText(nickname);
                ageT.setText(age);
                
                ViewUtil.bindNetImage(headI, photo, CarPlayValueFix.optionsDefault.toString());
                
                if (TextUtils.isEmpty(carModel))
                {
                    carModelT.setText("带我飞~");
                }
                else
                {
                    carModelT.setText(carModel + "  " + drivingExperience + "年");
                }
                
                if (TextUtils.isEmpty(carBrandLogo) || carBrandLogo.equals("null"))
                {
                    carBrandLogoI.setVisibility(View.GONE);
                }
                else
                {
                    carBrandLogoI.setVisibility(View.VISIBLE);
                    ViewUtil.bindNetImage(carBrandLogoI, carBrandLogo, CarPlayValueFix.optionsDefault.toString());
                }
                
                if (gender.equals("男"))
                    genderR.setBackgroundResource(R.drawable.man);
                else
                    genderR.setBackgroundResource(R.drawable.woman);
                
                postNumberT.setText(postNumber);
                subscribeNumberT.setText(subscribeNumber);
                joinNumberT.setText(joinNumber);
                
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        Intent it;
        
        switch (v.getId())
        {
            case R.id.my_attention:
                it = new Intent(self, MyAttentionActiveActivity.class);
                startActivity(it);
                break;
            
            case R.id.my_release:
                it = new Intent(self, MyReleaseActiveActivity.class);
                startActivity(it);
                break;
            
            case R.id.my_participation:
                it = new Intent(self, MyParticipationActiveActivity.class);
                startActivity(it);
                break;
            case R.id.carchat:
                it = new Intent(self, PlayCarChatActivity.class);
                startActivity(it);
                break;
            case R.id.people_concerned:
                it = new Intent(self, AttentionPersonActivity.class);
                startActivity(it);
                break;
            case R.id.owners_certification:
                it = new Intent(self, AuthenticateOwnersActivity.class);
                startActivity(it);
                break;
            case R.id.login:
                it = new Intent(self, LoginActivity.class);
                startActivityForResult(it, LOGINCODE);
                break;
            default:
                break;
        }
    }
    
}
