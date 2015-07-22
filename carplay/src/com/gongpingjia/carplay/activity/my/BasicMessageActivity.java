package com.gongpingjia.carplay.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * 车玩基本信息
 * 
 * @author Administrator
 * 
 */
public class BasicMessageActivity extends CarPlayBaseActivity implements
		OnClickListener {
	/** 头像 */
	private ImageView headI = null;

	/** 昵称 */
	private EditText nicknameT = null;

	/** 性别 */
	private RadioGroup sexR = null;

	/** 年龄 */
	private LinearLayout ageLl = null;

	/** 城市 */
	private LinearLayout cityLl = null;

	/** 下一步 */
	private Button nextBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_message);

	}

	@Override
	public void initView() {
		setTitle("注册");

		sexR = (RadioGroup) findViewById(R.id.tab);
		sexR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				for (int i = 0; i < sexR.getChildCount(); i++) {
					RadioButton rb_all = (RadioButton) sexR.getChildAt(i);
					if (rb_all.isChecked())
						rb_all.setTextColor(Color.parseColor("#FD6D53"));
					else
						rb_all.setTextColor(Color.parseColor("#aab2bd"));
				}

			}
		});

		headI = (ImageView) findViewById(R.id.head);
		nicknameT = (EditText) findViewById(R.id.nickname);
		ageLl = (LinearLayout) findViewById(R.id.age);
		cityLl = (LinearLayout) findViewById(R.id.city);
		nextBtn = (Button) findViewById(R.id.next);

		headI.setOnClickListener(this);
		ageLl.setOnClickListener(this);
		cityLl.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.head:
			
			break;
		case R.id.age:

			break;
		case R.id.city:

			break;
		case R.id.next:

			break;

		default:
			break;
		}
	}

}
