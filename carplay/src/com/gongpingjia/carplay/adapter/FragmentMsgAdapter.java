package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.Constant;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.chat.util.SmileUtils;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    OnEmptyListener onEmptyListener;

    public FragmentMsgAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (conversationList == null || conversationList.size() == 0) {
            if (onEmptyListener != null) {
                onEmptyListener.onEmpty(true);
            }
            return 0;
        } else {
            if (onEmptyListener != null) {
                onEmptyListener.onEmpty(false);
            }
            return conversationList.size();
        }
    }

    public OnEmptyListener getOnEmptyListener() {
        return onEmptyListener;
    }

    public void setOnEmptyListener(OnEmptyListener onEmptyListener) {
        this.onEmptyListener = onEmptyListener;
    }

    public interface OnEmptyListener {
        void onEmpty(boolean isEmpty);
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
                    holder.right_headI = (RoundImageView) convertView.findViewById(R.id.right_head);

                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.item_dynamic_admin, null);
                    holder.iconI = (ImageView) convertView.findViewById(R.id.icon);
                    holder.timeT = (TextView) convertView.findViewById(R.id.time);
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
                    holder.layout_sex_and_ageV = convertView.findViewById(R.id.layout_sex_and_age);
                    break;

            }
            holder.contentT = (TextView) convertView.findViewById(R.id.content);
            holder.titleT = (TextView) convertView.findViewById(R.id.title);
            holder.msg_pointI = (ImageView) convertView.findViewById(R.id.msg_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EMConversation conversation = conversationList.get(position);
        EMMessage lastMessage = conversation.getLastMessage();
        switch (type) {
            case 0:
                holder.titleT.setText("感兴趣的人");
                ViewUtil.bindNetImage(holder.right_headI,
                        lastMessage.getStringAttribute("avatar", ""), "head");
//                holder.right_headI.setTag(lastMessage.getStringAttribute(""));
                break;

            case 1:
                String username = conversation.getUserName();
                if (username.equals("UserViewAdmin")) {
                    holder.titleT.setText("最近访客");
                    holder.iconI.setImageResource(R.drawable.trends_visit);
                } else if (username.equals("ActivityStateAdmin")) {
                    holder.titleT.setText("活动动态");
                    holder.iconI.setImageResource(R.drawable.trends_active);

                } else if (username.equals("SubscribeAdmin")) {
                    holder.titleT.setText("谁关注我");
                    holder.iconI.setImageResource(R.drawable.trends_attention);

                } else if (username.equals("OfficialAdmin")) {
                    holder.titleT.setText("车玩官方");
                    holder.iconI.setImageResource(R.drawable.trends_official);
                } else if (username.equals("NearbyAdmin")) {
                    holder.titleT.setText("附近");
                    holder.iconI.setImageResource(R.drawable.icon_near);
                }

                break;

            case 2:
                String username1 = conversation.getUserName();
                EMGroup group = EMGroupManager.getInstance().getGroup(username1);
                if (group != null) {
                    holder.layout_sex_and_ageV.setVisibility(View.GONE);
                    holder.head_one.setVisibility(View.GONE);
                    ViewUtil.bindView(convertView.findViewById(R.id.title),
                            group.getGroupName());
                    String des = group.getDescription();
                    Log.d("msg", "群组头像" + des);
                    String urls;
                    urls = des.replaceAll("\\|", "/");
                    Log.d("urls", urls);
                    String[] urlArray = urls.split(";");
                    Log.d("urlArray", urlArray.toString());
                    setPicState(holder, urlArray, urlArray.length);
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
                    holder.head_two.setVisibility(View.GONE);
                    holder.head_three.setVisibility(View.GONE);
                    holder.head_four.setVisibility(View.GONE);
                    holder.head_one.setVisibility(View.VISIBLE);
                    holder.layout_sex_and_ageV.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(lastMessage.getStringAttribute("headUrl", ""))) {
                        getUserInfo(username1, holder);
                    } else {
                        ViewUtil.bindNetImage(holder.head_one,
                                lastMessage.getStringAttribute("headUrl", ""), "head");
                        ViewUtil.bindView(holder.titleT,
                                lastMessage.getStringAttribute("nickName", ""));
                        try {
                            ViewUtil.bindView(holder.tv_ageT,
                                    lastMessage.getIntAttribute("age") + "");


                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                        CarPlayUtil.bindSexView(lastMessage.getStringAttribute("gender", ""), holder.layout_sex_and_ageV);
                    }
                }
                break;
        }

        if (conversation.getMsgCount() != 0) {

            // 把最后一条消息的内容作为item的message内容
            holder.contentT.setText(

                    SmileUtils.getSmiledText(mContext,
                            getMessageDigest(lastMessage, (mContext))),
                    BufferType.SPANNABLE);
            if (type != 0) {
                holder.timeT.setVisibility(View.VISIBLE);
                ViewUtil.bindView(holder.timeT, lastMessage.getMsgTime(), "neartime");
            }
            if (lastMessage.direct == EMMessage.Direct.SEND
                    && lastMessage.status == EMMessage.Status.FAIL) {
//                    msg_stateI.setVisibility(View.VISIBLE);
            } else {
//                    msg_stateI.setVisibility(View.GONE);
            }

        } else {
            holder.contentT.setText("暂无消息");
            if (type != 0) {
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
                || conversion.getUserName().equals("OfficialAdmin")
                || conversion.getUserName().equals("NearbyAdmin")) {
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
     */
    private void setPicState(ViewHolder holder, String[] urls, int poi) {
        holder.head_one.setVisibility(View.GONE);
        holder.head_two.setVisibility(View.GONE);
        holder.head_three.setVisibility(View.GONE);
        holder.head_four.setVisibility(View.GONE);
        switch (poi) {
            case 0:
                break;
            case 1:
                holder.head_one.setVisibility(View.VISIBLE);
                ViewUtil.bindNetImage(holder.head_one, urls[0], "head");
                break;
            case 2:
                holder.head_two.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_two, urls);
                break;
            case 3:
                holder.head_three.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_three, urls);
                break;
            default:
                holder.head_four.setVisibility(View.VISIBLE);
                setHeadImg(holder.head_four, urls);
                break;
        }
    }

    /**
     * 设置群组头像
     *
     * @param rel
     */
    private void setHeadImg(RelativeLayout rel, String[] urls) {
        for (int i = 0; i < rel.getChildCount(); i++) {
            RoundImageView icon = (RoundImageView) rel.getChildAt(i);
            ViewUtil.bindNetImage(icon, urls[i], "head");
        }
    }

    class ViewHolder {
        RoundImageView head_one;
        RelativeLayout head_two, head_three, head_four;
        TextView titleT, contentT, timeT, tv_ageT;

        ImageView iconI, msg_pointI;
        RoundImageView right_headI;
        View layout_sex_and_ageV;

    }

    public void setListHead(List<JSONArray> jslist, List<String> jsGroupId) {
        headList = jslist;
        GroupIdlist = jsGroupId;
        notifyDataSetChanged();
    }

    private void getUserInfo(String username, final ViewHolder holder) {
        DhNet dhNet = new DhNet(API2.getProfileFromHx(User.getInstance().getUserId(), User.getInstance().getToken(), username));
        dhNet.doGet(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jsonObject = response.jSONFrom("data");
                    try {
                        holder.titleT.setText(jsonObject.getString("nickname"));
                        ImageLoader.getInstance().displayImage(jsonObject.getString("avatar"), holder.head_one);
                        CarPlayUtil.bindSexView(jsonObject.getString("gender"), holder.layout_sex_and_ageV);
                        ViewUtil.bindView(holder.tv_ageT, jsonObject.getString("age"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
