package com.gongpingjia.carplay.activity;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.os.Bundle;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;

/***
 * 新的留言页面 hqh
 * 
 * @author Administrator
 * 
 */
public class NewMessageActivity extends CarPlayBaseActivity {

	private NetRefreshAndMoreListView listView=null;
	
	NetJSONAdapter mJsonAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
	}

	@Override
	public void initView() {
		setTitle("新的留言");
		listView=(NetRefreshAndMoreListView) findViewById(R.id.listview);
		
		  mJsonAdapter = new NetJSONAdapter(API.allCarData, self, R.layout.item_newmessage_list);
	        mJsonAdapter.addparam("source", "personal");
	        mJsonAdapter.addparam("city", "南京");
//	        mJsonAdapter.addparam("brand", "");
//	        mJsonAdapter.addparam("model", "");
//	        mJsonAdapter.addparam("price", "");
//	        mJsonAdapter.addparam("year", "");
//	        mJsonAdapter.addparam("sort", "");
//	        mJsonAdapter.addparam("mile", "");
	        mJsonAdapter.fromWhat("car_list");
	        mJsonAdapter.addField(new FieldMap("title", R.id.name) {
				
				@Override
				public Object fix(View itemV, Integer position, Object o, Object jo) {
					// TODO Auto-generated method stub
					return "半夜偷西瓜";
				}
			});
	        mJsonAdapter.addField("thumbnail", R.id.pic, "carpic");
	        mJsonAdapter.addField(new FieldMap("mile", R.id.age)
	        {
	            
	            @Override
	            public Object fix(View itemV, Integer position, Object o, Object jo)
	            {
	                
	                return "23";
	            }
	            
	        });
	        mJsonAdapter.addField(new FieldMap("price", R.id.content)
	        {
	            
	            @Override
	            public Object fix(View itemV, Integer position, Object o, Object jo)
	            {
	                return o+"年驾龄";
	            }
	        });
	        
	        mJsonAdapter.addField(new FieldMap("year", R.id.sex)
	        {
	            
	            @Override
	            public Object fix(View itemV, Integer position, Object o, Object jo)
	            {
	            	if (Integer.parseInt(o.toString())%2==0) {
	            		itemV.findViewById(R.id.sex).setBackgroundResource(R.drawable.woman);
					}else{
						itemV.findViewById(R.id.sex).setBackgroundResource(R.drawable.man);
					}
	            	return o;
	            }
	        });
	       
	        
	        listView.setAdapter(mJsonAdapter);
	        mJsonAdapter.showNextInDialog();
		
	}
}
