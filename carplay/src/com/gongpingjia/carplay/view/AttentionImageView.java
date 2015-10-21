package com.gongpingjia.carplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/10/21.
 */
public class AttentionImageView extends ImageView {
    ScaleAnimation sa, sa1;

    int imageResoure;

    public AttentionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setImage(int drawableId) {
        this.imageResoure = drawableId;
        startAnim();
    }


    private void startAnim() {

        sa1 = new ScaleAnimation(1.2f, 1f, 1.2f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(300);
        sa1.setRepeatCount(0);


        sa = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        sa.setRepeatCount(0);
        sa.setFillAfter(true);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setImageResource(imageResoure);
                AttentionImageView.this.startAnimation(sa1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AttentionImageView.this.startAnimation(sa);


    }
}
