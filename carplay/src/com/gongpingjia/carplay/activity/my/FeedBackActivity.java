package com.gongpingjia.carplay.activity.my;

import java.io.File;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;

public class FeedBackActivity extends CarPlayBaseActivity implements
		OnClickListener {

	// 图片缓存根目录
	private File mCacheDir;

	String mPhotoPath;

	EditText contentE;

	ImageView picI;

	User user;

	String photoId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

	}

	@Override
	public void initView() {
		user = User.getInstance();
		mCacheDir = new File(getExternalCacheDir(), "CarPlay");
		mCacheDir.mkdirs();
		setTitle("意见反馈");
		setLeftAction(-1, null, null);
		setRightAction("提交", -1, new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
		picI = (ImageView) findViewById(R.id.pic);
		picI.setOnClickListener(this);
		contentE = (EditText) findViewById(R.id.content);
	}

	private void submit() {
		String content = contentE.getText().toString();
		if (TextUtils.isEmpty(content)) {
			showToast("请输入反馈内容");
			return;
		}

		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/feedback/submit?token=" + user.getToken());
		net.addParam("photo", photoId);
		net.addParam("content", content);
		net.doPostInDialog("提交中...", new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("您的反馈已成功提交!");
					finish();
				}
			}
		});
	}

	private void uploadPic(String path) {
		Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
		picI.setScaleType(ScaleType.FIT_XY);
		picI.setImageBitmap(bmp);
		DhNet net = new DhNet(API.CWBaseurl + "/feedback/upload");
		net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					photoId = JSONUtil.getString(jo, "photoId");
				} else {
					picI.setScaleType(ScaleType.CENTER);
					picI.setImageResource(R.drawable.feedback_camera);
				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.TAKE_PHOTO:
				Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
				PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
				btp1.recycle();
				uploadPic(newPath);
				break;
			case Constant.PICK_PHOTO:
				Bitmap btp = PhotoUtil.checkImage(self, data);
				PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
				btp.recycle();
				uploadPic(mPhotoPath);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pic:
			mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
					+ ".jpg").getAbsolutePath();
			PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
					new File(mPhotoPath));
			break;

		default:
			break;
		}
	}

}
