package com.gongpingjia.carplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/17.
 */
public class BeSubscribedAdaptertwo extends BaseAdapter {

    private Context mContext;

    private SubscribeListener mSubscribeListener;

    List<JSONObject> mDatum;

    int type = 0;
    User user = User.getInstance();

    public void setSubscribeListener(SubscribeListener listener) {
        mSubscribeListener = listener;
    }

    public BeSubscribedAdaptertwo(Context context) {
        mContext = context;
    }

    public BeSubscribedAdaptertwo(Context context, int type) {
        mContext = context;
        this.type = type;
    }


    @Override
    public int getCount() {

        return mDatum == null ? 0 : mDatum.size();
    }

    public void setData(List<JSONObject> mDatum) {
        this.mDatum = mDatum;
        notifyDataSetChanged();

    }

    @Override
    public JSONObject getItem(int position) {
        return mDatum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final JSONObject obj = getItem(position);
        final JSONObject carjo = JSONUtil.getJSONObject(obj, "car");
        Boolean subscribeFlag = JSONUtil.getBoolean(obj, "subscribeFlag");
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_be_subscribedtwo, parent, false);
            holder = new ViewHolder();
            holder.visitors_time = (TextView) convertView.findViewById(R.id.visitors_time);
            holder.textDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.heartView = (ImageView) convertView.findViewById(R.id.cb_heart);
//            holder.heartView = (HeartView) convertView.findViewById(R.id.iv_heart);
            holder.roundImageView = (RoundImageView) convertView.findViewById(R.id.iv_avatar);
            holder.textNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.textAge = (TextView) convertView.findViewById(R.id.tv_age);
            holder.sexbgR = (RelativeLayout) convertView.findViewById(R.id.layout_sex_and_age);
            holder.sexI = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.dynamic_carlogoI = (ImageView) convertView.findViewById(R.id.dynamic_carlogo);
            holder.headstatusI = (ImageView) convertView.findViewById(R.id.headstatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.roundImageView.setTag(JSONUtil.getString(obj, "userId"));
        holder.textNickname.setText(JSONUtil.getString(obj, "nickname"));
        int distances = (int) Math.floor(JSONUtil.getDouble(obj, "distance"));
        holder.textDistance.setText(CarPlayUtil.numberWithDelimiter(distances));
        holder.textAge.setText(JSONUtil.getString(obj, "age"));
        String sex = JSONUtil.getString(obj, "gender");
        if (("男").equals(sex)) {
            holder.sexbgR.setBackgroundResource(R.drawable.radio_sex_man_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_man3x);
        } else {
            holder.sexbgR.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            holder.sexI.setBackgroundResource(R.drawable.icon_woman3x);
        }
        holder.heartView.setOnClickListener(new MyOnClick(holder, obj));
        if (subscribeFlag == true){
            holder.heartView.setImageResource(R.drawable.icon_subscribe_each);
        }else{
            holder.heartView.setImageResource(R.drawable.icon_heart);
        }
//        holder.heartView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (att_type == true) {
//                    //关注
//                    DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "listen?token=" + user.getToken());
////                    net.addParam("targetUserId",targetUserId);
//                    net.doPostInDialog(new NetTask(mContext) {
//                        @Override
//                        public void doInUI(Response response, Integer transfer) {
//                            if (response.isSuccess()) {
//                                holder.heartView.setImageResource(R.drawable.icon_subscribe_each);
//                                att_type = false;
//                            }
//                        }
//                    });
//                } else {
//                    //取消关注
//                    DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "unlisten?token=" + user.getToken());
////                    net.addParam("targetUserId",targetUserId);
//                    net.doPostInDialog(new NetTask(mContext) {
//                        @Override
//                        public void doInUI(Response response, Integer transfer) {
//                            if (response.isSuccess()) {
//                                holder.heartView.setImageResource(R.drawable.icon_heart);
//                                att_type = true;
//                            }
//                        }
//                    });
//                }
//            }
//        });
        String licenseAuthStatus = JSONUtil.getString(obj, "licenseAuthStatus");
        //车主认证
        if ("认证通过".equals(licenseAuthStatus)) {
            holder.dynamic_carlogoI.setVisibility(View.VISIBLE);
            ViewUtil.bindNetImage(holder.dynamic_carlogoI, JSONUtil.getString(carjo, "logo"), "default");
        } else {
            holder.dynamic_carlogoI.setVisibility(View.GONE);
        }

        //头像认证
        holder.headstatusI.setVisibility("认证通过".equals(JSONUtil.getString(obj, "photoAuthStatus")) ? View.VISIBLE : View.GONE);
        ImageLoader.getInstance().displayImage(JSONUtil.getString(obj, "avatar"), holder.roundImageView);
        long time = JSONUtil.getLong(obj, "subscribeTime");
        holder.visitors_time.setText(CarPlayValueFix.converTime(time) + " | ");
        return convertView;
    }

    class MyOnClick implements View.OnClickListener {
        ViewHolder holder;
        JSONObject jo;

        public MyOnClick(ViewHolder holder, JSONObject jo) {
            this.holder = holder;
            this.jo = jo;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_heart: {
                    String targetUserId = JSONUtil.getString(jo, "userId");
                    Boolean att_type = JSONUtil.getBoolean(jo, "subscribeFlag");
                    if (att_type == true) {
                        //取消关注
                        Toast.makeText(mContext,"取消关注",Toast.LENGTH_SHORT).show();
                        DhNet net = new DhNet(API2.getUnfollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                        net.addParam("targetUserId", targetUserId);
                        net.doPostInDialog(new NetTask(mContext) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {
                                if (response.isSuccess()) {
                                    holder.heartView.setImageResource(R.drawable.icon_heart);
                                    EventBus.getDefault().post("刷新关注状态");
                                }
                            }
                        });
                    } else {
                        //关注
                        Toast.makeText(mContext,"关注",Toast.LENGTH_SHORT).show();
                        DhNet net = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
                        net.addParam("targetUserId", targetUserId);
                        net.doPostInDialog(new NetTask(mContext) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {
                                if (response.isSuccess()) {
                                    holder.heartView.setImageResource(R.drawable.icon_subscribe_each);
                                    EventBus.getDefault().post("刷新关注状态");
                                }
                            }
                        });
                    }
                }
                break;
            }
        }
    }

    class ViewHolder {
        RoundImageView roundImageView;
        TextView textNickname;
        TextView textDistance;
        TextView textAge;
        TextView visitors_time;
        private RelativeLayout sexbgR;
        ImageView sexI, dynamic_carlogoI, headstatusI;
        ImageView heartView;
    }

//    private void attention() {
//        DhNet dhNet = new DhNet(API2.getFollowPerson(User.getInstance().getUserId(), User.getInstance().getToken()));
//        dhNet.addParam("targetUserId", targetId);
//        dhNet.doPostInDialog(new NetTask(self) {
//            @Override
//            public void doInUI(Response response, Integer transfer) {
//                if (response.isSuccess()) {
//                    //取消关注成功
//                    refresh();
//                }
//            }
//        });
//    }


}
