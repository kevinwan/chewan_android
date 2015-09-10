package com.gongpingjia.carplay.view.pop;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.UserLocation;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.ActiveParmasEB;
import com.gongpingjia.carplay.bean.FilterPreference;
import com.gongpingjia.carplay.bean.MapEB;
import com.gongpingjia.carplay.view.dialog.CityPickDialog;
import com.gongpingjia.carplay.view.dialog.CityPickDialog.OnPickResultListener;
import com.gongpingjia.carplay.view.dialog.CommonDialog;
import com.gongpingjia.carplay.view.dialog.CommonDialog.OnCommonDialogItemClickListener;

import de.greenrobot.event.EventBus;

public class ActiveFilterPop extends PopupWindow implements OnClickListener {
    Activity context;

    public PopupWindow pop;

    View contentV;

    long animduring = 300;

    int direction = 1;

    /**
     * 获取数据时提交的参数
     * "100:运动类型 101:活动综合排序 102:场馆综合排序 103:俱乐部综合排序 104:教练综合排序 105:陪练综合排序"
     */

    OnCheckResult onCheckResult;

    static ActiveFilterPop activeFilterPop;

    RadioGroup genderRG, authenticateRG, carLevelRG;

    View locationLayoutV, typeLayoutV;

    TextView addresssT, typeT;

    Button cancleB, okB;

    public String province, mcity, mdistrict;

    public static final int Location = 1;

    public static final int Type = 2;

    List<String> mTypeOptions;

    CityPickDialog citydialog;

    FilterPreference pre;

