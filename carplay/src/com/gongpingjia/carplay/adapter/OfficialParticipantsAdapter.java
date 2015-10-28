package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity2;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.chat.VoiceCallActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.NojoinOfficialDialog;
import com.gongpingjia.carplay.view.dialog.OfficialMsgDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/28.
 * 参与成员adapter
 */
public class OfficialParticipantsAdapter extends BaseAdapter {

    private final Context mContext;

    private List<JSONObject> data;

    private boolean isMember;

    private String activeid;

    private User user;

    public OfficialParticipantsAdapter(Context context) {
        mContext = context;
        user = User.getInstance();
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
        notifyDataSetChanged();
    }

    public void setData(List<JSONObject> data, boolean isMember, String activeid) {
        this.data = data;
        this.isMember = isMember;
        this.activeid = activeid;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data) {
            item = (JSONObject) data.get(position);
        }
        return item;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_official_participants, null);
            holder.head = (RoundImageView) convertView.findViewById(R.id.head);
            holder.name = (TextView) convertView.findViewById(R.id.nickname);
            holder.age = (TextView) convertView.findViewById(R.id.tv_age);
            holder.invitecount = (TextView) convertView.findViewById(R.id.invitecount);
            holder.invite = (TextView) convertView.findViewById(R.id.invite);
            holder.acceptedcount = (TextView) convertView.findViewById(R.id.acceptedcount);
            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
            holder.sexLayout = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.acceptedlayout = (LinearLayout) convertView.findViewById(R.id.acceptedlayout);
            holder.headstatus = (ImageView) convertView.findViewById(R.id.headstatus);
            holder.contactlayout = (LinearLayout) convertView.findViewById(R.id.contactlayout);
            holder.invitelayout = (LinearLayout) convertView.findViewById(R.id.invitelayout);
            holder.sms = (AnimButtonView) convertView.findViewById(R.id.sms);
            holder.call = (AnimButtonView) convertView.findViewById(R.id.call);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);
        final String userId = JSONUtil.getString(jo, "userId");

        ViewUtil.bindNetImage(holder.head, JSONUtil.getString(jo, "avatar"), "head");
        ViewUtil.bindView(holder.name, JSONUtil.getString(jo, "nickname"));
        ViewUtil.bindView(holder.age, JSONUtil.getInt(jo, "age"));
        String gender = JSONUtil.getString(jo, "gender");
        if (("男").equals(gender)) {
            holder.sexLayout.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sex.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexLayout.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sex.setBackgroundResource(R.drawable.icon_woman3x);
        }
        String photoAuthStatus = JSONUtil.getString(jo, "photoAuthStatus");
        holder.headstatus.setVisibility("已认证".equals(photoAuthStatus) ? View.VISIBLE : View.GONE);
        //邀请的状态
        final int inviteStatus = JSONUtil.getInt(jo, "inviteStatus");
        final int beInvitedStatus = JSONUtil.getInt(jo, "beInvitedStatus");
        setInviteStatus(holder, inviteStatus, beInvitedStatus, jo);
        System.out.print("------------------------" + JSONUtil.getInt(jo, "beInvitedStatus"));

        holder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已参加该活动
                if (isMember) {
                    inviteTogether(userId);
                } else {
                    if (inviteStatus == 0 && beInvitedStatus == 1) {
                        ((ActiveDetailsActivity2) mContext).showToast("已邀请您,请您去活动动态里处理邀请");
                    } else {
                        NojoinOfficialDialog dialog = new NojoinOfficialDialog(mContext);
                        dialog.show();
                    }
                }
            }
        });

        holder.invite.setVisibility(userId.equals(user.getUserId()) ? View.GONE : View.VISIBLE);

        int distance = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
        holder.invitecount.setText(CarPlayUtil.numberWithDelimiter(distance));

        //接受 人数 为0 不显示
        int acceptCount = JSONUtil.getInt(jo, "acceptCount");
        if (acceptCount == 0) {
            holder.acceptedlayout.setVisibility(View.GONE);
        } else {
            holder.acceptedlayout.setVisibility(View.VISIBLE);
            holder.acceptedcount.setText(acceptCount + "");
        }

        JSONArray headJsa = JSONUtil.getJSONArray(jo, "acceptMembers");
        setHeadLayout(holder, headJsa);


        return convertView;
    }

    /**
     * 设置 已受邀 成员头像
     *
     * @param holder
     * @param headJsa
     */
    private void setHeadLayout(ViewHolder holder, JSONArray headJsa) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        AcceptHeadAdapter mAdapter = new AcceptHeadAdapter(mContext);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(mAdapter);
        mAdapter.setData(headJsa);
    }


    class ViewHolder {
        //头像
        RoundImageView head;
        //昵称,年龄,被邀请人数,邀请同去按钮,接受邀请人数
        TextView name, age, invitecount, invite, acceptedcount;
        //接受邀请头像列表
        RecyclerView recyclerView;

        RelativeLayout sexLayout;
        //性别,头像认证
        ImageView sex, headstatus;

        LinearLayout acceptedlayout, contactlayout, invitelayout;

        AnimButtonView sms, call;
    }

    /**
     * 邀请同去按钮
     */
    private void inviteTogether(final String userId) {
        OfficialMsgDialog dialog = new OfficialMsgDialog(mContext);
        dialog.show();
        //无法弹出输入法的解决
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setOnOfficialResultListener(new OfficialMsgDialog.OnOfficialResultListener() {
            @Override
            public void onResult(boolean isinarch, String content) {
                DhNet net = new DhNet(API2.joinTogether + activeid + "/invite?userId=" + user.getUserId() + "&token=" + user.getToken());
                net.addParam("invitedUserId", userId);
                net.addParam("transfer", isinarch);
                net.addParam("message", content);
                net.doPostInDialog(new NetTask(mContext) {
                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        if (response.isSuccess()) {
                            ((ActiveDetailsActivity2) mContext).showToast("邀请成功");
                            EventBus.getDefault().post("刷新列表");
                        }
                    }
                });
            }
        });
    }

    /**
     * inviteStatus 邀请状态 当前登录用户 邀请 该用户的 状态；
     * 0 没有邀请过           1 邀请中     2 邀请同意             3 邀请被拒绝
     * <p/>
     * beInvitedStatus 该用户是否邀请过 登录用户
     * 0 没有邀请过           1 邀请中      2 邀请同意             3 邀请被拒绝
     */
    private void setInviteStatus(ViewHolder holder, int inviteStatus, int beInvitedStatus, final JSONObject jo) {
        switch (inviteStatus) {
            case 0:
                switch (beInvitedStatus) {
                    case 0:
                        holder.invitelayout.setVisibility(View.VISIBLE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.invite.setText("邀请同去");
                        holder.invite.setBackgroundResource(R.drawable.btn_blue_fillet);
                        holder.invite.setEnabled(true);
                        break;
                    case 1:
                        holder.invitelayout.setVisibility(View.VISIBLE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.invite.setText("邀请同去");
                        holder.invite.setBackgroundResource(R.drawable.btn_blue_fillet);
                        holder.invite.setEnabled(true);

                        break;
                    case 2:
                        holder.invitelayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.VISIBLE);
                        holder.sms.startScaleAnimation();
                        holder.call.startScaleAnimation();
                        holder.sms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ChatActivity.class);
                                intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                                intent.putExtra("activityId", activeid);
                                intent.putExtra("userId", JSONUtil.getString(jo, "emchatName"));
                                mContext.startActivity(intent);
                            }
                        });
                        holder.call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(mContext, VoiceCallActivity.class);
                                it.putExtra("username", JSONUtil.getString(jo, "emchatName"));
                                it.putExtra("isComingCall", false);
                                mContext.startActivity(it);
                            }
                        });
                        break;
                    case 3:
                        holder.invitelayout.setVisibility(View.VISIBLE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.invite.setText("邀请同去");
                        holder.invite.setBackgroundResource(R.drawable.btn_blue_fillet);
                        holder.invite.setEnabled(true);
                        break;
                }

                break;
            case 1:
                holder.invitelayout.setVisibility(View.VISIBLE);
                holder.contactlayout.setVisibility(View.GONE);
                holder.invite.setText("邀请中");
                holder.invite.setBackgroundResource(R.drawable.btn_grey_fillet);
                holder.invite.setEnabled(false);
                break;
            case 2:
                holder.invitelayout.setVisibility(View.GONE);
                holder.contactlayout.setVisibility(View.VISIBLE);
                holder.sms.startScaleAnimation();
                holder.call.startScaleAnimation();
                holder.sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                        intent.putExtra("activityId", activeid);
                        intent.putExtra("userId", JSONUtil.getString(jo, "emchatName"));
                        mContext.startActivity(intent);
                    }
                });
                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mContext, VoiceCallActivity.class);
                        it.putExtra("username", JSONUtil.getString(jo, "emchatName"));
                        it.putExtra("isComingCall", false);
                        mContext.startActivity(it);
                    }
                });
                break;
            case 3:
                holder.invitelayout.setVisibility(View.VISIBLE);
                holder.contactlayout.setVisibility(View.GONE);
                holder.invite.setText("邀请中");
                holder.invite.setBackgroundResource(R.drawable.btn_grey_fillet);
                holder.invite.setEnabled(false);
                break;
        }
    }
}
