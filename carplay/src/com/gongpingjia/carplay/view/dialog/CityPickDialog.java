package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.data.CityDataManage;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.wheel.OnWheelChangedListener;
import com.gongpingjia.carplay.view.wheel.WheelView;
import com.gongpingjia.carplay.view.wheel.adapter.ArrayWheelAdapter;

public class CityPickDialog extends BaseAlertDialog implements
		android.view.View.OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;

	private WheelView mViewCity;

	private WheelView mViewDistrict;

	private Button mBtnConfirm;

	Context mContext;

	String mCurrentDistrictName, mCurrentCityName, mCurrentProviceName;

	boolean showDistrict = true;

	OnPickResultListener onPickResultListener;

	public CityPickDialog(Context context, boolean showDistrict) {
		super(context);
		this.mContext = context;
		this.showDistrict = showDistrict;
	}

	public CityPickDialog(Context context) {
		super(context);
		this.mContext = context;
		CityDataManage.initProvinceDatas();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_citypick);
		initView();
	}

	private void initView() {

		Button cancleB = (Button) findViewById(R.id.cancle);
		cancleB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mViewProvince = (WheelView) findViewById(R.id.province);
		mViewCity = (WheelView) findViewById(R.id.city);
		mViewDistrict = (WheelView) findViewById(R.id.district);
		mViewDistrict.setVisibility(showDistrict ? View.VISIBLE : View.GONE);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);

		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				CityDataManage.mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = CityDataManage.mDistrictDatasMap
					.get(mCurrentCityName)[newValue];
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = CityDataManage.mCitisDatasMap
				.get(mCurrentProviceName)[pCurrent];
		String[] areas = CityDataManage.mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = CityDataManage.mDistrictDatasMap
				.get(mCurrentCityName)[0];
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = CityDataManage.mProvinceDatas[pCurrent];
		String[] cities = CityDataManage.mCitisDatasMap
				.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity
				.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			showSelectedResult();
			break;
		default:
			break;
		}
	}

	private void showSelectedResult() {
		if (onPickResultListener != null) {
			onPickResultListener.onResult(mCurrentProviceName,
					mCurrentCityName, mCurrentDistrictName);
		}
		dismiss();
	}

	public interface OnPickResultListener {
		void onResult(String provice, String city, String district);
	}

	public OnPickResultListener getOnPickResultListener() {
		return onPickResultListener;
	}

	public void setOnPickResultListener(
			OnPickResultListener onPickResultListener) {
		this.onPickResultListener = onPickResultListener;
	}

}
