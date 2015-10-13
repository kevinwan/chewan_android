package com.gongpingjia.carplay.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class HeartView extends ImageView {

    private boolean isFavorited;

    private ObjectAnimator scaleLargeAnimator;
    private ObjectAnimator scaleSmallAnimator;

    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isFavorited = false;
        PropertyValuesHolder largeX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.2f);
        PropertyValuesHolder largeY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.2f);
        PropertyValuesHolder smallX = PropertyValuesHolder.ofFloat("scaleX", 1.2f, 1.0f);
        PropertyValuesHolder smallY = PropertyValuesHolder.ofFloat("scaleY", 1.2f, 1.0f);
        scaleLargeAnimator = ObjectAnimator.ofPropertyValuesHolder(this, largeX, largeY);
        scaleSmallAnimator = ObjectAnimator.ofPropertyValuesHolder(this, smallX, smallY);
        scaleSmallAnimator.setDuration(100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(scaleLargeAnimator);
            animatorSet.play(scaleSmallAnimator).after(scaleLargeAnimator);
            animatorSet.setInterpolator(new AccelerateInterpolator());
            animatorSet.start();
        }
        return super.onTouchEvent(event);
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }
}