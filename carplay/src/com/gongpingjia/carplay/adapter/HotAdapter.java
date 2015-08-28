package com.gongpingjia.carplay.adapter;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

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
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity;
import com.gongpingjia.carplay.activity.active.ActiveMembersActivity;
import com.gongpingjia.carplay.activity.active.MyActiveMembersManageActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.JoinEB;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.CarPlayUtil;
import com.gongpingjia.carplay.util.PicLayoutUtil;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.HotGallery;
import com.gongpingjia.carplay.view.RoundImageView;
import com.gongpingjia.carplay.view.dialog.CarSeatSelectDialog;
import com.gongpingjia.carplay.view.dialog.CarSeatSelectDialog.OnSelectResultListener;

import de.greenrobot.event.EventBus;

public class HotAdapter extends NetJSONAdapter {
	LayoutInflater mLayoutInflater;

	int mResource;

	int piclayoutWidth;

	int headlayoutWidth;

	ViewHolder holder = null;

	MiddleViewHolder middleHolder;

	View[] views;

	public boolean isRefresh = false;

	JSONArray jsa = new JSONArray();

	Timer galleryTimer;

	int currentPosition = 0;

	Dialog progressdialog;

	public IDialog dialoger;

	public HotAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mResource = mResource;
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		int width = display.getWidth();
		piclayoutWidth = width - DhUtil.dip2px(context, 59 + 12 * 2 + 10);
		headlayoutWidth = piclayoutWidth
				- DhUtil.dip2px(context, 77 + 10 + 8 * 2);

