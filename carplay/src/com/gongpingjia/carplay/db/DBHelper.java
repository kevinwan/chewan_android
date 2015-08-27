package com.gongpingjia.carplay.db;

import com.gongpingjia.carplay.CarPlayApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    private static final String DB_NAME = "active_bak";

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ActiveBackDao.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            synchronized (dbHelper) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(CarPlayApplication.getInstance(), DB_NAME, null, 1);
                }
            }
        }
        return dbHelper;
    }

}
