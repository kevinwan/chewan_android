package com.gongpingjia.carplay.db;

import android.database.sqlite.SQLiteDatabase;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ActiveBackDao {
    
    private static final String TABLE_NAME="active";
    private static final String _ID = "_id";
    
    
    public static void createTable(SQLiteDatabase db){
        String sql = "";
        db.execSQL(sql);
    }
    
    public static void saveActive(){
        
    }
    
    public static void dropTable(){
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        String sql = "";
    }
}


