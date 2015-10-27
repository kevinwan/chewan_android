package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.my.MyPerSonDetailActivity2;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.adapter.BigImageAdapter;
import com.gongpingjia.carplay.adapter.OfficialMembersAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.CarPlayGallery;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * 活动详情
 */
public class ActiveDetailsActivity2 extends CarPlayBaseActivity implements View.OnClickListener {

    private ListView mListView;

    private LayoutInflater mInflater;

    private View mHeadView;

    private View mFootView;

    private Button joinBtn, chatBtn, buyticketsBtn;

    private LinearLayout startchatlayout;

    User user;

    //活动id
    String activeid;
    //群组id
    String emchatGroupId;

    //是否为成员
    boolean isMember = false;
    //第三方购票连接
    String linkTicketUrl;


    /**
     * headview
     */
    private ImageView imgfoldI, avatarT;
    private TextView nicknameT, contentT, startTimeT, endTimeT, priceT, placeT, participate_womanT, participate_manT, introduceT, creattimeT, unparticipateT;
    private RelativeLayout foldR;
    private CarPlayGallery mViewPager;

    /**
     * footview
     */
    private LinearLayout moreL, processL, explainL;
    private ImageView processIconI, explainIconI;
    private TextView explaintxtT, processT;
    private OfficialMembersAdapter membersAdapter;

    private boolean contentFlag = false;
    private boolean processFlag = false;
    private boolean explainFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details2);
        EventBus.getDefault().register(this);

