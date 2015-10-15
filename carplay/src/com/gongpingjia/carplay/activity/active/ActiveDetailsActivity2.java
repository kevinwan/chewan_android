package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.view.gallery.BasePagerAdapter;
import com.gongpingjia.carplay.view.gallery.GalleryViewPager;
import com.gongpingjia.carplay.view.gallery.UrlPagerAdapter;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 活动详情
 */
public class ActiveDetailsActivity2 extends CarPlayBaseActivity implements View.OnClickListener{

    private ListView mListView;

    private LayoutInflater mInflater;

    private View mHeadView;

    private View mFootView;

    private Button joinBtn;


    /** headview */
    private ImageView imgfoldI;
    private TextView titleT,contentT,startTimeT,endTimeT,priceT,placeT,participate_womanT,participate_manT,introduceT,creattimeT;
    private RelativeLayout foldR;
    private GalleryViewPager mViewPager;

    /** footview */
    private LinearLayout moreL,processL,explainL;
    private ImageView processIconI,explainIconI;
    private ListView processlistList;
    private TextView explaintxtT;


    private boolean contentFlag = false;
    private boolean processFlag = false;
    private boolean explainFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_details2);

//        http://cwapi.gongpingjia.com:8080/v2/official/activity/561f5eaa0cf2a1b735efa50a/info?userId=561ba2d60cf2429fb48e86bd&token=9927f747-c615-4362-bd43-a2ec31362205
    }

    @Override
    public void initView() {
        mInflater=LayoutInflater.from(this);
        mHeadView=mInflater.inflate(R.layout.item_active_details2_headview, null);
        mFootView=mInflater.inflate(R.layout.item_active_details2_footview, null);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.addHeaderView(mHeadView, null, false);
        mListView.addFooterView(mFootView, null, false);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });

        joinBtn = (Button) findViewById(R.id.join);

        titleT = (TextView) mHeadView.findViewById(R.id.title);
        contentT = (TextView) mHeadView.findViewById(R.id.content);
        foldR = (RelativeLayout) mHeadView.findViewById(R.id.fold);
        imgfoldI = (ImageView) mHeadView . findViewById(R.id.imgfold);
        startTimeT = (TextView) mHeadView.findViewById(R.id.starttime);
        endTimeT = (TextView) mHeadView.findViewById(R.id.endtime);
        priceT = (TextView) mHeadView.findViewById(R.id.price);
        placeT = (TextView) mHeadView.findViewById(R.id.place);
        participate_womanT = (TextView) mHeadView.findViewById(R.id.participate_woman);
        participate_manT = (TextView) mHeadView.findViewById(R.id.participate_man);
        creattimeT = (TextView) mHeadView.findViewById(R.id.creattime);
        introduceT = (TextView) mHeadView.findViewById(R.id.introduce);
        mViewPager = (GalleryViewPager) mHeadView.findViewById(R.id.viewer);


        moreL = (LinearLayout) mFootView.findViewById(R.id.more);
        processL = (LinearLayout) mFootView.findViewById(R.id.process);
        explainL = (LinearLayout) mFootView.findViewById(R.id.explain);
        processIconI = (ImageView) mFootView.findViewById(R.id.process_icon);
        explainIconI= (ImageView) mFootView.findViewById(R.id.explain_icon);
        processlistList = (ListView) mFootView.findViewById(R.id.processlist);
        explaintxtT = (TextView) mFootView.findViewById(R.id.explaintxt);

        joinBtn.setOnClickListener(this);
        foldR.setOnClickListener(this);
        processL.setOnClickListener(this);
        explainL.setOnClickListener(this);
