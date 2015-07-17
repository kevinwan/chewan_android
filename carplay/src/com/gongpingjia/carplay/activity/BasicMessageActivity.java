package com.gongpingjia.carplay.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gongpingjia.carplay.R;
/**
 * 车玩基本信息
 * @author Administrator
 *
 */
public class BasicMessageActivity extends CarPlayBaseActivity {

	private RadioGroup sex=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_message);
		
		setTitle("基本信息");
		
		sex=(RadioGroup) findViewById(R.id.tab);
		sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				for (int i = 0; i < sex.getChildCount(); i++) {
					RadioButton rb_all=(RadioButton) sex.getChildAt(i);
					if (rb_all.isChecked()) 
						rb_all.setTextColor(Color.parseColor("#FD6D53"));
					else 
						rb_all.setTextColor(Color.parseColor("#aab2bd"));
				}
				
			}
		});
	}

}
