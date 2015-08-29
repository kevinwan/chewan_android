package com.gongpingjia.carplay.util;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.DhUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.LargePICActivity;
import com.gongpingjia.carplay.view.ImageGallery;
import com.gongpingjia.carplay.view.RoundImageView;

public class PicLayoutUtil {
	LayoutParams params;

	JSONArray data;

	Context mContext;

	int count;

	int padding;

	LinearLayout layout;

	OnChildClickListener onChildClickListener;

	int width;

	int horizontalMax = 3;

	int headMax = 5;

	public int MorePic = 1;

	public int ThreePic = 2;

	/** type为1时表示直接加载,适用于详情页面,type==2表示只添加空的ImageView,不加载网络图片,用于adapter中 */
	int type;

	public static JSONArray picjsa;

	/** 直接加载网络图片 */
	public PicLayoutUtil(Context context, JSONArray jsa, int padding,
			LinearLayout layout, int width) {
		this.data = jsa;
		this.count = jsa.length();
		this.padding = DhUtil.dip2px(context, padding);
		this.layout = layout;
		this.mContext = context;
		this.width = width;
		this.type = 1;
	}

	/** 只添加Image,不加载图片 */
	public PicLayoutUtil(Context context, int count, int padding,
			LinearLayout layout, int width) {
		this.count = count;
		this.padding = DhUtil.dip2px(context, padding);
		this.layout = layout;
		this.mContext = context;
		this.width = width;
		this.type = 2;
	}

	/** 设置最多头像数量 */
	public void setHeadMaxCount(int count) {
		this.headMax = count;
	}

	public PicLayoutUtil(Context context) {
		this.mContext = context;
	}

