package com.gongpingjia.carplay.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.DotLinLayout;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.adapter.MyReleaseActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;

public class PersonDetailActivity extends CarPlayBaseActivity implements OnClickListener
{
    LinearLayout tab;
    
    View releaseV, attentionV, joinV;
    
    LayoutInflater mLayoutInflater;
    
    NetRefreshAndMoreListView listV;
    
    MyReleaseActiveAdapter releaseAdapter;
    
    ActiveAdapter attentionAdapter, joinAdapter;
    
    User user;
    
    String userId;
    
    View headV;
    
    RoundImageView headI;
    
    DotLinLayout dotLinLayout;
    
    CarPlayGallery gallery;
    
    TextView attentionT;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
    }
    
    @Override
    public void initView()
    {
        setTitle("他的详情");
        user = User.getInstance();
        userId = getIntent().getStringExtra("userId");
        headV = LayoutInflater.from(self).inflate(R.layout.head_user_details, null);
        tab = (LinearLayout)headV.findViewById(R.id.tab);
        headI = (RoundImageView)headV.findViewById(R.id.head);
        listV = (NetRefreshAndMoreListView)findViewById(R.id.listview);
        listV.addHeaderView(headV);
        gallery = (CarPlayGallery)headV.findViewById(R.id.gallery);
        dotLinLayout = (DotLinLayout)headV.findViewById(R.id.dots);
        dotLinLayout.setDotImage(R.drawable.dot_n, R.drawable.dot_f);
        
        attentionT = (TextView)headV.findViewById(R.id.attention);
        attentionT.setOnClickListener(this);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int current, long arg3)
            {
                dotLinLayout.setCurrentFocus(current);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });
        
        getPersonData();
        buildAdapter();
        initTopTab();
        setTab(0);
    }
    
    private void initTopTab()
    {
        for (int i = 0; i < tab.getChildCount(); i = i + 2)
        {
            final int index = i;
            View childV = tab.getChildAt(i);
            childV.setOnClickListener(new View.OnClickListener()
            {
                
                @Override
                public void onClick(View arg0)
                {
                    setTab(index);
                    switch (index)
                    {
                        case 0:
                            listV.setAdapter(releaseAdapter);
                            break;
                        
                        case 2:
                            listV.setAdapter(attentionAdapter);
                            break;
                        
                        case 4:
                            listV.setAdapter(joinAdapter);
                            break;
                        
                        default:
                            break;
                    }
                }
            });
        }
        
    }
    
    private void getPersonData()
    {
        DhNet net =
            new DhNet(API.CWBaseurl + "/user/" + userId + "/info?userId=" + user.getUserId() + "&token="
                + user.getToken());
        net.doGetInDialog(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    
                    JSONObject jo = response.jSONFromData();
                    int isSubscribed = JSONUtil.getInt(jo, "isSubscribed");
                    ViewUtil.bindView(attentionT, isSubscribed == 1 ? "取消关注" : "关注");
                    ViewUtil.bindNetImage(headI, JSONUtil.getString(jo, "photo"), "default");
                    ViewUtil.bindView(headV.findViewById(R.id.nickname), JSONUtil.getString(jo, "nickname"));
                    ViewUtil.bindView(headV.findViewById(R.id.drive_age), JSONUtil.getString(jo, "carModel") + ","
                        + JSONUtil.getString(jo, "drivingExperience") + "年驾龄");
                    ViewUtil.bindView(headV.findViewById(R.id.content), JSONUtil.getString(jo, "introduction"));
                    
                    ViewUtil.bindNetImage((ImageView)headV.findViewById(R.id.car_logo),
                        JSONUtil.getString(jo, "carBrandLogo"),
                        "optionsDefault");
                    
                    ViewUtil.bindView(headV.findViewById(R.id.releaseCount), JSONUtil.getString(jo, "postNumber"));
                    ViewUtil.bindView(headV.findViewById(R.id.attention_count),
                        JSONUtil.getString(jo, "subscribeNumber"));
                    ViewUtil.bindView(headV.findViewById(R.id.active_count), JSONUtil.getString(jo, "joinNumber"));
                    
                    JSONArray albumPhotosJsa = JSONUtil.getJSONArray(jo, "albumPhotos");
                    bingGallery(albumPhotosJsa);
                }
            }
        });
    }
    
    private void buildAdapter()
    {
        releaseAdapter =
            new MyReleaseActiveAdapter(API.CWBaseurl + "/user/" + userId + "/post?userId=" + user.getUserId()
                + "&token=" + user.getToken(), self, R.layout.item_myrelease_active);
        releaseAdapter.fromWhat("data");
        listV.setAdapter(releaseAdapter);
        releaseAdapter.showNextInDialog();
        
        attentionAdapter =
            new ActiveAdapter(API.CWBaseurl + "/user/" + userId + "/subscribe?userId=" + user.getUserId() + "&token="
                + user.getToken(), self, R.layout.item_active_list);
        
        attentionAdapter.fromWhat("data");
        attentionAdapter.showNext();
        
        joinAdapter =
            new ActiveAdapter(API.CWBaseurl + "/user/" + userId + "/join?userId=" + user.getUserId() + "&token="
                + user.getToken(), self, R.layout.item_active_list);
        
        joinAdapter.fromWhat("data");
        joinAdapter.showNext();
    }
    
    private void bingGallery(JSONArray jsa)
    {
        int count = jsa.length();
        if (count > 0)
        {
            dotLinLayout.setDotCount(count);
            dotLinLayout.setCurrentFocus(count / 2);
            gallery.setSelection(count / 2);
        }
        GalleryAdapter adapter = new GalleryAdapter(self, jsa);
        gallery.setAdapter(adapter);
    }
    
    private void setTab(int index)
    {
        for (int i = 0; i < tab.getChildCount(); i = i + 2)
        {
            LinearLayout childV = (LinearLayout)tab.getChildAt(i);
            View img = (View)childV.findViewById(R.id.tabline);
            TextView numText = (TextView)childV.getChildAt(0);
            TextView desText = (TextView)childV.getChildAt(1);
            if (index == i)
            {
                img.setVisibility(View.VISIBLE);
                numText.setTextColor(getResources().getColor(R.color.text_orange));
                desText.setTextColor(getResources().getColor(R.color.text_orange));
            }
            else
            {
                img.setVisibility(View.GONE);
                numText.setTextColor(getResources().getColor(R.color.text_grey));
                desText.setTextColor(getResources().getColor(R.color.text_grey));
            }
        }
    }
    
    /** 关注人 */
    private void attention(String userid)
    {
        DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId() + "/listen?&token=" + user.getToken());
        net.addParam("targetUserId", userid);
        net.doPost(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    showToast("关注成功!");
                    attentionT.setText("取消关注");
                }
            }
        });
    }
    
    /** 关注人 */
    private void cancleAttention(String userid)
    {
        DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId() + "/unlisten?&token=" + user.getToken());
        net.addParam("targetUserId", userid);
        net.doPost(new NetTask(self)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                if (response.isSuccess())
                {
                    showToast("取消关注成功!");
                    attentionT.setText("关注");
                }
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.attention:
                if (attentionT.getText().toString().equals("关注"))
                {
                    attention(userId);
                }
                else
                {
                    cancleAttention(userId);
                }
                break;
            
            default:
                break;
        }
    }
}
