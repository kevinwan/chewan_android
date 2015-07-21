package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.BrandAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.BrandDetails;
import com.gongpingjia.carplay.bean.CarBrand;
import com.gongpingjia.carplay.view.SideBar;

/**
 * @Description 车型选择
 * @author Administrator
 * @date 2015-7-21 上午10:27:00
 */
public class CarTypeSelectActivity extends CarPlayBaseActivity {

    private SideBar mSideBar;

    private ListView mListView;

    private TextView mDialogText;

    private BrandAdapter mBrandAdapter;

    private List<CarBrand> mDatas;

    private List<BrandDetails> mBrands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_select);
        mSideBar = (SideBar) findViewById(R.id.sideBar);
        mListView = (ListView) findViewById(R.id.lv_car_type);
        mDialogText = (TextView) findViewById(R.id.tv_dialog);

        mBrands = new ArrayList<>();

        mDatas = new ArrayList<CarBrand>();
        DhNet dhNet = new DhNet(API.allCarBrands);
        dhNet.doGetInDialog(new NetTask(this) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONArray array = response.jSONArrayFrom("data");
                    for (int i = 0; i < array.length(); i++) {
                        CarBrand brand = new CarBrand();
                        try {
                            JSONObject json = array.getJSONObject(i);
                            brand.setBrand(json.getString("name"));
                            brand.setFirstLetter(json.getString("first_letter"));
                            brand.setUrl(json.getString("logo_img"));
                            brand.setSlug(json.getString("slug"));
                            mDatas.add(brand);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    mSideBar.setListView(mListView);
                    mSideBar.setTextView(mDialogText);
                    mBrandAdapter = new BrandAdapter(CarTypeSelectActivity.this, mDatas);
                    mListView.setAdapter(mBrandAdapter);
                }
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                DhNet dhNet = new DhNet(API.carDetails);
                dhNet.addParam("brand", mDatas.get(position).getSlug());
                dhNet.doGetInDialog(new NetTask(CarTypeSelectActivity.this) {

                    @Override
                    public void doInUI(Response response, Integer transfer) {
                        // TODO Auto-generated method stub
                        if (response.isSuccess()) {
                            JSONArray array = response.jSONArrayFrom("data");
                            for (int i = 0; i < array.length(); i++) {
                                BrandDetails details = new BrandDetails();
                                try {
                                    JSONObject json = array.getJSONObject(i);
                                    details.setMum(json.getString("mum"));
                                    details.setName(json.getString("name"));
                                    details.setSlug(json.getString("slug"));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                mBrands.add(details);
                                Log.e("brand", details.toString());
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

}
