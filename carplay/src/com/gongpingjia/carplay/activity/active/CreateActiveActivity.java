package com.gongpingjia.carplay.activity.active;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ImageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.MD5Util;
import com.gongpingjia.carplay.util.Utils;
import com.gongpingjia.carplay.view.NestedGridView;
import com.gongpingjia.carplay.view.dialog.CommonDialog;
import com.gongpingjia.carplay.view.dialog.DateDialog;
import com.gongpingjia.carplay.view.dialog.DateDialog.OnDateResultListener;

/***
 * 
 * 创建活动
 * 
 * @author Administrator
 * 
 */
public class CreateActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private static final int REQUEST_DESCRIPTION = 1;

    private Button mFinishBtn, mFinishInviteBtn;

    private View mTypeLayout, mDescriptionLayout, mDestimationLayout, mStartTimeLayout, mEndTimeLayout, mFeeLayout,
            mSeatLayout;

    private TextView mTypeText, mDescriptionText, mStartTimeText, mEndTimeText, mFeeText, mSeatText;

    private NestedGridView mPhotoGridView;

    private ImageAdapter mImageAdapter;

    private List<PhotoState> mPhotoStates;

    // 最后一张图片的状态
    private PhotoState mLastPhoto;

    // 活动类型
    private String mActiveType;

    // 活动介绍
    private String mActiveIntroduction;

    // 上传图片返回的id
    private List<String> mPicIds;

    private String mActiveDestination;

    private long mStartTime;

    private long mEndTime;

    private String mPayType;

    private int mSeats;

    private DhNet mDhNet;

    // 图片缓存根目录
    private File mCacheDir;

    // 当前选择图片的路径
    private String mCurPath;

    private List<String> mFeeOptions;

    private List<String> mTypeOptions;

    private List<String> mSeatOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_active);
        setTitle("创建活动");

        mPicIds = new ArrayList<String>();
        mFeeOptions = new ArrayList<String>();
        mTypeOptions = new ArrayList<String>();
        mSeatOptions = new ArrayList<String>();

        mFeeOptions.add("AA制");
        mFeeOptions.add("我请客");
        mFeeOptions.add("请我吧");

        mTypeOptions.add("看电影啊");
        mTypeOptions.add("唱歌啊");
        mTypeOptions.add("跑步啊");
        mTypeOptions.add("爬山啊");
        mTypeOptions.add("吃饭啊");

        mSeatOptions.add("1");
        mSeatOptions.add("2");
        mSeatOptions.add("3");
        mSeatOptions.add("4");

        mTypeText = (TextView) findViewById(R.id.tv_active_type);
        mDescriptionText = (TextView) findViewById(R.id.tv_description);
        mStartTimeText = (TextView) findViewById(R.id.tv_start_time);
        mEndTimeText = (TextView) findViewById(R.id.tv_end_time);
        mFeeText = (TextView) findViewById(R.id.tv_fee);
        mSeatText = (TextView) findViewById(R.id.tv_seat);

        mTypeLayout = findViewById(R.id.layout_active_type);
        mDescriptionLayout = findViewById(R.id.layout_description);
        mDestimationLayout = findViewById(R.id.layout_destination);
        mStartTimeLayout = findViewById(R.id.layout_start_time);
        mEndTimeLayout = findViewById(R.id.layout_end_time);
        mFeeLayout = findViewById(R.id.layout_fee);
        mSeatLayout = findViewById(R.id.layout_seats);

        // 初始化开始时间,默认为当前的时间
        mStartTimeText.setText(Utils.getDate());

        mTypeLayout.setOnClickListener(this);
        mDescriptionLayout.setOnClickListener(this);
        mDestimationLayout.setOnClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mSeatLayout.setOnClickListener(this);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mFinishBtn = (Button) findViewById(R.id.btn_finish);
        mFinishInviteBtn = (Button) findViewById(R.id.btn_finish_invite);
        mPhotoGridView = (NestedGridView) findViewById(R.id.gv_photo);

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
                    mCurPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO, new File(mCurPath));
                } else {
                    mPhotoStates.remove(position);
                    mImageAdapter.notifyDataSetChanged();
                }
            }
        });

        DhNet net = new DhNet(API.login);
        net.addParam("phone", "18951650020");
        net.addParam("password", MD5Util.string2MD5("123456"));
        net.doPost(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFrom("data");
                    User user = User.getInstance();
                    user.setUserId(JSONUtil.getString(jo, "userId"));
                    user.setToken(JSONUtil.getString(jo, "token"));
                    showToast("登陆成功");
                } else {
                    showToast(response.msg);
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
        Intent it = null;
        CommonDialog dlg = null;
        DateDialog date = null;
        switch (id) {

        case R.id.layout_active_type:
            dlg = new CommonDialog(self, mTypeOptions, "请选择活动");
            dlg.setOnItemClickListener(new CommonDialog.OnItemClickListener() {

                @Override
                public void onItemClickListener(int which) {
                    // TODO Auto-generated method stub
                    mTypeText.setText(mTypeOptions.get(which));
                }
            });
            dlg.show();
            break;

        case R.id.layout_description:
            it = new Intent(self, ActiveDescriptionActivity.class);
            startActivityForResult(it, REQUEST_DESCRIPTION);
            break;

        case R.id.layout_start_time:
            date = new DateDialog();
            date.setOnDateResultListener(new OnDateResultListener() {

                @Override
                public void result(String date, long datetime, int year, int month, int day) {
                    // TODO Auto-generated method stub
                    mStartTimeText.setText(date);
                }
            });
            date.show(self);
            break;
        case R.id.layout_end_time:
            date = new DateDialog();
            date.setOnDateResultListener(new OnDateResultListener() {

                @Override
                public void result(String date, long datetime, int year, int month, int day) {
                    // TODO Auto-generated method stub
                    mEndTimeText.setText(date);
                }
            });
            date.show(self);
            break;
        case R.id.layout_fee:
            dlg = new CommonDialog(self, mFeeOptions, "请选择付费方式");
            dlg.setOnItemClickListener(new CommonDialog.OnItemClickListener() {

                @Override
                public void onItemClickListener(int which) {
                    // TODO Auto-generated method stub
                    mFeeText.setText(mFeeOptions.get(which));
                }
            });
            dlg.show();
            break;
        case R.id.layout_seats:
            dlg = new CommonDialog(self, mSeatOptions, "请选择提供座位数");
            dlg.setOnItemClickListener(new CommonDialog.OnItemClickListener() {

                @Override
                public void onItemClickListener(int which) {
                    // TODO Auto-generated method stub
                    mSeatText.setText(mSeatOptions.get(which));
                }
            });
            dlg.show();
            break;

        case R.id.btn_finish:

            if (mTypeText.getText().toString().equals("")) {
                showToast("请选择活动");
                return;
            }

            if (mStartTimeText.getText().toString().length() == 0) {
                showToast("请选择开始日期");
                return;
            }
            if (mEndTimeText.getText().toString().length() == 0) {
                showToast("请选择结束日期");
                return;
            }
            if (mSeatText.getText().toString().length() == 0) {
                showToast("请提供座位数");
                return;
            }
            User user = User.getInstance();
            mDhNet = new DhNet("http://cwapi.gongpingjia.com/v1/activity/register?userId=" + user.getUserId()
                    + "&token=" + user.getToken());
            mDhNet.addParam("type", mTypeText.getText().toString());
            mDhNet.addParam("introduction", mDescriptionText.getText().toString());
            mDhNet.addParam("cover", "[" + mPicIds.get(0) + "]");
            mDhNet.addParam("start", mStartTimeText.getText().toString());
            mDhNet.addParam("location", "紫金山");
            mDhNet.addParam("city", "南京");
            mDhNet.addParam("pay", mFeeText.getText().toString());
            mDhNet.addParam("seat", mSeatText.getText().toString());

            mDhNet.doPost(new NetTask(this) {

                @Override
                public void doInUI(Response response, Integer transfer) {
                    // TODO Auto-generated method stub
                    if (response.isSuccess()) {
                        JSONObject json = response.jSON();
                        showToast("发布成功");
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
            break;
        case R.id.btn_finish_invite:

            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            case REQUEST_DESCRIPTION:
                mDescriptionText.setText(data.getStringExtra("des"));
                break;

            case Constant.PICK_PHOTO:
                PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mCurPath, data, 1, 1, 1000);
            case Constant.ZOOM_PIC:
                User user = User.getInstance();
                DhNet net = new DhNet(API.uploadPictures + "userId=" + user.getUserId() + "&token=" + user.getToken());
                net.upload(new FileInfo("attach", new File(mCurPath)), new NetTask(self) {

                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        // TODO Auto-generated method stub
                        if (response.isSuccess()) {
                            JSONObject jo = response.jSONFrom("data");
                            try {
                                String picId = jo.getString("photoId");
                                mPicIds.add(picId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });

                mPhotoStates.remove(mPhotoStates.size() - 1);
                PhotoState state = new PhotoState();
                state.setChecked(true);
                state.setLast(false);
                state.setPath(mCurPath);
                mPhotoStates.add(state);
                if (mPhotoStates.size() == 9) {
                    mImageAdapter.notifyDataSetChanged();
                    return;
                }
                mPhotoStates.add(mLastPhoto);
                mImageAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