    public ActiveFilterPop(Activity context) {
        EventBus.getDefault().register(this);
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.pop_active_filter, null);
        pop = new PopupWindow(contentV, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupMenu);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();  

    }

    public static ActiveFilterPop getInstance(Activity context) {
        activeFilterPop = new ActiveFilterPop(context);
        return activeFilterPop;

    }

    public void initView() {
        pre = IocContainer.getShare().get(FilterPreference.class);
        pre.load();

        View bgV = contentV.findViewById(R.id.bg);
        bgV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pop.dismiss();
            }
        });

        // 不可以去掉
        contentV.findViewById(R.id.content_layout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        genderRG = (RadioGroup) contentV.findViewById(R.id.gender_rg);
        authenticateRG = (RadioGroup) contentV.findViewById(R.id.authenticate_rg);
        carLevelRG = (RadioGroup) contentV.findViewById(R.id.carLevel_rg);
        locationLayoutV = contentV.findViewById(R.id.locationLayout);
        typeLayoutV = contentV.findViewById(R.id.typeLayout);
        addresssT = (TextView) contentV.findViewById(R.id.addresss);
        addresssT.setText("不限");
        typeT = (TextView) contentV.findViewById(R.id.type);

        cancleB = (Button) contentV.findViewById(R.id.cancle);

        okB = (Button) contentV.findViewById(R.id.ok);
        cancleB.setOnClickListener(this);
        okB.setOnClickListener(this);
        locationLayoutV.setOnClickListener(this);
        typeLayoutV.setOnClickListener(this);
        pop.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                EventBus.getDefault().unregister(this);
            }
        });

        mTypeOptions = new ArrayList<String>();
        mTypeOptions.add("吃饭");
        mTypeOptions.add("唱歌");
        mTypeOptions.add("看电影");
        mTypeOptions.add("周边游");
        mTypeOptions.add("运动");
        mTypeOptions.add("拼车");
        mTypeOptions.add("购物");
        mTypeOptions.add("亲子游");
        mTypeOptions.add("不限");

        citydialog = new CityPickDialog(context, false);
        citydialog.setOnPickResultListener(new OnPickResultListener() {

            @Override
            public void onResult(String provice, String city, String district) {
                pre.setProvince(provice);
                pre.setCity(city);
                pre.setDistrict(district);

                if (provice.equals(city)) {
                    addresssT.setText(provice);
                    pre.setCity("");
                    pre.setProvince("");
                } else {
                    addresssT.setText(provice + city);
                }
                if (provice.contains("市")) {
                    mcity = provice;
                    mdistrict = city;
                } else {
                    mcity = city;
                    mdistrict = district;
                }
            }
        });

        // 初始化选择器
        if (pre.getFirst() != null) {
            if (!TextUtils.isEmpty(pre.getProvince())) {
                // 初始化城市
                if (pre.getProvince().equals(pre.getCity())) {
                    addresssT.setText(pre.getProvince());
                } else {
                    addresssT.setText(pre.getProvince() + pre.getCity());
                }
                if (pre.getProvince().contains("市")) {
                    mcity = pre.getProvince();
                    mdistrict = pre.getCity();
                } else {
                    mcity = pre.getCity();
                    mdistrict = pre.getDistrict();
                }
            } else {
                // 直辖市
                if (UserLocation.getInstance().getProvice() == null) {
                    addresssT.setText(UserLocation.getInstance().getCity() + UserLocation.getInstance().getDistrict());
                } else {
                    addresssT.setText(UserLocation.getInstance().getProvice() + UserLocation.getInstance().getCity());
                }
            }
            if (pre.getType().equals("")) {
                typeT.setText("不限");
            } else {
                typeT.setText(pre.getType());
            }
            if ("女".equals(pre.getGender())) {
                genderRG.check(R.id.gender_rg_center);
            } else if ("男".equals(pre.getGender())) {
                genderRG.check(R.id.gender_rg_left);
            } else {
                genderRG.check(R.id.gender_rg_right);
            }

            switch (pre.getIsAuthenticate()) {
            case 1:
                authenticateRG.check(R.id.authenticate_rg_left);
                break;
            case 0:
                authenticateRG.check(R.id.authenticate_rg_center);
                break;
            case 3:
            default:
                authenticateRG.check(R.id.authenticate_rg_right);
                break;
            }

            if ("normal".equals(pre.getCarLevel())) {
                carLevelRG.check(R.id.carLevel_rg_left);
            } else if ("good".equals(pre.getCarLevel())) {
                carLevelRG.check(R.id.carLevel_rg_center);
            } else {
                carLevelRG.check(R.id.carLevel_rg_right);
            }
        } else {
            // 直辖市
            if (UserLocation.getInstance().getProvice() == null) {
                addresssT.setText(UserLocation.getInstance().getCity() + UserLocation.getInstance().getDistrict());
            } else {
                addresssT.setText(UserLocation.getInstance().getProvice() + UserLocation.getInstance().getCity());
            }
        }

    }

    public void setAddress(String address) {
        addresssT.setText(address);
    }

    public void show(View v) {
        pop.showAsDropDown(v);
        // if (addresssT != null) {
        // addresssT.setText("不限");
        // }
    }

    public OnCheckResult getOnCheckResult() {
        return onCheckResult;
    }

    public void setOnCheckResult(OnCheckResult onCheckResult) {
        this.onCheckResult = onCheckResult;
    }

    public interface OnCheckResult {
        void result(String province, String city, String district, String gender, String type, Integer authenticate,
                String carLevel);
    }

    private void getSelectResult() {
        String gender = "";

        Integer authenticate = 3;

        String carLevel = "";
        if (genderRG.getCheckedRadioButtonId() != -1) {
            RadioButton grb = (RadioButton) genderRG.findViewById(genderRG.getCheckedRadioButtonId());
            gender = grb.getText().toString();
            if (gender.equals("不限")) {
                gender = "";
                pre.setGender("");
            } else if (gender.equals("男生")) {
                gender = "男";
                pre.setGender("男");
            } else {
                gender = "女";
                pre.setGender("女");
            }
        }

        if (authenticateRG.getCheckedRadioButtonId() != -1) {
            RadioButton grb = (RadioButton) authenticateRG.findViewById(authenticateRG.getCheckedRadioButtonId());
            String authenticateString = grb.getText().toString();
            if (authenticateString.equals("车主")) {
                authenticate = 1;
                pre.setIsAuthenticate(1);
            } else if (authenticateString.equals("非车主")) {
                authenticate = 0;
                pre.setIsAuthenticate(0);
            } else {
                authenticate = 3;
                pre.setIsAuthenticate(3);
            }
        }

        if (carLevelRG.getCheckedRadioButtonId() != -1) {
            RadioButton grb = (RadioButton) carLevelRG.findViewById(carLevelRG.getCheckedRadioButtonId());
            carLevel = grb.getText().toString();
            if (carLevel.equals("一般")) {
                carLevel = "normal";
                pre.setCarLevel("normal");
            } else if (carLevel.equals("好车")) {
                carLevel = "good";
                pre.setCarLevel("good");
            } else {
                carLevel = "";
                pre.setCarLevel("");
            }
        }

        ActiveParmasEB pa = new ActiveParmasEB();
        if (addresssT.getText().toString().equals("不限")) {
            pa.setCity("");
            pa.setDistrict("");
        } else {
            pa.setCity(mcity);
            pa.setDistrict(mdistrict);
        }
        if (typeT.getText().toString().equals("不限")) {
            pa.setActiveType("");
            pre.setType("");
        } else {
            pa.setActiveType(typeT.getText().toString());
            pre.setType(typeT.getText().toString());
        }
        pa.setAuthenticate(authenticate);
        pa.setCarLevel(carLevel);
        pa.setGender(gender);
        EventBus.getDefault().post(pa);
        pre.setFirst("not first");
        pre.commit();
        pop.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.cancle:
            pop.dismiss();
            break;

        case R.id.ok:
            getSelectResult();
            dismiss();
            break;

        case R.id.locationLayout:
            citydialog.show();
            // Intent it = new Intent(context, MapActivity.class);
            // context.startActivityForResult(it, Location);
            break;

        case R.id.typeLayout:

            CommonDialog dialog = new CommonDialog(context, mTypeOptions, "活动类型");
            dialog.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

                @Override
                public void onDialogItemClick(int position) {
                    // TODO Auto-generated method stub
                    typeT.setText(mTypeOptions.get(position));
                }
            });
            dialog.show();
            break;

        default:
            break;
        }
    }

    public void onEventMainThread(MapEB map) {
        // addresssT.setText(map.getCity() + map.getDistrict());
    }

}
