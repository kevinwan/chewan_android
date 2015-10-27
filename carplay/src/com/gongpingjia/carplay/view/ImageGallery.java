package com.gongpingjia.carplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.gallery.BasePagerAdapter.OnItemChangeListener;
import com.gongpingjia.carplay.view.gallery.GalleryViewPager;
import com.gongpingjia.carplay.view.gallery.UrlPagerAdapter;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageGallery extends CarPlayBaseActivity {

    private GalleryViewPager mViewPager;

    private TextView mIndicatorText;

    private RelativeLayout remove;

    User user;

    int photoCurrent;

    List<String> items;
    List<String> itemid;

    UrlPagerAdapter pagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagegallery);
        user = User.getInstance();

    }

    @Override
    public void initView() {
        mIndicatorText = (TextView) findViewById(R.id.tv_large_pic_title);
        remove = (RelativeLayout) findViewById(R.id.remove);

        Bundle bundle = getIntent().getExtras();

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

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (items.size() != 0) {
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
                                if (items.size() != 0) {
                                    pagerAdapter.notifyDataSetChanged();
                                    mViewPager.setCurrentItem(0);
                                    mIndicatorText.setText(getIndicatorString(0,
                                            items.size()));
                                } else {
                                    user.setHasAlbum(false);         //设置相册状态
                                    finish();
                                }
                                EventBus.getDefault().post(new String("上传成功"));

                            }
                        }
                    });
//                } else {
//                    EventBus.getDefault().post(new String("上传成功"));
//                    finish();
//                }
            }
        });
    }

    private String getIndicatorString(int index, int total) {
        return (index + 1) + "/" + total;
    }

    private void deletePhoto(){
        
    }

}
