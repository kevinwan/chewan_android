package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.LookAroundDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/20.
 */
public class LookAroundPop {

    Activity context;

    View contentV;

    PopupWindow pop;

    static LookAroundPop instance;

    DhNet dhNet;

    User user;

    RelativeLayout layoutR;

    public LookAroundPop(Activity context) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.activity_lookaround, null);
        pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupLookAround);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();

    }

    public static LookAroundPop getInstance(Activity context) {
        instance = new LookAroundPop(context);
        return instance;

    }

    private void initView() {
        user = User.getInstance();
        layoutR = (RelativeLayout) contentV.findViewById(R.id.lookRelativelayout);
        contentV.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        getData();
    }


    private void getData() {
        UserLocation location = UserLocation.getInstance();
        dhNet = new DhNet(API2.CWBaseurl + "activity/randomLook?");
        dhNet.addParam("latitude", location.getLatitude());
        dhNet.addParam("longitude", location.getLongitude());
        dhNet.addParam("limit", 10);
        dhNet.addParam("token", user.getToken());
        dhNet.addParam("userId", user.getUserId());
        dhNet.doGetInDialog(new NetTask(context) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONArray jsa = response.jSONArrayFromData();

                    bindData(jsa);
                }
            }
        });

    }

    private void bindData(JSONArray jsa) {
        int position = 0;
        if (jsa != null && jsa.length() >= 10) {
            for (int i = 0; i < layoutR.getChildCount(); i++) {
                LinearLayout linear = (LinearLayout) layoutR.getChildAt(i);
                for (int j = 0; j < linear.getChildCount(); j++) {
                    RoundImageView img;
                    TextView txt;
                    if (!(i == 1 && j == 0)) {
                        img = (RoundImageView) ((LinearLayout) linear.getChildAt(j)).getChildAt(0);
                        txt = (TextView) ((LinearLayout) linear.getChildAt(j)).getChildAt(1);
                        try {
                            final JSONObject json = JSONUtil.getJSONObjectAt(jsa, position);
                            JSONObject jo = json.getJSONObject("organizer");
                            ViewUtil.bindNetImage(img, jo.getString("avatar"), "head");
                            ViewUtil.bindView(txt, json.getString("type"));

                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LookAroundDialog dialog = new LookAroundDialog(context, json);
                                    dialog.show();
                                    ;
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new mHandler((LinearLayout) linear.getChildAt(j)).sendEmptyMessageDelayed(0, position * 1000);

                        position = position + 1;
                    }

                }
            }
        }

    }

    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
        // if (addresssT != null) {
        // addresssT.setText("不限");
        // }
    }

    private AnimationSet initAnimationSet_bg() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);
        as.addAnimation(sa);
        return as;
    }

    class mHandler extends Handler {
        View bg;

        public mHandler(View bg) {
            this.bg = bg;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    bg.startAnimation(initAnimationSet_bg());
                    bg.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
