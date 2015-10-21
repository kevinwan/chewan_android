package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.RoundImageView;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.CachePolicy;

import org.json.JSONArray;

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
        getData();
    }


    private void getData() {
        dhNet = new DhNet(API2.CWBaseurl + "activity/list?");
        dhNet.useCache(CachePolicy.POLICY_CACHE_ONLY);
        dhNet.addParam("latitude", "32");
        dhNet.addParam("longitude", "118");
        dhNet.addParam("maxDistance", "5000000");
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
                    RoundImageView img = (RoundImageView) ((LinearLayout)linear.getChildAt(j)).getChildAt(0);
                    TextView txt = (TextView) ((LinearLayout)linear.getChildAt(j)).getChildAt(1);
                    JSONUtil.getJSONObjectAt(jsa,position);
//                    ViewUtil.bindNetImage(img, JSONUtil.getJSONObject(position));
                    position = position + 1;
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


}
