package com.gongpingjia.carplay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gongpingjia.carplay.R;

import net.duohuo.dhroid.util.DhUtil;

public class AnimButtonView2 extends RelativeLayout {


    Context mContext;

//    private static final int OFFSET = 600; // 每个动画的播放时间间隔
//    private static final int MSG_WAVE2_ANIMATION = 2;

    ImageView image1, image2;


    ImageView bg;

    private AnimationSet mAnimationSet_bg, mAnimationSet_bg2, mAnimationSet_img, mAnimationSet_img2;


//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_WAVE2_ANIMATION:
//                    image2.startAnimation(mAnimationSet2);
//                    break;
//            }
//        }
//    };


    public AnimButtonView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AnimButtonView);


        int srcId = a.getResourceId(R.styleable.AnimButtonView_src, 0);
        int bgId = a.getResourceId(R.styleable.AnimButtonView_bg, 0);


        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int margin = DhUtil.dip2px(mContext, 15);

        pa.setMargins(margin, margin, margin, margin);
        image1 = new ImageView(mContext);
//        image2 = new ImageView(mContext);
        bg = new ImageView(mContext);
        image1.setImageResource(srcId);
//        image2.setImageResource(srcId);
        bg.setImageResource(bgId);
        image1.setLayoutParams(pa);
//        image2.setLayoutParams(pa);
        bg.setLayoutParams(pa);

        addView(image1);
//        addView(image2);
        addView(bg);
        mAnimationSet_bg = initAnimationSet_bg();
        mAnimationSet_bg2 = initAnimationSet_bg2();
        mAnimationSet_img = initAnimationSet_img();
        mAnimationSet_img2 = initAnimationSet_img2();
        a.recycle();

//        startAnimation();
    }

    private AnimationSet initAnimationSet_bg() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(350);
//        sa.setRepeatCount(Animation.INFINITE);// 设置循环
//        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        AlphaAnimation aa = new AlphaAnimation(1, 1f);
        aa.setDuration(350);
//        aa.setRepeatCount(Animation.INFINITE);// 设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private AnimationSet initAnimationSet_bg2() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1.4f, 1f, 1.4f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(350);
//        sa.setRepeatCount(Animation.INFINITE);// 设置循环
//        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        AlphaAnimation aa = new AlphaAnimation(1, 1f);
        aa.setDuration(350);
//        aa.setRepeatCount(Animation.INFINITE);// 设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private AnimationSet initAnimationSet_img() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
//        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        as.addAnimation(sa);
        return as;
    }

    private AnimationSet initAnimationSet_img2() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1.1f, 1f, 1.1f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        as.addAnimation(sa);
        return as;
    }

    public void startAnimation() {
        bg.startAnimation(mAnimationSet_img);
        mHandler2.sendEmptyMessageDelayed(1, 300);
        mHandler2.sendEmptyMessageDelayed(2, 600);
        mHandler2.sendEmptyMessageDelayed(3, 950);
//        image1.startAnimation(mAnimationSet1);
//        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
    }

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    bg.startAnimation(mAnimationSet_img2);
                    break;
                case 2:
                    image1.startAnimation(mAnimationSet_bg);
                    break;
                case 3:
                    image1.startAnimation(mAnimationSet_bg2);
                    break;
            }
        }
    };

    public void clearAnimation() {

        if (mAnimationSet_bg != null) {
            mAnimationSet_bg.cancel();
        }

        if (mAnimationSet_bg2 != null) {
            mAnimationSet_bg2.cancel();
        }
        if (mAnimationSet_img != null) {
            mAnimationSet_img.cancel();
        }
        if (mAnimationSet_img2 != null) {
            mAnimationSet_img2.cancel();
        }
    }


    public ImageView getRealImage() {
        return bg;
    }

}
