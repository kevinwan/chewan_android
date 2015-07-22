package com.gongpingjia.carplay.activity.active;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.net.DhNet;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/***
 * 
 * 创建活动
 * 
 * @author Administrator
 * 
 */
public class CreatActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private Button mFinishBtn, mFinishInviteBtn;

    // 活动类型
    private String mActiveType;

    // 活动介绍
    private String mActiveDescription;

    // 上传图片返回的七牛云存储地址
    private List<String> mPictures;

    private String mActiveDestination;

    private String mStartTime;

    private String mEndTime;

    private String mPayType;

    private int mSeats;

    private DhNet mDhNet;

    private Map<String, Object> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_active);
        setTitle("创建活动");

        mDhNet = new DhNet();
        mParams = new HashMap<String, Object>();

        mFinishBtn = (Button) findViewById(R.id.btn_finish);
        mFinishInviteBtn = (Button) findViewById(R.id.btn_finish_invite);

        mFinishBtn.setOnClickListener(this);
        mFinishInviteBtn.setOnClickListener(this);
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
            mParams.put("type", "watch movie");
            mParams.put("description", "we will go to the Motion Pictures Group watch movie");
            mParams.put("cover", "[gongpingjia.qiniu.com/fsdfadfasdijowe.jpg]");
            mParams.put("start", "");
            mDhNet.setParams(mParams);
            break;
        case R.id.btn_finish_invite:

            break;
        }
    }
}
