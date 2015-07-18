package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.BeanAdapter.ViewHolder;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;

/**
 * 
 * @Description 我关注的人
 * @author wang
 * @date 2015-7-17 上午11:09:27
 */

public class AttentionPersonActivity extends CarPlayBaseActivity {
    NetJSONAdapter adapter;

    NetRefreshAndMoreListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_person);

    }

    @Override
    public void initView() {
        setTitle("我关注的人");
        listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
        adapter = new NetJSONAdapter(API.allCarData, self, R.layout.itme_attention_person);
        adapter.addparam("source", "personal");
        adapter.addparam("city", "哈尔滨");
        adapter.addparam("brand", "");
        adapter.addparam("model", "");
        adapter.addparam("price", "");
        adapter.addparam("year", "");
        adapter.addparam("sort", "");
        adapter.addparam("mile", "");
        adapter.fromWhat("car_list");
        adapter.addField("title", R.id.car_age);
        adapter.addField("thumbnail", R.id.car_img, "carpic");
        adapter.addField(new FieldMap("mile", R.id.person_name) {

            @Override
            public Object fix(View itemV, Integer position, Object o, Object jo) {

                return "秦始皇" ;
            }

        });
         adapter.addField(new FieldMap("price", R.id.person_age)
         {
        
         @Override
         public Object fix(View itemV, Integer position, Object o, Object jo)
         {
         return  "23" ;
         }
         });

         adapter.addField(new FieldMap("title", R.id.car_age)
         {
        
         @Override
         public Object fix(View itemV, Integer position, Object o, Object jo)
         {
         return "御车    ,  2222年驾龄" ;
         }
         });

        listView.setAdapter(adapter);
        adapter.showNextInDialog();
    }
}
