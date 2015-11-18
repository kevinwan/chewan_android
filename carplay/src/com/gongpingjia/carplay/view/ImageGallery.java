package com.gongpingjia.carplay.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.gallery.BasePagerAdapter.OnItemChangeListener;
import com.gongpingjia.carplay.view.gallery.GalleryViewPager;
import com.gongpingjia.carplay.view.gallery.UrlPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageGallery extends CarPlayBaseActivity implements View.OnClickListener {

    private GalleryViewPager mViewPager;

    private TextView mIndicatorText;

    private TextView remove, save, cancel, sethead;

    private ImageView back, more;

    private RelativeLayout operationLayout;

    User user;

    String type;

    String mPhotoPath;
    String newPhotoPath;
    String photoUid;
    String head_url;


    int photoCurrent;

    List<String> items;
    List<String> itemid;
    List<String> itemspath;

    UrlPagerAdapter pagerAdapter;

    boolean showFlag = true;

    private File mCacheDir;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagegallery);
        user = User.getInstance();
        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();
    }

    @Override
    public void initView() {
        mIndicatorText = (TextView) findViewById(R.id.tv_large_pic_title);
        remove = (TextView) findViewById(R.id.remove);
        save = (TextView) findViewById(R.id.save);
        sethead = (TextView) findViewById(R.id.sethead);
        cancel = (TextView) findViewById(R.id.cancel);
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        operationLayout = (RelativeLayout) findViewById(R.id.operationLayout);

        Bundle bundle = getIntent().getExtras();

        type = bundle.getString("type");
        final String[] urls = bundle.getStringArray("imgurls");
        final String[] ids = bundle.getStringArray("imgids");
        final String[] files = bundle.getStringArray("imgfile");
        items = new ArrayList<String>();
        if (urls != null)
            Collections.addAll(items, urls);
        itemid = new ArrayList<String>();
        if (ids != null)
            Collections.addAll(itemid, ids);
        itemspath = new ArrayList<String>();
        if (files != null)
            Collections.addAll(itemspath, files);

        pagerAdapter = new UrlPagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                photoCurrent = currentPosition;
                mIndicatorText.setText(getIndicatorString(currentPosition,
                        items.size()));
            }
        });
        Intent it = getIntent();
        mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(it.getIntExtra("currentItem", 0));
        mIndicatorText.setText(getIndicatorString(
                it.getIntExtra("currentItem", 0) + 1, urls.length));

        if ("myalbum".equals(type)) {
            remove.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            sethead.setVisibility(View.VISIBLE);
        } else {
            remove.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            sethead.setVisibility(View.GONE);
        }

        remove.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        sethead.setOnClickListener(this);
    }

    private String getIndicatorString(int index, int total) {
        return (index + 1) + "/" + total;
    }

    /**
     * 删除图片
     */
    private void deletePhoto() {
        DhNet net = new DhNet(API2.CWBaseurl + "user/" + user.getUserId() + "/album/photos?token=" + user.getToken());
        JSONArray jsa = new JSONArray();
        jsa.put(itemid.get(photoCurrent));
        net.addParam("photos", jsa);
        net.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {

                    items.remove(photoCurrent);
                    itemid.remove(photoCurrent);
                    itemspath.remove(photoCurrent);
                    if (items.size() < 2) {
                        user.setHasAlbum(false);         //设置相册状态
                        EventBus.getDefault().post(new String("刷新附近列表"));
                    }
                    if (items.size() != 0) {
                        pagerAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(0);
                        mIndicatorText.setText(getIndicatorString(0,
                                items.size()));
                    } else {
                        finish();
                    }
                    EventBus.getDefault().post(new String("上传成功"));

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //删除图片
            case R.id.remove:
                deletePhoto();
                showOperation();
                break;
            //保存图片
            case R.id.save:
                showOperation();
                Bitmap btm = ImageLoader.getInstance().getMemoryCache().get(items.get(photoCurrent));
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        byte[] data = getImage();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(
                                data, 0, data.length);
                        System.out.println("Bitmap*****" + bitmap);
                        new Handler(getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                savePhoto(bitmap);
                            }
                        });

                    }
                }).start();

                break;
            //取消
            case R.id.cancel:
                showOperation();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.more:
                showOperation();
                break;
            //设置为头像
            case R.id.sethead:
//                showOperation();
//                mPhotoPath = itemspath.get(photoCurrent);
//                PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
//                        PhotoUtil.getLocalImage(new File(mPhotoPath)), 1, 1, 1000);

                showOperation();
                mPhotoPath = itemspath.get(photoCurrent);       //获取选中图片路径
                newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();     //生成新路径
                Bitmap bitmap = PhotoUtil.getLocalImage(new File(mPhotoPath));
                PhotoUtil.saveLocalImage(bitmap, new File(newPhotoPath));        //保存为临时图片
                Bitmap bt = PhotoUtil.getLocalImage(new File(newPhotoPath));      //获取临时图片的bitmap
                PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, newPhotoPath,        //裁剪临时图片
                        bt, 1, 1, 1000);
                break;
        }
    }

    //url转化为bitmap
    public byte[] getImage() {
        byte[] data = null;
        try {
            // 建立URL
            URL url = new URL(items.get(photoCurrent));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);

            InputStream input = conn.getInputStream();// 到这可以直接BitmapFactory.decodeFile也行。 返回bitmap

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            input.close();
            data = output.toByteArray();
            System.out.println("下载完毕！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }


    private void showOperation() {
        if (showFlag) {
            operationLayout.setVisibility(View.VISIBLE);
            showFlag = !showFlag;
        } else {
            operationLayout.setVisibility(View.GONE);
            showFlag = !showFlag;
        }
    }


    private void savePhoto(Bitmap bitmap) {
        File appDir = new File(Environment
                .getExternalStorageDirectory(), "carplay");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        PhotoUtil.saveLocalImage(bitmap, file);
        IocContainer.getShare().get(IDialog.class)
                .showToastShort(self, "图片保存成功!");
        Intent intent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.ZOOM_PIC:
                    showProgressDialog("上传头像中...");
                    uploadHead(newPhotoPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 上传头像
     *
     * @param path
     */
    private void uploadHead(String path) {
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/avatar?token=" + user.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
//                System.out.println("更改头像返回："+user.getUserId() + "---------" + user.getToken());
                hidenProgressDialog();
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    photoUid = JSONUtil.getString(jo, "photoId");
                    head_url = JSONUtil.getString(jo, "photoUrl");
                    boolean a = ImageLoader.getInstance().getDiskCache()
                            .remove(head_url);
                    Bitmap b = ImageLoader.getInstance().getMemoryCache()
                            .remove(head_url);
//                    System.out.println("第一个：+++++++++++" + a);
//                    System.out.println("第二个：***************" + b);
//                    System.out.println("更改头像返回：" + JSONUtil.getString(jo, "photoUrl"));
                    showToast("更改头像成功");
                    EventBus.getDefault().post("上传成功");
                    finish();
//
                } else {
                    photoUid = "";
                }
            }
        });
    }
}
