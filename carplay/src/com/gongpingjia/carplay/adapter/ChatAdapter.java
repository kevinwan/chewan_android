package com.gongpingjia.carplay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.ChatMessage;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ChatAdapter extends BaseAdapter {

    Context mContext;

    List<ChatMessage> mDatas;

    LayoutInflater mInflater;

    OnRefreshListener mRefreshListener;

    public ChatAdapter(Context context, List<ChatMessage> data) {
        this.mContext = context;
        this.mDatas = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (mDatas.get(position).getType().equals("from")) {
            // 来的消息
            return 0;
        } else if (mDatas.get(position).equals("send")) {
            // 发送的消息
            return 1;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ChatMessage msg = mDatas.get(position);
        if (convertView == null) {
            if (msg.getType().equals("from")) {
                // 来的消息
                convertView = mInflater.inflate(R.layout.listitem_msg_left, null);
            } else if (msg.getType().equals("send")) {
                // 发送的消息
                convertView = mInflater.inflate(R.layout.listitem_msg_right, null);
            }
        }

        if (msg.getType().equals("from")) {
            TextView msgText = ViewHolder.getView(convertView, R.id.tv_msg_left);
            msgText.setText(msg.getMsg());
        } else if (msg.getType().equals("send")) {
            TextView msgText = ViewHolder.getView(convertView, R.id.tv_msg_right);
            msgText.setText(msg.getMsg());
        }

        return convertView;
    }

    public void refresh() {
        notifyDataSetChanged();
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    public void setRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