//        http://cwapi.gongpingjia.com:8080/v2/official/activity/561f5eaa0cf2a1b735efa50a/info?userId=561ba2d60cf2429fb48e86bd&token=9927f747-c615-4362-bd43-a2ec31362205
    }


    @Override
    public void initView() {
        user = User.getInstance();
        setTitle("活动详情");
        mInflater = LayoutInflater.from(self);
        mHeadView = mInflater.inflate(R.layout.item_active_details2_headview, null);
        mFootView = mInflater.inflate(R.layout.item_active_details2_footview, null);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.addHeaderView(mHeadView, null, false);
        mListView.addFooterView(mFootView, null, false);
        membersAdapter = new OfficialMembersAdapter(self);
        mListView.setAdapter(membersAdapter);

        startchatlayout = (LinearLayout) findViewById(R.id.startchatlayout);
        joinBtn = (Button) findViewById(R.id.join);
        chatBtn = (Button) findViewById(R.id.chatbtn);
        buyticketsBtn = (Button) findViewById(R.id.buytickets);

        nicknameT = (TextView) mHeadView.findViewById(R.id.nickname);
        contentT = (TextView) mHeadView.findViewById(R.id.content);
        foldR = (RelativeLayout) mHeadView.findViewById(R.id.fold);
        imgfoldI = (ImageView) mHeadView.findViewById(R.id.imgfold);
        avatarT = (ImageView) mHeadView.findViewById(R.id.avatar);
        startTimeT = (TextView) mHeadView.findViewById(R.id.starttime);
        endTimeT = (TextView) mHeadView.findViewById(R.id.endtime);
        priceT = (TextView) mHeadView.findViewById(R.id.price);
        placeT = (TextView) mHeadView.findViewById(R.id.place);
        participate_womanT = (TextView) mHeadView.findViewById(R.id.participate_woman);
        participate_manT = (TextView) mHeadView.findViewById(R.id.participate_man);
        creattimeT = (TextView) mHeadView.findViewById(R.id.creattime);
        introduceT = (TextView) mHeadView.findViewById(R.id.introduce);
        unparticipateT = (TextView) mHeadView.findViewById(R.id.unparticipate);
        mViewPager = (CarPlayGallery) mHeadView.findViewById(R.id.viewer);


        moreL = (LinearLayout) mFootView.findViewById(R.id.more);
        processL = (LinearLayout) mFootView.findViewById(R.id.process);
        explainL = (LinearLayout) mFootView.findViewById(R.id.explain);
        processIconI = (ImageView) mFootView.findViewById(R.id.process_icon);
        explainIconI = (ImageView) mFootView.findViewById(R.id.explain_icon);
        explaintxtT = (TextView) mFootView.findViewById(R.id.explaintxt);
        processT = (TextView) mFootView.findViewById(R.id.processlist);

        joinBtn.setOnClickListener(this);
        foldR.setOnClickListener(this);
        processL.setOnClickListener(this);
        explainL.setOnClickListener(this);
        moreL.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        buyticketsBtn.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 || position != parent.getCount() - 1) {
                    Intent it;
                    String userId = JSONUtil.getString(membersAdapter.getItem(position - 1), "userId");
                    if (userId.equals(user.getUserId())) {
                        it = new Intent(self, MyPerSonDetailActivity2.class);
                        startActivity(it);
                    } else {
                        it = new Intent(self, PersonDetailActivity2.class);
                        it.putExtra("activeid", userId);
                        startActivity(it);
                    }
                }
            }
        });

        getActiveDetailsData();
    }

    private void getActiveDetailsData() {
        activeid = getIntent().getStringExtra("activityId");

        DhNet verifyNet = new DhNet(API2.ActiveDetails + activeid + "/info?userId=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGet(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    //第三方购票连接
                    linkTicketUrl = JSONUtil.getString(jo,"linkTicketUrl");
                    //群组id
                    emchatGroupId = JSONUtil.getString(jo,"emchatGroupId");

                    //目的地
                    JSONObject js = JSONUtil.getJSONObject(jo, "destination");
                    ViewUtil.bindView(placeT, JSONUtil.getString(js, "province") + "省" + JSONUtil.getString(js, "city") + "市" + JSONUtil.getString(js, "detail"));

                    //开始-结束时间,创建时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    Date sdate = new Date(JSONUtil.getLong(jo, "start"));
                    ViewUtil.bindView(startTimeT, format.format(sdate));
                    Date edate = new Date(JSONUtil.getLong(jo, "end"));
                    ViewUtil.bindView(endTimeT, format.format(edate));
                    ViewUtil.bindView(creattimeT, CarPlayValueFix.converTime(JSONUtil.getLong(jo, "createTime")));

                    //活动名字,头像,标题,介绍,价格,补贴,说明
                    JSONObject jsname = JSONUtil.getJSONObject(jo, "organizer");
                    ViewUtil.bindView(nicknameT, JSONUtil.getString(jsname, "nickname"));
                    ViewUtil.bindNetImage(avatarT, JSONUtil.getString(jsname, "avatar"), "head");
                    ViewUtil.bindView(introduceT, JSONUtil.getString(jo, "title"));
                    ViewUtil.bindView(contentT, JSONUtil.getString(jo, "instruction"));
                    ViewUtil.bindView(processT, JSONUtil.getString(jo, "description"));
                    ViewUtil.bindView(priceT, JSONUtil.getDouble(jo, "price") + "元/人(现在报名立减" + JSONUtil.getDouble(jo, "subsidyPrice") + "元)");
                    ViewUtil.bindView(explaintxtT, JSONUtil.getString(jo, "instruction"));

                    isMember = JSONUtil.getBoolean(jo, "isMember");
                    if (isMember) {
                        joinBtn.setVisibility(View.INVISIBLE);
                        startchatlayout.setVisibility(View.VISIBLE);
                    } else {
                        joinBtn.setVisibility(View.VISIBLE);
                        startchatlayout.setVisibility(View.INVISIBLE);
                    }

                    if (contentT.getLineCount() < 4) {
                        foldR.setVisibility(View.GONE);
                    }

                    //0:无限制 1：限制总人数 2：限制男女人数
                    int limitType = JSONUtil.getInt(jo, "limitType");
                    //男生,女生数量,总量
                    if (limitType == 1) {
                        findViewById(R.id.limitedlayout).setVisibility(View.GONE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.VISIBLE);
                        ViewUtil.bindView(unparticipateT, JSONUtil.getInt(jo, "nowJoinNum") + "/" + JSONUtil.getInt(jo, "totalLimit"));
                    } else if (limitType == 2) {
                        findViewById(R.id.limitedlayout).setVisibility(View.VISIBLE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.GONE);
                        ViewUtil.bindView(participate_womanT, JSONUtil.getInt(jo, "femaleNum") + "/" + JSONUtil.getInt(jo, "femaleLimit"));
                        ViewUtil.bindView(participate_manT, JSONUtil.getInt(jo, "maleNum") + "/" + JSONUtil.getInt(jo, "maleLimit"));
                    } else {
                        findViewById(R.id.limitedlayout).setVisibility(View.GONE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.VISIBLE);
                        ViewUtil.bindView(unparticipateT, JSONUtil.getInt(jo, "nowJoinNum") + "/" + "人数不限");
                    }

                    //参与成员
                    JSONArray membersJsa = JSONUtil.getJSONArray(jo, "members");
                    setMembersData(membersJsa);

                    /** GalleryViewPager  */
                    final String[] urls;

                    //活动大图
                    JSONArray jsc = JSONUtil.getJSONArray(jo, "covers");
                    if (jsc != null) {
                        BigImageAdapter adapter = new BigImageAdapter(self, jsc);
                        mViewPager.setAdapter(adapter);
                    }


                }
            }
        });
    }


