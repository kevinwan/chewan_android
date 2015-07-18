package com.gongpingjia.carplay.activity;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;

/**
 * 玩转车聊页面 hqh
 */
public class PlayCarChatActivity extends CarPlayBaseActivity {
	private NetRefreshAndMoreListView listView = null;

	NetJSONAdapter mJsonAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_car_chat);
	}

	@Override
	public void initView() {
		
		setTitle("玩转车聊");
		listView=(NetRefreshAndMoreListView) findViewById(R.id.listview);
		
		  mJsonAdapter = new NetJSONAdapter(API.allCarData, self, R.layout.item_carchat_list);
	        mJsonAdapter.addparam("source", "personal");
	        mJsonAdapter.addparam("city", "南京");
//	        mJsonAdapter.addparam("brand", "");
//	        mJsonAdapter.addparam("model", "");
//	        mJsonAdapter.addparam("price", "");
//	        mJsonAdapter.addparam("year", "");
//	        mJsonAdapter.addparam("sort", "");
//	        mJsonAdapter.addparam("mile", "");
	        mJsonAdapter.fromWhat("car_list");
	        mJsonAdapter.addField(new FieldMap("title", R.id.text_title) {
				
				@Override
				public Object fix(View itemV, Integer position, Object o, Object jo) {
					// TODO Auto-generated method stub
					return o;
				}
			});
	        mJsonAdapter.addField(new FieldMap("price", R.id.content)
	        {
	            
	            @Override
	            public Object fix(View itemV, Integer position, Object o, Object jo)
	            {
	                return "您只需要将话题已井号加话题即 # 话题 (自定义)#+活动内容的形式发布话题即可";
	            }
	        });
	        
	        
	        listView.setAdapter(mJsonAdapter);
	        mJsonAdapter.showNextInDialog();
	}

}
