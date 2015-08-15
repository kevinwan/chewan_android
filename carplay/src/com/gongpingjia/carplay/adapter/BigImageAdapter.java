package com.gongpingjia.carplay.adapter;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.easemob.util.ImageUtils;
import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.chat.task.LoadLocalBigImgTask;
import com.gongpingjia.carplay.chat.view.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class BigImageAdapter extends BaseAdapter {

	LayoutInflater mLayoutInflater;

	Activity mcontext;

	String url;

	JSONArray jsa;

	ImageView img;
	int screenWidth;

	public BigImageAdapter(Context context, JSONArray jsa) {
		this.mcontext = (Activity) context;
		mLayoutInflater = LayoutInflater.from(context);
		this.jsa = jsa;
		DisplayMetrics metrics = new DisplayMetrics();
		mcontext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
	}

	@Override
	public int getCount() {

		if (jsa == null || jsa.length() == 0) {
			return 0;
		}
		if (jsa.length() == 1) {
			return 1;
		}
		return Integer.MAX_VALUE;
	}

	// @Override
	// public int getCount() {
	// if (jsa == null) {
	// return 0;
	// } else {
	// return jsa.length();
	//
	// }
	// }

	@Override
	public Object getItem(int position) {

		Object o = null;
		if (jsa != null && jsa.length() != 0) {
			try {
				o = jsa.get(position % jsa.length());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return o;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater
					.inflate(R.layout.item_big_image, null);
		}

		// ProgressBar loadLocalPb = (ProgressBar) convertView
		// .findViewById(R.id.pb_load_local);
		JSONObject jo = (JSONObject) getItem(position);
		// LoadLocalBigImgTask task = new LoadLocalBigImgTask(context,
		// JSONUtil.getString(jo, "original_pic"),
		// (PhotoView) convertView.findViewById(R.id.pic), loadLocalPb,
		// ImageUtils.SCALE_IMAGE_WIDTH, ImageUtils.SCALE_IMAGE_HEIGHT);
		// if (android.os.Build.VERSION.SDK_INT > 10) {
		// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		// } else {
		// task.execute();
		// }

		img = (ImageView) convertView.findViewById(R.id.pic);
		ViewGroup.LayoutParams para = img.getLayoutParams();
		para.width = screenWidth;
		img.setLayoutParams(para);
		ImageLoader.getInstance().displayImage(
				JSONUtil.getString(jo, "original_pic"),
				(ImageView) convertView.findViewById(R.id.pic),
				CarPlayValueFix.optionsDefault, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(Bitmap bitmap) {
						// TODO Auto-generated method stub

						int bwidth = bitmap.getWidth();
						int bHeight = bitmap.getHeight();
						int height = screenWidth * bHeight / bwidth;
						ViewGroup.LayoutParams para = img.getLayoutParams();
						para.height = height;
						img.setLayoutParams(para);
					}

					@Override
					public void onLoadingCancelled() {
						// TODO Auto-generated method stub

					}
				});
		// ViewUtil.bindNetImage((ImageView) convertView.findViewById(R.id.pic),
		// JSONUtil.getString(jo, "original_pic"), "default",new );
		return convertView;
	}

}
