package com.gongpingjia.carplay.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;

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

    private TextView remove, save, cancel,sethead;

    private ImageView back, more;

    private RelativeLayout operationLayout;

    User user;

    String type;

    String mPhotoPath;

    int photoCurrent;

    List<String> items;
    List<String> itemid;

    UrlPagerAdapter pagerAdapter;

    boolean showFlag = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagegallery);
        user = User.getInstance();

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
        items = new ArrayList<String>();
        Collections.addAll(items, urls);
        itemid = new ArrayList<String>();
        Collections.addAll(itemid, ids);

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
                    if (items.size() < 2){
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
                setHeadImage();

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
                case Constant.PICK_PHOTO:


                    PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
                            data, 1, 1, 1000);

//                    Bitmap btp = PhotoUtil.checkImage(self, data);
//                    PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
//                    btp.recycle();
//                    showProgressDialog("上传头像中...");
//                    uploadHead(mPhotoPath);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setHeadImage(){
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
                        File appDir = new File(Environment
                                .getExternalStorageDirectory(), "carplay");
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        String fileName = System.currentTimeMillis() + ".jpg";
                        File file = new File(appDir, fileName);
                        Uri uri = Uri.fromFile(file);
//                        mPhotoPath = PhotoUtil.getRealPathFromURI(self, uri);
                        mPhotoPath=getRealPath(uri);
                        System.out.println("mPhotoPath: -----------   "+mPhotoPath);
//                        Intent getImage = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        getImage.addCategory(Intent.CATEGORY_OPENABLE);
//                        getImage.setType("image/jpeg");
//                        startActivityForResult(getImage, Constant.PICK_PHOTO);
                        PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
                                bitmap, 1, 1, 1000);
                    }
                });

            }
        }).start();
    }

    private String getRealPath(Uri fileUrl){
        String fileName = null;
        Uri filePathUri = fileUrl;
        if(fileUrl!= null){
            if (fileUrl.getScheme().toString().compareTo("content")==0)           //content://开头的uri
            {
                Cursor cursor = getContentResolver().query(fileUrl, null, null, null, null);
                if (cursor != null && cursor.moveToFirst())
                {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileName = cursor.getString(column_index);          //取出文件路径
                    if(!fileName.startsWith("/mnt")){
//检查是否有”/mnt“前缀

                        fileName = "/mnt" + fileName;
                    }
                    cursor.close();
                }
            }else if (fileUrl.getScheme().compareTo("file")==0)         //file:///开头的uri
            {
                fileName = filePathUri.toString();
                fileName = filePathUri.toString().replace("file://", "");
//替换file://
                if(!fileName.startsWith("/mnt")){
//加上"/mnt"头
                    fileName += "/mnt";
                }
            }
        }
        return fileName;
    }
}