//    private SpannableStringBuilder setTextColor(String newcontent){
//        /** 文字变色 */
//                    SpannableStringBuilder style = new SpannableStringBuilder(newcontent);
//        style.setSpan(new ForegroundColorSpan(self.getResources()
//                        .getColor(R.color.text_orange)), day_start,
//                day_start + day.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //报名参加
            case R.id.join:
                joinActive();
                break;
            //活动描述
            case R.id.fold:
                textFold();
                break;
            //活动流程
            case R.id.process:
                processFold();
                break;
            //活动说明
            case R.id.explain:
                explainFold();
                break;
            //加载更多
            case R.id.more:

                break;
            //前往购票
            case R.id.buytickets:

                break;
            //进入群聊
            case R.id.chatbtn:
                Intent intent = new Intent(self, ChatActivity.class);
                EMGroup group = EMGroupManager.getInstance().getGroup(emchatGroupId);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                intent.putExtra("activityId", activeid);
                intent.putExtra("groupId", group.getGroupId());
                startActivity(intent);
                break;
        }
    }

    /**
     * 活动内容展开收缩
     */
    private void textFold() {
        if (contentFlag) {
            contentT.setEllipsize(TextUtils.TruncateAt.END);//收缩
            contentT.setMaxLines(3);
            imgfoldI.setImageResource(R.drawable.detail_down);
            contentFlag = false;
        } else {
            contentT.setEllipsize(null); // 展开
            contentT.setMaxLines(100);
            imgfoldI.setImageResource(R.drawable.detail_up);
            contentFlag = true;
        }
    }

    /**
     * 活动流程展开收缩
     */
    private void processFold() {
        if (!processFlag) {
            processFlag = !processFlag;
            processIconI.setImageResource(R.drawable.up_btn);
            processT.setVisibility(View.VISIBLE);
        } else {
            processFlag = !processFlag;
            processIconI.setImageResource(R.drawable.down_btn);
            processT.setVisibility(View.GONE);
        }
    }

    /**
     * 活动说明展开收缩
     */
    private void explainFold() {
        if (!explainFlag) {
            explainFlag = !explainFlag;
            explainIconI.setImageResource(R.drawable.up_btn);
            explaintxtT.setVisibility(View.VISIBLE);
        } else {
            explainFlag = !explainFlag;
            explainIconI.setImageResource(R.drawable.down_btn);
            explaintxtT.setVisibility(View.GONE);
        }
    }

    /**
     * 加入活动
     */
    private void joinActive() {
        DhNet joinnet = new DhNet(API2.joinActive + activeid + "/join?userId=" + user.getUserId() + "&token=" + user.getToken());
        joinnet.doPost(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    isMember = !isMember;
                    membersAdapter.setIsMember(isMember);
                    getActiveDetailsData();
                }
            }
        });
    }

    /**
     * 设置参与成员信息
     */
    private void setMembersData(JSONArray jsa) {
        membersAdapter.setData(jsa, isMember, activeid);
    }

    public void onEventMainThread(String success) {
        if ("报名参加".equals(success)) {
            joinActive();
        } else if ("刷新列表".equals(success)) {
            getActiveDetailsData();
        }
    }


}