	/**
	 * 添加多个用户头像
	 * 
	 */
	public void AddChild() {
		int drawCount;
		int childWidth = (width - (headMax - 1) * padding) / headMax;
		params = new LayoutParams(childWidth, childWidth);
		params.rightMargin = padding;
		if (count >= headMax) {
			drawCount = headMax;
		} else {
			drawCount = count + 1;
		}

		// if (drawCount == headMax) {
		for (int i = 0; i < drawCount; i++) {
			try {
				if (i == drawCount - 1) {
					layout.addView(createCountTextView(count), params);
				} else {
					layout.addView(createHeadImageView(data.getJSONObject(i)),
							params);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// } else {
		// for (int i = 0; i < drawCount; i++) {
		// try {
		// layout.addView(createHeadImageView(data.getJSONObject(i)),
		// params);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

	}

	/**
	 * 添加5个用户头像(不加载图片)
	 * 
	 */
	public void AddAllHead() {
		int childWidth = (width - (headMax - 1) * padding) / headMax;
		params = new LayoutParams(childWidth, childWidth);
		params.rightMargin = padding;
		for (int i = 0; i < headMax; i++) {
			if (i == headMax - 1) {
				layout.addView(createCountTextView(), params);
			} else {
				layout.addView(createHeadImageView(null), params);
			}
		}
	}

	private TextView createCountTextView(int count) {
		TextView text = new TextView(mContext);
		text.setBackgroundResource(R.drawable.head_grey);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(mContext.getResources().getColor(R.color.white));
		text.setText(count + "");
		return text;
	}

	private TextView createCountTextView() {
		TextView text = new TextView(mContext);
		text.setBackgroundResource(R.drawable.head_grey);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(mContext.getResources().getColor(R.color.white));
		text.setVisibility(View.GONE);
		return text;
	}

	/** 加载网络头像 */
	public void BindHeadImage(LinearLayout layout, JSONArray jsa) {
		int drawcount;

		if (jsa.length() >= headMax) {
			drawcount = headMax;
		} else {
			drawcount = jsa.length() + 1;
		}

		// if (drawcount == headMax) {
		for (int i = 0; i < headMax; i++) {
			try {
				if (i == headMax - 1) {
					TextView text = (TextView) layout.getChildAt(headMax - 1);
					text.setText(jsa.length() + "");
					text.setVisibility(View.VISIBLE);
				} else {
					RoundImageView img = (RoundImageView) layout.getChildAt(i);
					img.setVisibility(View.GONE);
					if (i < drawcount - 1) {
						JSONObject jo = jsa.getJSONObject(i);
						ViewUtil.bindNetImage((RoundImageView) img,
								JSONUtil.getString(jo, "photo"), "head");
						img.setTag(JSONUtil.getString(jo, "userId"));
						img.setVisibility(View.VISIBLE);
					}
				}
				// JSONObject jo = jsa.getJSONObject(i);
				// ViewUtil.bindNetImage(img, JSONUtil.getString(jo,
				// "photo"),
				// "head");
				// img.setTag(JSONUtil.getString(jo, "userId"));
				// img.setVisibility(View.VISIBLE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// } else {
		// for (int i = 0; i < headMax; i++) {
		// try {
		// View img = (View) layout.getChildAt(i);
		// img.setVisibility(View.GONE);
		// for (int j = 0; j < jsa.length(); j++) {
		// JSONObject jo = jsa.getJSONObject(i);
		// ViewUtil.bindNetImage((RoundImageView) img,
		// JSONUtil.getString(jo, "photo"), "head");
		// img.setTag(JSONUtil.getString(jo, "userId"));
		// img.setVisibility(View.VISIBLE);
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

	}

	/** 加载网络图片 */
	public void BindImageView(LinearLayout layout, JSONArray jsa) {

		int lastChildCount = 0;
		for (int i = 0; i < layout.getChildCount(); i++) {
			LinearLayout lc = (LinearLayout) layout.getChildAt(i);
			if (i == 0) {
				try {
					lastChildCount = lc.getChildCount();
					for (int j = 0; j < lc.getChildCount(); j++) {
						if (j == 0) {
							JSONObject jo = jsa.getJSONObject(j);
							ImageView img = (ImageView) lc.getChildAt(j);
							img.setBackgroundResource(R.drawable.img_loading);
							ViewUtil.bindNetImage(img,
									JSONUtil.getString(jo, "thumbnail_pic"),
									"bigPicOptions");
							largePic(img, jsa, j);
						} else {
							JSONObject jo = jsa.getJSONObject(j);
							ImageView img = (ImageView) lc.getChildAt(j);
							ViewUtil.bindNetImage(img,
									JSONUtil.getString(jo, "thumbnail_pic"),
									"default");
							largePic(img, jsa, j);
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (i == 1) {
				try {
					for (int j = 0; j < lc.getChildCount(); j++) {
						JSONObject jo = jsa.getJSONObject(lastChildCount + j);
						ImageView img = (ImageView) lc.getChildAt(j);
						ViewUtil.bindNetImage(img,
								JSONUtil.getString(jo, "thumbnail_pic"),
								"default");
						largePic(img, jsa, lastChildCount + j);
					}
					lastChildCount += lc.getChildCount();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (i == 2) {
				try {
					for (int j = 0; j < lc.getChildCount(); j++) {
						JSONObject jo = jsa.getJSONObject(lastChildCount + j);
						ImageView img = (ImageView) lc.getChildAt(j);
						ViewUtil.bindNetImage(img,
								JSONUtil.getString(jo, "thumbnail_pic"),
								"default");
						largePic(img, jsa, lastChildCount + j);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/** 进入大图页面 */
	private void largePic(ImageView img, final JSONArray jsa, final int index) {
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String[] imgUrls = new String[jsa.length()];
				for (int i = 0, len = jsa.length(); i < len; i++) {
					try {
						imgUrls[i] = jsa.getJSONObject(i).getString(
								"original_pic");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Intent it = new Intent(mContext, ImageGallery.class);
				it.putExtra("imgurls", imgUrls);
				it.putExtra("currentItem", index);
				mContext.startActivity(it);

				// Intent it;
				// it = new Intent(mContext, LargePICActivity.class);
				// it.putExtra("index", index);
				// setPicjsa(jsa);
				// mContext.startActivity(it);
			}
		});
	}

	/** 进入大图页面 */
	private void largePic(LinearLayout Linear) {
		/** 进入大图页面 */
		for (int i = 0; i < Linear.getChildCount(); i++) {
			ImageView img = (ImageView) Linear.getChildAt(i);
			final int index = i;
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					Intent it;
					it = new Intent(mContext, LargePICActivity.class);
					it.putExtra("index", index);
					setPicjsa(data);
					mContext.startActivity(it);
				}
			});
		}
	}

	public void setPicjsa(JSONArray jsa) {
		picjsa = jsa;
	}

	public JSONArray getPicjsa() {
		return picjsa;
	}

	/**
	 * 添加活动的多个图片
	 * 
	 * 
	 */
	public void addMoreChild() {
		if (count == 1) {
			addPicOneChild();
		} else if (count == 4 || count == 2) {

			int childWidth = (width - (horizontalMax - 1) * padding)
					/ horizontalMax;
			params = new LayoutParams(childWidth, childWidth);
			params.rightMargin = padding;
			for (int i = 0; i < count; i++) {
				try {
					if (type == 1) {
						addMorePic(data.getJSONObject(i), 1, i);
					} else {
						addMorePic(null, 1, i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {

			int childWidth = (width - (horizontalMax - 1) * padding)
					/ horizontalMax;
			params = new LayoutParams(childWidth, childWidth);
			params.rightMargin = padding;

			for (int i = 0; i < count; i++) {
				try {
					if (type == 1) {
						addMorePic(data.getJSONObject(i), 2, i);
					} else {
						addMorePic(null, 2, i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// private void AddPicThreeChild()
	// {
	// LinearLayout child = addChild(layout);
	// for (int i = 0; i < count; i++)
	// {
	// try
	// {
	// if (type == 1)
	// {
	// child.addView(createPicImageView(data.getJSONObject(i)), params);
	// }
	// else
	// {
	// child.addView(createPicImageView(null), params);
	// }
	// }
	// catch (JSONException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

	private ImageView createHeadImageView(JSONObject jo) {
		RoundImageView img = new RoundImageView(mContext);
		img.setLayoutParams(params);
		if (type == 1) {
			ViewUtil.bindNetImage(img, JSONUtil.getString(jo, "photo"), "head");
			img.setTag(JSONUtil.getString(jo, "userId"));
			// img.setOnClickListener(new OnClickListener()
			// {
			//
			// @Override
			// public void onClick(final View v)
			// {
			// UserInfoManage manager = UserInfoManage.getInstance();
			// manager.checkLogin((Activity)mContext, new LoginCallBack()
			// {
			//
			// @Override
			// public void onisLogin()
			// {
			// ImageView i = (ImageView)v;
			// Intent it = new Intent(mContext, PersonDetailActivity.class);
			// it.putExtra("userId", i.getTag().toString());
			// mContext.startActivity(it);
			// }
			//
			// @Override
			// public void onLoginFail()
			// {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// }
			// });
			img.setVisibility(View.VISIBLE);
		} else {
			img.setVisibility(View.GONE);
		}

		return img;
	}

	private ImageView createPicImageView(JSONObject jo, int index) {
		ImageView img = new ImageView(mContext);
		img.setLayoutParams(params);
		if (type == 1) {
			ViewUtil.bindNetImage(img, JSONUtil.getString(jo, "thumbnail_pic"),
					"default");
		}
		img.setScaleType(ScaleType.FIT_XY);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onChildClickListener != null) {
					onChildClickListener.onclick("userid");
				}
			}
		});
		largePic(img, data, index);
		return img;
	}

	private void addPicOneChild() {
		LinearLayout child = addChild(layout);
		try {
			params = new LayoutParams(DhUtil.dip2px(mContext, 200),
					DhUtil.dip2px(mContext, 150));
			ImageView img = new ImageView(mContext);
			img.setBackgroundResource(R.drawable.img_loading);
			img.setLayoutParams(params);
			if (type == 1) {
				JSONObject jo = data.getJSONObject(0);
				ViewUtil.bindNetImage(img,
						JSONUtil.getString(jo, "thumbnail_pic"),
						"bigPicOptions");
			}
			child.addView(img, params);

			img.setScaleType(ScaleType.CENTER_CROP);
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onChildClickListener != null) {
						onChildClickListener.onclick("userid");
					}
					Intent it = new Intent(mContext, ImageGallery.class);
					String[] imgUrls = new String[data.length()];
					for (int i = 0, len = imgUrls.length; i < len; i++) {
						try {
							imgUrls[i] = data.getJSONObject(i).getString(
									"original_pic");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					it.putExtra("currentItem", 0);
					it.putExtra("imgurls", imgUrls);
					mContext.startActivity(it);
					// Intent it;
					// it = new Intent(mContext, LargePICActivity.class);
					// it.putExtra("index", 0);
					// setPicjsa(data);
					// mContext.startActivity(it);
				}
			});
			largePic(img, data, 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 添加多张图片 一行2个 */
	private void addMorePic(JSONObject jo, int horizontalMax, int index) {
		LinearLayout child;
		if (layout.getChildCount() == 0) {
			child = addChild(layout);
		} else {
			child = (LinearLayout) layout
					.getChildAt(layout.getChildCount() - 1);
			if (child.getChildCount() > horizontalMax) {
				child = addChild(layout);
			}
		}
		child.addView(createPicImageView(jo, index), params);
	}

	private LinearLayout addChild(LinearLayout piclayout) {
		LinearLayout child = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		params.bottomMargin = padding;
		piclayout.addView(child, params);
		return child;
	}

	public interface OnChildClickListener {
		void onclick(String userid);
	}
}
