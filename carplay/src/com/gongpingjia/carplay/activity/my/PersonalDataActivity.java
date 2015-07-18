package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * 
 * @Description 编辑资料界面
 * @author wang
 * @date 2015-7-16 上午9:21:59
 */
public class PersonalDataActivity extends CarPlayBaseActivity implements OnClickListener
{
    RelativeLayout personal_city, personal_driving_years, personal_head, personal_nickname, personal_sex;
    
    ImageView head_img;
    
    TextView nickname_txt, sex_txt, driving_years_txt, city_txt, title;
    
    private PersonalDataActivity mySelf = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);
        personal_city = (RelativeLayout)findViewById(R.id.personal_city);
        personal_driving_years = (RelativeLayout)findViewById(R.id.personal_driving_years);
        personal_head = (RelativeLayout)findViewById(R.id.personal_head);
        personal_nickname = (RelativeLayout)findViewById(R.id.personal_nickname);
        personal_sex = (RelativeLayout)findViewById(R.id.personal_sex);
        head_img = (ImageView)findViewById(R.id.head_img);
        nickname_txt = (TextView)findViewById(R.id.nickname_txt);
        sex_txt = (TextView)findViewById(R.id.sex_txt);
        driving_years_txt = (TextView)findViewById(R.id.driving_years_txt);
        city_txt = (TextView)findViewById(R.id.city_txt);
        title = (TextView)findViewById(R.id.title);
        title.setText("编辑资料");
        personal_city.setOnClickListener(this);
        personal_driving_years.setOnClickListener(this);
        personal_head.setOnClickListener(this);
        personal_nickname.setOnClickListener(this);
        personal_sex.setOnClickListener(this);
        
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.personal_head:
                
                break;
            case R.id.personal_driving_years:
                
                break;
            case R.id.personal_nickname:
                
                break;
            case R.id.personal_sex:
                
                break;
            case R.id.personal_city:
                
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }
    
}
