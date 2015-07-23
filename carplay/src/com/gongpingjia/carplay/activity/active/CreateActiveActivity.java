package com.gongpingjia.carplay.activity.active;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ImageAdapter;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;

/***
 * 
 * 创建活动
 * 
 * @author Administrator
 * 
 */
public class CreateActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private Button mFinishBtn, mFinishInviteBtn;

    private GridView mPhotoGridView;

    private ImageAdapter mImageAdapter;

    private List<PhotoState> mPhotoStates;

    // 最后一张图片的状态
    private PhotoState mLastPhoto;

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
        mPhotoGridView = (GridView) findViewById(R.id.gv_photo);

        mFinishBtn.setOnClickListener(this);
        mFinishInviteBtn.setOnClickListener(this);

        mPhotoStates = new ArrayList<PhotoState>();
        mLastPhoto = new PhotoState();
        mLastPhoto.setLast(true);
        mLastPhoto.setChecked(false);
        mPhotoStates.add(mLastPhoto);
        mImageAdapter = new ImageAdapter(this, mPhotoStates);

        mPhotoGridView.setAdapter(mImageAdapter);

        mPhotoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (mPhotoStates.get(position).isLast()) {
                    view.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            showToast("last position");
                        }
                    });
                } else {
                    if (mPhotoStates.get(position).isChecked()) {
                        mPhotoStates.get(position).setChecked(false);
                        view.findViewById(R.id.imgView_visible).setVisibility(View.GONE);
                    } else {
                        mPhotoStates.get(position).setChecked(true);
                        view.findViewById(R.id.imgView_visible).setVisibility(View.VISIBLE);
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
