package net.duohuo.dhroid.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class DhUtil {
	
	public static int dip2px(Context context,float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;		
		return (int) (scale*dipValue+0.5f);		
	}
	
	public static int px2dip(Context context,float pxValue){
		float scale=context.getResources().getDisplayMetrics().density;		
		return (int) (pxValue/scale+0.5f);
	}

	
	public static String delHtml(String str)
	{
		String info = str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");  
		info = info.replaceAll("[(/>)<]", ""); 
		return info;
	}
	/**
	 * 通过 uri 获取图片的文件
	 * @param context
	 * @param uri
	 * @return
	 */
	public File uriToImageFile(Activity context,Uri uri){
	    String[] proj = { MediaStore.Images.Media.DATA };  
	    Cursor actualimagecursor =context.managedQuery(uri,proj,null,null,null);  
	    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	    actualimagecursor.moveToFirst();  
	    String img_path = actualimagecursor.getString(actual_image_column_index);  
	    File file = new File(img_path);  
	    return file;
	}
	
}
