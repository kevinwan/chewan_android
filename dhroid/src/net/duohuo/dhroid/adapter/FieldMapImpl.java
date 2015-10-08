package net.duohuo.dhroid.adapter;

import android.view.View;

/**
 * 简单实现
 * 
 * @author duohuo-jinghao
 * 
 */
public class FieldMapImpl extends FieldMap {

	public FieldMapImpl(String key, Integer refId) {
		super(key, refId);
	}

	public FieldMapImpl(String key, Integer refId, String type) {
		super(key, refId, type);
	}

	@Override
	public Object fix(View itemV, Integer position, Object o, Object jo) {
		return o;
	}


}
