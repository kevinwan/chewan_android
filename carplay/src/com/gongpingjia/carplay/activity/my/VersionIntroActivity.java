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

    private TextView mIntroText,carplay_version;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);
    }

    @Override
    public void initView() {

        setTitle("版本介绍");
        bundle = getIntent().getExtras();
        mIntroText = (TextView) findViewById(R.id.tv_version_intro);
        carplay_version = (TextView) findViewById(R.id.carplay_version);
        carplay_version.setText(bundle.getString("carplay_version")+"版本");
        String text = "<font color='#aab2bd'>推荐活动: </font>官方精选的靠谱的活动，活跃度高，安全有保障<br><font color='#aab2bd'>附近活动: </font>附近的Ta发布活动了，唱歌、吃饭、购物，还包接送<br><font color='#aab2bd'>匹配活动: </font>想去嗨皮？不用发完活动干等着，分分钟找到附近和你一起想去嗨皮的小伙伴<br><font color='#aab2bd'>车主认证: </font>百分百车主官方认证，交友出行100%有保障";
        mIntroText.setText(Html.fromHtml(text));
    }

}
