package com.gongpingjia.carplay.activity.my;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/**
 * 
 * @Description 认证车主
 * @author wang
 * @date 2015-7-17 上午10:14:30
 */
public class AuthenticateOwnersActivity extends CarPlayBaseActivity {

	private TextView modelT=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_owners);
    }

    @Override
    public void initView() {
    	setTitle("车主认证");
        
    	modelT=(TextView) findViewById(R.id.model);
    	modelT.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(self,CarTypeSelectActivity.class);
				startActivity(intent);
			}
		});
    	
    	Bundle b=getIntent().getExtras();
    	Bundle bundle=(Bundle) b.get("data");
    	System.out.println(bundle.get("phone")+"-------"+bundle.get("code")+"-------"+bundle.get("pswd")+"-------"+bundle.get("nickname")+"-------"+bundle.get("sex")+"-------"+bundle.get("age")+"-------"+bundle.get("city"));
    	
    }

}
