package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/***
 * 
 * 创建活动
 * @author Administrator
 *
 */
public class CreatActiveActivity extends CarPlayBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_event);
		
		setTitle("创建活动");
	}

    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }

}
