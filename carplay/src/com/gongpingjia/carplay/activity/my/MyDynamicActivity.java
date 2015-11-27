package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.adapter.MyDyanmicBaseAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/19.
 * 我的活动
 */
public class MyDynamicActivity extends CarPlayListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, ILoadSuccess {

    private ListView recyclerView;

    PullToRefreshListView listV;
    LinearLayout empty;
    TextView msg;
    User user = User.getInstance();
    MyDyanmicBaseAdapter adapter;
    File mCacheDir;
    //上传图片总数
    private int uploadPhotoCount = 0;

    //已上传的图片
    private int uploadedCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        EventBus.getDefault().register(this);
    }


    @Override
    public void initView() {
        setTitle("我的活动");
        empty = (LinearLayout) findViewById(R.id.empty);
        msg = (TextView) findViewById(R.id.msg);
        mCacheDir = new File(self.getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
        listV = (PullToRefreshListView) findViewById(R.id.listview);
        listV.setMode(PullToRefreshBase.Mode.BOTH);
        listV.setOnRefreshListener(this);
        recyclerView = listV.getRefreshableView();
        adapter = new MyDyanmicBaseAdapter(self);
        recyclerView.setAdapter(adapter);
        setOnLoadSuccess(this);
        fromWhat("data");

        setUrl(API2.CWBaseurl + "/user/" + user.getUserId() + "/appointment/list?token=" + user.getToken() + "&status=" + 1 + "&status=" + 2+ "&status=" + 4);
//        setUrl(API2.CWBaseurl + "user/5609eb2c0cf224e7d878f693/appointment/list?token=67666666-f2ff-456d-a9cc-e83761749a6a&status=邀请中&status=应邀");

        showNext();
    }


    @Override
    public void loadSuccess() {
        adapter.setData(mVaules);
        listV.postDelayed(new Runnable() {
            @Override
            public void run() {
                listV.onRefreshComplete();
            }
        }, 500);

    }

    @Override
    public void loadSuccessOnFirst() {
        if (mVaules.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            msg.setText("此处暂无活动");
        } else {
            empty.setVisibility(View.GONE);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PICK_PHOTO:
                    showProgressDialog("图片上传中...");
                    if (data != null && data.getExtras() != null) {
                        @SuppressWarnings("unchecked")
                        List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
                        if (photos == null || photos.isEmpty()) {
                            showToast("没有选择图片!");
                        } else {
                            uploadPhotoCount = photos.size();
                            for (int i = 0; i < photos.size(); i++) {
                                String newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
                                        .getAbsolutePath();
                                Bitmap btp = PhotoUtil.getLocalImage(new File(photos.get(i).getOriginalPath()));
                                PhotoUtil.saveLocalImageSquare(btp, new File(newPhotoPath));
                                uploadHead(newPhotoPath);
                            }
                        }
                    }
                    break;
                case Constant.TAKE_PHOTO:
                    String mPhotoPath=adapter.mPhotoPath;
                    if (!TextUtils.isEmpty(mPhotoPath)){
                        Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
                        String newPath = new File(mCacheDir, System.currentTimeMillis()
                                + ".jpg").getAbsolutePath();
                        int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
                        PhotoUtil.saveLocalImageSquare(btp1, new File(newPath), degree);
                        btp1.recycle();
                        showProgressDialog("图片上传中...");
                        uploadPhotoCount = 1;
                        uploadHead(newPath);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path) {

        Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
//        addPhoto.setImageBitmap(bmp);
        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/album/upload?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                hidenProgressDialog();
                if (response.isSuccess()) {
                    uploadedCount = uploadedCount + 1;
                    JSONObject jo = response.jSONFromData();
                    String success = "上传成功";
                    if (uploadPhotoCount == uploadedCount) {
                        showToast("上传成功");
                        //控制附近列表刷新
                        EventBus.getDefault().post(new String("刷新附近列表"));
                        EventBus.getDefault().post(success);
                        refresh();
                        uploadedCount = 0;
                        setHasAlbm();
                        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/photoCount?token=" + user.getToken());
                        net.addParam("count", uploadPhotoCount);
                        net.doPost(new NetTask(self) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void setHasAlbm(){
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId()
                + "/info?viewUser=" + user.getUserId() + "&token=" + user.getToken());
        net.doGet(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                JSONObject jo = response.jSONFromData();
                JSONArray albumJsa = JSONUtil.getJSONArray(jo, "album");
                user.setHasAlbum(albumJsa.length() > 1);         //设置相册状态
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        showNext();
    }
    public void onEventMainThread(String success) {
        if ("刷新我的活动".equals(success)) {
            refresh();
        }
    }
}
