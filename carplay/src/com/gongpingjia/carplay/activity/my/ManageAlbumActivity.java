package com.gongpingjia.carplay.activity.my;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ImageAdapter;
import com.gongpingjia.carplay.bean.PhotoState;

/**
 * @Description 相册管理
 * @author Administrator
 * @date 2015-7-21 上午9:36:10
 */
public class ManageAlbumActivity extends CarPlayBaseActivity implements OnClickListener {

    // 从相册取照片
    private static final int REQUEST_PICK = 1;

    // 相机拍照
    private static final int REQUEST_TAKE = 2;

    // 剪切之后的照片
    private static final int RESULT_CROP = 10;

    private GridView mPhotoGridView;

    private List<PhotoState> mDatas;

    private File mCacheDir;

    // 当前正在处理图片状态，GridView最后一个图片状态
    private PhotoState mCurPhotoState, mLastPhotoState;

    private ImageAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_album);

        setTitle("相册管理");
        setRightAction("编辑", -1, this);
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mDatas = new ArrayList<PhotoState>();
        mLastPhotoState = new PhotoState();
        mLastPhotoState.setChecked(false);
        mLastPhotoState.setLast(true);
        mDatas.add(mLastPhotoState);

        mPhotoAdapter = new ImageAdapter(this, mDatas);
        mPhotoGridView.setAdapter(mPhotoAdapter);

        mPhotoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (mDatas.get(position).isLast()) {
//                    PhotoUtil.getPhotoFromPick(ManageAlbumActivity.this, REQUEST_PICK);
                } else {
                    if (mDatas.get(position).isChecked()) {
                        view.findViewById(R.id.imgView_visible).setVisibility(View.GONE);
                        mDatas.get(position).setChecked(false);
                    } else {
                        view.findViewById(R.id.imgView_visible).setVisibility(View.VISIBLE);
                        mDatas.get(position).setChecked(true);
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
            case REQUEST_PICK:
                String path = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                mCurPhotoState = new PhotoState();
                mCurPhotoState.setChecked(false);
                mCurPhotoState.setLast(false);
                mCurPhotoState.setPath(path);
//                PhotoUtil.onPhotoFromPick(this, data, path, RESULT_CROP);
                break;
            case RESULT_CROP:
                if (mDatas.size() != 9) {
                    mDatas.remove(mDatas.size() - 1);
                    mDatas.add(mCurPhotoState);
                    mDatas.add(mLastPhotoState);
                    mPhotoAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
