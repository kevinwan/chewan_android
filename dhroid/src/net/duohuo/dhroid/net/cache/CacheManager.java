package net.duohuo.dhroid.net.cache;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.MD5;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * 网络缓存管理
 * 
 * @author duohuo-jinghao
 * 
 */
public class CacheManager {

	Dao<Cache, Integer> cacheDao;
	
	public CacheManager() {
		super();
		OrmLiteSqliteOpenHelper daoHelper = IocContainer.getShare().get(
				OrmLiteSqliteOpenHelper.class);
		if (daoHelper != null) {
			try {
				cacheDao = daoHelper.getDao(Cache.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建缓存
	 * 
	 * @param url
	 * @param params
	 * @param result
	 */ 
	public void creata(String url, Map<String, Object> params, String result) {
		delete(url, params);
		Cache cache=new Cache();
		cache.setKey(buildKey(url, params));
		cache.setResult(result);
		cache.setUpdateTime(System.currentTimeMillis());
		if(cache!=null){
			try {
				cacheDao.create(cache);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param url
	 * @param params
	 */
	public String  get(String url, Map<String, Object> params) {
		try {
		QueryBuilder<Cache , Integer> builder=cacheDao.queryBuilder();
			builder.where().eq("key", buildKey(url, params));
			builder.orderBy("id", false);
			List<Cache> caches=builder.query();
				if(caches!=null&&caches.size()>0){
					return caches.get(0).getResult();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 删除缓存
	 * @param url
	 * @param params
	 */
	public void delete(String url, Map<String, Object> params) {
		try {
			DeleteBuilder<Cache , Integer> builder=cacheDao.deleteBuilder();
				builder.where().eq("key", buildKey(url, params));
				cacheDao.delete(builder.prepare());
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	
	
	
	/**
	 * 删除多少天前的缓存
	 * @param dayAgo
	 */
	public void deleteByDate(Integer dayAgo){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -dayAgo);
		Date time=calendar.getTime();
		try {
		DeleteBuilder<Cache , Integer> builder=	cacheDao.deleteBuilder();
			builder.where().gt("updateTime",time.getTime());
			cacheDao.delete(builder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private String buildKey(String url, Map<String, Object> params) {
		if (params != null) {
			url+=params.toString();
		}
		try {
			return	MD5.encryptMD5(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
