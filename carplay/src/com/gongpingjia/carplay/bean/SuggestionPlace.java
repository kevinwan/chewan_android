package com.gongpingjia.carplay.bean;

import android.text.SpannableStringBuilder;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class SuggestionPlace {

    private String details;

    private SpannableStringBuilder styleName;

    public SpannableStringBuilder getStyleName() {
        return styleName;
    }

    public void setStyleName(SpannableStringBuilder styleName) {
        this.styleName = styleName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
