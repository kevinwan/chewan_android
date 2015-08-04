package com.gongpingjia.carplay.activity.main;

import java.util.List;
import java.util.Stack;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.ioc.IocContainer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
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
		for (int i = 0; i < tabV.getChildCount(); i++) {
			View childV = tabV.getChildAt(i);
			ImageView img = (ImageView) childV.findViewById(R.id.img);
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
					UserInfoManage.getInstance().checkLogin(self,
							new LoginCallBack() {

								@Override
								public void onisLogin() {
									switchContent(MsgFragment.getInstance());
								}

								@Override
								public void onLoginFail() {
									// TODO Auto-generated method stub

								}
							});
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
									Intent it = new Intent(MainActivity.this,
											ManageAlbumActivity.class);
									startActivity(it);
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

}
