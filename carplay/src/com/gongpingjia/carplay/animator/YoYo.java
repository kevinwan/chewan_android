package com.gongpingjia.carplay.animator;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class YoYo {

    private static final int DURATION = 1000;

    private static final long DELAY = 0;

    private static BaseViewAnimator mAnimator;

    public static BaseViewAnimator with(BaseViewAnimator animator) {
        mAnimator = animator;
        return animator;
    }
    
    
}
