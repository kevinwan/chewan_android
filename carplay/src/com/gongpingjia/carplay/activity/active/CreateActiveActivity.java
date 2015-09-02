package com.gongpingjia.carplay.activity.active;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.UserLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.ImageAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.Active;
import com.gongpingjia.carplay.bean.ActiveCreateEB;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.photo.model.PhotoModel;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.Utils;
import com.gongpingjia.carplay.view.NestedGridView;
import com.gongpingjia.carplay.view.dialog.CommonDialog;
import com.gongpingjia.carplay.view.dialog.CommonDialog.OnCommonDialogItemClickListener;
import com.gongpingjia.carplay.view.dialog.DateTimePickerDialog;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import de.greenrobot.event.EventBus;

/***
 * 
 * 创建活动
 * 
 * @author Administrator
 * 
 */
public class CreateActiveActivity extends CarPlayBaseActivity implements OnClickListener {

    private static final int REQUEST_DESCRIPTION = 1;

    private static final int REQUEST_DESTINATION = 2;

    private Button mFinishBtn, mFinishInviteBtn;

    private View mTypeLayout, mDescriptionLayout, mDestimationLayout, mStartTimeLayout, mEndTimeLayout, mFeeLayout,
            mSeatLayout;

    private TextView mTypeText, mDescriptionText, mStartTimeText, mEndTimeText, mFeeText, mSeatText, mDestimationText,
            mSeatHintText;

    private NestedGridView mPhotoGridView;

    private ImageAdapter mImageAdapter;

    private List<PhotoState> mPhotoStates;

    // 最后一张图片的状态
    private PhotoState mLastPhoto;

    // 上传图片返回的id
    private List<String> mPicIds;

    private DhNet mDhNet;

    // 图片缓存根目录
    private File mCacheDir;

    // 当前选择图片的路径
    private String mCurPath;

    private List<String> mFeeOptions;

    private List<String> mTypeOptions;

    private List<String> mSeatOptions;

    private String mCity, mProvince, mDistrict;

    private String mLocation;

    private String mAddress;

    private double mLatitude, mLongitude;

    // 开始时间默认为当前的时间戳
    private long mStartTimeStamp = System.currentTimeMillis();

    private long mEndTimeStamp = 0;

    private User mUser = User.getInstance();

    Integer degree;

    // umeng分享
    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    // 微信id(正式版)
    // static String sAppId = "wx4c127cf07bd7d80b";
    // static String sAppSecret = "315ce754c5a1096c5188b4b69a7b9f04";

    // 微信(测试版)
    static String sAppId = Constant.WX_APP_KEY;

    static String sAppSecret = Constant.WX_APP_SECRET;

    // 微信好友
    private UMWXHandler wxHandler;

    // 微信朋友圈
    private UMWXHandler wxCircleHandler;

    private boolean isEditable = false;

    private ImageView mDelImgView;

    private boolean shouldSave = true;

    List<String> pthotoUrls = new ArrayList<String>();

