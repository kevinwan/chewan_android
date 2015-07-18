package com.gongpingjia.carplay.adapter;

import android.util.SparseArray;
import android.view.View;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ViewHolder {

    public static <T extends View> T getView(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
