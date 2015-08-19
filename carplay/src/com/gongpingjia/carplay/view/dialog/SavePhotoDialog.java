package com.gongpingjia.carplay.view.dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.PhotoUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.BaseAlertDialog;

public class SavePhotoDialog extends BaseAlertDialog {

	Bitmap bitmap;
	Context mContext;

	public SavePhotoDialog(Context context, Bitmap bitmap, int theme) {
		super(context, theme);

		this.bitmap = bitmap;
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_savephoto);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				File appDir = new File(Environment
						.getExternalStorageDirectory(), "carplay");
				if (!appDir.exists()) {
					appDir.mkdir();
				}
				String fileName = System.currentTimeMillis() + ".jpg";
				File file = new File(appDir, fileName);

				PhotoUtil.saveLocalImage(bitmap, file);
				IocContainer.getShare().get(IDialog.class)
						.showToastShort(mContext, "图片保存成功!");
				dismiss();
				Intent intent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.fromFile(file);
				intent.setData(uri);
				mContext.sendBroadcast(intent);
			}
		});

		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				if (bitmap != null) {
					// bitmap.recycle();
				}
			}
		});
	}

}