//        getActiveDetailsData();
    }

    private void getActiveDetailsData(){
        DhNet verifyNet = new DhNet(API2.ActiveDetails + "561e177089f5f20c9606daf9" + "/info?userId=" + "561ba2d60cf2429fb48e86bd" + "&token=" + "9927f747-c615-4362-bd43-a2ec31362205");
        verifyNet.doGet(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();

                    //目的地
                    JSONObject js= JSONUtil.getJSONObject(jo,"destination");
                    ViewUtil.bindView(placeT,JSONUtil.getString(js,"province")+"省"+JSONUtil.getString(js,"city")+"市"+JSONUtil.getString(js,"detail"));

                    //开始-结束时间,创建时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    Date sdate = new Date(JSONUtil.getLong(jo,"start"));
                    ViewUtil.bindView(startTimeT,format.format(sdate));
                    Date edate = new Date(JSONUtil.getLong(jo,"end"));
                    ViewUtil.bindView(endTimeT,format.format(edate));

                    Date cdate = new Date(JSONUtil.getLong(jo,"createTime"));
                    ViewUtil.bindView(creattimeT,format.format(cdate));

                    //标题,介绍,内容,价格,补贴
                    ViewUtil.bindView(titleT,JSONUtil.getString(jo, "title"));
                    ViewUtil.bindView(introduceT,JSONUtil.getString(jo, "instruction"));
                    ViewUtil.bindView(contentT,JSONUtil.getString(jo, "description"));
                    ViewUtil.bindView(priceT,JSONUtil.getDouble(jo, "price")+"元/人(现在报名立减"+JSONUtil.getDouble(jo, "subsidyPrice")+"元)");

                    if (contentT.getLineCount()<4){
                        foldR.setVisibility(View.GONE);
                    }

                    //男生,女生数量,总量
                    ViewUtil.bindView(participate_womanT,JSONUtil.getInt(jo, "femaleNum")+"/"+JSONUtil.getInt(jo, "femaleLimit"));
                    ViewUtil.bindView(participate_manT,JSONUtil.getInt(jo, "maleNum")+"/"+JSONUtil.getInt(jo, "maleLimit"));

                    //活动大图
                    JSONObject jsc= JSONUtil.getJSONObject(jo,"cover");
                    String photo=JSONUtil.getString(jsc,"url");

                    /** GalleryViewPager  */
                    final String[] urls = new String[1];
                    urls[0] = photo;
                    List<String> items = new ArrayList<String>();
                    Collections.addAll(items, urls);

                    UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(self, items);
                    pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
                        @Override
                        public void onItemChange(int currentPosition) {
                        }
                    });
                    mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
                    mViewPager.setOffscreenPageLimit(3);
                    mViewPager.setAdapter(pagerAdapter);


                    /** 文字变色 */
//                    SpannableStringBuilder style = new SpannableStringBuilder(newcontent);
//                        style.setSpan(new ForegroundColorSpan(context.getResources()
//                                        .getColor(R.color.text_orange)), day_start,
//                                day_start + day.length(),
//                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //报名参加
            case R.id.join:
                Intent it = new Intent(self,MateActivity.class);
                startActivity(it);
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
        }
    }

    /** 活动内容展开收缩 */
    private void textFold(){
        if (contentFlag){
            contentT.setEllipsize(TextUtils.TruncateAt.END);//收缩
            contentT.setMaxLines(3);
            imgfoldI.setImageResource(R.drawable.detail_down);
            contentFlag = false;
        }else {
            contentT.setEllipsize(null); // 展开
            contentT.setMaxLines(100);
            imgfoldI.setImageResource(R.drawable.detail_up);
            contentFlag=true;
        }
    }

    /** 活动流程展开收缩 */
    private void processFold(){
        if (!processFlag){
            processFlag=!processFlag;
            processIconI.setImageResource(R.drawable.up_btn);
            processlistList.setVisibility(View.VISIBLE);
        }else {
            processFlag=!processFlag;
            processIconI.setImageResource(R.drawable.down_btn);
            processlistList.setVisibility(View.GONE);
        }
    }

    /** 活动说明展开收缩 */
    private void explainFold(){
        if (!explainFlag){
            explainFlag= !explainFlag;
            explainIconI.setImageResource(R.drawable.up_btn);
            explaintxtT.setVisibility(View.VISIBLE);
        }else {
            explainFlag= !explainFlag;
            explainIconI.setImageResource(R.drawable.down_btn);
            explaintxtT.setVisibility(View.GONE);
        }
    }
}
