package com.gongpingjia.carplay.adapter;

import java.util.Iterator;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveMembersActivity;
import com.gongpingjia.carplay.activity.active.MyActiveMembersManageActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.JoinEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.PicLayoutUtil;
import com.gongpingjia.carplay.view.dialog.CarSeatSelectDialog;
import com.gongpingjia.carplay.view.dialog.CarSeatSelectDialog.OnSelectResultListener;

import de.greenrobot.event.EventBus;

public class MyReleaseActiveAdapter extends NetJSONAdapter {

	LayoutInflater mLayoutInflater;

	int mResource;

	int piclayoutWidth;

	int headlayoutWidth;

	Dialog progressdialog;

	public IDialog dialoger;

	public MyReleaseActiveAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);

		mLayoutInflater = LayoutInflater.from(context);
		this.mResource = mResource;
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		int width = display.getWidth();
		piclayoutWidth = width
				- DhUtil.dip2px(context, 46 + 3 + 3 + 6 + 10 + 12);
		headlayoutWidth = piclayoutWidth - DhUtil.dip2px(context, 77)
				- DhUtil.dip2px(context, 10) - DhUtil.dip2px(context, 8) * 2;
		EventBus.getDefault().register(this);
		dialoger = IocContainer.getShare().get(IDialog.class);
	}

	@Override
	public int getItemViewType(int position) {
		JSONObject jo = mVaules.get(position);
		int piccount = JSONUtil.getJSONArray(jo, "cover").length();
		return piccount;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.addressT = (TextView) convertView.findViewById(R.id.address);
			holder.contentT = (TextView) convertView.findViewById(R.id.content);
			holder.date_leftT = (TextView) convertView
					.findViewById(R.id.date_left);
			holder.dateT = (TextView) convertView.findViewById(R.id.date);
			holder.joinT = (TextView) convertView.findViewById(R.id.join);
			holder.payTypeT = (TextView) convertView
					.findViewById(R.id.pay_type);
			holder.piclayoutV = (LinearLayout) convertView
					.findViewById(R.id.piclayout);
			holder.headlayoutV = (LinearLayout) convertView
					.findViewById(R.id.headlayout);
			holder.lineTopI = (ImageView) convertView
					.findViewById(R.id.line_top);
			convertView.setTag(holder);

			PicLayoutUtil picUtil = new PicLayoutUtil(mContext, type, 5,
					holder.piclayoutV, piclayoutWidth);
			picUtil.addMoreChild();

			PicLayoutUtil headUtil = new PicLayoutUtil(mContext, 5, 5,
					holder.headlayoutV, headlayoutWidth);
			headUtil.AddAllHead();
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		holder.lineTopI.setVisibility(position == 0 ? View.INVISIBLE
				: View.VISIBLE);

		final JSONObject jo = mVaules.get(position);
		JSONObject creater = JSONUtil.getJSONObject(jo, "organizer");
		ViewUtil.bindView(holder.contentT,
				JSONUtil.getString(jo, "introduction"));
		JSONArray picJsa = JSONUtil.getJSONArray(jo, "cover");
		// holder.piclayoutV.removeAllViews();
		PicLayoutUtil util = new PicLayoutUtil(mContext);
		util.BindImageView(holder.piclayoutV, picJsa);
		// util.addMoreChild();
		// holder.headlayoutV.removeAllViews();
		JSONArray headJsa = JSONUtil.getJSONArray(jo, "members");
		PicLayoutUtil headUtil = new PicLayoutUtil(mContext);
		headUtil.BindHeadImage(holder.headlayoutV, headJsa);

		if (JSONUtil.getLong(jo, "startDate") == 0) {
			ViewUtil.bindView(holder.dateT, "不确定");
		} else {
			ViewUtil.bindView(holder.dateT, JSONUtil.getLong(jo, "startDate"),
					"time");
		}
		ViewUtil.bindView(holder.addressT, JSONUtil.getString(jo, "location"));

		ViewUtil.bindView(holder.payTypeT, JSONUtil.getString(jo, "pay"));
		ViewUtil.bindView(holder.date_leftT,
				JSONUtil.getString(jo, "publishDate"));

		int isOrganizer = JSONUtil.getInt(jo, "isOrganizer");
		int isMember = JSONUtil.getInt(jo, "isMember");
		if (JSONUtil.getInt(jo, "isOver") == 0) {

			if (isOrganizer == 1) {
				holder.joinT.setText("管理");
				holder.joinT
						.setBackgroundResource(R.drawable.button_yanzheng_bg);
			} else {
				if (isMember == 1) {
					holder.joinT.setText("已加入");
					holder.joinT
							.setBackgroundResource(R.drawable.button_yanzheng_bg);
				} else if (isMember == 0) {
					holder.joinT.setText("我要去玩");
					holder.joinT
							.setBackgroundResource(R.drawable.button_yanzheng_bg);
				} else {
					holder.joinT.setText("申请中");
					holder.joinT
							.setBackgroundResource(R.drawable.btn_grey_dark_bg);
				}
			}

		} else {
			holder.joinT.setText("已结束");
			holder.joinT.setBackgroundResource(R.drawable.btn_grey_dark_bg);
		}
		holder.joinT.setVisibility(View.VISIBLE);
		final String activityId = JSONUtil.getString(jo, "activityId");
		final long startTime = JSONUtil.getLong(jo, "start");
		holder.joinT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User user = User.getInstance();
				if (user.isLogin) {
					Intent it;
					if (holder.joinT.getText().equals("管理")) {
						JSONObject shareJo = getShareContent(jo);
						it = new Intent(mContext,
								MyActiveMembersManageActivity.class);
						it.putExtra("activityId", activityId);
						it.putExtra("isJoin", true);
						it.putExtra("shareContent", shareJo.toString());
						mContext.startActivity(it);

					} else if (holder.joinT.getText().toString().equals("已加入")) {
						it = new Intent(mContext, ActiveMembersActivity.class);
						it.putExtra("activityId", activityId);
						it.putExtra("isJoin", true);
						mContext.startActivity(it);

					} else if (holder.joinT.getText().equals("我要去玩")) {
						isAuthen(activityId, jo);
					} else if (holder.joinT.getText().equals("申请中")) {

					} else {

					}
				} else {
					UserInfoManage.getInstance().checkLogin(
							(Activity) mContext, new LoginCallBack() {

								@Override
								public void onisLogin() {
									refreshDialog();
								}

								@Override
								public void onLoginFail() {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		});
		holder.headlayoutV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User user = User.getInstance();
				Intent it;
				if (user.isLogin()) {
					int isMember = JSONUtil.getInt(jo, "isMember");
					if (holder.joinT.getText().equals("管理")) {
						JSONObject shareJo = getShareContent(jo);
						it = new Intent(mContext,
								MyActiveMembersManageActivity.class);
						it.putExtra("activityId", activityId);
						it.putExtra("isJoin", isMember == 1 ? true : false);
						it.putExtra("shareContent", shareJo.toString());
						mContext.startActivity(it);
					} else {
						it = new Intent(mContext, ActiveMembersActivity.class);
						it.putExtra("activityId", activityId);
						it.putExtra("startTime", startTime);
						it.putExtra("isJoin", isMember == 1 ? true : false);
						mContext.startActivity(it);
					}
				} else {
					UserInfoManage.getInstance().checkLogin(
							(Activity) mContext, new LoginCallBack() {

								@Override
								public void onisLogin() {
									refreshDialog();
								}

								@Override
								public void onLoginFail() {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		});
		return convertView;
	}

	class ViewHolder {

		TextView contentT, addressT, payTypeT;

		LinearLayout piclayoutV, headlayoutV;

		TextView joinT, dateT, date_leftT;

		ImageView lineTopI;

	}

	public JSONObject getShareContent(JSONObject jo) {
		JSONArray picJsa = JSONUtil.getJSONArray(jo, "cover");
		JSONObject picjo;
		JSONObject shareJo = new JSONObject();
		try {
			picjo = picJsa.getJSONObject(0);
			String imgUrl = JSONUtil.getString(picjo, "thumbnail_pic");
			JSONObject userJo = JSONUtil.getJSONObject(jo, "organizer");
			String shareTitle = JSONUtil.getString(userJo, "nickname")
					+ "邀请您参加" + JSONUtil.getString(jo, "introduction") + "活动";
			long time = JSONUtil.getLong(jo, "start");
			String startTime = CarPlayValueFix.getStandardTime(time, "MM月dd日 ");
			String shareContent = "开始时间: " + startTime + "\n目的地: "
					+ JSONUtil.getString(jo, "location") + "\n费用: "
					+ JSONUtil.getString(jo, "pay");
			String shareUrl = API.share + "share.html?code="
					+ JSONUtil.getString(jo, "activityId");

			shareJo.put("imgUrl", imgUrl);
			shareJo.put("shareTitle", shareTitle);
			shareJo.put("shareContent", shareContent);
			shareJo.put("shareUrl", shareUrl);
			shareJo.put("activityId", JSONUtil.getString(jo, "activityId"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return shareJo;
	}

	/**
	 * 加入活动
	 */
	private void joinActive(String activityId, int seatCount,
			final JSONObject jo) {
		User user = User.getInstance();
		DhNet net = new DhNet(API.CWBaseurl + "/activity/" + activityId
				+ "/join?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.addParam("seat", seatCount);
		net.doPost(new NetTask(mContext) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (progressdialog != null) {
					progressdialog.dismiss();
				}
				if (response.isSuccess()) {
					IocContainer.getShare().get(IDialog.class)
							.showToastShort(mContext, "已提交加入活动申请,等待管理员审核!");
					try {
						jo.put("isMember", 2);
						notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void isAuthen(final String activityId, final JSONObject jo) {
		progressdialog = dialoger.showProgressDialog(mContext, "");
		User user = User.getInstance();
		DhNet mDhNet = new DhNet(API.availableSeat + user.getUserId()
				+ "/seats?token=" + user.getToken());
		mDhNet.doGet(new NetTask(mContext) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					JSONObject json = response.jSONFrom("data");
					try {
						User user = User.getInstance();
						user.setIsAuthenticated(json.getInt("isAuthenticated"));

						if (user.getIsAuthenticated() == 1) {
							if (progressdialog != null) {
								progressdialog.dismiss();
							}
							CarSeatSelectDialog dialog = new CarSeatSelectDialog(
									mContext, activityId);
							dialog.setOnSelectResultListener(new OnSelectResultListener() {

								@Override
								public void click(int seatCount) {
									progressdialog = dialoger
											.showProgressDialog(mContext,
													"申请加入中...");
									joinActive(activityId, seatCount, jo);
								}
							});

							dialog.show();
						} else {
							joinActive(activityId, 0, jo);

						}
						// 认证车主
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/** 接受加入或者退出活动事件 */
	public void onEventMainThread(JoinEB join) {
		if (mVaules != null && mVaules.size() != 0) {
			for (Iterator iterator = mVaules.iterator(); iterator.hasNext();) {
				JSONObject jo = (JSONObject) iterator.next();
				if (JSONUtil.getString(jo, "activityId").equals(
						join.getActivityId())) {
					try {
						jo.put("isMember", join.getIsMember());
						if (join.getIsMember() == 0) {
							JSONArray newjsa = new JSONArray();
							JSONArray headJsa = JSONUtil.getJSONArray(jo,
									"members");
							for (int i = 0; i < headJsa.length(); i++) {
								JSONObject headJo = (JSONObject) headJsa.get(i);
								if (!JSONUtil.getString(headJo, "userId")
										.equals(User.getInstance().getUserId())) {
									newjsa.put(headJo);
								}
							}
							jo.put("members", newjsa);
						}
						notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}
}
