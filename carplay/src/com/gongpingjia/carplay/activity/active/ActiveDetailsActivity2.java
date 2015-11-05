package com.gongpingjia.carplay.activity.active;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayListActivity;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.my.MyPerSonDetailActivity2;
import com.gongpingjia.carplay.activity.my.PersonDetailActivity2;
import com.gongpingjia.carplay.adapter.BigImageAdapter;
import com.gongpingjia.carplay.adapter.OfficialMembersAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.PointRecord;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 活动详情
 */
public class ActiveDetailsActivity2 extends CarPlayListActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess, CarPlayListActivity.onLoadDataSuccess {

//    private ListView mListView;

    private LayoutInflater mInflater;

    private View mHeadView;

    private View mFootView;

    private Button joinBtn, chatBtn, buyticketsBtn;

    private LinearLayout startchatlayout;

    private ListView mRecyclerView;
    //    LinearLayout empty;
//    TextView msg;
    PullToRefreshListView listV;
    private OfficialMembersAdapter membersAdapter;

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
    private ImageView imgfoldI;
    private TextView nicknameT, contentT, startTimeT, endTimeT, priceT,subsidyPriceT, placeT, participate_womanT, participate_manT, introduceT, creattimeT, unparticipateT;
    private RelativeLayout foldR;
    private CarPlayGallery mViewPager;
    RoundImageView avatarT;

    /**
     * footview
     */
    private LinearLayout moreL, processL, explainL;
    private ImageView processIconI, explainIconI;
    private TextView explaintxtT, processT;

    private boolean contentFlag = false;
    private boolean processFlag = false;
    private boolean explainFlag = false;

    List<JSONObject> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details2);
        EventBus.getDefault().register(this);
        memberList = new ArrayList<>();
