package com.gongpingjia.carplay.activity.active;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.Utils;
import com.gongpingjia.carplay.view.NestedGridView;
import com.gongpingjia.carplay.view.dialog.CommonDialog;
import com.gongpingjia.carplay.view.dialog.CommonDialog.OnCommonDialogItemClickListener;
import com.gongpingjia.carplay.view.dialog.DateTimePickerDialog;

public class EditActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private static final int REQUEST_DESCRIPTION = 1;

    private static final int REQUEST_DESTINATION = 2;

    private Button mSaveBtn;

    private View mTypeLayout, mDescriptionLayout, mDestimationLayout, mStartTimeLayout, mEndTimeLayout, mFeeLayout;

    private TextView mTypeText, mDescriptionText, mStartTimeText, mEndTimeText, mFeeText, mDestimationText;

    private NestedGridView mPhotoGridView;

    private ImageAdapter mImageAdapter;

    private List<PhotoState> mPhotoStates;

    // 最后一张图片的状态
    private PhotoState mLastPhoto;

    // 上传图片返回的id
    private List<String> mPicIds;

    private DhNet mDhNet;

    // 图片缓存根目录
    private File mCacheDir;

    // 当前选择图片的路径
    private String mCurPath;

    private List<String> mFeeOptions;

    private List<String> mTypeOptions;

    private String mCity;

    private String mLocation;

    // 开始时间默认为当前的时间戳
    private long mStartTimeStamp;

    private long mEndTimeStamp = 0;

    private String mActiveId;

    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        mUser = User.getInstance();
        setRightAction(null, R.drawable.action_delete, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Iterator<PhotoState> iterator = mPhotoStates.iterator();
                while (iterator.hasNext()) {
                    PhotoState state = iterator.next();
                    if (state.isChecked()) {
                        // 去除图片id
                        mPicIds.remove(mPhotoStates.indexOf(state));
                        // 删除选中图片
                        iterator.remove();
                    }
                }
                mImageAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        setTitle("编辑活动");

        mPicIds = new ArrayList<String>();
        mFeeOptions = new ArrayList<String>();
        mTypeOptions = new ArrayList<String>();
        mFeeOptions.add("AA制");
        mFeeOptions.add("我请客");
        mFeeOptions.add("请我吧");

        mTypeOptions.add("看电影");
        mTypeOptions.add("吃饭");
        mTypeOptions.add("唱歌");
        mTypeOptions.add("旅行");
        mTypeOptions.add("运动");
        mTypeOptions.add("拼车");
        mTypeOptions.add("代驾");

        mTypeText = (TextView) findViewById(R.id.tv_active_type);
        mDescriptionText = (TextView) findViewById(R.id.tv_description);
        mStartTimeText = (TextView) findViewById(R.id.tv_start_time);
        mEndTimeText = (TextView) findViewById(R.id.tv_end_time);
        mFeeText = (TextView) findViewById(R.id.tv_fee);
        mDestimationText = (TextView) findViewById(R.id.tv_destination);

        mTypeLayout = findViewById(R.id.layout_active_type);
        mDescriptionLayout = findViewById(R.id.layout_description);
        mDestimationLayout = findViewById(R.id.layout_destination);
        mStartTimeLayout = findViewById(R.id.layout_start_time);
        mEndTimeLayout = findViewById(R.id.layout_end_time);
        mFeeLayout = findViewById(R.id.layout_fee);

        // 初始化开始时间,默认为当前的时间
        mStartTimeText.setText(Utils.getDate());

        mTypeLayout.setOnClickListener(this);
        mDescriptionLayout.setOnClickListener(this);
        mDestimationLayout.setOnClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mFeeLayout.setOnClickListener(this);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mSaveBtn = (Button) findViewById(R.id.btn_save);
        mPhotoGridView = (NestedGridView) findViewById(R.id.gv_photo);

        mSaveBtn.setOnClickListener(this);

        mPhotoStates = new ArrayList<PhotoState>();
        mLastPhoto = new PhotoState();
        mLastPhoto.setLast(true);
        mLastPhoto.setChecked(false);

        mImageAdapter = new ImageAdapter(this, mPhotoStates);
        initDatas();
        mPhotoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPhotoStates.get(position).isLast()) {
                    mCurPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO, new File(mCurPath));
                } else {
                    if (mPhotoStates.get(position).isChecked()) {
                        mPhotoStates.get(position).setChecked(false);
                    } else {
                        mPhotoStates.get(position).setChecked(true);
                    }
                    mImageAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    // 获取页面传递过来的数据
    private void initDatas() {
        Intent it = getIntent();
        String json = it.getStringExtra("json");

        if (json != null) {
            try {
                JSONObject data = new JSONObject(json);
                mActiveId = data.getString("activityId");
                String location = data.getString("location");
                String introduction = data.getString("introduction");
                Log.e("tag", mActiveId);
                long startTime = data.getLong("start");
                long endTime = data.getLong("end");
                String pay = data.getString("pay");
                String type = data.getString("type");

                mStartTimeStamp = startTime;
                mEndTimeStamp = endTime;
                mLocation = location;

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd HH:mm");
                mDescriptionText.setText(introduction);
                mTypeText.setText(type);
                mFeeText.setText(pay);
                mDestimationText.setText(location);
                mStartTimeText.setText(format.format(startTime));
                if (endTime != 0) {
                    mEndTimeText.setText(format.format(endTime));
                } else {
                    mEndTimeText.setText("不确定");
                }

                JSONArray pics = data.getJSONArray("cover");
                for (int i = 0; i < pics.length(); i++) {
                    JSONObject pic = pics.getJSONObject(i);
                    String picId = pic.getString("coverId");
                    mPicIds.add(picId);
                    PhotoState state = new PhotoState();
                    state.setChecked(false);
                    state.setLast(false);
                    state.setPath(pic.getString("thumbnail_pic"));
                    mPhotoStates.add(state);
                    Log.e("tag", state.toString());
                }
                if (mPhotoStates.size() < 9) {
                    mPhotoStates.add(mLastPhoto);
                }
                mImageAdapter = new ImageAdapter(this, mPhotoStates);
                mPhotoGridView.setAdapter(mImageAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        Intent it = null;
        CommonDialog dlg = null;
        DateTimePickerDialog date = null;
        switch (id) {

        case R.id.layout_active_type:
            dlg = new CommonDialog(self, mTypeOptions, "请选择活动");
            dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    mTypeText.setText(mTypeOptions.get(position));
                }
            });
            dlg.show();
            break;

        case R.id.layout_description:
            it = new Intent(self, ActiveDescriptionActivity.class);
            startActivityForResult(it, REQUEST_DESCRIPTION);
            break;

        case R.id.layout_destination:
            it = new Intent(self, MapActivity.class);
            startActivityForResult(it, REQUEST_DESTINATION);
            break;

        case R.id.layout_start_time:
            date = new DateTimePickerDialog(self, System.currentTimeMillis());
            date.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                public void OnDateTimeSet(AlertDialog dialog, long date) {
                    mStartTimeText.setText(CarPlayUtil.getStringDate(date));
                    mStartTimeStamp = date;
                }
            });
            date.show();
            break;
        case R.id.layout_end_time:
            date = new DateTimePickerDialog(self, System.currentTimeMillis());
            date.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                public void OnDateTimeSet(AlertDialog dialog, long date) {
                    mEndTimeText.setText(CarPlayUtil.getStringDate(date));
                    mEndTimeStamp = date;
                }
            });
            date.show();
            break;
        case R.id.layout_fee:
            dlg = new CommonDialog(self, mFeeOptions, "请选择付费方式");
            dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    mFeeText.setText(mFeeOptions.get(position));
                }
            });
            dlg.show();
            break;
        case R.id.btn_save:
            if (mTypeText.getText().toString().equals("")) {
                showToast("请选择活动");
                return;
            }
            if (mDestimationText.getText().toString().length() == 0) {
                showToast("请选择目的地");
                return;
            }
            if (mPicIds.size() == 0) {
                showToast("请至少选择一张图片");
            }
            User user = User.getInstance();
            mDhNet = new DhNet(API.editActive + mActiveId + "/info?userId=" + user.getUserId() + "&token="
                    + user.getToken());
            mDhNet.addParam("type", mTypeText.getText().toString());
            mDhNet.addParam("introduction", mDescriptionText.getText().toString());
            JSONArray array = new JSONArray(mPicIds);
            mDhNet.addParam("cover", array);
            mDhNet.addParam("location", mLocation);
            if (mCity != null) {
                mDhNet.addParam("city", mCity);
            }
            if (!mDestimationText.getText().toString().equals(mLocation)) {
                mDhNet.addParam("address", mDestimationText.getText().toString());
            }
            mDhNet.addParam("start", mStartTimeStamp);
            mDhNet.addParam("pay", mFeeText.getText().toString());
            if (mEndTimeStamp != 0) {
                mDhNet.addParam("end", mEndTimeStamp);
            }

            Log.e("tag", "url:" + mDhNet.getUrl());
            Map<String, Object> params = mDhNet.getParams();
            for (String key : params.keySet()) {
                Log.e("tag", key + ": " + params.get(key));
            }
            mDhNet.doPostInDialog(new NetTask(this) {

                @Override
                public void doInUI(Response response, Integer transfer) {
                    // TODO Auto-generated method stub
                    if (response.isSuccess()) {
                        showToast("修改成功");
                    } else {
                        try {
                            Log.e("err", response.jSON().getString("errmsg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
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

            case REQUEST_DESTINATION:
                mDestimationText.setText(data.getStringExtra("destination"));
                mCity = data.getStringExtra("city");
                mLocation = data.getStringExtra("location");
                break;
            case Constant.TAKE_PHOTO:
                Bitmap btp1 = PhotoUtil.getLocalImage(new File(mCurPath));
                String newPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                int degree = PhotoUtil.getBitmapDegree(mCurPath);
                PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                btp1.recycle();
                upLoadPic(newPath);
                // PhotoUtil.onPhotoFromCamera(self, Constant.ZOOM_PIC,
                // mCurPath, 1, 1, 1000);
                break;
            case Constant.PICK_PHOTO:
                Bitmap btp = PhotoUtil.checkImage(self, data);
                PhotoUtil.saveLocalImage(btp, new File(mCurPath));
                upLoadPic(mCurPath);
                // PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mCurPath,
                // data, 1, 1, 1000);
                break;
            }
        }
    }

    private void upLoadPic(String path) {
        mPhotoStates.remove(mPhotoStates.size() - 1);
        PhotoState state = new PhotoState();
        state.setChecked(true);
        state.setLast(false);
        state.setPath(mCurPath);
        mPhotoStates.add(state);
        if (mPhotoStates.size() != 9) {
            mPhotoStates.add(mLastPhoto);
        }
        mImageAdapter.notifyDataSetChanged();
        DhNet net = new DhNet(API.uploadPictures + "userId=" + mUser.getUserId() + "&token=" + mUser.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

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
                    showToast("图片上传成功");
                } else {

                    if (mPhotoStates.size() != 9) {
                        mPhotoStates.remove(mPhotoStates.size() - 2);
                    } else {
                        if (mPhotoStates.get(mPhotoStates.size() - 1).isLast()) {
                            mPhotoStates.remove(mPhotoStates.size() - 2);
                        } else {
                            mPhotoStates.remove(mPhotoStates.size() - 1);
                        }
                    }
                    try {
                        showToast(response.jSON().getString("errmsg") + " 图片上传失败,请重新选择上传");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
