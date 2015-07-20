package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/**
 * 活动参与申请
 * @author wang
 *
 */
public class ParticipateApplicationActivity extends CarPlayBaseActivity {
	 NetJSONAdapter adapter;

	    NetRefreshAndMoreListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_application);
    }

    @Override
    public void initView() {
        setTitle("活动参与申请");
        listView = (NetRefreshAndMoreListView) findViewById(R.id.listview);
        adapter = new NetJSONAdapter(API.allCarData, self, R.layout.itme_participate_application);
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
        adapter.addField("thumbnail", R.id.car_brand, "carpic");
        adapter.addField(new FieldMap("mile", R.id.participate_name) {

            @Override
            public Object fix(View itemV, Integer position, Object o, Object jo) {

                return "淘气小布丁" ;
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

         adapter.addField(new FieldMap("title", R.id.participate_txt)
         {
        
         @Override
         public Object fix(View itemV, Integer position, Object o, Object jo)
         {
         return "想加入昆明湖一日游活动" ;
         }
         });

        listView.setAdapter(adapter);
        adapter.showNextInDialog();
    }

}
