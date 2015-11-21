package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.model.HXSDKModel;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MsgNotice extends CarPlayBaseActivity {
    RelativeLayout layout_sound, layout_shock;
    CheckBox sound_check, shock_check;

    HXSDKModel model;

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

        model = HXSDKHelper.getInstance().getModel();
        if (model.getSettingMsgNotification()) {
            sound_check.setChecked(model.getSettingMsgSound());
            shock_check.setChecked(model.getSettingMsgVibrate());
        }
        sound_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setSettingMsgSound(isChecked);
            }
        });

        shock_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setSettingMsgVibrate(isChecked);
            }
        });

    }
}
