package net.duohuo.dhroid.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.HttpManager;
import net.duohuo.dhroid.util.MD5;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/***
 * @description:图片本地缓存
 * @author : duohuo-jinghao ****************************************************
 *        
 *       
 *       
 ****/

public class ImageCacheManager {
	private String IMAGE_CACHE_DIR;
	private boolean IMAGE_CACHE_INSD;
	private int CACHE_NUM;
	private long CACHE_NUM_TIME;
	private static ImageCacheManager localSDImage = new ImageCacheManager();
	File dir;

	private ImageCacheManager() {
		
	}

	public static ImageCacheManager getInstanse() {
		localSDImage.prepare();
		return localSDImage;
	}

	private void prepare() {
		IMAGE_CACHE_DIR = Const.image_cache_dir;
		IMAGE_CACHE_INSD = Const.image_cache_is_in_sd;
		CACHE_NUM = Const.image_cache_num;
		CACHE_NUM_TIME = Const.image_cache_time;

		if (IMAGE_CACHE_INSD) {
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 不可用內存卡
				dir = IocContainer.getShare().getApplication()
						.getApplicationContext().getCacheDir();
				if (!dir.exists())
					dir.mkdirs();
				IMAGE_CACHE_INSD = false;
				return;
			}
			dir = new File(Environment.getExternalStorageDirectory(),
					IMAGE_CACHE_DIR);
			if (!dir.exists())
				dir.mkdirs();
		} else {
			dir = IocContainer.getShare().getApplication()
					.getApplicationContext().getCacheDir();
			if (!dir.exists())
				dir.mkdirs();
		}
	}

	/**
	 * 获取图片名
	 * 
	 * @param url
	 * @return
	 */
	private String urlToFileName(String url, int w, int hignt) {
		try {
			String md5 = MD5.encryptMD5(url);
			;
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/***
	 * 获取本地缓存图片
	 * 
	 * @param url
	 *            图片的链接
	 * @return 如果没有返回null
	 * **/
	public Bitmap getLocalImage(String url, int width, int height) {
		String filename = urlToFileName(url, width, height);
		File file = new File(dir, filename);
		if (file.exists()) {
			try {
				file.setLastModified(System.currentTimeMillis());
				FileInputStream in = new FileInputStream(file);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				int sWidth = width;
				int sHeight = height;
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

	/***
	 * 将图片缓存到本地
	 * ****/
	public void saveLocalImage(Bitmap bm, String url, int width, int hight) {
		if (bm == null)
			return;
		String filename = urlToFileName(url, width, hight);
		File file = new File(dir, filename);
		try {
			file.createNewFile();
			OutputStream outStream;
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 删除缓存
	 * 
	 * @param isAll
	 *            true 删除全部缓存 else 根据缓存策略删除
	 * 
	 * **/
	private void deleteCache(Boolean isAll) {
		// 删除全部缓存
		if (isAll) {
			deleteDirAll(dir);
		} else {
			deleteSCacheInTime(dir.getAbsolutePath());
			deleteSCacheInNum(dir.getAbsolutePath());
		}
	}

	/**
	 * 删除全部缓存 根据缓存策略删除
	 * 
	 * **/
	public void deleteAllCache() {
		deleteCache(true);
	}

	/**
	 * 根据缓存策略删除
	 * **/
	public void deleteCache() {
		deleteCache(false);
	}

	/**
	 * 刪除全部緩存文件
	 * 
	 * @param dir
	 */
	private void deleteDirAll(File dir) {
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			if (child.isDirectory()) {
				deleteDirAll(child);
				child.delete();
			} else {
				child.delete();
			}
		}
	}

	/**
	 * 按数量删除
	 * 
	 * @param dirPath
	 */
	private void deleteSCacheInNum(String dirPath) {
		File dir = new File(dirPath);
		File[] children = dir.listFiles();
		if (children.length > CACHE_NUM) {
			Arrays.sort(children, new FileTimeComparator());
			for (int i = 0; i < CACHE_NUM * 0.4; i++) {
				File child = children[i];
				child.delete();
			}
		}
	}

	/**
	 * 按时间删除
	 * 
	 * @param dirPath
	 */
	private void deleteSCacheInTime(String dirPath) {
		File dir = new File(dirPath);
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			if (System.currentTimeMillis() - child.lastModified() > CACHE_NUM_TIME) {
				child.delete();
			}
		}
	}

	public String sizeToString(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取缓存文件夹大小
	 * 
	 * @return
	 */
	public String getCacheSizeStr() {
		return sizeToString(getCacheSize());
	}

	/**
	 * 获取缓存文件夹大小
	 * 
	 * @return
	 */
	public Long getCacheSize() {
		long allsize = 0l;
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			long size = child.length();
			allsize += size;
		}
		return allsize;
	}

	class FileTimeComparator implements Comparator<File> {
		public int compare(File file1, File file2) {
			if (file1.lastModified() > file2.lastModified()) {
				return 1;
			} else if (file1.lastModified() == file2.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
