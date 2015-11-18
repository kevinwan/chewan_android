package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MsgNotice extends CarPlayBaseActivity{
    RelativeLayout layout_sound,layout_shock;
    CheckBox sound_check,shock_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgnotice);
    }

    @Override
    public void initView() {
        setTitle("消息通知");
         layout_sound = (RelativeLayout) findViewById(R.id.layout_sound);
         layout_shock = (RelativeLayout) findViewById(R.id.layout_shock);
         sound_check = (CheckBox) findViewById(R.id.sound_check);
         shock_check = (CheckBox) findViewById(R.id.shock_check);

    }
}
