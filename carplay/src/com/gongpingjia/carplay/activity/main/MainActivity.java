package com.gongpingjia.carplay.activity.main;

import java.io.File;
import java.util.List;
import java.util.Stack;

import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.active.ActiveListFragment;
import com.gongpingjia.carplay.activity.active.CreateActiveActivity;
import com.gongpingjia.carplay.activity.msg.MsgFragment;
import com.gongpingjia.carplay.activity.my.ManageAlbumActivity;
import com.gongpingjia.carplay.activity.my.MyFragment;
import com.gongpingjia.carplay.activity.my.SettingActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.PhotoState;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.manage.UserInfoManage;
import com.gongpingjia.carplay.manage.UserInfoManage.LoginCallBack;
import com.gongpingjia.carplay.util.CarPlayPerference;
import com.gongpingjia.carplay.view.pop.ActiveFilterPop;

public class MainActivity extends BaseFragmentActivity {
	LinearLayout layout;

	LinearLayout tabV;

	FragmentManager fm;

	// Fragment 的栈
	public static Stack<Fragment> slist;

	ActiveFilterPop activeFilterPop;

	View titleBar;

	CarPlayPerference per;

	String tempPath;

	File mCacheDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		isAuthen();
		// updateApp();
	}

	public void initView() {
		slist = new Stack<Fragment>();
		fm = getSupportFragmentManager();
		tabV = (LinearLayout) findViewById(R.id.tab);
		activeFilterPop = ActiveFilterPop.getInstance(self);
		titleBar = findViewById(R.id.titlebar);
		initTab();
		setTab(0);

		per = IocContainer.getShare().get(CarPlayPerference.class);
		per.load();
		if (per.isShowMainGuilde == 0) {
			findViewById(R.id.guide).setVisibility(View.VISIBLE);
		}

		findViewById(R.id.know).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				per.load();
				per.isShowMainGuilde = 1;
				per.commit();
				findViewById(R.id.guide).setVisibility(View.GONE);
			}
		});
	}

	private void initTab() {
		for (int i = 0; i < tabV.getChildCount(); i++) {
			final int index = i;
			View childV = tabV.getChildAt(i);
			childV.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setTab(index);
				}
			});
		}
	}

	private void setTab(int index) {

		if (index == 1) {
			if (!User.getInstance().isLogin()) {
				UserInfoManage.getInstance().checkLogin(self,
						new LoginCallBack() {

							@Override
							public void onisLogin() {
								setTab(1);
							}

							@Override
							public void onLoginFail() {
							}
						});
			}
		}

		if (index == 1 && !User.getInstance().isLogin()) {
			return;
		}

		for (int i = 0; i < tabV.getChildCount(); i++) {
			View childV = tabV.getChildAt(i);
			final ImageView img = (ImageView) childV.findViewById(R.id.img);
			TextView text = (TextView) childV.findViewById(R.id.text);
			if (index == i) {
				text.setTextColor(getResources().getColor(
						R.color.text_blue_light));
				switch (index) {
				case 0:
					setTitle("同城");
					img.setImageResource(R.drawable.city_f);
					switchContent(ActiveListFragment.getInstance());
					setRightAction("创建活动", -1, new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							UserInfoManage.getInstance().checkLogin(self,
									new LoginCallBack() {

										@Override
										public void onisLogin() {
											Intent it = new Intent(
													MainActivity.this,
													CreateActiveActivity.class);
											startActivity(it);
										}

										@Override
										public void onLoginFail() {
											// TODO Auto-generated method stub

										}
									});
						}
					});
					setLeftAction(R.drawable.filtrate, "筛选",
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									activeFilterPop.show(titleBar);
								}
							});
					break;

				case 1:
					setTitle("消息");
					setRightVISIBLEOrGone(View.GONE);
					switchContent(MsgFragment.getInstance());

					img.setImageResource(R.drawable.msg_f);
					setLeftAction(-2, null, new OnClickListener() {

						@Override
						public void onClick(View arg0) {
						}
					});
					break;

				case 2:
					setTitle("我的");
					switchContent(MyFragment.getInstance());
					img.setImageResource(R.drawable.my_f);
					setLeftAction(R.drawable.icon_setting, null,
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent it = new Intent(MainActivity.this,
											SettingActivity.class);
									startActivity(it);
								}
							});
					setRightAction(null, R.drawable.icon_camera,
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									UserInfoManage.getInstance().checkLogin(
											self, new LoginCallBack() {

												@Override
												public void onisLogin() {
													// TODO Auto-generated
													// method stub
													mCacheDir = new File(
															getExternalCacheDir(),
															"CarPlay");
													mCacheDir.mkdirs();
													tempPath = new File(
															mCacheDir,
															System.currentTimeMillis()
																	+ ".jpg")
															.getAbsolutePath();
													PhotoUtil
															.getPhoto(
																	self,
																	Constant.TAKE_PHOTO,
																	Constant.PICK_PHOTO,
																	new File(
																			tempPath));

												}

												@Override
												public void onLoginFail() {

												}
											});
								}
							});
					break;

				default:
					break;

				}
			} else {
				text.setTextColor(getResources().getColor(R.color.text_grey));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.city_n);
					break;

				case 1:
					img.setImageResource(R.drawable.msg_n);
					break;

				case 2:
					img.setImageResource(R.drawable.my_n);
					break;

				default:
					break;
				}
			}
		}
	}

	public void switchContent(Fragment fragment) {
		try {
			FragmentTransaction t = fm.beginTransaction();
			List<Fragment> flist = fm.getFragments();
			if (flist == null) {
				t.add(R.id.main_content, fragment);
			} else {
				if (!flist.contains(fragment)) {
					t.add(R.id.main_content, fragment);
				}
				t.hide(slist.get(slist.size() - 1));
				t.show(fragment);
			}

			if (slist.contains(fragment)) {
				slist.remove(fragment);
			}

			slist.add(fragment);

			t.commitAllowingStateLoss();

		} catch (Exception e) {
		}
	}

	private void isAuthen() {
		User user = User.getInstance();
		if (user.isLogin()) {
			DhNet mDhNet = new DhNet(API.availableSeat + user.getUserId()
					+ "/seats?token=" + user.getToken());
			mDhNet.doGet(new NetTask(self) {

				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					if (response.isSuccess()) {
						JSONObject json = response.jSONFrom("data");
						try {
							User user = User.getInstance();
							user.setIsAuthenticated(json
									.getInt("isAuthenticated"));
							// 认证车主
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	public void updateApp() {
		final String mCurrentVersion = getAppVersion();
		DhNet net = new DhNet(API.update);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSON();
					String version = JSONUtil.getString(jo, "version");
					if (0 < version.compareTo(mCurrentVersion)) {
						showUpdateDialog(jo);
					}
				}
			}
		});
	}

	private String getAppVersion() {
		String versionName = null;
		try {
			String pkName = this.getPackageName();
			versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;

		} catch (Exception e) {
			return null;
		}
		return versionName;
	}

	private void showUpdateDialog(final JSONObject jo) {
		Builder builder = new Builder(this);
		builder.setTitle("发现新版本 " + JSONUtil.getString(jo, "version"));
		builder.setMessage(JSONUtil.getString(jo, "remarks"));
		builder.setPositiveButton("立即更新",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it = new Intent(Intent.ACTION_VIEW);
						Uri uri = Uri.parse(JSONUtil.getString(jo, "url"));
						it.setData(uri);
						startActivity(it);
					}

				});
		builder.setNegativeButton("以后再说",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.TAKE_PHOTO:
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				String path = PhotoUtil.onPhotoFromCamera(self,
						Constant.ZOOM_PIC, tempPath, 3, 2, 1000, newPath);
				tempPath = path;
				break;
			case Constant.PICK_PHOTO:
				PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, tempPath,
						data, 3, 2, 1000);
				break;
			case Constant.ZOOM_PIC:
				upLoadPic(tempPath);
				break;

			// case Constant.PICK_PHOTO:
			// Bitmap btp = PhotoUtil.checkImage(self, data);
			// PhotoUtil.saveLocalImage(btp, new File(mCurPath));
			// btp.recycle();
			// upLoadPic(mCurPath);
			// break;
			// case Constant.TAKE_PHOTO:
			// Bitmap btp1 = PhotoUtil.getLocalImage(new File(mCurPath));
			// String newPath = new File(mCacheDir, System.currentTimeMillis()
			// + ".jpg").getAbsolutePath();
			// int degree = PhotoUtil.getBitmapDegree(mCurPath);
			// PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
			// btp1.recycle();
			// upLoadPic(newPath);
			// break;
			}
		}
	}

	private void upLoadPic(String path) {
		User user = User.getInstance();
		DhNet net = new DhNet(API.uploadAlbum + user.getUserId()
				+ "/album/upload?token=" + user.getToken());
		Log.e("url", net.getUrl());
		net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					IocContainer.getShare().get(IDialog.class)
							.showToastShort(self, "图片上传成功!");
					Intent it = new Intent(MainActivity.this,
							ManageAlbumActivity.class);
					it.putExtra("tempPath", tempPath);
					startActivity(it);
				}
			}
		});
	}

}
