package com.gongpingjia.carplay.activity.my;

import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.DotLinLayout;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.activity.active.ActiveDetailsActivity;
import com.gongpingjia.carplay.activity.active.CreateActiveActivity;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.gongpingjia.carplay.adapter.ActiveAdapter;
import com.gongpingjia.carplay.adapter.GalleryAdapter;
import com.gongpingjia.carplay.adapter.MyReleaseActiveAdapter;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.view.CarPlayGallery;
import com.gongpingjia.carplay.view.RoundImageView;

public class PersonDetailActivity extends CarPlayBaseActivity implements
		OnClickListener {
	LinearLayout tab;

	View releaseV, attentionV, joinV;

	LayoutInflater mLayoutInflater;

	NetRefreshAndMoreListView listV;

	MyReleaseActiveAdapter releaseAdapter;

	ActiveAdapter attentionAdapter, joinAdapter;

	User user;

	String userId;

	View headV;

	RoundImageView headI;

	DotLinLayout dotLinLayout;

	CarPlayGallery gallery;

	TextView attentionT;

	public static final int RELEASE = 1;

	public static final int JOIN = 2;

	public static final int ATTENTION = 3;

	View emptyV;

	int type;

	int currentPosition = 200;

	int galleryCount;

	Timer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail);
	}

	@Override
	public void initView() {
		setTitle("他的详情");
//		setRightAction("举报", -1, new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				UserInfoManage.getInstance().checkLogin(self,
//						new LoginCallBack() {
//							@Override
//							public void onisLogin() {
//							}
//
//							@Override
//							public void onLoginFail() {
//							}
//						});
		// }
		// });
		user = User.getInstance();
		userId = getIntent().getStringExtra("userId");
		headV = LayoutInflater.from(self).inflate(R.layout.head_user_details,
				null);
		tab = (LinearLayout) headV.findViewById(R.id.tab);
		headI = (RoundImageView) headV.findViewById(R.id.head);
		emptyV = headV.findViewById(R.id.empty);
		listV = (NetRefreshAndMoreListView) findViewById(R.id.listview);
		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = null;
				JSONObject jo = null;
				if (type == RELEASE) {
					jo = (JSONObject) releaseAdapter.getItem(position
							- listV.getHeaderViewsCount());
				} else if (type == ATTENTION) {
					jo = (JSONObject) attentionAdapter.getItem(position
							- listV.getHeaderViewsCount());
				} else {
					jo = (JSONObject) joinAdapter.getItem(position
							- listV.getHeaderViewsCount());
				}
				it = new Intent(self, ActiveDetailsActivity.class);
				it.putExtra("activityId", JSONUtil.getString(jo, "activityId"));
				startActivity(it);

			}
		});
		listV.addHeaderView(headV);
		gallery = (CarPlayGallery) headV.findViewById(R.id.gallery);
		dotLinLayout = (DotLinLayout) headV.findViewById(R.id.dots);
		dotLinLayout.setDotImage(R.drawable.dot_n, R.drawable.dot_f);

		attentionT = (TextView) headV.findViewById(R.id.attention);
		attentionT.setOnClickListener(this);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(self, ManageAlbumActivity.class);
				startActivity(it);
			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position >= galleryCount) {
					if (galleryCount != 0) {
						position = position % galleryCount;
					}
				}
				dotLinLayout.setCurrentFocus(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		getPersonData();
		buildAdapter();
		initTopTab();
		setTab(0);
	}

	@Override
	public void onResume() {
		super.onResume();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 0, 10 * 1000);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	private void initTopTab() {
		for (int i = 0; i < tab.getChildCount(); i = i + 2) {
			final int index = i;
			View childV = tab.getChildAt(i);
			childV.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setTab(index);
					switch (index) {
					case 0:
						listV.setAdapter(releaseAdapter);
						type = RELEASE;
						bindEmptyView(emptyV, type);
						break;

					case 2:
						listV.setAdapter(attentionAdapter);
						type = ATTENTION;
						bindEmptyView(emptyV, type);
						break;

					case 4:
						listV.setAdapter(joinAdapter);
						type = JOIN;
						bindEmptyView(emptyV, type);
						break;

					default:
						break;
					}
					if (listV.getAdapter().getCount() == 3) {
						emptyV.setVisibility(View.VISIBLE);
					} else {
						emptyV.setVisibility(View.GONE);
					}
				}

			});
		}

	}

	private void getPersonData() {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + userId
				+ "/info?userId=" + user.getUserId() + "&token="
				+ user.getToken());
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {

					JSONObject jo = response.jSONFromData();
					int isSubscribed = JSONUtil.getInt(jo, "isSubscribed");
					ViewUtil.bindView(attentionT, isSubscribed == 1 ? "取消关注"
							: "关注");
					ViewUtil.bindNetImage(headI,
							JSONUtil.getString(jo, "photo"), "head");
					ViewUtil.bindView(headV.findViewById(R.id.nickname),
							JSONUtil.getString(jo, "nickname"));
					ViewUtil.bindView(
							headV.findViewById(R.id.drive_age),
							JSONUtil.getString(jo, "carModel")
									+ ","
									+ JSONUtil.getString(jo,
											"drivingExperience") + "年驾龄");
					ViewUtil.bindView(headV.findViewById(R.id.content),
							JSONUtil.getString(jo, "introduction"));

					ViewUtil.bindNetImage(
							(ImageView) headV.findViewById(R.id.car_logo),
							JSONUtil.getString(jo, "carBrandLogo"), "carlogo");

					ViewUtil.bindView(headV.findViewById(R.id.releaseCount),
							JSONUtil.getString(jo, "postNumber"));
					ViewUtil.bindView(headV.findViewById(R.id.attention_count),
							JSONUtil.getString(jo, "subscribeNumber"));
					ViewUtil.bindView(headV.findViewById(R.id.active_count),
							JSONUtil.getString(jo, "joinNumber"));

					JSONArray albumPhotosJsa = JSONUtil.getJSONArray(jo,
							"albumPhotos");
					bingGallery(albumPhotosJsa);
				}
			}
		});
	}

	private void buildAdapter() {
		releaseAdapter = new MyReleaseActiveAdapter(API.CWBaseurl + "/user/"
				+ userId + "/post?userId=" + user.getUserId() + "&token="
				+ user.getToken(), self, R.layout.item_myrelease_active);
		releaseAdapter.fromWhat("data");
		listV.setAdapter(releaseAdapter);
		releaseAdapter.showNextInDialog();

		attentionAdapter = new ActiveAdapter(API.CWBaseurl + "/user/" + userId
				+ "/subscribe?userId=" + user.getUserId() + "&token="
				+ user.getToken(), self, R.layout.item_active_list);

		attentionAdapter.fromWhat("data");
		attentionAdapter.showNext();

		joinAdapter = new ActiveAdapter(API.CWBaseurl + "/user/" + userId
				+ "/join?userId=" + user.getUserId() + "&token="
				+ user.getToken(), self, R.layout.item_active_list);

		joinAdapter.fromWhat("data");
		joinAdapter.showNext();
	}

	private void bingGallery(JSONArray jsa) {
		galleryCount = jsa.length();
		if (galleryCount > 0) {
			dotLinLayout.setDotCount(galleryCount);
			dotLinLayout.setCurrentFocus(galleryCount / 2);
			gallery.setSelection(galleryCount / 2);
		}
		GalleryAdapter adapter = new GalleryAdapter(self, jsa);
		gallery.setAdapter(adapter);
		gallery.setSelection(200);
		currentPosition = 200;
	}

	private void setTab(int index) {
		for (int i = 0; i < tab.getChildCount(); i = i + 2) {
			LinearLayout childV = (LinearLayout) tab.getChildAt(i);
			View img = (View) childV.findViewById(R.id.tabline);
			TextView numText = (TextView) childV.getChildAt(0);
			TextView desText = (TextView) childV.getChildAt(1);
			if (index == i) {
				img.setVisibility(View.VISIBLE);
				numText.setTextColor(getResources().getColor(
						R.color.text_orange));
				desText.setTextColor(getResources().getColor(
						R.color.text_orange));
			} else {
				img.setVisibility(View.GONE);
				numText.setTextColor(getResources().getColor(R.color.text_grey));
				desText.setTextColor(getResources().getColor(R.color.text_grey));
			}
		}
	}

	/** 关注人 */
	private void attention(String userid) {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/listen?&token=" + user.getToken());
		net.addParam("targetUserId", userid);
		net.doPost(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("关注成功!");
					attentionT.setText("取消关注");
				}
			}
		});
	}

	/** 取消关注人 */
	private void cancleAttention(String userid) {
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/unlisten?&token=" + user.getToken());
		net.addParam("targetUserId", userid);
		net.doPost(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("取消关注成功!");
					attentionT.setText("关注");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.attention:
			if (attentionT.getText().toString().equals("关注")) {
				attention(userId);
			} else {
				cancleAttention(userId);
			}
			break;

		default:
			break;
		}
	}

	private void bindEmptyView(View emptyV, final int type) {

		switch (type) {
		case RELEASE:
			// ViewUtil.bindView(findViewById(R.id.btn_submit), "发布活动");
			ViewUtil.bindView(findViewById(R.id.icon_msg),
					R.drawable.no_release);
			ViewUtil.bindView(findViewById(R.id.msg), "您还没有发布任何活动,赶紧添加吧!");
			break;

		case JOIN:
			// ViewUtil.bindView(findViewById(R.id.btn_submit), "添加关注");
			ViewUtil.bindView(findViewById(R.id.icon_msg),
					R.drawable.no_attention);
			ViewUtil.bindView(findViewById(R.id.msg), "您还没有添加任何关注,赶紧添加吧!");
			break;

		case ATTENTION:
			// ViewUtil.bindView(findViewById(R.id.btn_submit), "参与活动");
			ViewUtil.bindView(findViewById(R.id.icon_msg), R.drawable.no_join);
			ViewUtil.bindView(findViewById(R.id.msg), "您还没有参与任何活动,赶紧参与吧!");
			break;

		default:
			break;
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			currentPosition = currentPosition + 1;
			gallery.setSelection(currentPosition);
		};
	};
}
