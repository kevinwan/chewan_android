package com.gongpingjia.carplay.view.dialog;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.dialog.CommonDialog.OnCommonDialogItemClickListener;

public class CarSeatSelectDialog extends BaseAlertDialog {

	RadioGroup radiogroup;

	OnSelectResultListener onSelectResultListener;

	TextView countT;

	Context mContext;

	private List<String> mSeatOptions;

	View seatLayoutV;

	TextView desT;

	String activityId;
	
	TextView unitT;

	public CarSeatSelectDialog(Context context, String activityId) {
		super(context);
		this.mContext = context;
		this.activityId = activityId;
		mSeatOptions = new ArrayList<String>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_carseat_select);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		countT = (TextView) findViewById(R.id.count);
		unitT=(TextView) findViewById(R.id.count_unit);
		desT = (TextView) findViewById(R.id.des);
		seatLayoutV = findViewById(R.id.seatLayout);
		seatLayoutV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (radiogroup.getCheckedRadioButtonId() != R.id.radio_right) {
					if (mSeatOptions.size() == 0) {
						IocContainer.getShare().get(IDialog.class)
								.showToastShort(mContext, "正在加载可提供座位的数量,请稍等!");
						return;
					}
					CommonDialog dlg = new CommonDialog(mContext, mSeatOptions,
							"提供空座数");
					dlg.setOnDialogItemClickListener(new OnCommonDialogItemClickListener() {

						@Override
						public void onDialogItemClick(int position) {
							// TODO Auto-generated method stub
							countT.setText(mSeatOptions.get(position));
							countT.setTextColor(mContext.getResources()
									.getColor(R.color.text_black));
						}
					});
					dlg.show();
				}
			}
		});
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_right) {
					countT.setText(0 + "");
					countT.setTextColor(mContext.getResources().getColor(
							R.color.text_grey));
					unitT.setTextColor(mContext.getResources().getColor(
							R.color.text_grey));
				}else {
					unitT.setTextColor(mContext.getResources().getColor(
							R.color.text_black));
				}
			}
		});
		Button btn_submitB = (Button) findViewById(R.id.btn_submit);
		btn_submitB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onSelectResultListener != null) {
					// if (TextUtils.isEmpty(countT.getText().toString())) {
					// IocContainer
					// .getShare()
					// .get(IDialog.class)
					// .showToastShort(mContext,
					// "请选择" + desT.getText().toString());
					// return;
					// }
					int count = 0;
					if (TextUtils.isEmpty(countT.getText().toString())) {
						count = 0;
					} else {
						count = Integer.parseInt(countT.getText().toString());
					}

					if (radiogroup.getCheckedRadioButtonId() == R.id.radio_left
							&& count == 0) {
						IocContainer
								.getShare()
								.get(IDialog.class)
								.showToastShort(mContext,
										"请选择" + desT.getText().toString() + "!");
						return;
					}
					onSelectResultListener.click(count);
				}
				dismiss();
			}
		});
		getSeatCount();
	}

	private void getSeatCount() {
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/seats?token=" + user.getToken() + "&activityId="
				+ activityId);
		net.doGet(new NetTask(mContext) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject json = response.jSONFrom("data");
					try {
						if (json.getInt("isAuthenticated") == 1) {
							// 认证车主
							int minSeat = json.getInt("minValue");
							int maxSeat = json.getInt("maxValue");
							for (int i = minSeat; i <= maxSeat; i++) {
								mSeatOptions.add(String.valueOf(i));
							}
						} else {
							// 未认证
							desT.setText("邀请人数");
							mSeatOptions.add("1");
							mSeatOptions.add("2");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public OnSelectResultListener getOnSelectResultListener() {
		return onSelectResultListener;
	}

	public void setOnSelectResultListener(
			OnSelectResultListener onSelectResultListener) {
		this.onSelectResultListener = onSelectResultListener;
	}

	public interface OnSelectResultListener {
		void click(int seatCount);
	}

}
