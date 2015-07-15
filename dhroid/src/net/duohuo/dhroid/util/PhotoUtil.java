package net.duohuo.dhroid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

/***
 * 
 * 头像上传工具类
 * 调用 getPhoto
 * 在onactivityResult 调用
 * 
 * onPhotoFromCamera
 * 
 * onPhotoFromPick
 */
public class PhotoUtil {

	/**
	 * 因为处理不同
	 * 
	 * @param takePhotoCode
	 *            Uri originalUri = data.getData();
	 *            image=ImageUtil.getBitmapFromUrl(originalUri.toString());
	 ********************************************************************************** 
	 * @param imageCode
	 *            Bundle extras = data.getExtras(); image = (Bitmap)
	 *            extras.get("data");
	 * @param tempFile
	 *            拍照时的临时文件 需要zoom时
	 * **/
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
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							activity.startActivityForResult(getImage, imageCode);
						}
					}
				}).create();
		dlg.show();
		return true;
	}

	/**
	 * 拍照获取图片的方式
	 * 
	 * @param context
	 * @param zoomCode
	 * @param temppath
	 *            拍照前生成的临时路劲
	 * @return 新的路劲
	 */
	public static String onPhotoFromCamera(Activity context, int zoomCode,
			String temppath,int aspectX,int aspectY,int outx) {
		Bitmap btp = getLocalImage(new File(temppath));
		saveLocalImage(btp, new File(temppath + "temp"));
		photoZoom(context, Uri.fromFile(new File(temppath + "temp")),Uri.fromFile(new File(temppath)), 
				zoomCode,aspectX,aspectY, outx);
		return temppath + "temp";
	}
	
	

	/**
	 * 相册获取图片
	 * 
	 * @param context
	 * @param zoomCode
	 * @param temppath
	 *            希望生成的路劲
	 * @param data
	 */
	public static void onPhotoFromPick(Activity context, int zoomCode,
			String temppath,Intent data,int aspectX,int aspectY,int outx) {
		Bitmap btp = checkImage(context, data);
		saveLocalImage(btp, new File(temppath));
		PhotoUtil.photoZoom(context, Uri.fromFile(new File(temppath)),Uri.fromFile(new File(temppath)),
				zoomCode,aspectX,aspectY, outx);
	}

	/**
	 * data 中检出图片
	 * 
	 * @param activity
	 * @param data
	 * @return
	 */
	public static Bitmap checkImage(Activity activity, Intent data) {
		Bitmap bitmap = null;
		try {
			Uri originalUri = data.getData();
			String path = getRealPathFromURI(activity, originalUri);
			File f = activity.getExternalCacheDir();
			String pp = f.getAbsolutePath();
			if (path.indexOf(pp) != -1) {
				path = path.substring(path.indexOf(pp), path.length());
			}
			bitmap = getLocalImage(new File(path));
		} catch (Exception e) {
		} finally {
			return bitmap;
		}
	}

	/**
	 * 通过URI 获取真实路劲
	 * 
	 * @param activity
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {
		Cursor cursor = null;
		String result = contentUri.toString();
		String[] proj = { MediaColumns.DATA };
		cursor = activity.managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			throw new NullPointerException("reader file field");
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
			if (Integer.parseInt(Build.VERSION.SDK) < 14) {
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * 保存临时图片到本地
	 * 
	 * @param bm
	 * @param f
	 */
	public static void saveLocalImage(Bitmap bm, File f) {
		if (bm == null)
			return;
		File file = f;
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100,
					outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 由本地获取图片
	 * 
	 * @param f
	 * @return
	 */
	public static Bitmap getLocalImage(File f) {
		File file = f;
		if (file.exists()) {
			try {
				file.setLastModified(System.currentTimeMillis());
				FileInputStream in = new FileInputStream(file);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				int sWidth = 500;
				int sHeight = 500;
				int mWidth = options.outWidth;
				int mHeight = options.outHeight;
				int s = 1;
				while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2)) {
					s *= 2;
				}
				options = new BitmapFactory.Options();
				options.inSampleSize = s;
				in.close();
				// 再次获取
				in = new FileInputStream(file);
				Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
				in.close();
				return bitmap;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * aspectY Y对于X的比例 outputX X 的宽
	 * **/
	public static void photoZoom(Activity activity, Uri uri,Uri outUri,
			int photoResoultCode,int aspectX,  int aspectY, int outputX) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		if (aspectY > 0) {
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
		}
//		 outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", outputX);
//		if (aspectY > 0) {
//			intent.putExtra("outputY", (int)(aspectY/(float)aspectX) * outputX);
//		}
		 intent.putExtra("scale", true);
		 intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		 intent.putExtra("return-data", false);
		 intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		 intent.putExtra("noFaceDetection", false); // 加入人脸识别
		activity.startActivityForResult(intent, photoResoultCode);
	}

}
