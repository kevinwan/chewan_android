package com.gongpingjia.carplay.activity.active;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.CarSeatUtil;
import com.gongpingjia.carplay.util.CarSeatUtil.OnSeatClickListener;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.ActiveMsgDialog;
import com.gongpingjia.carplay.view.dialog.ActiveMsgDialog.OnClickResultListener;
import com.gongpingjia.carplay.view.dialog.SeatDialog;
import com.gongpingjia.carplay.view.dialog.SeatDialog.OnGradResultListener;
/**
 * 参与成员
 * @author Administrator
 *
 */
public class ActiveMembersActivity extends CarPlayBaseActivity implements OnClickListener
{
    View headV;
    
    ListView listV;
    
    NetJSONAdapter adapter;
    
    LinearLayout carLayoutV;
    
    CarSeatUtil carUtil;
    
    User user;
    
    String activityId;
    
    SeatDialog grabDialog;
    
    boolean isJoin = false;
    
    /** 退出活动按钮 */
    Button quitB, joinB;
    
    TextView quite_desT;
    
    long startTime;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_members);
    }
    
    @Override
    public void initView()
    {
        setTitle("参与成员");
        user = User.getInstance();
        activityId = getIntent().getStringExtra("activityId");
        startTime = getIntent().getLongExtra("startTime", 0);
        isJoin = getIntent().getBooleanExtra("isJoin", false);
        quitB = (Button)findViewById(R.id.quit);
        quitB.setOnClickListener(this);
        joinB = (Button)findViewById(R.id.join);
        joinB.setOnClickListener(this);
        quite_desT = (TextView)findViewById(R.id.quite_des);
        initQuitAndJoinButton(isJoin);
        
        headV = LayoutInflater.from(self).inflate(R.layout.head_active_members, null);
        
        LinearLayout carDesV = (LinearLayout)headV.findViewById(R.id.seat_layout);
        carDesV.setVisibility(View.VISIBLE);
        final TextView cardesT = (TextView)headV.findViewById(R.id.seat_des);
        
        final TextView seat_countT = (TextView)headV.findViewById(R.id.seat_count);
        
        carLayoutV = (LinearLayout)headV.findViewById(R.id.car_layout);
        carUtil = new CarSeatUtil(self, carLayoutV);
        carUtil.setOnSeatClickListener(new OnSeatClickListener()
        {
            
            @Override
            public void onSeatClick(String carId, int childPosition)
            {
                showGradDialog(carId, childPosition);
            }
            
            @Override
            public void onHeadClick(JSONObject headJo)
            {
            }
            
            @Override
            public void seatCount(int totalCount, int emptyCount)
            {
                cardesT.setText("共" + totalCount + "个座位,   还剩下");
                seat_countT.setText(emptyCount + "");
            }
        });
        listV = (ListView)findViewById(R.id.listview);
        listV.addHeaderView(headV);
        String url =
            API.CWBaseurl + "/activity/" + activityId + "/members?userId=" + user.getUserId() + "&token="
                + user.getToken();
        adapter = new NetJSONAdapter(url, self, R.layout.item_newmessage_list);
        adapter.addField(new FieldMap("nickname", R.id.name)
        {
            
            @Override
            public Object fix(View itemV, Integer position, Object o, Object jo)
            {
                JSONObject itemjo = (JSONObject)jo;
                
                RoundImageView headI = (RoundImageView)itemV.findViewById(R.id.head);
                ViewUtil.bindNetImage(headI, JSONUtil.getString(itemjo, "photo"), "default");
                headI.setTag(JSONUtil.getString(itemjo, "userId"));
                
                View sexBg = itemV.findViewById(R.id.sex);
                
                if (JSONUtil.getString(itemjo, "gender").equals("男"))
                {
                    sexBg.setBackgroundResource(R.drawable.man);
                }
                else
                {
                    sexBg.setBackgroundResource(R.drawable.woman);
                }
                
                ViewUtil.bindView(itemV.findViewById(R.id.age), JSONUtil.getString(itemjo, "age"));
                ViewUtil.bindView(itemV.findViewById(R.id.name), JSONUtil.getString(itemjo, "nickname"));
                CarPlayUtil.bindDriveAge(itemjo,
                    (ImageView)itemV.findViewById(R.id.car_logo),
                    (TextView)itemV.findViewById(R.id.drive_age));
                // TODO Auto-generated method stub
                return o;
            }
        });
        adapter.fromWhat("data.members");
        listV.setAdapter(adapter);
        adapter.showNext();
        grabDialog = new SeatDialog(self);
        getData();
    }
    
    private void getData()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/members?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doGetInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                JSONArray carJsa = response.jSONArrayFrom("data.cars");
                carUtil.addCar(carJsa);
                
            }
        });
    }
    
    private void initQuitAndJoinButton(boolean isJoin)
    {
        quitB.setVisibility(isJoin ? View.VISIBLE : View.GONE);
        joinB.setVisibility(isJoin ? View.GONE : View.VISIBLE);
        quite_desT.setVisibility(isJoin ? View.VISIBLE : View.INVISIBLE);
    }
    
    private void showGradDialog(String carId, int seatIndex)
    {
        grabDialog.setOnGradResultListener(new OnGradResultListener()
        {
            
            @Override
            public void click(String carId, int seatIndex)
            {
                gradSeat(carId, seatIndex);
            }
        });
        grabDialog.show();
        grabDialog.setSeatData(carId, seatIndex);
    }
    
    /**
     * 
     * 抢座
     */
    private void gradSeat(String carId, int seatIndex)
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/seat/take?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.addParam("carId", carId);
        net.addParam("seatIndex", seatIndex);
        net.doPostInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    showToast("抢座成功!");
                    getData();
                }
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.quit:
                long currentTime = System.currentTimeMillis();
                ActiveMsgDialog dialog;
                if (startTime != 0 && currentTime - startTime > 1000 * 60 * 60 * 6)
                {
                    dialog = new ActiveMsgDialog(self, "确定退出活动?");
                    dialog.setOnClickResultListener(new OnClickResultListener()
                    {
                        
                        @Override
                        public void onclick()
                        {
                            quiteActive();
                        }
                    });
                }
                else
                {
                    dialog = new ActiveMsgDialog(self, "距离活动开始还有6小时", "您无法退出活动");
                }
                dialog.show();
                break;
            case R.id.join:
                joinActive();
                break;
            default:
                break;
        }
    }
    
    /**
     * 退出活动
     */
    private void quiteActive()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/quit?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doPost(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    showToast("退出活动成功");
                    initQuitAndJoinButton(false);
                    adapter.refresh();
                    getData();
                }
            }
        });
    }
    
    /**
     * 加入活动
     */
    private void joinActive()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/join?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doPost(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    findViewById(R.id.bottom_bar).setVisibility(View.GONE);
                    showToast("已提交加入活动申请,等待管理员审核!");
                }
            }
        });
    }
    
}