//        http://cwapi.gongpingjia.com:8080/v2/official/activity/561f5eaa0cf2a1b735efa50a/info?userId=561ba2d60cf2429fb48e86bd&token=9927f747-c615-4362-bd43-a2ec31362205
    }


    @Override
    public void initView() {
        user = User.getInstance();
        setTitle("活动详情");
        activeid = getIntent().getStringExtra("activityId");


        //参与成员信息
        listV = (PullToRefreshListView) findViewById(R.id.listview);
        mRecyclerView = listV.getRefreshableView();
        listV.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        listV.setOnRefreshListener(this);
        membersAdapter = new OfficialMembersAdapter(self);
        mRecyclerView.setAdapter(membersAdapter);
        setOnLoadSuccess(this);
        setOnLoadDataSuccess(this);
        fromWhat("data.members");
        setUrl(API2.CWBaseurl + "/official/activity/" + activeid + "/members?userId=" + user.getUserId() + "&token=" + user.getToken());

        mInflater = LayoutInflater.from(self);
        mHeadView = mInflater.inflate(R.layout.item_active_details2_headview, null);
        mFootView = mInflater.inflate(R.layout.item_active_details2_footview, null);
        mRecyclerView.addHeaderView(mHeadView, null, false);
        mRecyclerView.addFooterView(mFootView, null, false);

        startchatlayout = (LinearLayout) findViewById(R.id.startchatlayout);
        joinBtn = (Button) findViewById(R.id.join);
        chatBtn = (Button) findViewById(R.id.chatbtn);
        buyticketsBtn = (Button) findViewById(R.id.buytickets);

        nicknameT = (TextView) mHeadView.findViewById(R.id.nickname);
        contentT = (TextView) mHeadView.findViewById(R.id.content);
        foldR = (RelativeLayout) mHeadView.findViewById(R.id.fold);
        imgfoldI = (ImageView) mHeadView.findViewById(R.id.imgfold);
        avatarT = (RoundImageView) mHeadView.findViewById(R.id.avatar);
        startTimeT = (TextView) mHeadView.findViewById(R.id.starttime);
        endTimeT = (TextView) mHeadView.findViewById(R.id.endtime);
        priceT = (TextView) mHeadView.findViewById(R.id.price);
        subsidyPriceT = (TextView) mHeadView.findViewById(R.id.subsidyPrice);
        placeT = (TextView) mHeadView.findViewById(R.id.place);
        participate_womanT = (TextView) mHeadView.findViewById(R.id.participate_woman);
        participate_manT = (TextView) mHeadView.findViewById(R.id.participate_man);
        unparticipateT = (TextView) mHeadView.findViewById(R.id.unparticipate);
        creattimeT = (TextView) mHeadView.findViewById(R.id.creattime);
        introduceT = (TextView) mHeadView.findViewById(R.id.introduce);
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

        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0 || position != parent.getCount() - 1) {
                Intent it;
                String userId = JSONUtil.getString(membersAdapter.getItem(position - 2), "userId");
                if (userId.equals(user.getUserId())) {
                    it = new Intent(self, MyPerSonDetailActivity2.class);
                    startActivity(it);
                } else {
                    it = new Intent(self, PersonDetailActivity2.class);
                    it.putExtra("userId", userId);
                    startActivity(it);
                }
//                }
            }
        });

        getActiveDetailsData();
        showNext();
    }

    private void getActiveDetailsData() {
//        if (memberList != null) {
//            memberList.clear();
//        }
//        mVaules.clear();
//        setUrl(API2.ActiveDetails + activeid + "/info?userId=" + user.getUserId() + "&token=" + user.getToken());
//        fromWhat("data.members");
//        setOnLoadSuccess(this);
        DhNet verifyNet = new DhNet(API2.ActiveDetails + activeid + "/info?userId=" + user.getUserId() + "&token=" + user.getToken());
        verifyNet.doGet(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    //第三方购票连接
                    linkTicketUrl = JSONUtil.getString(jo, "linkTicketUrl");
                    if(linkTicketUrl.isEmpty()){
                        buyticketsBtn.setVisibility(View.GONE);
                    }else{
                        buyticketsBtn.setVisibility(View.VISIBLE);
                    }
                    //群组id
                    emchatGroupId = JSONUtil.getString(jo, "emchatGroupId");

                    //目的地
                    JSONObject js = JSONUtil.getJSONObject(jo, "destination");
                    ViewUtil.bindView(placeT, JSONUtil.getString(js, "province") + "省" + JSONUtil.getString(js, "city") + "市" + JSONUtil.getString(js, "detail"));

                    //开始-结束时间,创建时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");

                    Date sdate = new Date(JSONUtil.getLong(jo, "start"));
                    ViewUtil.bindView(startTimeT, format.format(sdate));
                    if (JSONUtil.getLong(jo,"end") == 0){
                        ViewUtil.bindView(endTimeT, "待定");
                    }else{
                        Date edate = new Date(JSONUtil.getLong(jo, "end"));
                        ViewUtil.bindView(endTimeT, format.format(edate));
                    }

                    ViewUtil.bindView(creattimeT, CarPlayValueFix.converTime(JSONUtil.getLong(jo, "createTime")));

                    //活动名字,头像,标题,介绍,价格,补贴,说明
                    JSONObject jsname = JSONUtil.getJSONObject(jo, "organizer");
                    ViewUtil.bindView(nicknameT, JSONUtil.getString(jsname, "nickname"));
                    ViewUtil.bindNetImage(avatarT, JSONUtil.getString(jsname, "avatar"), "head");
                    String citystr="["+JSONUtil.getString(js, "city")+"]  ";
                    String title = citystr+JSONUtil.getString(jo, "title");
                    ViewUtil.bindView(introduceT, CarPlayUtil.setTextColor(self, citystr, title, R.color.text_orange));
                    ViewUtil.bindView(contentT, JSONUtil.getString(jo, "instruction"));
                    ViewUtil.bindView(processT, JSONUtil.getString(jo, "description"));
                    ViewUtil.bindView(subsidyPriceT,"(现在报名立减" + JSONUtil.getDouble(jo, "subsidyPrice") + "元! )");
                    ViewUtil.bindView(priceT,JSONUtil.getDouble(jo, "price")+"元/人");
                    ViewUtil.bindView(explaintxtT, JSONUtil.getString(jo, "extraDesc"));

                    isMember = JSONUtil.getBoolean(jo, "isMember");
                    if (isMember) {
                        joinBtn.setVisibility(View.INVISIBLE);
                        startchatlayout.setVisibility(View.VISIBLE);
                    } else {
                        joinBtn.setVisibility(View.VISIBLE);
                        startchatlayout.setVisibility(View.INVISIBLE);
                    }
//                    membersAdapter.setIsMember(isMember);


                    //判断内容为三行以内则不显示
                    ViewTreeObserver vto = contentT.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            if (contentT.getLineCount() < 3) {
                                foldR.setVisibility(View.GONE);
                            }
                            return true;
                        }
                    });



                    //0:无限制 1：限制总人数 2：限制男女人数
                    int limitType = JSONUtil.getInt(jo, "limitType");
                    //男生,女生数量,总量
                    if (limitType == 1) {
                        findViewById(R.id.limitedlayout).setVisibility(View.GONE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.VISIBLE);
                        ViewUtil.bindView(unparticipateT,CarPlayUtil.setTextColor(self,JSONUtil.getInt(jo, "nowJoinNum")+" / ",JSONUtil.getInt(jo, "nowJoinNum") + " / " + JSONUtil.getInt(jo, "totalLimit"),R.color.text_grey) );
                    } else if (limitType == 2) {
                        findViewById(R.id.limitedlayout).setVisibility(View.VISIBLE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.GONE);
                        ViewUtil.bindView(participate_womanT,CarPlayUtil.setTextColor(self,JSONUtil.getInt(jo, "femaleNum")+" / ",JSONUtil.getInt(jo, "femaleNum") + " / " + JSONUtil.getInt(jo, "femaleLimit"),R.color.text_grey) );
                        ViewUtil.bindView(participate_manT,CarPlayUtil.setTextColor(self,JSONUtil.getInt(jo, "maleNum")+" / ",JSONUtil.getInt(jo, "maleNum") + " / " + JSONUtil.getInt(jo, "maleLimit"),R.color.text_grey) );
                    } else {
                        findViewById(R.id.limitedlayout).setVisibility(View.GONE);
                        findViewById(R.id.unlimitedlayout).setVisibility(View.VISIBLE);
                        ViewUtil.bindView(unparticipateT,CarPlayUtil.setTextColor(self,JSONUtil.getInt(jo, "nowJoinNum")+" / ",JSONUtil.getInt(jo, "nowJoinNum") + " / " + "人数不限",R.color.text_grey)) ;
                    }

