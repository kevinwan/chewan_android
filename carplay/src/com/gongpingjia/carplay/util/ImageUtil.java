package com.gongpingjia.carplay.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    public static Bitmap getBitmap(String imgPath) {
        return getBitmap(imgPath, 1);
    }

    public static Bitmap getBitmap(String imgPath, int sampleSize) {
        Options option = new Options();
        option.inDither = false;
        option.inSampleSize = sampleSize;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imgPath, "r");
            return BitmapFactory.decodeFileDescriptor(file.getFD(), null, option);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
