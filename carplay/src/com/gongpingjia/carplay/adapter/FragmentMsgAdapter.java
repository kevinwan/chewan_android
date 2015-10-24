package com.gongpingjia.carplay.adapter;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.util.SmileUtils;
import com.gongpingjia.carplay.view.BadgeView;
import com.gongpingjia.carplay.view.RoundImageView;
import com.google.gson.JsonArray;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FragmentMsgAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    JSONObject jo;

    List<EMConversation> conversationList;

    List<JSONArray> headList;
    List<String> GroupIdlist;

    public FragmentMsgAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (conversationList == null) {
            return 0;
        } else {
            return conversationList.size();
        }
    }

    @Override
    public EMConversation getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setGroupMessageData(List<EMConversation> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 0:
                    convertView = mInflater.inflate(R.layout.item_dynamic_interested, null);
                    holder.iconI = (ImageView) convertView.findViewById(R.id.icon);
                    holder.right_headI = (ImageView) convertView.findViewById(R.id.right_head);

                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.item_dynamic_admin, null);
                    holder.iconI = (ImageView) convertView.findViewById(R.id.icon);
                    holder.timeT = (TextView) convertView.findViewById(R.id.time);
                    holder.contentT = (TextView) convertView.findViewById(R.id.content);
                    break;

                case 2:
                    convertView = mInflater.inflate(R.layout.item_group_message2, null);
                    holder.head_one = (RoundImageView) convertView
                            .findViewById(R.id.head_one);
                    holder.head_two = (RelativeLayout) convertView
                            .findViewById(R.id.head_two);
                    holder.head_three = (RelativeLayout) convertView
                            .findViewById(R.id.head_three);
                    holder.head_four = (RelativeLayout) convertView
                            .findViewById(R.id.head_four);
                    holder.timeT = (TextView) convertView.findViewById(R.id.time);
                    holder.tv_ageT = (TextView) convertView.findViewById(R.id.tv_age);
                    holder.contentT = (TextView) convertView.findViewById(R.id.content);
                    break;

            }
            holder.titleT = (TextView) convertView.findViewById(R.id.title);
            holder.msg_pointI = (ImageView) convertView.findViewById(R.id.msg_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EMConversation conversation = conversationList.get(position);
        switch (type) {
            case 0:

                break;

            case 1:
                String username = conversation.getUserName();
                if (username.equals("UserViewAdmin")) {
                    holder.titleT.setText("活动动态");
                    holder.iconI.setImageResource(R.drawable.trends_active);
                } else if (username.equals("ActivityStateAdmin")) {
                    holder.titleT.setText("活动动态");

                } else if (username.equals("SubscribeAdmin")) {
                    holder.titleT.setText("活动动态");
                } else {
                    holder.titleT.setText("活动动态");
                }

                break;

            case 2:
                String username1 = conversation.getUserName();
                EMGroup group = EMGroupManager.getInstance().getGroup(username1);
                if (group != null) {
                    ViewUtil.bindView(convertView.findViewById(R.id.title),
                            group.getGroupName());
                    String des = group.getDescription();
                    Log.d("msg", "群组头像"+des);
//                    //判断当前群组
//                    if (headList != null && GroupIdlist != null) {
//                        for (int i = 0; i < GroupIdlist.size(); i++) {
//                            if (group.getGroupId().equals(GroupIdlist.get(i))) {
////						System.out.println("");
//                                //设置头像
//                                setPicState(holder, headList.get(i), headList.get(i).length());
//                            }
//                        }
//                    }

                } else {
                    ViewUtil.bindView(convertView.findViewById(R.id.title),
                            username1);
                    holder.head_one.setVisibility(View.VISIBLE);
//                    ViewUtil.bindNetImage(holder.head_one,
//                            message.getStringAttribute("headUrl", ""), "head");
                }
                break;
        }


        if (type != 0) {
            if (conversation.getMsgCount() != 0) {

                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                holder.contentT.setText(

                        SmileUtils.getSmiledText(mContext,
                                getMessageDigest(lastMessage, (mContext))),
                        BufferType.SPANNABLE);
                holder.timeT.setVisibility(View.VISIBLE);
                ViewUtil.bindView(holder.timeT, lastMessage.getMsgTime(), "neartime");
                if (lastMessage.direct == EMMessage.Direct.SEND
                        && lastMessage.status == EMMessage.Status.FAIL) {
//                    msg_stateI.setVisibility(View.VISIBLE);
                } else {
//                    msg_stateI.setVisibility(View.GONE);
                }
            } else {
                holder.contentT.setText("暂无消息");
                holder.timeT.setVisibility(View.GONE);
            }
        }

        holder.msg_pointI
                .setVisibility(
                        conversation.getUnreadMsgCount() > 0 ? View.VISIBLE
                                : View.GONE);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        EMConversation conversion = conversationList.get(position);
        if (conversion.getUserName().equals("InterestAdmin")) {
            return 0;
        } else if (conversion.getUserName().equals("UserViewAdmin")
                || conversion.getUserName().equals("ActivityStateAdmin")
                || conversion.getUserName().equals("SubscribeAdmin")
                || conversion.getUserName().equals("OfficialAdmin")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    // digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture);
                ;
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息

                if (((DemoHXSDKHelper) HXSDKHelper.getInstance())
                        .isRobotMenuMessage(message)) {
                    digest = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
                            .getRobotMenuMessageDigest(message);
                } else if (message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call)
                            + txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:

                return "";
        }

        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 设置群组头像状态
     *
     * @param holder
     * @param count
     */
    private void setPicState(ViewHolder holder, JSONArray jsona, int poi) {
        holder.head_one.setVisibility(View.GONE);
        holder.head_two.setVisibility(View.GONE);
        holder.head_three.setVisibility(View.GONE);
        holder.head_four.setVisibility(View.GONE);
        switch (poi) {
            case 0:
                break;
            case 1:
                holder.head_one.setVisibility(View.VISIBLE);
                try {
                    ViewUtil.bindNetImage(holder.head_one, jsona.getString(0), "head");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 2:
                holder.head_two.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_two, jsona);
                break;
            case 3:
                holder.head_three.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_three, jsona);
                break;
            default:
                holder.head_four.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_four, jsona);
                break;
        }
    }

    /**
     * 设置群组头像
     *
     * @param rel
     */
    private void setHeadImg(RelativeLayout rel, JSONArray jsona) {
        for (int i = 0; i < rel.getChildCount(); i++) {
            RoundImageView icon = (RoundImageView) rel.getChildAt(i);
            try {
                ViewUtil.bindNetImage(icon, jsona.getString(i), "head");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class ViewHolder {
        RoundImageView head_one;
        RelativeLayout head_two, head_three, head_four;
        TextView titleT, contentT, timeT, tv_ageT;

        ImageView iconI, right_headI, msg_pointI;

    }

    public void setListHead(List<JSONArray> jslist, List<String> jsGroupId) {
        headList = jslist;
        GroupIdlist = jsGroupId;
        notifyDataSetChanged();
    }

}