    Dao<Active, Integer> activeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_active);
        setupShare();
    }

    private void setupShare() {
        // 微信朋友圈
        wxCircleHandler = new UMWXHandler(this, sAppId, sAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        // 微信好友
        wxHandler = new UMWXHandler(this, sAppId, sAppSecret);
        wxHandler.addToSocialSDK();
    }

    @Override
    public void initView() {
        setTitle("创建活动");
        mPicIds = new ArrayList<String>();
        mFeeOptions = new ArrayList<String>();
        mTypeOptions = new ArrayList<String>();
        mSeatOptions = new ArrayList<String>();

        mFeeOptions.add("AA制");
        mFeeOptions.add("我请客");
        mFeeOptions.add("请我吧");

        mTypeOptions.add("吃饭");
        mTypeOptions.add("唱歌");
        mTypeOptions.add("看电影");
        mTypeOptions.add("周边游");
        mTypeOptions.add("运动");
        mTypeOptions.add("拼车");
        mTypeOptions.add("购物");
        mTypeOptions.add("亲子游");

        mTypeText = (TextView) findViewById(R.id.tv_active_type);
        mDescriptionText = (TextView) findViewById(R.id.tv_description);
        mStartTimeText = (TextView) findViewById(R.id.tv_start_time);
        mEndTimeText = (TextView) findViewById(R.id.tv_end_time);
        mFeeText = (TextView) findViewById(R.id.tv_fee);
        mSeatText = (TextView) findViewById(R.id.tv_seat);
        mDestimationText = (TextView) findViewById(R.id.tv_destination);
        mSeatHintText = (TextView) findViewById(R.id.tv_seat_hint);

        mTypeLayout = findViewById(R.id.layout_active_type);
        mDescriptionLayout = findViewById(R.id.layout_description);
        mDestimationLayout = findViewById(R.id.layout_destination);
        mStartTimeLayout = findViewById(R.id.layout_start_time);
        mEndTimeLayout = findViewById(R.id.layout_end_time);
        mFeeLayout = findViewById(R.id.layout_fee);
        mSeatLayout = findViewById(R.id.layout_seats);

        // 初始化开始时间,默认为当前的时间
        mStartTimeText.setText(Utils.getDate());

        mTypeLayout.setOnClickListener(this);
        mDescriptionLayout.setOnClickListener(this);
        mDestimationLayout.setOnClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mSeatLayout.setOnClickListener(this);
        mFeeLayout.setOnClickListener(this);

        mCacheDir = new File(getExternalCacheDir(), "CarPlay");
        mCacheDir.mkdirs();

        mFinishBtn = (Button) findViewById(R.id.btn_finish);
        mFinishInviteBtn = (Button) findViewById(R.id.btn_finish_invite);
        mPhotoGridView = (NestedGridView) findViewById(R.id.gv_photo);

        mDelImgView = (ImageView) findViewById(R.id.right_icon);
        mDelImgView.setImageResource(R.drawable.action_delete);
        mDelImgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isEditable) {
                    boolean any = false;
                    Iterator<PhotoState> iterator = mPhotoStates.iterator();
                    while (iterator.hasNext()) {
                        PhotoState state = iterator.next();
                        if (state.isChecked()) {
                            any = true;
                            int index = mPhotoStates.indexOf(state);
                            mPicIds.remove(index);
                            pthotoUrls.remove(index);
                            iterator.remove();
                        }
                    }
                    if (!any) {
                        showToast("请至少选择一张图片");
                        return;
                    } else {
                        if (!mPhotoStates.get(mPhotoStates.size() - 1).isLast()) {
                            mPhotoStates.add(mLastPhoto);
                        }
                        mImageAdapter.notifyDataSetChanged();
                        isEditable = false;
                        mDelImgView.setVisibility(View.GONE);
                    }
                }
            }
        });

        // 获取可用座位数
        mDhNet = new DhNet(API.availableSeat + mUser.getUserId() + "/seats?token=" + mUser.getToken());
        mDhNet.doGet(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONObject json = response.jSONFrom("data");
                    if (json != null) {
                        try {
                            if (json.getInt("isAuthenticated") == 1) {
                                // 认证车主
                                int minSeat = json.getInt("minValue");
                                int maxSeat = json.getInt("maxValue");
                                for (int i = minSeat; i <= maxSeat; i++) {
                                    mSeatOptions.add(String.valueOf(i));
                                }
                            } else {
                                // 未认证
                                mSeatHintText.setText("邀请人数");
                                mSeatOptions.add("1");
                                mSeatOptions.add("2");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mFinishBtn.setOnClickListener(this);
        mFinishInviteBtn.setOnClickListener(this);

        mPhotoStates = new ArrayList<PhotoState>();
        mLastPhoto = new PhotoState();
        mLastPhoto.setLast(true);
        mLastPhoto.setChecked(false);

        mImageAdapter = new ImageAdapter(this, mPhotoStates);
        mPhotoGridView.setAdapter(mImageAdapter);
        mPhotoGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (!isEditable && !mPhotoStates.get(position).isLast()) {
                    isEditable = true;
                    mDelImgView.setVisibility(View.VISIBLE);
                    mPhotoStates.get(position).setChecked(true);
                    mImageAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        mPhotoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPhotoStates.get(position).isLast()) {
                    mCurPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                    // CarPlayUtil.getPhoto(self, Constant.TAKE_PHOTO,
                    // Constant.PICK_PHOTO, new File(mCurPath));
                    CarPlayUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO, new File(mCurPath),
                            10 - mPhotoStates.size());
                } else {
                    if (isEditable) {
                        if (mPhotoStates.get(position).isChecked()) {
                            mPhotoStates.get(position).setChecked(false);
                        } else {
                            mPhotoStates.get(position).setChecked(true);
                        }
                        mImageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                OrmLiteSqliteOpenHelper daoHelper = IocContainer.getShare().get(OrmLiteSqliteOpenHelper.class);
                try {
                    activeDao = daoHelper.getDao(Active.class);
                    final List<Active> list = activeDao.queryForAll();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (list.size() > 0) {
                                Active active = list.get(list.size() - 1);
                                initData(active);
                            } else {
                                mPhotoStates.add(mLastPhoto);
                                mImageAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initData(Active active) {
        mEndTimeStamp = active.end;
        mStartTimeStamp = active.start;

        mLocation = active.location;
        mAddress = active.address;
        mCity = active.city;
        mProvince = active.province;
        mLatitude = active.latitude;
        mLongitude = active.longitude;
        mDistrict = active.district;

        mTypeText.setText(active.type);

        if (!TextUtils.isEmpty(active.introduction)) {
            mDescriptionText.setText(active.introduction);
        }
        if (!TextUtils.isEmpty(mLocation)) {
            mDestimationText.setText(active.location);
        }

        if (mStartTimeStamp != 0) {
            mStartTimeText.setText(CarPlayUtil.getStringDate(mStartTimeStamp));
        }

        if (mEndTimeStamp != 0) {
            mEndTimeText.setText(CarPlayUtil.getStringDate(mEndTimeStamp));
        }
        mFeeText.setText(active.pay);
        mSeatText.setText(active.seat);

        if (!active.seat.equals("请选择座位数")) {
            mSeatText.setTextColor(getBaseContext().getResources().getColorStateList(R.color.text_black_light));
        }

        String picIds = active.photoIds;
        if (!TextUtils.isEmpty(picIds)) {
            String[] ids = picIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                mPicIds.add(ids[i]);
            }
        }

        String picUrls = active.photoUrls;
        if (!TextUtils.isEmpty(picUrls)) {
            String[] ids = picUrls.split(",");
            for (int i = 0; i < ids.length; i++) {
                PhotoState state = new PhotoState();
                state.setChecked(false);
                state.setLast(false);
                state.setPath(ids[i]);
                mPhotoStates.add(state);
                pthotoUrls.add(ids[i]);
            }

            if (mPhotoStates.size() < 9) {
                mPhotoStates.add(mLastPhoto);
            }

        } else {
            mPhotoStates.add(mLastPhoto);
        }
        mImageAdapter.notifyDataSetChanged();
        try {
            activeDao.delete(active);
            System.out.println("删除成功!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        Intent it = null;
        CommonDialog dlg = null;
        DateTimePickerDialog date = null;
        switch (id) {

        case R.id.layout_active_type:
            dlg = new CommonDialog(self, mTypeOptions, "请选择活动");
            dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    mTypeText.setText(mTypeOptions.get(position));
                }
            });
            dlg.show();
            break;

        case R.id.layout_description:
            it = new Intent(self, ActiveDescriptionActivity.class);
            it.putExtra("description", mDescriptionText.getText().toString());
            startActivityForResult(it, REQUEST_DESCRIPTION);
            break;

        case R.id.layout_destination:
            it = new Intent(self, MapActivity.class);
            startActivityForResult(it, REQUEST_DESTINATION);
            break;

        case R.id.layout_start_time:
            date = new DateTimePickerDialog(self, System.currentTimeMillis());
            date.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                public void OnDateTimeSet(AlertDialog dialog, long date) {
                    mStartTimeText.setText(CarPlayUtil.getStringDate(date));
                    mStartTimeStamp = date;
                }
            });
            date.show();
            break;
        case R.id.layout_end_time:
            date = new DateTimePickerDialog(self, System.currentTimeMillis());
            date.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                public void OnDateTimeSet(AlertDialog dialog, long date) {
                    mEndTimeText.setText(CarPlayUtil.getStringDate(date));
                    mEndTimeStamp = date;
                }
            });
            date.show();
            break;
        case R.id.layout_fee:
            dlg = new CommonDialog(self, mFeeOptions, "请选择付费方式");
            dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    mFeeText.setText(mFeeOptions.get(position));
                }
            });
            dlg.show();
            break;
        case R.id.layout_seats:
            if (mSeatOptions.size() == 0) {
                IocContainer.getShare().get(IDialog.class).showToastLong(self, "正在加载可提供座位的数量,请稍等!");
                return;
            }
            dlg = new CommonDialog(self, mSeatOptions, "请选择空座位数");
            dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    mSeatText.setText(mSeatOptions.get(position));
                    mSeatText.setTextColor(getBaseContext().getResources().getColorStateList(R.color.text_black_light));
                }
            });
            dlg.show();
            break;

        case R.id.btn_finish:
            if (mTypeText.getText().toString().equals("")) {
                showToast("请选择活动");
                return;
            }
            if (mDescriptionText.getText().toString().equals("")) {
                showToast("请输入活动描述");
                return;
            }
            if (mPicIds.size() == 0) {
                showToast("请至少选择一张图片");
                return;
            }
            if (mDestimationText.getText().toString().length() == 0) {
                showToast("请选择目的地");
                return;
            }
            if (mSeatText.getText().toString().length() == 0 || mSeatText.getText().toString().equals("请选择座位数")) {
                showToast("请选择空座位数");
                return;
            }
            if (mStartTimeStamp < System.currentTimeMillis() - 5 * 60 * 1000) {
                showToast("活动开始时间不能小于当前时间");
                return;
            }
            if (!mEndTimeText.getText().toString().equals("不确定")) {
                if (mEndTimeStamp < mStartTimeStamp) {
                    showToast("活动截止时间应大于开始时间");
                    return;
                }
            }

            // 创建活动
            mDhNet = new DhNet(API.createActive + "userId=" + mUser.getUserId() + "&token=" + mUser.getToken());
            mDhNet.addParam("type", mTypeText.getText().toString());
            mDhNet.addParam("introduction", mDescriptionText.getText().toString());
            JSONArray array = new JSONArray(mPicIds);
            mDhNet.addParam("cover", array);
            mDhNet.addParam("location", mLocation);
            mDhNet.addParam("city", mCity);
            mDhNet.addParam("address", mAddress);
            mDhNet.addParam("start", mStartTimeStamp);
            mDhNet.addParam("pay", mFeeText.getText().toString());
            mDhNet.addParam("seat", mSeatText.getText().toString());
            if (mEndTimeStamp != 0) {
                mDhNet.addParam("end", mEndTimeStamp);
            }
            mDhNet.addParam("latitude", mLatitude);
            mDhNet.addParam("longitude", mLongitude);

            if (UserLocation.getInstance().getCity() != null) {
                mDhNet.addParam("currentCity", UserLocation.getInstance().getCity());
            } else {
                mDhNet.addParam("currentCity", UserLocation.getInstance().getProvice());
            }
            mDhNet.addParam("currentDistrict", UserLocation.getInstance().getDistrict());

            Map<String, Object> params = mDhNet.getParams();
            for (String key : params.keySet()) {
                Log.e("tag", key + ": " + params.get(key));
            }
            mDhNet.doPostInDialog(new NetTask(this) {

                @Override
                public void doInUI(Response response, Integer transfer) {
                    if (response.isSuccess()) {
                        showToast("发布成功");
                        shouldSave = false;
                        try {
                            String activeId = response.jSONFrom("data").getString("activityId");
                            Intent it = new Intent(self, ActiveDetailsActivity.class);
                            it.putExtra("activityId", activeId);
                            startActivity(it);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        self.finish();
                        EventBus.getDefault().post(new ActiveCreateEB());
                    } else {
                        try {
                            Log.e("err", response.jSON().getString("errmsg"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
            break;

        case R.id.btn_finish_invite:
            if (mTypeText.getText().toString().equals("")) {
                showToast("请选择活动");
                return;
            }
            if (mPicIds.size() == 0) {
                showToast("请至少选择一张图片");
                return;
            }
            if (mDescriptionText.getText().equals("")) {
                showToast("请输入活动介绍");
                return;
            }
            if (mDestimationText.getText().toString().length() == 0) {
                showToast("请选择目的地");
                return;
            }
            if (mSeatText.getText().toString().length() == 0 || mSeatText.getText().toString().equals("请选择座位数")) {
                showToast("请选择空座位数");
                return;
            }

            // 获取可用的座位数
            mDhNet = new DhNet(API.createActive + "userId=" + mUser.getUserId() + "&token=" + mUser.getToken());
            mDhNet.addParam("type", mTypeText.getText().toString());
            mDhNet.addParam("introduction", mDescriptionText.getText().toString());
            JSONArray array1 = new JSONArray(mPicIds);
            mDhNet.addParam("cover", array1);
            mDhNet.addParam("location", mLocation);
            mDhNet.addParam("city", mCity);
            mDhNet.addParam("address", mDestimationText.getText().toString());
            mDhNet.addParam("start", mStartTimeStamp);
            mDhNet.addParam("province", mProvince);
            mDhNet.addParam("district", mDistrict);
            mDhNet.addParam("pay", mFeeText.getText().toString());
            mDhNet.addParam("seat", mSeatText.getText().toString());

            if (!TextUtils.isEmpty(UserLocation.getInstance().getCity())) {
                mDhNet.addParam("currentCity", UserLocation.getInstance().getCity());
            } else {
                mDhNet.addParam("currentCity", UserLocation.getInstance().getProvice());
            }
            mDhNet.addParam("currentDistrict", UserLocation.getInstance().getDistrict());

            if (mEndTimeStamp != 0) {
                mDhNet.addParam("end", mEndTimeStamp);
            }
            mDhNet.addParam("latitude", mLatitude);
            mDhNet.addParam("longitude", mLongitude);
            mDhNet.doPostInDialog(new NetTask(this) {

                @Override
                public void doInUI(Response response, Integer transfer) {
                    if (response.isSuccess()) {
                        showToast("创建成功");
                        shouldSave = false;
                        JSONObject json = response.jSONFrom("data");
                        try {
                            final String activeId = json.getString("activityId");
                            final String shareContent = json.getString("shareContent");
                            final String shareTitle = json.getString("shareTitle");
                            final String shareUrl = json.getString("shareUrl");
                            final UMImage image = new UMImage(self, json.getString("imgUrl"));
                            View shareView = LayoutInflater.from(self).inflate(R.layout.pop_share, null);

                            final PopupWindow popWin = new PopupWindow(shareView, LayoutParams.MATCH_PARENT,
                                    LayoutParams.MATCH_PARENT);
                            // 分享到微信朋友
                            shareView.findViewById(R.id.layout_share_weixin).setOnClickListener(
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated
                                            // method stub
                                            WeiXinShareContent wxContent = new WeiXinShareContent();
                                            wxContent.setTargetUrl(shareUrl);
                                            wxContent.setTitle(shareTitle);
                                            wxContent.setShareContent(shareContent);
                                            wxContent.setShareImage(image);
                                            mController.setShareMedia(wxContent);
                                            mController.postShare(self, SHARE_MEDIA.WEIXIN, new SnsPostListener() {

                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
                                                    if (!activeId.equals("")) {
                                                        Intent it = new Intent(self, ActiveDetailsActivity.class);
                                                        it.putExtra("activityId", activeId);
                                                        startActivity(it);
                                                    }
                                                    popWin.dismiss();
                                                    self.finish();
                                                }
                                            });
                                        }
                                    });

                            // 分享到朋友圈
                            shareView.findViewById(R.id.layout_share_wxcircle).setOnClickListener(
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated
                                            // method stub
                                            CircleShareContent ccContent = new CircleShareContent();
                                            ccContent.setTargetUrl(shareUrl);
                                            ccContent.setShareContent(shareContent);
                                            ccContent.setTitle(shareTitle);
                                            ccContent.setShareImage(image);
                                            mController.setShareMedia(ccContent);

                                            mController.postShare(self, SHARE_MEDIA.WEIXIN_CIRCLE,
                                                    new SnsPostListener() {

                                                        @Override
                                                        public void onStart() {
                                                        }

                                                        @Override
                                                        public void onComplete(SHARE_MEDIA arg0, int arg1,
                                                                SocializeEntity arg2) {
                                                            if (!activeId.equals("")) {
                                                                Intent it = new Intent(self,
                                                                        ActiveDetailsActivity.class);
                                                                it.putExtra("activityId", activeId);
                                                                startActivity(it);
                                                            }
                                                            popWin.dismiss();
                                                            self.finish();
                                                        }
                                                    });
                                        }
                                    });
                            shareView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    popWin.dismiss();
                                    if (!activeId.equals("")) {
                                        Intent it = new Intent(self, ActiveDetailsActivity.class);
                                        it.putExtra("activityId", activeId);
                                        startActivity(it);
                                    }
                                    self.finish();
                                }
                            });
                            shareView.findViewById(R.id.layout_bg).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    popWin.dismiss();
                                    if (!activeId.equals("")) {
                                        Intent it = new Intent(self, ActiveDetailsActivity.class);
                                        it.putExtra("activityId", activeId);
                                        startActivity(it);
                                    }
                                    self.finish();
                                }
                            });
                            popWin.showAsDropDown(findViewById(R.id.title_bar));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            Log.e("err", response.jSON().getString("errmsg"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            case REQUEST_DESCRIPTION:
                mDescriptionText.setText(data.getStringExtra("des"));
                break;

            case REQUEST_DESTINATION:
                mDestimationText.setText(data.getStringExtra("location"));
                mCity = data.getStringExtra("city");
                mLocation = data.getStringExtra("location");
                mLatitude = data.getDoubleExtra("latitude", 0);
                mLongitude = data.getDoubleExtra("longitude", 0);
                mAddress = data.getStringExtra("address");
                mProvince = data.getStringExtra("province");
                mDistrict = data.getStringExtra("district");
                break;
            // case Constant.TAKE_PHOTO:
            // String newPath = new File(mCacheDir, System.currentTimeMillis()
            // + ".jpg").getAbsolutePath();
            // String path = PhotoUtil.onPhotoFromCamera(self,
            // Constant.ZOOM_PIC, mCurPath, 3, 2, 1000, newPath);
            // mCurPath = path;
            // break;
            // case Constant.PICK_PHOTO:
            // PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mCurPath,
            // data, 3, 2, 1000);
            // break;
            // case Constant.ZOOM_PIC:
            // upLoadPic(mCurPath);
            // break;

            case Constant.TAKE_PHOTO:
                Bitmap btp1 = PhotoUtil.getLocalImage(new File(mCurPath));
                String newPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                int degree = PhotoUtil.getBitmapDegree(mCurPath);
                PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
                btp1.recycle();
                upLoadPic(newPath);
                // PhotoUtil.onPhotoFromCamera(self, Constant.ZOOM_PIC,
                // mCurPath, 1, 1, 1000);
                break;
            case Constant.PICK_PHOTO:

                if (data != null && data.getExtras() != null) {
                    @SuppressWarnings("unchecked")
                    List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
                    if (photos == null || photos.isEmpty()) {
                        showToast("没有选择图片!");
                    } else {
                        for (int i = 0; i < photos.size(); i++) {
                            String newPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
                                    .getAbsolutePath();
                            Bitmap btp = PhotoUtil.getLocalImage(new File(photos.get(i).getOriginalPath()));
                            PhotoUtil.saveLocalImage(btp, new File(newPhotoPath));
                            upLoadPic(newPhotoPath);
                        }
                    }
                }

                // PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mCurPath,
                // data, 1, 1, 1000);
                break;

            }
        }
    }

    private void upLoadPic(String path) {
        mPhotoStates.remove(mPhotoStates.size() - 1);
        PhotoState state = new PhotoState();
        state.setChecked(false);
        state.setLast(false);
        state.setPath(path);
        mPhotoStates.add(state);
        if (mPhotoStates.size() != 9) {
            mPhotoStates.add(mLastPhoto);
        }
        mImageAdapter.notifyDataSetChanged();
        DhNet net = new DhNet(API.uploadPictures + "userId=" + mUser.getUserId() + "&token=" + mUser.getToken());
        net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFrom("data");
                    pthotoUrls.add(JSONUtil.getString(jo, "photoUrl"));
                    try {
                        String picId = jo.getString("photoId");
                        mPicIds.add(picId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToast("图片上传成功");
                } else {

                    if (mPhotoStates.size() != 9) {
                        mPhotoStates.remove(mPhotoStates.size() - 1);
                    } else {
                        if (mPhotoStates.get(mPhotoStates.size() - 1).isLast()) {
                            mPhotoStates.remove(mPhotoStates.size() - 2);
                        } else {
                            mPhotoStates.remove(mPhotoStates.size() - 1);
                        }
                    }
                    try {
                        showToast(response.jSON().getString("errmsg") + " 图片上传失败,请重新选择上传");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (shouldSave) {
            Active active = new Active();
            active.end = mEndTimeStamp;
            active.start = mStartTimeStamp;
            active.introduction = mDescriptionText.getText().toString();
            active.latitude = mLatitude;
            active.longitude = mLongitude;
            active.address = mAddress;
            active.city = mCity;
            active.location = mLocation;
            active.type = mTypeText.getText().toString();
            active.pay = mFeeText.getText().toString();
            active.district = mDistrict;
            if (mPicIds.size() != 0) {
                for (int i = 0; i < mPicIds.size(); i++) {
                    String id = mPicIds.get(i);
                    if (i == 0) {
                        active.photoIds = active.photoIds + id;
                    } else {
                        active.photoIds = active.photoIds + "," + id;
                    }
                }
            } else {
                active.photoIds = "";
            }
            if (pthotoUrls.size() != 0) {
                for (int i = 0; i < pthotoUrls.size(); i++) {
                    String url = pthotoUrls.get(i);
                    if (i == 0) {
                        active.photoUrls = active.photoUrls + url;
                    } else {
                        active.photoUrls = active.photoUrls + "," + url;
                    }
                }
            } else {
                active.photoUrls = "";
            }

            active.seat = mSeatText.getText().toString();
            active.province = mProvince;
            try {
                activeDao.createOrUpdate(active);
                System.out.println("进入数据库");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
