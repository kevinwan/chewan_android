package com.gongpingjia.carplay.activity.my;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ImageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;

/**
 * @Description 相册管理
 * @author Administrator
 * @date 2015-7-21 上午9:36:10
 */
public class ManageAlbumActivity extends CarPlayBaseActivity implements OnClickListener {

    private GridView mPhotoGridView;

    private List<PhotoState> mPhotoStates;

    private List<String> mPicIds;

    private File mCacheDir;

    // 当前正在处理图片状态，GridView最后一个图片状态
    private PhotoState mLastPhotoState;

    private ImageAdapter mPhotoAdapter;

    private String mCurPath;

    private TextView mRightText;

    private TextView mLeftText;

    private ImageView mRightImage;

    private ImageView mLeftImage;

    User mUser = User.getInstance();

    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_album);

        setTitle("相册管理");
        setRightAction("编辑", -1, this);

        mRightText = (TextView) findViewById(R.id.right_text);
        mRightImage = (ImageView) findViewById(R.id.right_icon);
        mLeftText = (TextView) findViewById(R.id.left_text);
        mLeftImage = (ImageView) findViewById(R.id.back);

        mLeftImage.setOnClickListener(this);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mRightImage.setOnClickListener(this);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mPhotoStates = new ArrayList<PhotoState>();
        mLastPhotoState = new PhotoState();
        mLastPhotoState.setChecked(false);
        mLastPhotoState.setLast(true);
        mPhotoStates.add(mLastPhotoState);

        mPicIds = new ArrayList<String>();

        mPhotoAdapter = new ImageAdapter(this, mPhotoStates);
        mPhotoGridView.setAdapter(mPhotoAdapter);

        mPhotoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (mPhotoStates.get(position).isLast()) {
                    mCurPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO, new File(mCurPath));
                } else {
                    if (isEditable) {
                        if (mPhotoStates.get(position).isChecked()) {
                            view.findViewById(R.id.imgView_visible).setVisibility(View.GONE);
                            mPhotoStates.get(position).setChecked(false);
                        } else {
                            view.findViewById(R.id.imgView_visible).setVisibility(View.VISIBLE);
                            mPhotoStates.get(position).setChecked(true);
                        }
                        mPhotoAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mPhotoGridView = (GridView) findViewById(R.id.gv_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case Constant.PICK_PHOTO:
                Bitmap btp = PhotoUtil.checkImage(self, data);
                PhotoUtil.saveLocalImage(btp, new File(mCurPath));
                btp.recycle();
                upLoadPic(mCurPath);
                break;
            case Constant.TAKE_PHOTO:
                Bitmap btp1 = PhotoUtil.getLocalImage(new File(mCurPath));
                String newPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                int degree = PhotoUtil.getBitmapDegree(mCurPath);
                PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                btp1.recycle();
                upLoadPic(newPath);
                break;
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.right_icon:
            if (isEditable) {
                Iterator<PhotoState> iterator = mPhotoStates.iterator();
                while (iterator.hasNext()) {
                    PhotoState state = iterator.next();
                    if (state.isChecked()) {
                        int index = mPhotoStates.indexOf(state);
                        mPicIds.remove(index);
                        iterator.remove();
                    }
                }
                mPhotoAdapter.notifyDataSetChanged();
            } else {
                showToast("不可编辑");
            }
            break;
        case R.id.right_text:
            if (mRightText.getText().toString().equals("编辑")) {
                isEditable = true;
                mRightText.setVisibility(View.GONE);
                mRightImage.setVisibility(View.VISIBLE);
                mRightImage.setBackground(getResources().getDrawable(R.drawable.action_delete));
                mLeftImage.setVisibility(View.GONE);
                mLeftText.setText("取消");
                mLeftText.setVisibility(View.VISIBLE);
            }
            break;
        case R.id.left_text:
            if (mLeftText.getText().toString().equals("取消")) {
                isEditable = false;
                mLeftText.setVisibility(View.GONE);
                mLeftImage.setVisibility(View.VISIBLE);

                mRightText.setVisibility(View.VISIBLE);
                mRightImage.setVisibility(View.GONE);
                mRightText.setText("编辑");
            }
            break;
        case R.id.back:
            this.finish();
            break;
        }

    }

    private void upLoadPic(String path) {
        mPhotoStates.remove(mPhotoStates.size() - 1);
        PhotoState curState = new PhotoState();
        curState.setChecked(false);
        curState.setLast(false);
        curState.setPath(path);
        mPhotoStates.add(curState);
        if (mPhotoStates.size() != 9) {
            mPhotoStates.add(mLastPhotoState);
        }
        mPhotoAdapter.notifyDataSetChanged();

        DhNet net = new DhNet(API.uploadPictures + "userId=" + mUser.getUserId() + "&token=" + mUser.getToken());
        net.upload(new FileInfo("attach", new File(mCurPath)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
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
                    mPhotoStates.remove(mPhotoStates.size() - 2);
                    mPhotoAdapter.notifyDataSetChanged();
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
