package com.gongpingjia.carplay.activity.my;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.BrandAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.BrandDetails;
import com.gongpingjia.carplay.bean.CarBrand;
import com.gongpingjia.carplay.view.SideBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Description 车型选择
 * @author Administrator
 * @date 2015-7-21 上午10:27:00
 */
public class CarTypeSelectActivity extends CarPlayBaseActivity
{
    
    private View mHeaderLayout;
    
    private SideBar mSideBar;
    
    // 车辆型号list view
    private ListView mListView;
    
    private TextView mDialogText;
    
    private BrandAdapter mBrandAdapter;
    
    // 品牌列表
    private List<CarBrand> mDatas;
    
    // 详细列表
    private List<BrandDetails> mBrands;
    
    private PopupWindow mPopWindow;
    
    private View mPopView;
    
    // 弹出list view
    private ListView mPopListView;
    
    private View mBrandHeader;
    
    CarBrand currentBrand;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_select);
        
        mHeaderLayout = findViewById(R.id.title_bar);
        
        mSideBar = (SideBar)findViewById(R.id.sideBar);
        mListView = (ListView)findViewById(R.id.lv_car_type);
        mDialogText = (TextView)findViewById(R.id.tv_dialog);
        
        mSideBar.setListView(mListView);
        mSideBar.setTextView(mDialogText);
        
        // 弹出车型
        mPopView = LayoutInflater.from(this).inflate(R.layout.pop_brand_details, null);
        mPopListView = (ListView)mPopView.findViewById(R.id.lv_brands);
        mPopListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO Auto-generated method stub
                if (mPopWindow.isShowing())
                {
                    mPopWindow.dismiss();
                    Intent it = getIntent();
                    it.putExtra("brandName", currentBrand.getBrand());
                    it.putExtra("brandLogo", currentBrand.getUrl());
                    it.putExtra("model", mBrands.get(position).getName());
                    setResult(Activity.RESULT_OK, it);
                    // Log.e("", mBrands.get(position).toString());
                }
            }
        });
        mBrandHeader = LayoutInflater.from(self).inflate(R.layout.listitem_car_brand, null);
        mPopListView.addHeaderView(mBrandHeader);
        
        // 弹出window
        mPopWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        // 请求主页面所有车型
        mBrands = new ArrayList<BrandDetails>();
        mDatas = new ArrayList<CarBrand>();
        DhNet dhNet = new DhNet(API.allCarBrands);
        dhNet.doGetInDialog(new NetTask(this)
        {
            
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                // TODO Auto-generated method stub
                if (response.isSuccess())
                {
                    JSONArray array = response.jSONArrayFrom("data");
                    for (int i = 0; i < array.length(); i++)
                    {
                        CarBrand brand = new CarBrand();
                        try
                        {
                            JSONObject json = array.getJSONObject(i);
                            brand.setBrand(json.getString("name"));
                            brand.setFirstLetter(json.getString("first_letter"));
                            brand.setUrl(json.getString("logo_img"));
                            brand.setSlug(json.getString("slug"));
                            mDatas.add(brand);
                        }
                        catch (JSONException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    
                    mBrandAdapter = new BrandAdapter(CarTypeSelectActivity.this, mDatas);
                    mListView.setAdapter(mBrandAdapter);
                }
            }
        });
        
        // 设置车型点击事件
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
            {
                // TODO Auto-generated method stub
                DhNet dhNet = new DhNet(API.carDetails);
                dhNet.addParam("brand", mDatas.get(position).getSlug());
                dhNet.doGetInDialog(new NetTask(CarTypeSelectActivity.this)
                {
                    
                    @Override
                    public void doInUI(Response response, Integer transfer)
                    {
                        // TODO Auto-generated method stub
                        if (response.isSuccess())
                        {
                            JSONArray array = response.jSONArrayFrom("data");
                            List<String> strs = new ArrayList<String>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                BrandDetails details = new BrandDetails();
                                try
                                {
                                    JSONObject json = array.getJSONObject(i);
                                    details.setMum(json.getString("mum"));
                                    details.setName(json.getString("name"));
                                    details.setSlug(json.getString("slug"));
                                    
                                    strs.add(json.getString("name"));
                                }
                                catch (JSONException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                mBrands.add(details);
                            }
                            // 设置数据源
                            ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(self, R.layout.listitem_brand, R.id.tv_brand_name, strs);
                            
                            // 更新list view header数据
                            ImageView image = (ImageView)mBrandHeader.findViewById(R.id.imgView_car_logo);
                            ImageLoader.getInstance().displayImage(mDatas.get(position).getUrl(),
                                image,
                                CarPlayValueFix.optionsDefault);
                            TextView text = (TextView)mBrandHeader.findViewById(R.id.tv_brand_name);
                            text.setText(mDatas.get(position).getBrand());
                            
                            mPopListView.setAdapter(adapter);
                            if (!mPopWindow.isShowing())
                            {
                                mPopWindow.showAsDropDown(mHeaderLayout);
                            }
                            currentBrand = mDatas.get(position);
                        }
                    }
                });
            }
        });
        
    }
    
    @Override
    public void initView()
    {
        // TODO Auto-generated method stub
        
    }
    
}
