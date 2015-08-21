package com.gongpingjia.carplay.activity.my;

import java.io.File;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.upload.FileInfo;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.api.Constant;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.dialog.PhotoSelectDialog;
import com.gongpingjia.carplay.view.dialog.PhotoSelectDialog.OnStateChangeListener;

/**
 * 
 * @Description 认证车主
 * @author wang
 * @date 2015-7-17 上午10:14:30
 */
public class AuthenticateOwnersActivity extends CarPlayBaseActivity implements
		OnClickListener {

	private TextView modelT = null;

	ImageView picI;

	User user;

	String mPhotoPath;

	public static final int MODEL = 1;

	String brandName, brandLogo, modelName, modelSlug;

	EditText drivingExperienceE;

	String picUid;

	Button submitB;

	// 图片缓存根目录
	private File mCacheDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate_owners);
	}

	@Override
	public void initView() {
		mCacheDir = new File(getExternalCacheDir(), "CarPlay");
		mCacheDir.mkdirs();
		setTitle("车主认证");
		String type = getIntent().getStringExtra("type");
		if (TextUtils.isEmpty(type)) {
			setRightAction("跳过", -1, new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}
			});
		}
		modelT = (TextView) findViewById(R.id.model);
		modelT.setOnClickListener(this);
		picI = (ImageView) findViewById(R.id.pic);
		picI.setOnClickListener(this);
		user = User.getInstance();

		drivingExperienceE = (EditText) findViewById(R.id.drivingExperience);
		submitB = (Button) findViewById(R.id.submit);
		submitB.setOnClickListener(this);
	}

	private void authtion() {
		if (TextUtils.isEmpty(picUid)) {
			showToast("请上传驾驶证!");
			return;
		}

		if (TextUtils.isEmpty(drivingExperienceE.getText().toString())) {
			showToast("请输入架龄!");
			return;
		}

		if (TextUtils.isEmpty(brandName)) {
			showToast("请选择车型品牌!");
			return;
		}

		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/authentication?token=" + user.getToken());
		net.addParam("drivingExperience", drivingExperienceE.getText()
				.toString());
		net.addParam("carBrand", brandName);
		net.addParam("carBrandLogo", brandLogo);
		net.addParam("carModel", modelName);
		net.addParam("slug", modelSlug);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast("认证车主申请成功,请等待审核!");
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}
			}
		});
	}

	private void uploadPic(String path) {
		Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
		picI.setImageBitmap(bmp);
		DhNet net = new DhNet(API.CWBaseurl + "/user/" + user.getUserId()
				+ "/license/upload?token=" + user.getToken());
		net.upload(new FileInfo("attach", new File(path)), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				hidenProgressDialog();
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					picUid = JSONUtil.getString(jo, "photoId");
				} else {
					showToast("上传失败，请重新上传");
					picUid = "";
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.model:
			Intent intent = new Intent(self, CarTypeSelectActivity.class);
			startActivityForResult(intent, MODEL);
			break;

		case R.id.pic:
			mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
					+ ".jpg").getAbsolutePath();
			PhotoUtil.getPhoto(self, Constant.TAKE_PHOTO, Constant.PICK_PHOTO,
					new File(mPhotoPath));
			break;

		case R.id.submit:
			authtion();
			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// case Constant.TAKE_PHOTO:
			// String newPath = new File(mCacheDir, System.currentTimeMillis() +
			// ".jpg").getAbsolutePath();
			// String path = PhotoUtil.onPhotoFromCamera(self,
			// Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000, newPath);
			// mPhotoPath = path;
			// break;
			// case Constant.PICK_PHOTO:
			// PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
			// data, 1, 1, 1000);
			// break;
			// case Constant.ZOOM_PIC:
			// Bitmap bmp = PhotoUtil.getLocalImage(new File(mPhotoPath));
			// picI.setImageBitmap(bmp);
			// showProgressDialog("上传图片中...");
			// uploadPic(mPhotoPath);
			// break;

			case Constant.PICK_PHOTO:
				Bitmap btp = PhotoUtil.checkImage(self, data);
				PhotoUtil.saveLocalImage(btp, new File(mPhotoPath));
				btp.recycle();
				showProgressDialog("上传图片中...");
				uploadPic(mPhotoPath);
				break;
			case Constant.TAKE_PHOTO:
				Bitmap btp1 = PhotoUtil.getLocalImage(new File(mPhotoPath));
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				int degree = PhotoUtil.getBitmapDegree(mPhotoPath);
				PhotoUtil.saveLocalImage(btp1, new File(newPath), degree);
				btp1.recycle();
				showProgressDialog("上传图片中...");
				uploadPic(newPath);
				break;

			case MODEL:
				brandName = data.getStringExtra("brandName");
				brandLogo = data.getStringExtra("brandLogo");
				modelName = data.getStringExtra("modelName");
				modelSlug = data.getStringExtra("modelSlug");
				modelT.setText(modelName);
				break;
			}
		}
	}

}
