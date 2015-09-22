package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.wheel.WheelView;
import com.gongpingjia.carplay.view.wheel.adapter.ArrayWheelAdapter;

public class ActiveDatePickDialog extends BaseAlertDialog implements
		android.view.View.OnClickListener {

	private WheelView mViewDay;

	private WheelView mViewTime;

	private Button mBtnConfirm;

	Context mContext;

	String mCurrentDayName, mCurrentTimeName;

	OnActiveDateResultListener onActiveDateResultListener;

	String[] days;

	String[] times;

	public ActiveDatePickDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_activedate);
		initView();
	}

	/**
	 * 获取dialog展示数据
	 */
	private void getDate() {
		days = new String[] { "今天", "明天", "后天" };
		times = new String[] { "上午", "中午", "下午", "晚上" };
	}

	private void initView() {
		getDate();
		Button cancleB = (Button) findViewById(R.id.cancle);
		cancleB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mViewDay = (WheelView) findViewById(R.id.day);
		mViewTime = (WheelView) findViewById(R.id.time);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);

		mViewDay.setViewAdapter(new ArrayWheelAdapter<String>(mContext, days));
		mViewTime
				.setViewAdapter(new ArrayWheelAdapter<String>(mContext, times));
		// 设置可见条目数量
		mViewDay.setVisibleItems(5);
		mViewTime.setVisibleItems(5);
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
		if (onActiveDateResultListener != null) {
			int dayCurrent = mViewDay.getCurrentItem();
			int timeCurrent = mViewTime.getCurrentItem();
			mCurrentDayName = days[dayCurrent];
			mCurrentTimeName = times[timeCurrent];
			onActiveDateResultListener.onResult(mCurrentDayName,
					mCurrentTimeName);
		}
		dismiss();
	}

	public interface OnActiveDateResultListener {
		void onResult(String day, String time);
	}

	public OnActiveDateResultListener getOnPickResultListener() {
		return onActiveDateResultListener;
	}

	public void setOnPickResultListener(
			OnActiveDateResultListener onPickResultListener) {
		this.onActiveDateResultListener = onPickResultListener;
	}

}
