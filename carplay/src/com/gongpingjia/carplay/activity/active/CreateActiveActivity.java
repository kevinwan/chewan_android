package com.gongpingjia.carplay.activity.active;

import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.MD5Util;

/***
 * 
 * 创建活动
 * 
 * @author Administrator
 * 
 */
public class CreateActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private Button mFinishBtn, mFinishInviteBtn;

    // 活动类型
    private String mActiveType;

    // 活动介绍
    private String mActiveIntroduction;

    // 上传图片返回的七牛云存储地址
    private List<String> mPictures;

    private String mActiveDestination;

    private String mStartTime;

    private String mEndTime;

    private String mPayType;

    private int mSeats;

    private DhNet mDhNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_active);
        setTitle("创建活动");

        mFinishBtn = (Button) findViewById(R.id.btn_finish);
        mFinishInviteBtn = (Button) findViewById(R.id.btn_finish_invite);

        mFinishBtn.setOnClickListener(this);
        mFinishInviteBtn.setOnClickListener(this);

        DhNet net = new DhNet("http://cwapi.gongpingjia.com/v1/user/login");
        net.addParam("phone", "18951650020");
        net.addParam("password", MD5Util.string2MD5("123456"));
        net.doPost(new NetTask(this) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONObject json = response.jSON();
                    User user = User.getInstance();
                    try {
                        user.setUserId(JSONUtil.getString(json.getJSONObject("data"), "userId"));
                        user.setToken(JSONUtil.getString(json.getJSONObject("data"), "token"));
                        showToast("登陆成功");
                        Log.e("userId", user.getUserId());
                        Log.e("Token", user.getToken());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    try {
                        showToast(response.jSON().getString("errmsg"));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
        case R.id.btn_finish:
            User user = User.getInstance();
            mDhNet = new DhNet("http://cwapi.gongpingjia.com/v1/activity/register?userId=" + user.getUserId()
                    + "&token=" + user.getToken());
            mDhNet.addParam("type", "旅行");
            mDhNet.addParam("introduction", "活动期间晴空万里，道路通畅");
            mDhNet.addParam("cover", "[\"68cbd3b0-d19b-4754-8b08-835b9d94a869\"]");
            mDhNet.addParam("start", System.currentTimeMillis());
            mDhNet.addParam("location", "紫金山");
            mDhNet.addParam("city", "南京");
            mDhNet.addParam("pay", "我请客");
            mDhNet.addParam("seat", 1);

            mDhNet.doPost(new NetTask(this) {

                @Override
                public void doInUI(Response response, Integer transfer) {
                    // TODO Auto-generated method stub
                    if (response.isSuccess()) {
                        JSONObject json = response.jSON();
                        showToast("发布成功");
                    } else {
                        try {
                            showToast(response.jSON().getString("errmsg").toString());
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
            break;
        case R.id.btn_finish_invite:

            break;
        }
    }
}
