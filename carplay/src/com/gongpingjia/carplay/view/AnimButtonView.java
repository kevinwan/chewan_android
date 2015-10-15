package com.gongpingjia.carplay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;

public class AnimButtonView extends RelativeLayout {


    Context mContext;

    private static final int OFFSET = 600; // 每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;

    ImageView image1, image2;

    private AnimationSet mAnimationSet1, mAnimationSet2, mTranslateAnimationSet;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    image2.startAnimation(mAnimationSet2);
                    break;
            }
        }
    };


    public AnimButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AnimButtonView);


        int srcId = a.getResourceId(R.styleable.AnimButtonView_src, 0);
        int bgId = a.getResourceId(R.styleable.AnimButtonView_bg, 0);

        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        pa.setMargins(30, 30, 30, 30);
        image1 = new ImageView(mContext);
        image2 = new ImageView(mContext);
        ImageView bg = new ImageView(mContext);
        image1.setImageResource(srcId);
        image2.setImageResource(srcId);
        bg.setImageResource(bgId);
        image1.setLayoutParams(pa);
        image2.setLayoutParams(pa);
        bg.setLayoutParams(pa);

        addView(image1);
        addView(image2);
        addView(bg);
        mAnimationSet1 = initAnimationSet();
        mAnimationSet2 = initAnimationSet();
        a.recycle();
        initAnimationTranslateAnimation();
    }


    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.3f, 1f, 1.3f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(1000);
        aa.setRepeatCount(Animation.INFINITE);// 设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private void initAnimationTranslateAnimation() {
        mTranslateAnimationSet = new AnimationSet(true);
        TranslateAnimation ta = new TranslateAnimation(0, -15, 0, 0);
        ta.setDuration(1000);
        ta.setRepeatMode(Animation.REVERSE);
        ta.setRepeatCount(Animation.INFINITE);// 设置循环
        mTranslateAnimationSet.addAnimation(ta);
        this.startAnimation(mTranslateAnimationSet);
    }


    public void startScaleAnimation() {
        image1.startAnimation(mAnimationSet1);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
    }

    public void clearAnimation() {

        if (mAnimationSet1 != null) {
            mAnimationSet1.cancel();
        }

        if (mAnimationSet2 != null) {
            mAnimationSet2.cancel();
        }
    }

}
