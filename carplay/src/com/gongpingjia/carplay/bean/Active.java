package com.gongpingjia.carplay.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "active")
public class Active {
	@DatabaseField(generatedId = true)
	public Integer id;

	@DatabaseField()
	public String introduction = "";

	@DatabaseField()
	public String location = "";

	@DatabaseField()
	public String city = "";

	@DatabaseField()
	public String address = "";

	@DatabaseField()
	public String pay = "";

	@DatabaseField()
	public String seat = "";

	@DatabaseField()
	public Long start = 0l;

	@DatabaseField()
	public Long end = 0l;

	@DatabaseField()
	public Double latitude = 0.0;

	@DatabaseField()
	public Double longitude = 0.0;

	@DatabaseField()
	public String photoUrls = "";

	@DatabaseField()
	public String photoIds = "";

	@DatabaseField()
	public String province = "";

	@DatabaseField()
	public String type = "";
	@DatabaseField()
	public String district = "";

	public Active() {

	}
}
