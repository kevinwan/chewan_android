package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class VersionIntroActivity extends CarPlayBaseActivity {

    private TextView mIntroText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);
    }

    @Override
    public void initView() {

        setTitle("版本介绍");
        mIntroText = (TextView) findViewById(R.id.tv_version_intro);
        String text = "<font color='#aab2bd'>同城活动: </font>最火爆的同城交友活动,自驾游吃饭...约上附近的车友一起各种happy<br><font color='#aab2bd'>发布活动: </font>不管你是有车还是没车都可以参加<br><font color='#aab2bd'>抢车座: </font>加入活动可以抢座在男神女神旁边活动成员聊天;活动的成员可以聚在一起各种聊天,成为朋友<br><font color='#aab2bd'>车主认证: </font>如果你有车你就可以认证车主通过以后你将更有吸引力";
        mIntroText.setText(Html.fromHtml(text));
    }

}
