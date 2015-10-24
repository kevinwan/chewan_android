package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity2;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.OfficialMsgDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/24.
 * 官方活动参与成员adapter
 */
public class OfficialMembersAdapter extends BaseAdapter{
    private final Context mContext;

    private JSONArray data;

    private boolean isMember;

    private String activeid;

    private User user;

    public OfficialMembersAdapter(Context context) {
        mContext = context;
        user = User.getInstance();
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
        notifyDataSetChanged();
    }

    public void setData(JSONArray data,boolean isMember,String activeid) {
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
        return data.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (null != data)
        {
            try {
                item = (JSONObject) data.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return item;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_active_memeber2, null);
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

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jo = getItem(position);
        final String userId=JSONUtil.getString(jo, "userId");

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
        holder.headstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMember){
                    OfficialMsgDialog dialog = new OfficialMsgDialog(mContext);
                    dialog.show();
                    dialog.setOnOfficialResultListener(new OfficialMsgDialog.OnOfficialResultListener() {
                        @Override
                        public void onResult(boolean isinarch, String content) {
                            DhNet net = new DhNet(API2.joinTogether+activeid+"/invite?userId="+user.getUserId()+"&token="+user.getToken());
                            net.addParam("invitedUserId", userId);
                            net.addParam("transfer", isinarch);
                            net.doPostInDialog(new NetTask(mContext) {
                                @Override
                                public void doInUI(Response response, Integer transfer) {
                                    if (response.isSuccess()) {
                                        ((ActiveDetailsActivity2)mContext).showToast("邀请成功");
                                    }
                                }
                            });
                        }
                    });
                }else {

                }
            }
        });

        holder.invite.setVisibility(userId.equals(user.getUserId()) ? View.GONE : View.VISIBLE);
        //受邀 人数 为0 不显示
        int invitedCount = JSONUtil.getInt(jo, "invitedCount");
        if (invitedCount==0){
            holder.invitecount.setVisibility(View.INVISIBLE);
        }else {
            holder.invitecount.setVisibility(View.VISIBLE);
            int distance = (int) Math.floor(JSONUtil.getDouble(jo, "distance"));
            holder.invitecount.setText(CarPlayUtil.numberWithDelimiter(distance)+"!已被"+invitedCount+"人邀请同去");
        }

        //接受 人数 为0 不显示
        int acceptCount = JSONUtil.getInt(jo, "acceptCount");
        if (acceptCount==0){
            holder.acceptedlayout.setVisibility(View.GONE);
        }else{
            holder.acceptedlayout.setVisibility(View.VISIBLE);
            holder.acceptedcount.setText(acceptCount+"");
        }

        JSONArray headJsa = JSONUtil.getJSONArray(jo,"acceptMembers");
        setHeadLayout(holder,headJsa);


        return convertView;
    }

    /**
     * 设置 已受邀 成员头像
     * @param holder
     * @param headJsa
     */
    private void setHeadLayout(ViewHolder holder,JSONArray headJsa ){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        AcceptHeadAdapter mAdapter = new AcceptHeadAdapter(mContext);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(mAdapter);
        mAdapter.setData(headJsa);
    }

    class ViewHolder{
        //头像
        RoundImageView head;
        //昵称,年龄,被邀请人数,邀请同去按钮,接受邀请人数
        TextView name,age,invitecount,invite,acceptedcount;
        //接受邀请头像列表
        RecyclerView recyclerView;

        RelativeLayout sexLayout;
        //性别,头像认证
        ImageView sex,headstatus;

        LinearLayout acceptedlayout;

    }
}
