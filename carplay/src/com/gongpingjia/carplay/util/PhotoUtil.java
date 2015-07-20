package com.gongpingjia.carplay.util;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class PhotoUtil {

    /**
     * @Description ��ͼ���ȡͼƬ
     * @author Administrator
     * @param activity
     * @param requestCode
     */
    public static void getPhotoFromPick(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @Description ��������ջ�ȡ
     * @author Administrator
     * @param activity
     * @param requestCode
     * @param file
     */
    public static void getPhotoFromCamera(Activity activity, int requestCode, String tmpPath) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tmpPath)));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @Description ��ϵͳͼ���н�ȡͼƬ
     * @author Administrator
     * @param activity
     * @param data 
     * @param path ͼƬ·��
     * @param requestCrop
     */

    public static void onPhotoFromPick(Activity activity, Intent data, String path, int requestCrop) {
        handleCrop(activity, data.getData(), path, requestCrop);
    }

     
    /** 
     * @Description ����������л�ȡͼƬ
     * @author Administrator
     * @param activity
     * @param path
     * @param requestCrop  
     */
      	
    public static void onPhotoFromCamera(Activity activity, String path, int requestCrop) {
        handleCrop(activity, Uri.fromFile(new File(path)), path, requestCrop);
    }

    /**
     * @Description �ü�ͼƬ
     * @author Administrator
     * @param context
     * @param data
     * @param tmpPath
     */
    private static void handleCrop(Activity activity, Uri uri, String tmpPath, int cropCode) {
        File file = new File(tmpPath);
        Uri outUri = Uri.fromFile(file);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop", true);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // ��������ʶ��
        activity.startActivityForResult(intent, cropCode);
    }

}
