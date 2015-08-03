package com.gongpingjia.carplay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class FileUtil {

    public enum UNIT_SACLE {
        B, KB, M, G
    }

    public static long getFileOrPathSize(File file, UNIT_SACLE scale) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                size += getFilesSize(file);
            } else {
                size += getFileSize(file);
            }
        } else {
            Log.e("FileUtil", "file doesn't exist");
        }
        return formatFileSize(size, scale);
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream is;
            try {
                is = new FileInputStream(file);
                size = is.available();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.e("tag", "File not found");
        }
        return size;
    }

    public static long getFilesSize(File file) {
        long size = 0;
        File[] files = file.listFiles();
        for (int i = 0, length = files.length; i < length; i++) {
            if (files[i].isDirectory()) {
                size += getFilesSize(files[i]);
            } else {
                size += getFileSize(files[i]);
            }
        }
        return size;
    }

    public static long formatFileSize(long size, UNIT_SACLE scale) {
        switch (scale) {
        case B:
            break;
        case KB:
            size = size / 1024;
            break;
        case M:
            size = size / (1024 * 1024);
            break;
        case G:
            size = size / (1024 * 1024 * 1024);
            break;
        default:
            break;
        }
        return size;
    }

    public static boolean deleteFile(File file) {
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            Log.e("tag", "file doesn't exist");
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                deleteDir(file);
            }
        }
        return dir.delete();
    }

    public static boolean deleteFileOrDir(File file) {
        if (file.isDirectory()) {
            return deleteDir(file);
        } else {
            return deleteFile(file);
        }
    }
}
