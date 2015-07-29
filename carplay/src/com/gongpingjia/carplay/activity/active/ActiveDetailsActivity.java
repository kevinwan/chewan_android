package com.gongpingjia.carplay.activity.active;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.my.LoginActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.PicLayoutUtil;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ActiveDetailsActivity extends CarPlayBaseActivity implements OnClickListener
{
    
    private NetRefreshAndMoreListView mListView;
    
    private LayoutInflater mInflater;
    
    private NetJSONAdapter mJsonAdapter;
    
    String activityId;
    
    View headV;
    
    int piclayoutWidth;
    
    int headlayoutWidth;
    
    Button releaseB;
    
    EditText comment_contentE;
    
    User user;
    
    View headlayoutV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details);
        
    }
    
    @Override
    public void initView()
    {
        user = User.getInstance();
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        piclayoutWidth = width - DhUtil.dip2px(self, 10 + 10);
        headlayoutWidth = piclayoutWidth - DhUtil.dip2px(self, 80 + 8 * 2 + 10);
        setTitle("活动详情");
        // /activity/$activityId/subscribe?
        activityId = getIntent().getStringExtra("activityId");
        mListView = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        mInflater = LayoutInflater.from(this);
        releaseB = (Button)findViewById(R.id.release);
        releaseB.setOnClickListener(this);
        comment_contentE = (EditText)findViewById(R.id.comment_content);
        headV = mInflater.inflate(R.layout.active_head_view, null);
        headlayoutV = headV.findViewById(R.id.headlayout);
        headlayoutV.setOnClickListener(this);
        mListView.addHeaderView(headV);
        mJsonAdapter =
            new NetJSONAdapter(API.CWBaseurl + "/activity/" + activityId + "/comment?userId=" + user.getUserId()
                + "&token=" + user.getToken(), this, R.layout.listitem_comment);
        mJsonAdapter.addField(new FieldMap("comment", R.id.tv_comment_content)
        {
            
            @Override
            public Object fix(View itemV, Integer position, Object o, Object jo)
            {
                
                View layout_sexV = itemV.findViewById(R.id.layout_sex);
                JSONObject jo1 = (JSONObject)jo;
                if (JSONUtil.getString(jo1, "gender").equals("男"))
                {
                    layout_sexV.setBackgroundResource(R.drawable.man);
                }
                else
                {
                    layout_sexV.setBackgroundResource(R.drawable.woman);
                }
                return o;
            }
        });
        mJsonAdapter.addField("nickname", R.id.tv_nickname);
        mJsonAdapter.addField("photo", R.id.imgView_avatar);
        mJsonAdapter.addField("publishTime", R.id.tv_publish_time, "neartime");
        mJsonAdapter.addField("age", R.id.age);
        mJsonAdapter.fromWhat("data");
        mListView.setAdapter(mJsonAdapter);
        mJsonAdapter.showNextInDialog();
        
        getData();
    }
    
    public void getData()
    {
        DhNet net = new DhNet(API.CWBaseurl + "/activity/" + activityId + "/info");
        net.doGetInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    JSONObject headJo = response.jSONFromData();
                    bindHeadView(headJo);
                }
            }
        });
    }
    
    private void bindHeadView(JSONObject headJo)
    {
        JSONObject createrJo = JSONUtil.getJSONObject(headJo, "organizer");
        
        if (JSONUtil.getString(createrJo, "userId").equals(user.getUserId()))
        {
            setRightAction("编辑活动", -1, new OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    Intent it = new Intent(ActiveDetailsActivity.this, EditActiveActivity.class);
                    startActivity(it);
                }
            });
        }
        else
        {
            setRightAction("关注", -1, new OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    UserInfoManage.getInstance().checkLogin(self, new LoginCallBack()
                    {
                        
                        @Override
                        public void onisLogin()
                        {
                            attention();
                        }
                        
                        @Override
                        public void onLoginFail()
                        {
                            
                        }
                    });
                }
            });
        }
        
        ViewUtil.bindView(headV.findViewById(R.id.name), JSONUtil.getString(createrJo, "nickname"));
        ViewUtil.bindView(headV.findViewById(R.id.drive_age), JSONUtil.getString(createrJo, "carModel") + ","
            + JSONUtil.getString(createrJo, "drivingExperience") + "年驾龄");
        ViewUtil.bindView(headV.findViewById(R.id.content), JSONUtil.getString(headJo, "introduction"));
        
        ViewUtil.bindNetImage((ImageView)headV.findViewById(R.id.car_logo),
            JSONUtil.getString(createrJo, "carBrandLogo"),
            "optionsDefault");
        ViewUtil.bindNetImage((ImageView)headV.findViewById(R.id.head),
            JSONUtil.getString(createrJo, "photo"),
            "optionsDefault");
        ViewUtil.bindView(headV.findViewById(R.id.publish_time), JSONUtil.getLong(headJo, "publishTime"), "neartime");
        
        ViewUtil.bindView(headV.findViewById(R.id.address), JSONUtil.getString(headJo, "location"));
        
        if (JSONUtil.getLong(headJo, "start") == 0)
        {
            ViewUtil.bindView(headV.findViewById(R.id.start_time), "不确定");
        }
        else
        {
            ViewUtil.bindView(headV.findViewById(R.id.start_time), JSONUtil.getLong(headJo, "start"), "time");
        }
        
        if (JSONUtil.getLong(headJo, "end") == 0)
        {
            ViewUtil.bindView(headV.findViewById(R.id.end_time), "不确定");
            
        }
        else
        {
            ViewUtil.bindView(headV.findViewById(R.id.end_time), JSONUtil.getLong(headJo, "end"), "time");
        }
        
        ViewUtil.bindView(headV.findViewById(R.id.pay), JSONUtil.getString(createrJo, "pay"));
        
        View layoutSex = headV.findViewById(R.id.layout_sex);
        if (JSONUtil.getString(createrJo, "gender").equals("男"))
        {
            layoutSex.setBackgroundResource(R.drawable.man);
        }
        else
        {
            layoutSex.setBackgroundResource(R.drawable.woman);
        }
        ViewUtil.bindView(headV.findViewById(R.id.age), JSONUtil.getString(createrJo, "age"));
        
        JSONArray picJsa = JSONUtil.getJSONArray(headJo, "cover");
        // holder.piclayoutV.removeAllViews();
        PicLayoutUtil util =
            new PicLayoutUtil(self, picJsa, 5, (LinearLayout)headV.findViewById(R.id.pic_layout), piclayoutWidth);
        util.addMoreChild();
        // holder.headlayoutV.removeAllViews();
        JSONArray headJsa = JSONUtil.getJSONArray(headJo, "members");
        PicLayoutUtil headUtil =
            new PicLayoutUtil(self, headJsa, 5, (LinearLayout)headV.findViewById(R.id.headlayout), headlayoutWidth);
        headUtil.setHeadMaxCount(6);
        headUtil.AddChild();
    }
    
    public void comment()
    {
        String commentContent = comment_contentE.getText().toString();
        if (TextUtils.isEmpty(commentContent))
        {
            showToast("请输入评论内容!");
            return;
        }
        User user = User.getInstance();
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/comment?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.addParam("replyUserId", "");
        net.addParam("comment", commentContent);
        net.doPostInDialog("发布评论中...", new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    comment_contentE.setText("");
                    showToast("评论发布成功");
                    mJsonAdapter.refresh();
                }
            }
        });
    }
    
    private void attention()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/activity/" + activityId + "/subscribe?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doPostInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    showToast("关注成功!");
                    setRightVISIBLEOrGone(View.GONE);
                }
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.release:
                comment();
                break;
            
            case R.id.headlayout:
                Intent it = new Intent(self, ActiveMembersActivity.class);
                it.putExtra("activityId", activityId);
                it.putExtra("isJoin", true);
                startActivity(it);
                break;
            
            default:
                break;
        }
    }
    
}
