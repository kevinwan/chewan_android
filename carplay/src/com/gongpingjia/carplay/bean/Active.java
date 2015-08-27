package com.gongpingjia.carplay.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "active")
public class Active {
	@DatabaseField(generatedId = true)
	public Integer id;

	public Active() {

	}
}
