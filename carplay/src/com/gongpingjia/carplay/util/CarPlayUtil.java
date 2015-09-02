package com.gongpingjia.carplay.util;

import java.io.File;
import java.text.SimpleDateFormat;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;

public class CarPlayUtil {
	public static void bindSexView(String gender, View sexBg) {
		if (!TextUtils.isEmpty(gender)) {
			if (gender.equals("男")) {
				sexBg.setBackgroundResource(R.drawable.man);
			} else {
				sexBg.setBackgroundResource(R.drawable.woman);
			}
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

	public static boolean getPhoto(final Activity activity,
			final int takePhotoCode, final int imageCode, final File tempFile) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(tempFile));
							activity.startActivityForResult(getImageByCamera,
									takePhotoCode);
						} else {

							Intent intent = new Intent(activity,
									PhotoSelectorActivity.class);
							intent.putExtra(PhotoSelectorActivity.KEY_MAX, 9);
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							activity.startActivityForResult(intent, imageCode);

							// Intent getImage = new Intent(
							// Intent.ACTION_GET_CONTENT);
							// getImage.addCategory(Intent.CATEGORY_OPENABLE);
							// getImage.setType("image/jpeg");
							// activity.startActivityForResult(getImage,
							// imageCode);
						}
					}
				}).create();
		Window window = dlg.getWindow();
		window.setWindowAnimations(R.style.mystyle);
		dlg.show();
		return true;
	}

	public static boolean getPhoto(final Activity activity,
			final int takePhotoCode, final int imageCode, final File tempFile,
			final int needPicNum) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(tempFile));
							activity.startActivityForResult(getImageByCamera,
									takePhotoCode);
						} else {
							Intent intent = new Intent(activity,
									PhotoSelectorActivity.class);
							intent.putExtra(PhotoSelectorActivity.KEY_MAX,
									needPicNum);
							intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							activity.startActivityForResult(intent, imageCode);

							// Intent getImage = new Intent(
							// Intent.ACTION_GET_CONTENT);
							// getImage.addCategory(Intent.CATEGORY_OPENABLE);
							// getImage.setType("image/jpeg");
							// activity.startActivityForResult(getImage,
							// imageCode);
						}
					}
				}).create();
		Window window = dlg.getWindow();
		window.setWindowAnimations(R.style.mystyle);
		dlg.show();
		return true;
	}

}
