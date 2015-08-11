package com.gongpingjia.carplay.animator.attention;

import android.view.View;

import com.gongpingjia.carplay.animator.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ShakeAnimator extends BaseViewAnimator {

    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "translationX", 0, 25, -25, 25, -25, 0));
    }

}
