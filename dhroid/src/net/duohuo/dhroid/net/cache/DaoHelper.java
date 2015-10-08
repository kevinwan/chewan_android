package net.duohuo.dhroid.net.cache;

import java.sql.SQLException;

import net.duohuo.dhroid.Const;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DaoHelper extends  OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "duohuo.db";
	private static final int DATABASE_VERSION = Const.DATABASE_VERSION;
	
	public DaoHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
	try {
		TableUtils.createTable(connectionSource, Cache.class);
		} catch (SQLException e) {
			Log.e("duohuo_DaoHelper","创建数据库失败");
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2,
	int arg3) {
	try {
		TableUtils.dropTable(connectionSource, Cache.class, true);
		onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() {
		super.close();
	}
}