package com.gongpingjia.carplay.activity;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.ChatAdapter;
import com.gongpingjia.carplay.adapter.ChatAdapter.OnRefreshListener;
import com.gongpingjia.carplay.bean.ChatMessage;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ChatActivity extends BaseActivity implements OnClickListener {

    private ListView mChatListView;

    private ChatAdapter mChatAdapter;

    private List<ChatMessage> mDatas;

    private ImageView mSendImgView;

    private EditText mMsgEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMsgEdit = (EditText) findViewById(R.id.et_comment);
        mSendImgView = (ImageView) findViewById(R.id.imgView_smile);
        mSendImgView.setOnClickListener(this);

        mDatas = new ArrayList<ChatMessage>();
        for (int i = 0; i < 10; i++) {
            ChatMessage msg = new ChatMessage();
            if (i % 4 == 0) {
                msg.setType("from");
                msg.setMsg("今天去哪吃啊啊啊啊");
            } else {
                msg.setType("send");
                msg.setMsg("你说去哪就去那啊啊");
            }
            mDatas.add(msg);
        }

        mChatListView = (ListView) findViewById(R.id.lv_chat);
        mChatAdapter = new ChatAdapter(getApplicationContext(), mDatas);
        mChatAdapter.setRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                mChatListView.setSelection(mChatListView.getCount() - 1);
            }
        });
        mChatListView.setAdapter(mChatAdapter);
        mChatAdapter.refresh();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.imgView_smile:
            String msg = mMsgEdit.getText().toString().trim();
            ChatMessage chatMsg = new ChatMessage();
            chatMsg.setMsg(msg);
            chatMsg.setType("send");
            mDatas.add(chatMsg);
            mChatAdapter.refresh();
            break;
        }
    }

}
