package com.gongpingjia.carplay.bean;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class PhotoState {

    private String path;

    private boolean isLast;
    
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

}
