package com.gongpingjia.carplay.util;

import java.text.SimpleDateFormat;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

public class CarPlayUtil {
	public static void bindSexView(String gender, View sexBg) {
		if (gender.equals("男")) {
			sexBg.setBackgroundResource(R.drawable.man);
		} else {
			sexBg.setBackgroundResource(R.drawable.woman);
		}
	}

	public static void bindDriveAge(JSONObject jo, ImageView carLogoI,
			TextView driveAgeT) {
		if (TextUtils.isEmpty(JSONUtil.getString(jo, "carModel"))) {
			carLogoI.setVisibility(View.GONE);
			ViewUtil.bindView(driveAgeT, "带我飞~");
		} else {
			carLogoI.setVisibility(View.VISIBLE);
			ViewUtil.bindView(driveAgeT, JSONUtil.getString(jo, "carModel")
					+ "," + JSONUtil.getString(jo, "drivingExperience") + "年驾龄");
			ViewUtil.bindNetImage((ImageView) carLogoI,
					JSONUtil.getString(jo, "carBrandLogo"), "carlogo");
		}
	}

	public static String getStringDate(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);

		return dateString;
	}

	public static int px2sp(float pxValue, Context context) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(float spValue, Context context) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale + 0.5f);
	}
}
