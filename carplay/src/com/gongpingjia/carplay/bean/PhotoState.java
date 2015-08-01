package com.gongpingjia.carplay.bean;

/** 
 * @Description 照片状态
 * @author Administrator
 * @date 2015-7-21 上午9:38:14 
 */ 
public class PhotoState {

    //图片的本地路径
    private String path;

    //是否是最后一张图片
    private boolean isLast;
    
    //图片是否被选中
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

    @Override
    public String toString() {
        return "PhotoState [path=" + path + ", isLast=" + isLast + ", isChecked=" + isChecked + "]";
    }

    
}