//                    //参与成员
//                    JSONArray membersJsa = JSONUtil.getJSONArray(jo, "members");
//                    if (membersJsa != null) {
//                        for (int i = 0; i < membersJsa.length(); i++) {
//                            try {
//                                JSONObject jo1 = membersJsa.getJSONObject(i);
//                                memberList.add(jo1);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
////                        if (membersJsa.length() < 10) {
////                            moreL.setVisibility(View.GONE);
////                        }
//                    }
//                    setMembersData(memberList);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //报名参加
            case R.id.join:
                UserInfoManage.getInstance().checkLogin((Activity) self, new UserInfoManage.LoginCallBack() {
                    @Override
                    public void onisLogin() {
                        joinActive();
                    }

                    @Override
                    public void onLoginFail() {

                    }
                });
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
//                showProgressDialog("加载中...");
//                showNext(memberList.size());
                showNext();
                break;
            //前往购票
            case R.id.buytickets:
                if (!TextUtils.isEmpty(linkTicketUrl)) {
                    PointRecord record = PointRecord.getInstance();
                    record.getOfficialActivityBuyTicketList().add(activeid);

                    Uri uri;
                    if (linkTicketUrl.contains("http://")) {
                        uri = Uri.parse(linkTicketUrl);
                    } else {
                        uri = Uri.parse("http://" + linkTicketUrl);
                    }
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }
                break;
            //进入群聊
            case R.id.chatbtn:
                Intent intent = new Intent(self, ChatActivity.class);
                EMGroup group = EMGroupManager.getInstance().getGroup(emchatGroupId);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                intent.putExtra("activityId", activeid);
                intent.putExtra("groupId", group.getGroupId());
                startActivity(intent);
                PointRecord record = PointRecord.getInstance();
                record.getOfficialActivityChatJoinList().add(activeid);
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
        joinnet.doPostInDialog(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    isMember = !isMember;
                    membersAdapter.setIsMember(isMember);
                    if (isMember) {
                        joinBtn.setVisibility(View.INVISIBLE);
                        startchatlayout.setVisibility(View.VISIBLE);
                    } else {
                        joinBtn.setVisibility(View.VISIBLE);
                        startchatlayout.setVisibility(View.INVISIBLE);
                    }
                    refresh();
//showNext();                    getActiveDetailsData();

                }
            }
        });
    }

    /**
     * 设置参与成员信息
     */
    private void setMembersData(List<JSONObject> jsa) {
        membersAdapter.setData(jsa, isMember, activeid);
    }

    public void onEventMainThread(String success) {
        if ("报名参加".equals(success)) {
            joinActive();
        } else if ("刷新列表".equals(success)) {
//            getActiveDetailsData();
            refresh();
            showNext();
        }
    }

    @Override
    public void loadSuccess() {
//        hidenProgressDialog();
//        memberList.addAll(mVaules);
//        setMembersData(memberList);
        if (hasMore) {
            moreL.setVisibility(View.VISIBLE);
        } else {
            moreL.setVisibility(View.GONE);
        }

        membersAdapter.setData(mVaules, isMember, activeid);
        listV.onRefreshComplete();
    }

    @Override
    public void loadSuccessOnFirst() {

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
    }

    @Override
    public void load(JSONObject jo) {
        JSONObject json = JSONUtil.getJSONObject(jo, "data");
        isMember = JSONUtil.getBoolean(json, "isMember");
//        System.out.println("官方活动详情"+JSONUtil.getBoolean(json, "isMember"));
    }
}
