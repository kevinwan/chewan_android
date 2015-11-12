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

    private TextView remove, save, cancel;

    private ImageView back, more;

    private RelativeLayout operationLayout;

    User user;

    String type;

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
        } else {
            remove.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
        }

        remove.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
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
//                    MediaStore.Images.Media.insertImage(getContentResolver(), items.get(photoCurrent), "title", "description");
                Bitmap btm = ImageLoader.getInstance().getMemoryCache().get(items.get(photoCurrent));
//                savePhoto(btm);
//                Bitmap map = ImageLoad.getBitmap(items.get(photoCurrent), 0, 0);
//                SavePhotoDialog dialog = new SavePhotoDialog(ImageGallery.this, map, 0);
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
//                                ((ImageView) findViewById(R.id.imageView1))
//                                        .setImageBitmap(bitmap);
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

}
