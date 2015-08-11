package com.gongpingjia.carplay.animator;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.view.ViewHelper;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public abstract class BaseViewAnimator {
    private static long DURATION = 500;

    private long mDuration = DURATION;

    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public abstract void prepare(View target);

    public BaseViewAnimator setTarget(View target) {
        reset(target);
        prepare(target);
        return this;
    }

    public void reset(View target) {
        ViewHelper.setAlpha(target, 0);
        ViewHelper.setRotation(target, 0);
        ViewHelper.setRotationX(target, 0);
        ViewHelper.setRotationY(target, 0);
        ViewHelper.setScaleX(target, 0);
        ViewHelper.setScaleY(target, 0);
        ViewHelper.setTranslationX(target, 0);
        ViewHelper.setTranslationY(target, 0);
        ViewHelper.setPivotX(target, target.getMeasuredWidth() / 2.0f);
        ViewHelper.setPivotY(target, target.getMeasuredHeight() / 2.0f);
    }

    public void start() {
        getAnimatorAgent().setDuration(mDuration);
        getAnimatorAgent().start();
    }

    public AnimatorSet getAnimatorAgent() {
        return mAnimatorSet;
    }
}
