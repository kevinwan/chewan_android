package com.gongpingjia.carplay.view.dialog;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.data.CityDataManage;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.wheel.OnWheelChangedListener;
import com.gongpingjia.carplay.view.wheel.WheelView;
import com.gongpingjia.carplay.view.wheel.adapter.ArrayWheelAdapter;

public class DateTimerDialog2 extends BaseAlertDialog implements
		android.view.View.OnClickListener, OnWheelChangedListener {
	private WheelView mViewYear;

	private WheelView mViewMonth;

	private WheelView mViewDay;

	private Button mBtnConfirm;

	Context mContext;

	String mCurrentYearName, mCurrentMonthName, mCurrentDayName;

	TextView yearT, monthT, dayT;

	OnDateTimerResultListener dateTimerResultListener;

	public DateTimerDialog2(Context context, boolean showDistrict) {
		super(context);
		this.mContext = context;
		CityDataManage.initProvinceDatas(0);
	}

	public DateTimerDialog2(Context context) {
		super(context);
		this.mContext = context;
		CityDataManage.initProvinceDatas(1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_datetimer);
		initView();
	}

	private void initView() {

		// Button cancleB = (Button) findViewById(R.id.cancle);
		// cancleB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// dismiss();
		// }
		// });
		mViewYear = (WheelView) findViewById(R.id.year);
		mViewMonth = (WheelView) findViewById(R.id.month);
		mViewDay = (WheelView) findViewById(R.id.day);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

		yearT = (TextView) findViewById(R.id.txt_year);
		monthT = (TextView) findViewById(R.id.txt_month);
		dayT = (TextView) findViewById(R.id.txt_day);

		// 添加change事件
		mViewYear.addChangingListener(this);
		// 添加change事件
		mViewMonth.addChangingListener(this);
		// 添加change事件
		mViewDay.addChangingListener(this);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);

		mViewYear.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				getYear()));
		mViewMonth.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				getMonth()));
		mViewDay.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				getDay(31)));
		// 设置可见条目数量
		mViewYear.setVisibleItems(3);
		mViewMonth.setVisibleItems(3);
		mViewDay.setVisibleItems(3);
		updateDay();
	}

	private String[] getYear() {
		String[] years = new String[200];
		for (int i = 0; i < 200; i++) {
			years[i] = String.valueOf((1900 + i));
		}
		return years;
	}

	private String[] getMonth() {
		String[] months = new String[12];
		for (int i = 1; i <= 12; i++) {
			months[i - 1] = String.valueOf(i);
		}
		return months;
	}

	private String[] getDay(int daycount) {
		String[] days = new String[daycount];
		for (int i = 1; i <= daycount; i++) {
			days[i - 1] = String.valueOf(i);
		}
		return days;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewYear) {
			mViewMonth.setCurrentItem(0);
			updateDay();
		} else if (wheel == mViewMonth) {
			updateDay();
		} else if (wheel == mViewDay) {
			dayT.setText(getDay(getMaxDate(mCurrentYearName,mCurrentMonthName))[mViewDay.getCurrentItem()]);
		}
	}

	/**
	 * 根据当前的月，更新日WheelView的信息
	 */
	private void updateDay() {
		int yCurrent = mViewYear.getCurrentItem();
		mCurrentYearName = getYear()[yCurrent];
		int mCurrent = mViewMonth.getCurrentItem();
		mCurrentMonthName = getMonth()[mCurrent];


		String[] Days = getDay(getMaxDate(mCurrentYearName,mCurrentMonthName));
		mViewDay.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Days));
		mViewDay.setCurrentItem(0);
		mCurrentDayName = getDay(getMaxDate(mCurrentYearName,mCurrentMonthName))[mViewDay.getCurrentItem()];

		yearT.setText(mCurrentYearName);
		monthT.setText(mCurrentMonthName);
		dayT.setText(mCurrentDayName);
	}
	
	//获取当前月几天
	private int getMaxDate(String year,String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1); // 输入6月
																			// 实为7月
		int maxDate = cal.getActualMaximum(Calendar.DATE);
		return maxDate;
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
		if (dateTimerResultListener != null) {
			mCurrentYearName = getYear()[mViewYear.getCurrentItem()];
			mCurrentMonthName = getMonth()[mViewMonth.getCurrentItem()];


			mCurrentDayName = getDay(getMaxDate(mCurrentYearName,mCurrentMonthName))[mViewDay.getCurrentItem()];
			dateTimerResultListener.onResult(mCurrentYearName,
					mCurrentMonthName, mCurrentDayName);
		}
		dismiss();
	}

	public interface OnDateTimerResultListener {
		void onResult(String year, String month, String day);
	}

	public OnDateTimerResultListener getOnDateTimerResultListener() {
		return dateTimerResultListener;
	}

	public void setOnDateTimerResultListener(
			OnDateTimerResultListener dateTimerResultListener) {
		this.dateTimerResultListener = dateTimerResultListener;
	}
}
