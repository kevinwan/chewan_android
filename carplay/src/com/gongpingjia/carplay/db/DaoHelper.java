package com.gongpingjia.carplay.db;

import java.sql.SQLException;
import java.util.ArrayList;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.net.cache.Cache;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gongpingjia.carplay.bean.Message;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DaoHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "duohuo.db";
	private static final int DATABASE_VERSION = Const.DATABASE_VERSION;

	public DaoHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			System.out.println("创建数据库..............");
			TableUtils.createTable(connectionSource, Cache.class);
			TableUtils.createTable(connectionSource, Message.class);
		} catch (SQLException e) {
			System.out.println("e" + e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			System.out.println("删除数据库..............");
			TableUtils.dropTable(connectionSource, Cache.class, true);
			TableUtils.dropTable(connectionSource, Message.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAll(String databaseName,
			ArrayList<ContentValues> valuesArr) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		for (ContentValues val : valuesArr) {
			db.insert(databaseName, null, val);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	@Override
	public void close() {
		super.close();
	}
}