package net.duohuo.dhroid.net.cache;

import java.util.Date;

import android.os.Parcel;

import com.j256.ormlite.field.DatabaseField;


/**
 * 缓存对象
 * @author duohuo-jinghao
 *
 */
public class Cache{
	public Cache() {
	}

	
	@DatabaseField(generatedId=true)
	public Integer id;
	@DatabaseField
	public String key;
	@DatabaseField
	public String result;
	@DatabaseField
	public Long updateTime;
	
	

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
