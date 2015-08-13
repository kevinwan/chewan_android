package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.R.color;
import com.gongpingjia.carplay.R.id;
import com.gongpingjia.carplay.R.layout;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
/**
 * 车主认证通知
 * @author Administrator
 *
 */
public class AttestationNotifyActivity extends CarPlayBaseActivity {
	TextView contentpassT,contentfailT;
	Button nextBtn;
	LinearLayout notpassL,passL;
	String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attestation_notify);
	}

	@Override
	public void initView() {
		setTitle("车主认证通知");
		contentpassT=(TextView) findViewById(R.id.contentpass);
		contentfailT=(TextView) findViewById(R.id.contentfail);
		nextBtn=(Button) findViewById(R.id.next);
		notpassL=(LinearLayout) findViewById(R.id.notpass);
		passL=(LinearLayout) findViewById(R.id.pass);
		
		Intent it=getIntent();
		String model=it.getExtras().getString("carModel");
		result=it.getExtras().getString("result");
		
		if(result.equals("0")){
			pass(model);
		}else{
			fail(model);
		}
		
		nextBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (result.equals("0")) {
					finish();
				}else{
					Intent intent=new Intent(self,AuthenticateOwnersActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	
	/** 通过 */
	private void pass(String model){
		notpassL.setVisibility(View.GONE);
		passL.setVisibility(View.VISIBLE);
		nextBtn.setText("开启我的车旅程");
		
		String newcontent = null;
		String a="您申请的";
		String b=model;
		String c="身份认证已经审核通过";
		newcontent=a+b+c;
		
		contentpassT.setText(strColor(newcontent,b));
	}
	/** 未通过 */
	private void fail(String model){
		notpassL.setVisibility(View.VISIBLE);
		passL.setVisibility(View.GONE);
		nextBtn.setText("重新认证");
		
		String newcontent = null;
		String a="您申请的";
		String b=model;
		String c="身份认证未通过审核";
		newcontent=a+b+c;
		
		contentfailT.setText(strColor(newcontent,b));
	}
	
	private SpannableStringBuilder strColor(String newcontent,String str){
		int start = newcontent.indexOf(str);
		SpannableStringBuilder style = new SpannableStringBuilder(
				newcontent);
		style.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.theme_bg)), start,
				start + str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

}
