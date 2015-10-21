package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.PlaceAdapter;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/10/12.
 * 匹配意向弹框(区域选择)
 */
public class MateRegionDialog extends BaseAlertDialog implements View.OnClickListener {

    Context mContext;

    OnMateRegionResultListener mateRegionResultListener;

    Button mBtnConfirm, mBtnReselect;
    View mLayoutPlace, mLayoutBtns;

    ListView mListView;
    TextView mTextGpsPlace;
    TextView mTextSelectPlace;
    TextView mTextTip;
    ImageView mImgClose;

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

        mTextTip = (TextView) findViewById(R.id.tv_tip);
        mTextSelectPlace = (TextView) findViewById(R.id.tv_select_place);
        mTextGpsPlace = (TextView) findViewById(R.id.tv_gps_place);
        mImgClose = (ImageView) findViewById(R.id.iv_dlg_close);

        mTextGpsPlace.setText(UserLocation.getInstance().getProvice() + " " + UserLocation.getInstance().getCity() + " " + UserLocation.getInstance().getDistrict());
        mBtnReselect.setOnClickListener(this);
        mBtnReselect.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject object = (JSONObject) mListView.getAdapter().getItem(position);
                try {
                    int item = object.getInt("code");
                    mTextSelectPlace.setText(mTextSelectPlace.getText() + " " + object.getString("name"));
                    if (object.getInt("level") != 4) {
                        //不是最底层的城市
                        getDatum(String.valueOf(item));
                    } else {
                        //返回结果
                        if (mateRegionResultListener != null)
                            mateRegionResultListener.onResult(mTextSelectPlace.getText().toString());
                        dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dlg_confirm:
                mateRegionResultListener.onResult(mTextGpsPlace.getText().toString());
                break;
            case R.id.btn_dlg_reselect:
                mLayoutPlace.setVisibility(View.GONE);
                mLayoutBtns.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mTextSelectPlace.setVisibility(View.VISIBLE);
                mTextTip.setText("请选择地点");

                //第一次获取省份信息
                getDatum(String.valueOf(0));
                break;

            case R.id.iv_dlg_close:
                dismiss();
                break;
        }
    }

    /**
     * 获取地域信息
     *
     * @param id 地域id
     */
    private void getDatum(String id) {
        DhNet dhNet = new DhNet(API2.getPlaces(id));
        dhNet.doGet(new NetTask(mContext) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONArray jsonArray = response.jSONArrayFrom("data");
                    PlaceAdapter placeAdapter = new PlaceAdapter(getContext(), jsonArray);
                    mListView.setAdapter(placeAdapter);
                }
            }
        });
    }

    public interface OnMateRegionResultListener {
        void onResult(String place);
    }

    public OnMateRegionResultListener getOnMateRegionResultListener() {
        return mateRegionResultListener;
    }

    public void setOnMateRegionResultListener(
            OnMateRegionResultListener mateRegionResultListener) {
        this.mateRegionResultListener = mateRegionResultListener;
    }

}