		EventBus.getDefault().register(this);
		dialoger = IocContainer.getShare().get(IDialog.class);
	}

	public void setJsa(JSONArray j) {
		isRefresh = true;
		jsa = j;
		notifyDataSetChanged();
	}

	@Override
	public JSONObject getItem(int position) {
		JSONObject jo = null;
		if (jsa != null && jsa.length() > 0) {
			if (position > 1) {
				jo = mVaules.get(position - 1);
			} else {
				if (position == 1) {
					jo = null;
				} else {
					jo = mVaules.get(position);
				}
			}
		} else {
			jo = mVaules.get(position);
		}

		return jo;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public int getItemViewType(int position) {
		int count = 0;
		if (position == 1) {
			if (jsa.length() > 0) {
				count = 0;
			} else {
				JSONObject jo = mVaules.get(position);
				count = JSONUtil.getJSONArray(jo, "cover").length();
			}
		} else {
			if (position == 0) {
				JSONObject jo = mVaules.get(position);
				count = JSONUtil.getJSONArray(jo, "cover").length();
			} else {
				JSONObject jo1 = mVaules.get(position - 1);
				count = JSONUtil.getJSONArray(jo1, "cover").length();
			}
		}

		return count;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getCount() {
		if (mVaules != null) {

			if (jsa != null && jsa.length() > 0 && mVaules.size() != 0) {
				return mVaules.size() + 1;
			}
			return mVaules.size();
			// currentPosition = 0;
		} else {
			isRefresh = true;
			// currentPosition = 0;
			return 0;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		// TODO Auto-generated method stub
		if (convertView == null) {
			if (type == 0) {
				convertView = mLayoutInflater.inflate(
						R.layout.item_hotlistview, null);
				middleHolder = new MiddleViewHolder();
				middleHolder.pager = (ViewPager) convertView
						.findViewById(R.id.page);
				// middleHolder.pager.setVisibility(View.GONE);
				convertView.setTag(middleHolder);
			} else {
				convertView = mLayoutInflater.inflate(mResource, null);
				holder = new ViewHolder();
				holder.addressT = (TextView) convertView
						.findViewById(R.id.address);
				holder.statusT = (TextView) convertView
						.findViewById(R.id.status);
				holder.seat_countT = (TextView) convertView
						.findViewById(R.id.seat_count);
				holder.nameT = (TextView) convertView.findViewById(R.id.name);
				holder.tv_publish_timeT = (TextView) convertView
						.findViewById(R.id.tv_publish_time);
				holder.contentT = (TextView) convertView
						.findViewById(R.id.content);
				holder.timeT = (TextView) convertView.findViewById(R.id.time);
				holder.joinT = (TextView) convertView.findViewById(R.id.join);
				holder.headI = (RoundImageView) convertView
						.findViewById(R.id.head);
				holder.car_logoI = (ImageView) convertView
						.findViewById(R.id.car_logo);
				holder.piclayoutV = (LinearLayout) convertView
						.findViewById(R.id.piclayout);
				holder.headlayoutV = (LinearLayout) convertView
						.findViewById(R.id.headlayout);
				holder.layout_sexV = convertView.findViewById(R.id.layout_sex);
				holder.ageT = (TextView) convertView.findViewById(R.id.age);
				holder.drive_ageT = (TextView) convertView
						.findViewById(R.id.drive_age);
				holder.seat_count_allT = (TextView) convertView
						.findViewById(R.id.seat_count_all);
				holder.member_layoutV = convertView
						.findViewById(R.id.member_layout);
				convertView.setTag(holder);
				PicLayoutUtil picUtil = new PicLayoutUtil(mContext, type, 5,
						holder.piclayoutV, piclayoutWidth);
				picUtil.addMoreChild();

				PicLayoutUtil headUtil = new PicLayoutUtil(mContext, 5, 5,
						holder.headlayoutV, headlayoutWidth);
				headUtil.AddAllHead();
			}
		} else {
			if (type == 0) {
				middleHolder = (MiddleViewHolder) convertView.getTag();
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		}

		if (type == 0) {
			if (isRefresh) {
				bindViewPageAdapter();
			} else if ((jsa != null && jsa.length() > 0)
					&& (middleHolder.pager != null && middleHolder.pager
							.getChildCount() == 0)) {
				bindViewPageAdapter();
			}

		} else {
			final JSONObject jo = getItem(position);
			if (jo != null) {
				final JSONObject creater = JSONUtil.getJSONObject(jo,
						"organizer");

				int isOrganizer = JSONUtil.getInt(jo, "isOrganizer");
				int isMember = JSONUtil.getInt(jo, "isMember");
				final long startTime = JSONUtil.getLong(jo, "start");

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
					holder.joinT
							.setBackgroundResource(R.drawable.btn_grey_dark_bg);
				}
				holder.joinT.setVisibility(View.VISIBLE);

				final String activityId = JSONUtil.getString(jo, "activityId");
				holder.joinT.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						User user = User.getInstance();
						if (user.isLogin) {
							Intent it;
							int isOrganizer = JSONUtil
									.getInt(jo, "isOrganizer");
							int isMember = JSONUtil.getInt(jo, "isMember");
							if (isOrganizer == 1) {
								JSONObject shareJo = getShareContent(jo);
								it = new Intent(mContext,
										MyActiveMembersManageActivity.class);
								it.putExtra("activityId", activityId);
								it.putExtra("isJoin", true);
								it.putExtra("shareContent", shareJo.toString());
								mContext.startActivity(it);

							} else if (isMember == 1) {
								it = new Intent(mContext,
										ActiveMembersActivity.class);
								it.putExtra("activityId", activityId);
								it.putExtra("isJoin", true);
								mContext.startActivity(it);

							} else if (isMember == 0) {
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
							int isOrganizer = JSONUtil
									.getInt(jo, "isOrganizer");
							if (isOrganizer == 1) {
								JSONObject shareJo = getShareContent(jo);
								it = new Intent(mContext,
										MyActiveMembersManageActivity.class);
								it.putExtra("activityId", activityId);
								it.putExtra("isJoin", isMember == 1 ? true
										: false);
								it.putExtra("shareContent", shareJo.toString());
								mContext.startActivity(it);
							} else {
								it = new Intent(mContext,
										ActiveMembersActivity.class);
								it.putExtra("activityId", activityId);
								it.putExtra("startTime", startTime);
								it.putExtra("isJoin", isMember == 1 ? true
										: false);
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
				ViewUtil.bindView(holder.nameT,
						JSONUtil.getString(creater, "nickname"));
				if (JSONUtil.getString(creater, "gender").equals("男")) {
					holder.layout_sexV.setBackgroundResource(R.drawable.man);
				} else {
					holder.layout_sexV.setBackgroundResource(R.drawable.woman);
				}

				ViewUtil.bindView(holder.ageT,
						JSONUtil.getString(creater, "age"));
				ViewUtil.bindView(holder.tv_publish_timeT,
						JSONUtil.getString(jo, "publishTime"), "neartime");

				CarPlayUtil.bindDriveAge(creater, holder.car_logoI,
						holder.drive_ageT);

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

				if (JSONUtil.getLong(jo, "start") == 0) {
					ViewUtil.bindView(holder.timeT, "不确定");
				} else {
					ViewUtil.bindView(holder.timeT,
							JSONUtil.getLong(jo, "start"), "time");
				}
				ViewUtil.bindView(holder.addressT,
						JSONUtil.getString(jo, "location"));
				ViewUtil.bindNetImage(holder.headI,
						JSONUtil.getString(creater, "photo"), "head");
				holder.headI.setTag(JSONUtil.getString(creater, "userId"));

				ViewUtil.bindView(holder.statusT, JSONUtil.getString(jo, "pay"));
				String pay = JSONUtil.getString(jo, "pay");
				holder.statusT.setVisibility(View.VISIBLE);
				if (pay.equals("我请客")) {
					holder.statusT
							.setBackgroundResource(R.drawable.button_radian);
				} else if (pay.equals("请我吧")) {
					holder.statusT.setBackgroundResource(R.drawable.grey_bg);
				} else {
					holder.statusT
							.setBackgroundResource(R.drawable.blue_light_bg);
				}

				if (JSONUtil.getString(jo, "holdingSeat").equals(
						JSONUtil.getString(jo, "totalSeat"))) {
					ViewUtil.bindView(holder.seat_countT, "已满");
					holder.seat_count_allT.setVisibility(View.GONE);
				} else {
					holder.seat_count_allT.setVisibility(View.VISIBLE);
					ViewUtil.bindView(holder.seat_countT,
							JSONUtil.getString(jo, "holdingSeat"));
					ViewUtil.bindView(holder.seat_count_allT,
							"/" + JSONUtil.getString(jo, "totalSeat") + "座");
				}
			}

		}

		return convertView;
	}

	class ViewHolder {
		RoundImageView headI;

		ImageView car_logoI;

		TextView statusT, seat_countT, nameT, tv_publish_timeT;

		TextView contentT, timeT, addressT, joinT;

		LinearLayout piclayoutV, headlayoutV;

		View layout_sexV;

		TextView ageT, drive_ageT, seat_count_allT;

		View member_layoutV;

	}

	class MiddleViewHolder {
		ViewPager pager;

		View[] views;

		TextView joinT;

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

	/** 官方活动 */
	private void bindViewPageAdapter() {
		isRefresh = false;
		// if (jsa != null && jsa.length() > 0) {
		// HotActiveAdapter adapter = new HotActiveAdapter(mContext, jsa);
		// middleHolder.pager.setAdapter(adapter);
		// }
		if (jsa != null && jsa.length() > 0) {
			views = new View[jsa.length()];
			for (int i = 0; i < jsa.length(); i++) {
				final JSONObject jot;
				try {
					jot = jsa.getJSONObject(i);
					View leftV = mLayoutInflater.inflate(
							R.layout.item_hotview_viewpage, null);

					ImageView pghead = (ImageView) leftV
							.findViewById(R.id.head);
					ImageView pglogo = (ImageView) leftV
							.findViewById(R.id.logo);
					TextView pgtitle = (TextView) leftV
							.findViewById(R.id.title);
					TextView pgendtime = (TextView) leftV
							.findViewById(R.id.endtime);
					TextView pgcontent = (TextView) leftV
							.findViewById(R.id.content);

					SimpleDateFormat formatdate = new SimpleDateFormat(
							"yyyy.MM.dd");
					pgendtime.setText("报名截止:  "
							+ formatdate.format(JSONUtil.getLong(jot, "end")));
					pgtitle.setText(JSONUtil.getString(jot, "title"));
					pgcontent.setText(JSONUtil.getString(jot, "content"));
					ViewUtil.bindNetImage(pghead,
							JSONUtil.getString(jot, "cover"), "cover");
					ViewUtil.bindNetImage(pglogo,
							JSONUtil.getString(jot, "logo"), "logo");

					leftV.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(mContext,
									ActiveDetailsActivity.class);
							intent.putExtra("activityId",
									JSONUtil.getString(jot, "activityId"));
							mContext.startActivity(intent);
						}
					});
					views[i] = leftV;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			SimplePageAdapter middleAdapter = new SimplePageAdapter(views);
			middleHolder.pager.setAdapter(middleAdapter);
			middleHolder.pager.setCurrentItem(0);
			startTimer();
			middleHolder.pager
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int arg0) {
							// if (arg0 == 0)
							// middleHolder.pager
							// .setCurrentItem(1);
							// else if (arg0 == views.length - 1)
							// middleHolder.pager.setCurrentItem(1);
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
							// TODO Auto-generated method stub

						}
					});
			// middleHolder.pager.setVisibility(View.VISIBLE);
		}
	}

	public void startTimer() {
		stopTimer();
		galleryTimer = new Timer();
		galleryTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 5 * 1000, 5 * 1000);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			int positon = currentPosition % jsa.length();
			middleHolder.pager.setCurrentItem(positon);
			// middleHolder.pager.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		};
	};

	public void stopTimer() {
		if (galleryTimer != null) {
			galleryTimer.cancel();
		}
	}
}
