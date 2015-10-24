package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.PlaceAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.Place;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.SideBar;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2015/10/12.
 * 匹配意向弹框(区域选择)
 */
public class MateRegionDialog extends BaseAlertDialog implements View.OnClickListener {

    Context mContext;

    OnMateRegionResultListener mateRegionResultListener;

    Button mBtnConfirm, mBtnReselect;
    View mLayoutPlace, mLayoutBtns;

    SideBar mSideBar;
    ListView mListView;
    TextView mTextGpsPlace;
    TextView mTextSelectPlace;
    TextView mTextTip;
    ImageView mImgClose;
    TextView mTextLastStep;

    //上一页,当前页
    private Stack<String> stack = new Stack<>();

    public MateRegionDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mate_region);
        initView();
    }

    private void initView() {
        mBtnConfirm = (Button) findViewById(R.id.btn_dlg_confirm);
        mBtnReselect = (Button) findViewById(R.id.btn_dlg_reselect);
        mListView = (ListView) findViewById(R.id.lv_dlg_places);

        mLayoutPlace = findViewById(R.id.layout_place);
        mLayoutBtns = findViewById(R.id.layout_btns);

        mTextLastStep = (TextView) findViewById(R.id.tv_last_step);
        mTextTip = (TextView) findViewById(R.id.tv_tip);
        mTextSelectPlace = (TextView) findViewById(R.id.tv_select_place);
        mTextGpsPlace = (TextView) findViewById(R.id.tv_gps_place);
        mImgClose = (ImageView) findViewById(R.id.iv_dlg_close);

        mSideBar = (SideBar) findViewById(R.id.sideBar);

        mTextGpsPlace.setText(UserLocation.getInstance().getProvice() + " " + UserLocation.getInstance().getCity() + " " + UserLocation.getInstance().getDistrict());
        mBtnReselect.setOnClickListener(this);
        mBtnReselect.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mTextLastStep.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = (Place) mListView.getAdapter().getItem(position);
                int item = place.getCode();
                mTextSelectPlace.setText(mTextSelectPlace.getText() + " " + place.getName());
                if (place.getLevel() != 4) {
                    //不是最底层的城市
                    getDatum(String.valueOf(item));
                    mTextLastStep.setVisibility(View.VISIBLE);
                } else {
                    //返回结果
                    if (mateRegionResultListener != null)
                        mateRegionResultListener.onResult(mTextSelectPlace.getText().toString());
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dlg_confirm:
                if (TextUtils.isEmpty(mTextGpsPlace.getText().toString().trim())) {
                    Toast.makeText(mContext, "定位失败请重新选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                mateRegionResultListener.onResult(mTextGpsPlace.getText().toString());
                dismiss();
                break;
            case R.id.btn_dlg_reselect:
                mLayoutPlace.setVisibility(View.GONE);
                mLayoutBtns.setVisibility(View.GONE);
                mTextTip.setText("请选择地点");

                //第一次获取省份信息
                getDatum(String.valueOf(0));
                break;

            case R.id.iv_dlg_close:
                dismiss();
                break;
            case R.id.tv_last_step:
                if (!stack.isEmpty()) {
                    stack.pop();
                    if (!stack.isEmpty()) {
                        getDatum(stack.pop());
                        String place = mTextSelectPlace.getText().toString().trim();
                        if (place.contains(" ")) {
                            mTextSelectPlace.setText(place.substring(0, place.lastIndexOf(" ")));
                        } else {
                            mTextSelectPlace.setText("");
                        }
                    }
                }
                break;
        }
    }

    /**
     * 获取地域信息
     *
     * @param id 地域id
     */
    private void getDatum(final String id) {
        DhNet dhNet = new DhNet(API2.getPlaces(id));
        dhNet.doGetInDialog(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    //操作记录进栈
                    stack.push(id);
                    if (stack.size() == 1) {
                        mTextLastStep.setVisibility(View.GONE);
                    }
                    mListView.setVisibility(View.VISIBLE);
                    mTextSelectPlace.setVisibility(View.VISIBLE);
                    mSideBar.setVisibility(View.VISIBLE);

                    JSONArray jsonArray = response.jSONArrayFrom("data");
                    List<Place> places = jsonArrayToList(jsonArray);
                    char[] chars = getUniqueLetters(places);
                    mSideBar.setLetters(chars);
                    PlaceAdapter placeAdapter = new PlaceAdapter(getContext(), places);
                    mListView.setAdapter(placeAdapter);
                    mSideBar.setListView(mListView);
                }
            }
        });
    }

    /**
     * 将json array转化为places
     *
     * @param jsonArray
     * @return
     */
    private List<Place> jsonArrayToList(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            throw new NullPointerException("JSONArray can't be empty or null");
        }
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Place place = new Place();
            try {
                String name = jsonArray.getJSONObject(i).getString("name");
                int code = jsonArray.getJSONObject(i).getInt("code");
                int level = jsonArray.getJSONObject(i).getInt("level");
                String firstLetter = getFirstSpell(name);
                place.setName(name);
                place.setCode(code);
                place.setLevel(level);
                place.setFirstLetter(firstLetter);
                places.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //按照首字母排序
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place lhs, Place rhs) {
                return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
            }
        });
        return places;
    }


    /**
     * 得到所有的首字母,places已排序
     *
     * @param places
     * @return
     */
    private char[] getUniqueLetters(List<Place> places) {
        char[] uniqueLetters = new char[26];
        int curPosition = 0;
        for (int i = 0, len = places.size(); i < len; i++) {
            Place place = places.get(i);
            if (i == 0) {
                //第一个位置直接赋值
                uniqueLetters[curPosition] = place.getFirstLetter().charAt(0);
                continue;
            }
            //比较当前字母和上一个已存储字母的大小,比它的的才存储起来
            if (place.getFirstLetter().charAt(0) > uniqueLetters[curPosition]) {
                uniqueLetters[++curPosition] = place.getFirstLetter().charAt(0);
            }
        }
        return uniqueLetters;
    }


    /**
     * 返回汉字首字母
     *
     * @param name
     * @return
     */
    private String getFirstSpell(String name) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = name.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        if (chars[0] > 128) {
            try {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(chars[0], format);
                if (temp != null) {
                    buffer.append(temp[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        } else {
            buffer.append(chars[0]);
        }

//        for (int i = 0; i < chars.length; i++) {
//            if (chars[i] > 128) {
//                String[] temp = new String[0];
//                try {
//                    temp = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
//                    if (temp != null) {
//                        buffer.append(temp[0].charAt(0));
//                    }
//                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//                    badHanyuPinyinOutputFormatCombination.printStackTrace();
//                }
//            } else {
//                buffer.append(chars[i]);
//            }
//        }
        return buffer.toString().replaceAll("\\W", "").trim();
    }

    public interface OnMateRegionResultListener {
        void onResult(String place);
    }

    public void setOnMateRegionResultListener(
            OnMateRegionResultListener mateRegionResultListener) {
        this.mateRegionResultListener = mateRegionResultListener;
    }

}
