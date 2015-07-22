package com.gongpingjia.carplay.activity.my;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.util.PhotoUtil;

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
	private TextView ageT = null;

	/** 城市 */
	private TextView cityT = null;

	/** 下一步 */
	private Button nextBtn = null;

	private String sex="男";
	
	
	
	 private String mPhotoPath;
	 
	 
	 private File dir;
	 
	 private static final int REQUEST_IMG_TAKE = 1;
	 private static final int REQUEST_CROP = 2;
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
					if (rb_all.isChecked()){
						sex=rb_all.getText().toString();
						rb_all.setTextColor(Color.parseColor("#FD6D53"));
					}else
						rb_all.setTextColor(Color.parseColor("#aab2bd"));
				}
				
			}
		});

		headI = (ImageView) findViewById(R.id.head);
		nicknameT = (EditText) findViewById(R.id.nickname);
		ageT = (TextView) findViewById(R.id.age);
		cityT = (TextView) findViewById(R.id.city);
		nextBtn = (Button) findViewById(R.id.next);

		headI.setOnClickListener(this);
		ageT.setOnClickListener(this);
		cityT.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		
		
		dir = new File(this.getExternalCacheDir(), "gpj");
        dir.mkdirs();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.head:
			 mPhotoPath = new File(dir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
             PhotoUtil.getPhotoFromCamera(this, REQUEST_IMG_TAKE, mPhotoPath);
			break;
		case R.id.age:

			break;
		case R.id.city:

			break;
		case R.id.next:
			
			nextStep();
			
			break;

		default:
			break;
		}
	}

	private void nextStep() {
		
		final String strnickname = nicknameT.getText().toString().trim();
		if (TextUtils.isEmpty(strnickname)) {
			showToast("昵称不能为空");
			return;
		}
		final String strage = ageT.getText().toString();
		if (TextUtils.isEmpty(strage)) {
			showToast("请设置您的年龄");
			return;
		}
		final String strcity = cityT.getText().toString();
		if (TextUtils.isEmpty(strcity)) {
			showToast("请设置您所在的城市");
			return;
		}
		
		
		Bundle bundle=new Bundle();
		Bundle b=getIntent().getExtras();
		if(b!=null){
			bundle.putString("phone", b.getString("phone"));
			bundle.putString("code", b.getString("code"));
			bundle.putString("pswd", b.getString("pswd"));
		}
		bundle.putString("nickname", strnickname);
		bundle.putString("sex",sex);
		bundle.putString("age", strage);
		bundle.putString("city", strcity);
		Intent it=new Intent(self,AuthenticateOwnersActivity.class);
		it.putExtra("data", bundle);
		startActivity(it);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            // Bundle bundle = data.getExtras();
            switch (requestCode)
            {
                case REQUEST_IMG_TAKE:
                    PhotoUtil.onPhotoFromCamera(this,mPhotoPath ,REQUEST_CROP );
                    break;
                
            }
        }
    }
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString("temppath", mPhotoPath);
        super.onSaveInstanceState(savedInstanceState);
    }
}
